package com.lulumart.service;

import com.lulumart.dao.CartDao;
import com.lulumart.dao.ProductDao;
import com.lulumart.dto.CartView;
import com.lulumart.exception.BadRequestException;
import com.lulumart.exception.NotFoundException;
import com.lulumart.model.CartItem;
import com.lulumart.model.Product;
import com.lulumart.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CartService {

    private final CartDao cartDao;
    private final ProductDao productDao;

    public CartService() {
        this(new CartDao(), new ProductDao());
    }

    public CartService(CartDao cartDao, ProductDao productDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
    }

    public CartView viewCart(long userId) {
        List<CartItem> raw = cartDao.findFullCart(userId);
        List<CartItem> items = cartDao.normalizeAgainstStock(raw);

        CartView view = new CartView();
        List<CartView.CartRow> rows = new java.util.ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            CartView.CartRow row = new CartView.CartRow();
            row.itemId = item.getId();
            row.productId = item.getProductId();
            row.productName = item.getProductName();
            row.imageUrl = item.getImageUrl();
            row.sellerName = item.getSellerName();
            row.unitPrice = item.getPrice();
            row.quantity = item.getQuantity();
            row.stockQty = item.getStockQty();
            row.subtotal = item.getSubtotal();
            rows.add(row);
            total = total.add(item.getSubtotal());
        }
        view.setItems(rows);
        view.setCount(rows.size());
        view.setTotalQuantity(rows.stream().mapToInt(r -> r.quantity).sum());
        view.setGrandTotal(total);
        return view;
    }

    public long cartCount(long userId) {
        return cartDao.countRows(userId);
    }

    public void add(long userId, long productId, int quantity) {
        Product product = productDao.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if (!product.isInStock()) {
            throw new BadRequestException("This product is currently out of stock");
        }
        int desired = quantity;
        Optional<Integer> existing = cartDao.quantityFor(userId, productId);
        if (existing.isPresent()) {
            desired += existing.get();
        }
        if (desired > product.getStockQty()) {
            throw new BadRequestException("Only " + product.getStockQty() + " unit(s) available in stock");
        }
        cartDao.addItem(userId, productId, quantity);
    }

    public void updateQuantity(long userId, long itemId, int newQty) {
        List<CartItem> cart = cartDao.findFullCart(userId);
        CartItem target = cart.stream().filter(c -> c.getId() == itemId)
                .findFirst().orElseThrow(() -> new NotFoundException("Cart item not found"));
        if (newQty > target.getStockQty()) {
            newQty = target.getStockQty();
        }
        if (newQty <= 0) {
            cartDao.removeItem(userId, itemId);
        } else {
            cartDao.updateQuantity(userId, itemId, newQty);
        }
    }

    public void remove(long userId, long itemId) {
        if (!cartDao.removeItem(userId, itemId)) {
            throw new NotFoundException("Cart item not found");
        }
    }

    public void clear(long userId) {
        cartDao.clear(userId);
    }
}