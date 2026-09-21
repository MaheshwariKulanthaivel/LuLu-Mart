package com.lulumart.dto;

public class SellerSummary {

    private long id;
    private String name;
    private String email;
    private long productCount;
    private java.time.LocalDateTime createdAt;

    public SellerSummary(long id, String name, String email, long productCount, java.time.LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.productCount = productCount;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getProductCount() {
        return productCount;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }
}