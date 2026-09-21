package com.lulumart.dao;

import com.lulumart.model.CartItem;
import com.lulumart.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartDao {

    private static final Logger log = LoggerFactory.getLogger(CartDao.class);

    public void addItem(long userId, long productId, int quantity) {
        String check = "SELECT id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        String insert = "INSERT INTO cart_items (user_id, product_id, quantity, created_at) VALUES (?, ?, ?, ?)";
        String update = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(check)) {
                ps.setLong(1, userId);
                ps.setLong(2, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        long itemId = rs.getLong("id");
                        int existing = rs.getInt("quantity");
                        try (PreparedStatement up = conn.prepareStatement(update)) {
                            up.setInt(1, existing + quantity);
                            up.setLong(2, itemId);
                            up.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement ins = conn.prepareStatement(insert)) {
                            ins.setLong(1, userId);
                            ins.setLong(2, productId);
                            ins.setInt(3, quantity);
                            ins.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.now()));
                            ins.executeUpdate();
                        }
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            log.error("addItem failed", e);
            throw new RuntimeException("Could not update cart");
        }
    }

    public boolean updateQuantity(long userId, long itemId, int newQty) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQty);
            ps.setLong(2, itemId);
            ps.setLong(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("updateQuantity failed", e);
            return false;
        }
    }

    public boolean removeItem(long userId, long itemId) {
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, itemId);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("removeItem failed", e);
            return false;
        }
    }

    public void clear(long userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("clear failed", e);
        }
    }

    public List<CartItem> findFullCart(long userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT ci.id, ci.user_id, ci.product_id, ci.quantity, ci.created_at, " +
                "p.name AS product_name, p.pet_type, p.category, p.image_url, p.stock_qty, p.price, " +
                "s.name AS seller_name " +
                "FROM cart_items ci " +
                "JOIN products p ON p.id = ci.product_id " +
                "JOIN users s ON s.id = p.seller_id " +
                "WHERE ci.user_id = ? ORDER BY ci.created_at DESC, ci.id DESC";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapCartItem(rs));
                }
            }
        } catch (SQLException e) {
            log.error("findFullCart failed", e);
        }
        return items;
    }

    /** Number of rows in the cart (badge count). */
    public long countRows(long userId) {
        String sql = "SELECT COUNT(*) FROM cart_items WHERE user_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("countRows failed", e);
            return 0;
        }
    }

    public Optional<Integer> quantityFor(long userId, long productId) {
        String sql = "SELECT quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            log.error("quantityFor failed", e);
        }
        return Optional.empty();
    }

    /** Caps each item quantity to its product stock and drops zero-stock rows. */
    public List<CartItem> normalizeAgainstStock(List<CartItem> cart) {
        if (cart == null) {
            return List.of();
        }
        List<CartItem> normalized = new ArrayList<>();
        for (CartItem item : cart) {
            if (item.getStockQty() <= 0) {
                removeItem(item.getUserId(), item.getId());
                continue;
            }
            if (item.getQuantity() > item.getStockQty()) {
                updateQuantity(item.getUserId(), item.getId(), item.getStockQty());
                item.setQuantity(item.getStockQty());
            }
            normalized.add(item);
        }
        return normalized;
    }

    public BigDecimal grandTotal(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }

    private CartItem mapCartItem(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setId(rs.getLong("id"));
        item.setUserId(rs.getLong("user_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setProductName(rs.getString("product_name"));
        item.setPetType(rs.getString("pet_type"));
        item.setCategory(rs.getString("category"));
        item.setImageUrl(rs.getString("image_url"));
        item.setStockQty(rs.getInt("stock_qty"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setSellerName(rs.getString("seller_name"));
        return item;
    }
}