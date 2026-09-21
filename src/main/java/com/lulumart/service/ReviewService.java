package com.lulumart.service;

import com.lulumart.dao.ProductDao;
import com.lulumart.dao.ReviewDao;
import com.lulumart.exception.BadRequestException;
import com.lulumart.model.Review;
import com.lulumart.util.ValidationUtil;

import java.util.List;

public class ReviewService {

    private final ReviewDao reviewDao;
    private final ProductDao productDao;

    public ReviewService() {
        this(new ReviewDao(), new ProductDao());
    }

    public ReviewService(ReviewDao reviewDao, ProductDao productDao) {
        this.reviewDao = reviewDao;
        this.productDao = productDao;
    }

    public Review submit(long userId, long productId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5 stars");
        }
        String cleanComment = ValidationUtil.optional(comment == null ? "" : comment, 1000);
        if (productDao.findById(productId).isEmpty()) {
            throw new BadRequestException("Product not found");
        }
        List<Long> eligible = reviewDao.eligibleOrderItemIds(userId, productId);
        if (eligible.isEmpty()) {
            throw new BadRequestException("You can only review products you have purchased and received");
        }
        Review review = new Review();
        review.setProductId(productId);
        review.setUserId(userId);
        review.setOrderItemId(eligible.get(0));
        review.setRating(rating);
        review.setComment(cleanComment);
        reviewDao.insert(review);
        return review;
    }

    public List<Review> reviewsFor(long productId) {
        return reviewDao.findByProduct(productId);
    }

    public double avgRating(long productId) {
        return reviewDao.avgRatingFor(productId);
    }

    public long reviewCount(long productId) {
        return reviewDao.countFor(productId);
    }
}