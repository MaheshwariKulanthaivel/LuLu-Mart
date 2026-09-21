package com.lulumart;

import com.lulumart.model.Product;
import com.lulumart.model.WishlistItem;
import com.lulumart.service.ProductService;
import com.lulumart.service.WishlistService;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WishlistServiceTest extends BaseDbTest {

    private final WishlistService wishlistService = new WishlistService();
    private final ProductService productService = new ProductService();

    @Test
    void toggleAddsThenRemoves() {
        Product p = productService.requireOwned(SELLER_ID, 1);
        if (wishlistService.contains(BUYER_ID, p.getId())) {
            wishlistService.remove(BUYER_ID, p.getId());
        }
        assertTrue(wishlistService.toggle(BUYER_ID, p.getId()));
        assertTrue(wishlistService.contains(BUYER_ID, p.getId()));

        assertFalse(wishlistService.toggle(BUYER_ID, p.getId()));
        assertFalse(wishlistService.contains(BUYER_ID, p.getId()));
    }

    @Test
    void itemsListShowsSavedProducts() {
        Product p = productService.requireOwned(SELLER_ID, 1);
        wishlistService.add(BUYER_ID, p.getId());
        java.util.List<WishlistItem> items = wishlistService.items(BUYER_ID);
        assertTrue(items.stream().anyMatch(i -> i.getProductId() == p.getId() && i.getProductName() != null));
    }

    @Test
    void toggleWorksForUsersWithoutAWishlistRow() {
        if (wishlistService.contains(18, 5)) {
            wishlistService.remove(18, 5);
        }
        assertTrue(wishlistService.toggle(18, 5));
        assertEquals(1, wishlistService.items(18).size());
        wishlistService.remove(18, 5);
    }
}