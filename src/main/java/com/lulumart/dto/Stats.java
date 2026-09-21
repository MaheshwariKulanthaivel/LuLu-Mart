package com.lulumart.dto;

import java.math.BigDecimal;

/** Used by both seller and admin dashboards. */
public class Stats {

    private long totalUsers;
    private long totalBuyers;
    private long totalSellers;
    private long totalProducts;
    private long totalOrders;
    private long lowStockProducts;
    private long incomingOrders;
    private BigDecimal totalSales = BigDecimal.ZERO;

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalBuyers() {
        return totalBuyers;
    }

    public void setTotalBuyers(long totalBuyers) {
        this.totalBuyers = totalBuyers;
    }

    public long getTotalSellers() {
        return totalSellers;
    }

    public void setTotalSellers(long totalSellers) {
        this.totalSellers = totalSellers;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    public long getIncomingOrders() {
        return incomingOrders;
    }

    public void setIncomingOrders(long incomingOrders) {
        this.incomingOrders = incomingOrders;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }
}