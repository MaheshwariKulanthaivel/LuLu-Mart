package com.lulumart;

import com.lulumart.dto.CartView;
import com.lulumart.exception.BadRequestException;
import com.lulumart.model.Order;
import com.lulumart.model.Product;
import com.lulumart.service.CartService;
import com.lulumart.service.OrderService;
import com.lulumart.service.ProductService;
import com.lulumart.service.ReviewService;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartAndOrderServiceTest extends BaseDbTest {

    private final CartService cartService = new CartService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    private final ReviewService reviewService = new ReviewService();

    private Product createTestProduct(int stock) {
        return productService.create(SELLER_ID, "Dogs", "Cart Test Treat", "Treats for cart tests.",
                "100.00", String.valueOf(stock), "Treats", "https://images.example.com/treat.jpg");
    }

    @Test
    void addToCartComputesTotals() {
        Product p = createTestProduct(50);
        cartService.add(BUYER_ID, p.getId(), 2);

        CartView cart = cartService.viewCart(BUYER_ID);
        assertTrue(cart.getItems().stream().anyMatch(r -> r.productId == p.getId()));
        CartView.CartRow row = cart.getItems().stream().filter(r -> r.productId == p.getId()).findFirst().orElseThrow();
        assertEquals(2, row.quantity);
        assertEquals(new BigDecimal("200.00"), row.subtotal);
        assertEquals(new BigDecimal("200.00"), cart.getGrandTotal());
    }

    @Test
    void addRejectsQuantityOverStock() {
        Product p = createTestProduct(5);
        cartService.add(BUYER_ID, p.getId(), 4);
        assertThrows(BadRequestException.class, () -> cartService.add(BUYER_ID, p.getId(), 2));
    }

    @Test
    void checkoutReducesStockClearsCartAndAllowsReview() {
        Product p = createTestProduct(10);
        cartService.add(BUYER_ID, p.getId(), 3);

        Order order = orderService.checkout(BUYER_ID, "42 Test Street, Test City 500001", "UPI");
        assertNotNull(order.getOrderRef());
        assertTrue(order.getOrderRef().startsWith("LM"));
        assertTrue(order.getId() > 0);

        assertEquals(7, productService.requireOwned(SELLER_ID, p.getId()).getStockQty());
        assertTrue(cartService.viewCart(BUYER_ID).isEmpty());

        orderService.updateStatus(order.getId(), "DELIVERED");
        var review = reviewService.submit(BUYER_ID, p.getId(), 5, "Great product!");
        assertTrue(review.getId() > 0);
        assertEquals(1, reviewService.reviewCount(p.getId()));
    }

    @Test
    void checkoutRejectsInvalidPaymentMethod() {
        Product p = createTestProduct(5);
        cartService.add(BUYER_ID, p.getId(), 1);
        assertThrows(BadRequestException.class,
                () -> orderService.checkout(BUYER_ID, "Address Way, Anywhere", "BTC"));
    }

    @Test
    void checkoutRejectsEmptyCart() {
        assertThrows(BadRequestException.class,
                () -> orderService.checkout(18, "Address", "CARD"));
    }

    @Test
    void reviewRequiresDeliveredPurchase() {
        Product p = createTestProduct(8);
        cartService.add(BUYER_ID, p.getId(), 1);
        orderService.checkout(BUYER_ID, "Address", "COD");
        assertThrows(BadRequestException.class,
                () -> reviewService.submit(BUYER_ID, p.getId(), 4, "Too soon"));
    }

    @Test
    void cannotReviewProductNeverPurchased() {
        Product p = createTestProduct(8);
        assertThrows(BadRequestException.class,
                () -> reviewService.submit(18, p.getId(), 4, "Never bought this"));
    }
}