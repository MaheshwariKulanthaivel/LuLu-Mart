package com.lulumart.dao;

import com.lulumart.dto.ProductSearchRequest;
import com.lulumart.dto.ProductView;
import com.lulumart.dto.SellerSummary;
import com.lulumart.model.Product;
import com.lulumart.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public class ProductDao {

    private static final Logger log = LoggerFactory.getLogger(ProductDao.class);

    private static final String VIEW_SELECT =
            "SELECT p.id, p.seller_id, s.name AS seller_name, p.pet_type, p.name, p.description, " +
            "p.price, p.stock_qty, p.category, p.image_url, " +
            "FORMATDATETIME(p.created_at, 'dd MMM yyyy') AS created_at, " +
            "COALESCE(AVG(r.rating), 0) AS avg_rating, COUNT(r.id) AS review_count " +
            "FROM products p JOIN users s ON s.id = p.seller_id " +
            "LEFT JOIN reviews r ON r.product_id = p.id ";

    public Optional<Product> findById(long id) {
        String sql = "SELECT id, seller_id, pet_type, name, description, price, stock_qty, category, image_url, created_at " +
                "FROM products WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapProduct(rs));
                }
            }
        } catch (SQLException e) {
            log.error("findById failed for {}", id, e);
        }
        return Optional.empty();
    }

    public Optional<ProductView> findViewById(long id) {
        String sql = VIEW_SELECT + " WHERE p.id = ? GROUP BY p.id, p.seller_id, s.name, p.pet_type, p.name, p.description, " +
                "p.price, p.stock_qty, p.category, p.image_url, p.created_at";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapView(rs));
                }
            }
        } catch (SQLException e) {
            log.error("findViewById failed for {}", id, e);
        }
        return Optional.empty();
    }

    public Product insert(Product product) {
        String sql = "INSERT INTO products (seller_id, pet_type, name, description, price, stock_qty, category, image_url, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, product.getSellerId());
            ps.setString(2, product.getPetType());
            ps.setString(3, product.getName());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getPrice());
            ps.setInt(6, product.getStockQty());
            ps.setString(7, product.getCategory());
            ps.setString(8, product.getImageUrl());
            ps.setTimestamp(9, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    product.setId(keys.getLong(1));
                }
            }
            return product;
        } catch (SQLException e) {
            log.error("insert product failed", e);
            throw new RuntimeException("Could not save product");
        }
    }

    public void update(Product product) {
        String sql = "UPDATE products SET pet_type = ?, name = ?, description = ?, price = ?, stock_qty = ?, category = ?, image_url = ? " +
                "WHERE id = ? AND seller_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getPetType());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQty());
            ps.setString(6, product.getCategory());
            ps.setString(7, product.getImageUrl());
            ps.setLong(8, product.getId());
            ps.setLong(9, product.getSellerId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("update product failed", e);
            throw new RuntimeException("Could not update product");
        }
    }

    public boolean updateStock(long productId, int newStock) {
        String sql = "UPDATE products SET stock_qty = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("updateStock failed", e);
            return false;
        }
    }

    public void delete(long productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("delete product failed", e);
            throw new RuntimeException("Could not delete product");
        }
    }

    /**
     * Full-text + filter search. All dynamic values go through parameters;
     * only fixed SQL fragments are appended based on controlled switches.
     */
    public List<ProductView> search(ProductSearchRequest req) {
        StringBuilder sql = new StringBuilder(VIEW_SELECT);
        List<Object> params = new ArrayList<>();
        sql.append(" WHERE 1=1 ");
        appendFilters(sql, params, req);
        sql.append(" GROUP BY p.id, p.seller_id, s.name, p.pet_type, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at ");
        String sort = req.getSort() == null ? "NEWEST" : req.getSort();
        switch (sort) {
            case "PRICE_ASC":
                sql.append(" ORDER BY p.price ASC, p.id ASC");
                break;
            case "PRICE_DESC":
                sql.append(" ORDER BY p.price DESC, p.id ASC");
                break;
            case "NAME_ASC":
                sql.append(" ORDER BY p.name ASC, p.id ASC");
                break;
            case "NEWEST":
            default:
                sql.append(" ORDER BY p.created_at DESC, p.id DESC");
                break;
        }
        sql.append(" LIMIT ?");
        params.add(Math.max(req.getLimit(), 1));

        List<ProductView> result = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapView(rs));
                }
            }
        } catch (SQLException e) {
            log.error("search failed", e);
        }
        return result;
    }

    public long searchCount(ProductSearchRequest req) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM products p JOIN users s ON s.id = p.seller_id WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        appendFilters(sql, params, req);
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("searchCount failed", e);
            return 0;
        }
    }

    private void appendFilters(StringBuilder sql, List<Object> params, ProductSearchRequest req) {
        if (req.getQ() != null && !req.getQ().isBlank()) {
            String like = "%" + req.getQ().toLowerCase() + "%";
            sql.append(" AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ? OR LOWER(p.pet_type) LIKE ? ");
            sql.append(" OR LOWER(p.category) LIKE ? OR LOWER(s.name) LIKE ?)");
            for (int i = 0; i < 5; i++) {
                params.add(like);
            }
        }
        if (req.getPetType() != null && !req.getPetType().isBlank()) {
            sql.append(" AND p.pet_type = ?");
            params.add(req.getPetType());
        }
        if (req.getCategory() != null && !req.getCategory().isBlank()) {
            sql.append(" AND p.category = ?");
            params.add(req.getCategory());
        }
        if (req.getMinPrice() != null && !req.getMinPrice().isBlank()) {
            sql.append(" AND p.price >= ?");
            params.add(new java.math.BigDecimal(req.getMinPrice()));
        }
        if (req.getMaxPrice() != null && !req.getMaxPrice().isBlank()) {
            sql.append(" AND p.price <= ?");
            params.add(new java.math.BigDecimal(req.getMaxPrice()));
        }
        if ("IN_STOCK".equals(req.getAvailability())) {
            sql.append(" AND p.stock_qty > 0");
        } else if ("OUT_OF_STOCK".equals(req.getAvailability())) {
            sql.append(" AND p.stock_qty = 0");
        }
        if (req.getSellerId() > 0) {
            sql.append(" AND p.seller_id = ?");
            params.add(req.getSellerId());
        }
    }

    public List<ProductView> listBySeller(long sellerId) {
        String sql = VIEW_SELECT + " WHERE p.seller_id = ? GROUP BY p.id, p.seller_id, s.name, p.pet_type, p.name, p.description, " +
                "p.price, p.stock_qty, p.category, p.image_url, p.created_at ORDER BY p.created_at DESC, p.id DESC";
        List<ProductView> result = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapView(rs));
                }
            }
        } catch (SQLException e) {
            log.error("listBySeller failed", e);
        }
        return result;
    }

    public long countBySeller(long sellerId) {
        String sql = "SELECT COUNT(*) FROM products WHERE seller_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("countBySeller failed", e);
            return 0;
        }
    }

    public long lowStockBySeller(long sellerId, int threshold) {
        String sql = "SELECT COUNT(*) FROM products WHERE seller_id = ? AND stock_qty <= ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            ps.setInt(2, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("lowStockBySeller failed", e);
            return 0;
        }
    }

    public long countAll() {
        String sql = "SELECT COUNT(*) FROM products";
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

    public BigDecimal sellerSales(long sellerId) {
        String sql = "SELECT COALESCE(SUM(oi.quantity * oi.unit_price), 0) FROM order_items oi " +
                "JOIN products p ON p.id = oi.product_id " +
                "JOIN orders o ON o.id = oi.order_id " +
                "WHERE p.seller_id = ? AND o.status <> 'CANCELLED'";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            log.error("sellerSales failed", e);
            return BigDecimal.ZERO;
        }
    }

    public List<ProductView> listAllViews() {
        ProductSearchRequest req = new ProductSearchRequest();
        req.setLimit(500);
        return search(req);
    }

    public List<ProductView> featured(int limit) {
        String sql = VIEW_SELECT +
                " GROUP BY p.id, p.seller_id, s.name, p.pet_type, p.name, p.description, " +
                "p.price, p.stock_qty, p.category, p.image_url, p.created_at " +
                " HAVING COUNT(r.id) > 0 ORDER BY avg_rating DESC, review_count DESC, p.id ASC LIMIT ?";
        return queryViews(sql, limit);
    }

    public List<ProductView> bestSellers(int limit) {
        String sql = "SELECT p.id, p.seller_id, s.name AS seller_name, p.pet_type, p.name, p.description, " +
                "p.price, p.stock_qty, p.category, p.image_url, " +
                "FORMATDATETIME(p.created_at, 'dd MMM yyyy') AS created_at, " +
                "COALESCE(AVG(r.rating), 0) AS avg_rating, COUNT(DISTINCT r.id) AS review_count " +
                "FROM order_items oi " +
                "JOIN products p ON p.id = oi.product_id " +
                "JOIN users s ON s.id = p.seller_id " +
                "LEFT JOIN reviews r ON r.product_id = p.id " +
                "WHERE (SELECT status FROM orders o WHERE o.id = oi.order_id) <> 'CANCELLED' " +
                "GROUP BY p.id, p.seller_id, s.name, p.pet_type, p.name, p.description, " +
                "p.price, p.stock_qty, p.category, p.image_url, p.created_at " +
                "ORDER BY SUM(oi.quantity) DESC, p.id ASC LIMIT ?";
        return queryViews(sql, limit);
    }

    public List<ProductView> newArrivals(int limit) {
        String sql = VIEW_SELECT +
                " GROUP BY p.id, p.seller_id, s.name, p.pet_type, p.name, p.description, " +
                "p.price, p.stock_qty, p.category, p.image_url, p.created_at " +
                " ORDER BY p.created_at DESC, p.id DESC LIMIT ?";
        return queryViews(sql, limit);
    }

    public List<SellerSummary> topSellers(int limit) {
        List<SellerSummary> list = new ArrayList<>();
        String sql = "SELECT u.id, u.name, u.email, u.created_at, COUNT(p.id) AS pc " +
                "FROM users u LEFT JOIN products p ON p.seller_id = u.id " +
                "WHERE u.role = 'SELLER' GROUP BY u.id, u.name, u.email, u.created_at " +
                "ORDER BY pc DESC, u.name LIMIT ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new SellerSummary(rs.getLong("id"), rs.getString("name"),
                            rs.getString("email"), rs.getLong("pc"),
                            rs.getTimestamp("created_at").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            log.error("topSellers failed", e);
        }
        return list;
    }

    private List<ProductView> queryViews(String sql, int limit) {
        List<ProductView> result = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapView(rs));
                }
            }
        } catch (SQLException e) {
            log.error("queryViews failed", e);
        }
        return result;
    }

    private ProductView mapView(ResultSet rs) throws SQLException {
        return ProductView.fromStock(
                rs.getLong("id"),
                rs.getLong("seller_id"),
                rs.getString("seller_name"),
                rs.getString("pet_type"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getInt("stock_qty"),
                rs.getString("category"),
                rs.getString("image_url"),
                rs.getString("created_at"),
                rs.getDouble("avg_rating"),
                rs.getLong("review_count"));
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setPetType(rs.getString("pet_type"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            p.setCreatedAt(ts.toLocalDateTime());
        }
        return p;
    }
}