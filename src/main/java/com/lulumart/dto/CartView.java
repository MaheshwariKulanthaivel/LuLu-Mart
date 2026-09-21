package com.lulumart.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartView {

    private List<CartRow> items;
    private long count;
    private int totalQuantity;
    private BigDecimal grandTotal;

    public static class CartRow {
        public long itemId;
        public long productId;
        public String productName;
        public String imageUrl;
        public String sellerName;
        public BigDecimal unitPrice;
        public int quantity;
        public int stockQty;
        public BigDecimal subtotal;
    }

    public List<CartRow> getItems() {
        return items;
    }

    public void setItems(List<CartRow> items) {
        this.items = items;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}