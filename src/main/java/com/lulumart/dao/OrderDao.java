package com.lulumart.dao;

import com.lulumart.dto.OrderView;
import com.lulumart.model.CartItem;
import com.lulumart.model.Order;
import com.lulumart.model.OrderItem;
import com.lulumart.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDao {

    private static final Logger log = LoggerFactory.getLogger(OrderDao.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    /**
     * Places an order atomically: creates the order, inserts items,
     * decrements stock, and clears the buyer's cart.
     * Throws Runtime exceptions (with rollback) if stock is insufficient.
     */
    public Order placeOrder(long buyerId, List<CartItem> cartItems, String shippingAddress,
                            String paymentMethod, String paymentStatus) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        try (Connection conn = DbUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Order order = new Order();
                order.setOrderRef(generateUniqueRef(conn));
                order.setBuyerId(buyerId);
                order.setStatus("PENDING");
                order.setTotalAmount(total);
                order.setShippingAddress(shippingAddress);
                order.setPaymentMethod(paymentMethod);
                order.setPaymentStatus(paymentStatus);
                order.setCreatedAt(java.time.LocalDateTime.now());

                String insertOrder = "INSERT INTO orders (order_ref, buyer_id, status, total_amount, shipping_address, " +
                        "payment_method, payment_status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                long orderId;
                try (PreparedStatement ps = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, order.getOrderRef());
                    ps.setLong(2, order.getBuyerId());
                    ps.setString(3, order.getStatus());
                    ps.setBigDecimal(4, order.getTotalAmount());
                    ps.setString(5, order.getShippingAddress());
                    ps.setString(6, order.getPaymentMethod());
                    ps.setString(7, order.getPaymentStatus());
                    ps.setTimestamp(8, Timestamp.valueOf(order.getCreatedAt()));
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        orderId = keys.getLong(1);
                    }
                }
                order.setId(orderId);

                String insertItem = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
                String decrement = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
                String clearCart = "DELETE FROM cart_items WHERE user_id = ?";

                for (CartItem item : cartItems) {
                    try (PreparedStatement ps = conn.prepareStatement(insertItem)) {
                        ps.setLong(1, orderId);
                        ps.setLong(2, item.getProductId());
                        ps.setInt(3, item.getQuantity());
                        ps.setBigDecimal(4, item.getPrice());
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = conn.prepareStatement(decrement)) {
                        ps.setInt(1, item.getQuantity());
                        ps.setLong(2, item.getProductId());
                        ps.setInt(3, item.getQuantity());
                        if (ps.executeUpdate() == 0) {
                            throw new IllegalStateException("Insufficient stock for: " + item.getProductName());
                        }
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(clearCart)) {
                    ps.setLong(1, buyerId);
                    ps.executeUpdate();
                }

                conn.commit();
                return order;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            log.error("placeOrder failed", e);
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException("Checkout failed", e);
        }
    }

    public void updateStatus(long orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("updateStatus failed", e);
            throw new RuntimeException("Could not update order status");
        }
    }

    public Optional<Order> findById(long orderId) {
        String sql = "SELECT id, order_ref, buyer_id, status, total_amount, shipping_address, payment_method, payment_status, created_at " +
                "FROM orders WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapOrder(rs));
                }
            }
        } catch (SQLException e) {
            log.error("findById failed", e);
        }
        return Optional.empty();
    }

    public List<OrderItem> itemsForOrder(long orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, " +
                "COALESCE(p.name, 'Removed listing') AS product_name, p.image_url AS product_image " +
                "FROM order_items oi LEFT JOIN products p ON p.id = oi.product_id WHERE oi.order_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductImage(rs.getString("product_image"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            log.error("itemsForOrder failed", e);
        }
        return items;
    }

    public Optional<OrderView> viewById(long orderId) {
        String sql = "SELECT o.id, o.order_ref, o.buyer_id, u.name AS buyer_name, o.status, o.total_amount, " +
                "o.shipping_address, o.payment_method, o.payment_status, o.created_at " +
                "FROM orders o JOIN users u ON u.id = o.buyer_id WHERE o.id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrderView view = mapOrderView(rs);
                    view.setItems(itemsForOrder(orderId));
                    return Optional.of(view);
                }
            }
        } catch (SQLException e) {
            log.error("viewById failed", e);
        }
        return Optional.empty();
    }

    public List<OrderView> viewsByBuyer(long buyerId) {
        List<OrderView> views = queryViews("SELECT o.id, o.order_ref, o.buyer_id, u.name AS buyer_name, o.status, o.total_amount, " +
                "o.shipping_address, o.payment_method, o.payment_status, o.created_at " +
                "FROM orders o JOIN users u ON u.id = o.buyer_id WHERE o.buyer_id = ? " +
                "ORDER BY o.created_at DESC, o.id DESC", buyerId);
        for (OrderView view : views) {
            view.setItems(itemsForOrder(view.getId()));
        }
        return views;
    }

    /** Distinct orders that contain at least one of the seller's products; only those line items are included. */
    public List<OrderView> viewsBySeller(long sellerId) {
        List<OrderView> views = queryViews("SELECT DISTINCT o.id, o.order_ref, o.buyer_id, u.name AS buyer_name, o.status, " +
                "o.total_amount, o.shipping_address, o.payment_method, o.payment_status, o.created_at " +
                "FROM orders o JOIN order_items oi ON oi.order_id = o.id " +
                "JOIN products p ON p.id = oi.product_id " +
                "JOIN users u ON u.id = o.buyer_id " +
                "WHERE p.seller_id = ? ORDER BY o.created_at DESC, o.id DESC", -1);
        List<OrderView> filtered = new ArrayList<>();
        for (OrderView view : views) {
            List<OrderItem> sellerItems = new ArrayList<>();
            for (OrderItem item : itemsForOrder(view.getId())) {
                Boolean belongs = itemBelongsToSeller(item, sellerId);
                if (Boolean.TRUE.equals(belongs)) {
                    sellerItems.add(item);
                } else if (belongs == null) {
                    sellerItems.add(item);
                }
            }
            if (!sellerItems.isEmpty()) {
                view.setItems(sellerItems);
                filtered.add(view);
            }
        }
        return filtered;
    }

    private Boolean itemBelongsToSeller(OrderItem item, long sellerId) {
        String sql = "SELECT 1 FROM order_items oi JOIN products p ON p.id = oi.product_id WHERE oi.id = ? AND p.seller_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, item.getId());
            ps.setLong(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            log.error("itemBelongsToSeller failed", e);
            return null;
        }
    }

    public boolean itemIdBelongsToSeller(long itemId, long sellerId) {
        String sql = "SELECT 1 FROM order_items oi JOIN products p ON p.id = oi.product_id WHERE oi.id = ? AND p.seller_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, itemId);
            ps.setLong(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            log.error("itemIdBelongsToSeller failed", e);
            return false;
        }
    }

    public List<OrderView> viewsAll() {
        List<OrderView> views = queryViews("SELECT o.id, o.order_ref, o.buyer_id, u.name AS buyer_name, o.status, " +
                "o.total_amount, o.shipping_address, o.payment_method, o.payment_status, o.created_at " +
                "FROM orders o JOIN users u ON u.id = o.buyer_id " +
                "ORDER BY o.created_at DESC, o.id DESC", -1);
        for (OrderView view : views) {
            view.setItems(itemsForOrder(view.getId()));
        }
        return views;
    }

    private List<OrderView> queryViews(String sql, long onlyBuyerId) {
        List<OrderView> views = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (onlyBuyerId > 0) {
                ps.setLong(1, onlyBuyerId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    views.add(mapOrderView(rs));
                }
            }
        } catch (SQLException e) {
            log.error("queryViews failed", e);
        }
        return views;
    }

    public long countAll() {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DbUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            log.error("countAll failed", e);
            return 0;
        }
    }

    public BigDecimal totalSales() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE status <> 'CANCELLED'";
        try (Connection conn = DbUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getBigDecimal(1);
        } catch (SQLException e) {
            log.error("totalSales failed", e);
            return BigDecimal.ZERO;
        }
    }

    public long countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("countByStatus failed", e);
            return 0;
        }
    }

    private String generateUniqueRef(Connection conn) throws SQLException {
        for (int attempt = 0; attempt < 10; attempt++) {
            String ref = "LM" + (100000 + new java.security.SecureRandom().nextInt(899999));
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE order_ref = ?")) {
                ps.setString(1, ref);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    if (rs.getLong(1) == 0) {
                        return ref;
                    }
                }
            }
        }
        throw new SQLException("Could not generate a unique order reference");
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setOrderRef(rs.getString("order_ref"));
        order.setBuyerId(rs.getLong("buyer_id"));
        order.setStatus(rs.getString("status"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setPaymentStatus(rs.getString("payment_status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            order.setCreatedAt(ts.toLocalDateTime());
        }
        return order;
    }

    private OrderView mapOrderView(ResultSet rs) throws SQLException {
        OrderView view = new OrderView();
        view.setId(rs.getLong("id"));
        view.setOrderRef(rs.getString("order_ref"));
        view.setBuyerId(rs.getLong("buyer_id"));
        view.setBuyerName(rs.getString("buyer_name"));
        view.setStatus(rs.getString("status"));
        view.setTotalAmount(rs.getBigDecimal("total_amount").toPlainString());
        view.setShippingAddress(rs.getString("shipping_address"));
        view.setPaymentMethod(rs.getString("payment_method"));
        view.setPaymentStatus(rs.getString("payment_status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            view.setCreatedAt(ts.toLocalDateTime().format(FMT));
        }
        return view;
    }
}