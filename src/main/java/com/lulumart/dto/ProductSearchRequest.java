package com.lulumart.dto;

public class ProductSearchRequest {

    private String q;
    private String petType;
    private String category;
    private String minPrice;
    private String maxPrice;
    private String availability; // IN_STOCK | OUT_OF_STOCK
    private String sort;         // PRICE_ASC, PRICE_DESC, NEWEST, NAME_ASC
    private long sellerId;
    private int limit = 100;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public static ProductSearchRequest fromParams(String q, String petType, String category, String minPrice,
                                                  String maxPrice, String availability, String sort) {
        ProductSearchRequest r = new ProductSearchRequest();
        r.q = q;
        r.petType = petType;
        r.category = category;
        r.minPrice = minPrice;
        r.maxPrice = maxPrice;
        r.availability = availability;
        r.sort = sort;
        return r;
    }
}