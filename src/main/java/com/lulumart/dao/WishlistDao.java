package com.lulumart.dao;

import com.lulumart.model.WishlistItem;
import com.lulumart.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WishlistDao {

    private static final Logger log = LoggerFactory.getLogger(WishlistDao.class);

    /** Returns the wishlist id for a user, creating it lazily if needed. */
    public long ensureWishlist(long userId) {
        String find = "SELECT id FROM wishlist WHERE user_id = ?";
        String insert = "INSERT INTO wishlist (user_id, created_at) VALUES (?, ?)";
        try (Connection conn = DbUtil.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(find, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, userId);
                ps.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    return keys.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.error("ensureWishlist failed", e);
            throw new RuntimeException("Could not access wishlist");
        }
    }

    public boolean addItem(long userId, long productId) {
        long wishlistId = ensureWishlist(userId);
        String sql = "MERGE INTO wishlist_items (wishlist_id, product_id, created_at) KEY (wishlist_id, product_id) VALUES (?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, wishlistId);
            ps.setLong(2, productId);
            ps.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error("addItem failed", e);
            return false;
        }
    }

    public boolean removeItem(long userId, long productId) {
        long wishlistId = ensureWishlist(userId);
        String sql = "DELETE FROM wishlist_items WHERE wishlist_id = ? AND product_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, wishlistId);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("removeItem failed", e);
            return false;
        }
    }

    public boolean contains(long userId, long productId) {
        long wishlistId = ensureWishlist(userId);
        String sql = "SELECT COUNT(*) FROM wishlist_items WHERE wishlist_id = ? AND product_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, wishlistId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1) > 0;
            }
        } catch (SQLException e) {
            log.error("contains failed", e);
            return false;
        }
    }

    public List<WishlistItem> items(long userId) {
        long wishlistId = ensureWishlist(userId);
        List<WishlistItem> list = new ArrayList<>();
        String sql = "SELECT wi.id, wi.wishlist_id, wi.product_id, wi.created_at, " +
                "p.name AS product_name, p.pet_type, p.category, p.image_url, p.price, p.stock_qty, s.name AS seller_name " +
                "FROM wishlist_items wi JOIN products p ON p.id = wi.product_id JOIN users s ON s.id = p.seller_id " +
                "WHERE wi.wishlist_id = ? ORDER BY wi.created_at DESC, wi.id DESC";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, wishlistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WishlistItem item = new WishlistItem();
                    item.setId(rs.getLong("id"));
                    item.setWishlistId(rs.getLong("wishlist_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setPetType(rs.getString("pet_type"));
                    item.setCategory(rs.getString("category"));
                    item.setImageUrl(rs.getString("image_url"));
                    item.setPrice(rs.getBigDecimal("price"));
                    item.setStockQty(rs.getInt("stock_qty"));
                    item.setSellerName(rs.getString("seller_name"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        item.setCreatedAt(ts.toLocalDateTime());
                    }
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            log.error("items failed", e);
        }
        return list;
    }
}