package com.lulumart.dto;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * Product joined with live rating/seller info, used on cards, lists and detail pages.
 */
public class ProductView {

    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private long id;
    private long sellerId;
    private String sellerName;
    private String petType;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQty;
    private String category;
    private String imageUrl;
    private String createdAt;
    private double avgRating;
    private long reviewCount;

    public static ProductView fromStock(long id, long sellerId, String sellerName, String petType, String name,
                                        String description, BigDecimal price, int stockQty, String category,
                                        String imageUrl, String createdAt, double avgRating, long reviewCount) {
        ProductView v = new ProductView();
        v.id = id;
        v.sellerId = sellerId;
        v.sellerName = sellerName;
        v.petType = petType;
        v.name = name;
        v.description = description;
        v.price = price;
        v.stockQty = stockQty;
        v.category = category;
        v.imageUrl = imageUrl;
        v.createdAt = createdAt;
        v.avgRating = avgRating;
        v.reviewCount = reviewCount;
        return v;
    }

    public long getId() {
        return id;
    }

    public long getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getPetType() {
        return petType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStockQty() {
        return stockQty;
    }

    public boolean isInStock() {
        return stockQty > 0;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public long getReviewCount() {
        return reviewCount;
    }
}