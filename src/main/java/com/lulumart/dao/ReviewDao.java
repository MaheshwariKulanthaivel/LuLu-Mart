package com.lulumart.dao;

import com.lulumart.model.Review;
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

public class ReviewDao {

    private static final Logger log = LoggerFactory.getLogger(ReviewDao.class);

    public void insert(Review review) {
        String sql = "INSERT INTO reviews (product_id, user_id, order_item_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, review.getProductId());
            ps.setLong(2, review.getUserId());
            ps.setLong(3, review.getOrderItemId());
            ps.setInt(4, review.getRating());
            ps.setString(5, review.getComment());
            ps.setTimestamp(6, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    review.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            log.error("insert review failed", e);
            throw new RuntimeException("Could not save review");
        }
    }

    public List<Review> findByProduct(long productId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.id, r.product_id, r.user_id, r.order_item_id, r.rating, r.comment, r.created_at, u.name AS reviewer_name " +
                "FROM reviews r JOIN users u ON u.id = r.user_id WHERE r.product_id = ? ORDER BY r.created_at DESC, r.id DESC";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReview(rs));
                }
            }
        } catch (SQLException e) {
            log.error("findByProduct failed", e);
        }
        return list;
    }

    /**
     * Order line items for which this buyer can still review this product:
     * item belongs to the product, the order reached SHIPPED/DELIVERED and is not cancelled,
     * and the buyer has not already reviewed that order item.
     */
    public List<Long> eligibleOrderItemIds(long userId, long productId) {
        List<Long> ids = new ArrayList<>();
        String sql = "SELECT oi.id FROM order_items oi " +
                "JOIN orders o ON o.id = oi.order_id " +
                "WHERE o.buyer_id = ? AND oi.product_id = ? " +
                "AND o.status IN ('SHIPPED', 'DELIVERED') " +
                "AND NOT EXISTS (SELECT 1 FROM reviews r WHERE r.order_item_id = oi.id)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            log.error("eligibleOrderItemIds failed", e);
        }
        return ids;
    }

    public double avgRatingFor(long productId) {
        String sql = "SELECT COALESCE(AVG(rating), 0) FROM reviews WHERE product_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            log.error("avgRatingFor failed", e);
            return 0;
        }
    }

    public long countFor(long productId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE product_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            log.error("countFor failed", e);
            return 0;
        }
    }

    public Review mapReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getLong("id"));
        review.setProductId(rs.getLong("product_id"));
        review.setUserId(rs.getLong("user_id"));
        review.setOrderItemId(rs.getLong("order_item_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setReviewerName(rs.getString("reviewer_name"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            review.setCreatedAt(ts.toLocalDateTime());
        }
        return review;
    }
}