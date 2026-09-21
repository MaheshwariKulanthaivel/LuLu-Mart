package com.lulumart.service;

import com.lulumart.dao.CartDao;
import com.lulumart.dao.OrderDao;
import com.lulumart.dto.CartView;
import com.lulumart.dto.OrderView;
import com.lulumart.exception.BadRequestException;
import com.lulumart.exception.NotFoundException;
import com.lulumart.model.CartItem;
import com.lulumart.model.Order;
import com.lulumart.util.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OrderService {

    private static final Set<String> VALID_STATUSES =
            Set.of("PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED");
    private static final Set<String> VALID_PAYMENT_METHODS = Set.of("UPI", "CARD", "COD");

    private final OrderDao orderDao;
    private final CartDao cartDao;

    public OrderService() {
        this(new OrderDao(), new CartDao());
    }

    public OrderService(OrderDao orderDao, CartDao cartDao) {
        this.orderDao = orderDao;
        this.cartDao = cartDao;
    }

    public Order checkout(long buyerId, String shippingAddress, String paymentMethod) {
        if (!VALID_PAYMENT_METHODS.contains(paymentMethod)) {
            throw new BadRequestException("Choose a valid payment method");
        }
        String address = ValidationUtil.require(shippingAddress, "Shipping address", 500);
        List<CartItem> cart = cartDao.normalizeAgainstStock(cartDao.findFullCart(buyerId));
        if (cart.isEmpty()) {
            throw new BadRequestException("Your cart is empty, add products before checkout");
        }
        String paymentStatus = "COD".equals(paymentMethod) ? "PENDING" : "PAID";
        return orderDao.placeOrder(buyerId, cart, address, paymentMethod, paymentStatus);
    }

    public OrderView viewForBuyer(long orderId, long buyerId) {
        OrderView view = orderDao.viewById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (view.getBuyerId() != buyerId) {
            throw new NotFoundException("Order not found");
        }
        return view;
    }

    public OrderView viewForAdmin(long orderId) {
        return orderDao.viewById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public OrderView viewForSeller(long orderId, long sellerId) {
        OrderView view = orderDao.viewById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        boolean hasSellerItem = orderDao.itemsForOrder(orderId).stream()
                .anyMatch(item -> orderDao.itemIdBelongsToSeller(item.getId(), sellerId));
        if (!hasSellerItem) {
            throw new NotFoundException("Order not found");
        }
        return view;
    }

    public List<OrderView> ordersForBuyer(long buyerId) {
        return orderDao.viewsByBuyer(buyerId);
    }

    public List<OrderView> ordersForSeller(long sellerId) {
        return orderDao.viewsBySeller(sellerId);
    }

    public List<OrderView> allOrders() {
        return orderDao.viewsAll();
    }

    public void updateStatus(long orderId, String newStatus) {
        if (!VALID_STATUSES.contains(newStatus)) {
            throw new BadRequestException("Invalid order status");
        }
        orderDao.updateStatus(orderId, newStatus);
    }
}