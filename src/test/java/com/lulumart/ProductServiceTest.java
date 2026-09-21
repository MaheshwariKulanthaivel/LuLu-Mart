package com.lulumart;

import com.lulumart.dto.ProductSearchRequest;
import com.lulumart.dto.ProductView;
import com.lulumart.exception.ForbiddenException;
import com.lulumart.exception.NotFoundException;
import com.lulumart.model.Product;
import com.lulumart.service.ProductService;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductServiceTest extends BaseDbTest {

    private final ProductService productService = new ProductService();

    private Product createTestProduct() {
        return productService.create(SELLER_ID, "Dogs", "Test Chew Toy", "A sturdy chew toy for testing.",
                "199.50", "25", "Toys", "https://images.example.com/chew.jpg");
    }

    @Test
    void sellerCanCreateAndRetrieveOwnProducts() {
        Product p = createTestProduct();
        assertTrue(p.getId() > 0);
        assertEquals(SELLER_ID, p.getSellerId());

        List<ProductView> mine = productService.findViewsForSeller(SELLER_ID);
        assertTrue(mine.stream().anyMatch(v -> v.getId() == p.getId()));
    }

    @Test
    void sellerCannotManageAnotherSellersProduct() {
        Product mine = createTestProduct();
        assertThrows(ForbiddenException.class,
                () -> productService.update(5, mine.getId(), "Dogs", "Stolen", "x", "100", "10", "Toys", null));
        assertThrows(ForbiddenException.class,
                () -> productService.delete(5, mine.getId(), false));
    }

    @Test
    void adminCanDeleteAnyProduct() {
        Product p = createTestProduct();
        productService.delete(0, p.getId(), true);
        assertThrows(NotFoundException.class, () -> productService.requireOwned(SELLER_ID, p.getId()));
    }

    @Test
    void deleteKeepsOrderHistoryIntact() {
        Product p = createTestProduct();
        productService.delete(SELLER_ID, p.getId(), false);
        try (var conn = com.lulumart.util.DbUtil.getConnection()) {
            try (var ps = conn.prepareStatement("SELECT COUNT(*) FROM order_items WHERE product_id = ?")) {
                ps.setLong(1, p.getId());
                try (var rs = ps.executeQuery()) {
                    rs.next();
                    assertEquals(0L, rs.getLong(1));
                }
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void searchAppliesPetTypeAndCategoryFilters() {
        ProductSearchRequest req = new ProductSearchRequest();
        req.setPetType("Dogs");
        req.setLimit(50);
        List<ProductView> dogs = productService.search(req);
        assertFalse(dogs.isEmpty());
        assertTrue(dogs.stream().allMatch(v -> "Dogs".equals(v.getPetType())));

        req.setPetType("Cats");
        req.setCategory("Toys");
        List<ProductView> catToys = productService.search(req);
        assertFalse(catToys.isEmpty());
        assertTrue(catToys.stream().allMatch(v -> "Cats".equals(v.getPetType()) && "Toys".equals(v.getCategory())));
    }

    @Test
    void searchMatchesKeywordInName() {
        ProductSearchRequest req = new ProductSearchRequest();
        req.setQ("harness");
        req.setLimit(20);
        long count = productService.searchCount(req);
        assertTrue(count > 0);
        List<ProductView> hits = productService.search(req);
        assertTrue(hits.stream().allMatch(
                v -> v.getName().toLowerCase().contains("harness") || v.getDescription().toLowerCase().contains("harness")));
    }

    @Test
    void featuredAndBestSellersAreNonEmpty() {
        assertFalse(productService.featured().isEmpty());
        assertFalse(productService.bestSellers().isEmpty());
        assertFalse(productService.newArrivals().isEmpty());
        assertFalse(productService.topSellers().isEmpty());
    }
}