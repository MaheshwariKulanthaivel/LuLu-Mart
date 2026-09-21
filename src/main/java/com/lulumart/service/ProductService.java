package com.lulumart.service;

import com.lulumart.dao.ProductDao;
import com.lulumart.dao.ReviewDao;
import com.lulumart.dto.ProductSearchRequest;
import com.lulumart.dto.ProductView;
import com.lulumart.dto.SellerSummary;
import com.lulumart.exception.BadRequestException;
import com.lulumart.exception.ForbiddenException;
import com.lulumart.exception.NotFoundException;
import com.lulumart.model.Product;
import com.lulumart.util.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductDao productDao;
    private final ReviewDao reviewDao;

    public ProductService() {
        this(new ProductDao(), new ReviewDao());
    }

    public ProductService(ProductDao productDao, ReviewDao reviewDao) {
        this.productDao = productDao;
        this.reviewDao = reviewDao;
    }

    public Product create(long sellerId, String petType, String name, String description,
                          String price, String stockQty, String category, String imageUrl) {
        Product product = new Product();
        product.setSellerId(sellerId);
        product.setPetType(petType);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(ValidationUtil.requirePrice(price));
        product.setStockQty(ValidationUtil.requireStock(stockQty));
        product.setCategory(category);
        product.setImageUrl(ValidationUtil.optional(imageUrl, 500));
        return productDao.insert(product);
    }

    public Product update(long sellerId, long productId, String petType, String name, String description,
                          String price, String stockQty, String category, String imageUrl) {
        Product existing = requireOwned(sellerId, productId);
        existing.setPetType(petType);
        existing.setName(name);
        existing.setDescription(description);
        existing.setPrice(ValidationUtil.requirePrice(price));
        existing.setStockQty(ValidationUtil.requireStock(stockQty));
        existing.setCategory(category);
        existing.setImageUrl(ValidationUtil.optional(imageUrl, 500));
        productDao.update(existing);
        return existing;
    }

    public void updateStock(long sellerId, long productId, int newStock) {
        requireOwned(sellerId, productId);
        if (newStock < 0) {
            throw new BadRequestException("Stock cannot be negative");
        }
        productDao.updateStock(productId, newStock);
    }

    public void delete(long sellerIdOrAdmin, long productId, boolean isAdmin) {
        if (!isAdmin) {
            requireOwned(sellerIdOrAdmin, productId);
        } else {
            productDao.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found"));
        }
        productDao.delete(productId);
    }

    public Product requireOwned(long sellerId, long productId) {
        Product product = productDao.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if (product.getSellerId() != sellerId) {
            throw new ForbiddenException("You can only manage your own products");
        }
        return product;
    }

    public Product findForView(long productId) {
        return productDao.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public ProductView view(long productId) {
        return productDao.findViewById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public List<ProductView> search(ProductSearchRequest req) {
        return productDao.search(req);
    }

    public long searchCount(ProductSearchRequest req) {
        return productDao.searchCount(req);
    }

    public List<ProductView> related(ProductView current) {
        ProductSearchRequest req = new ProductSearchRequest();
        req.setPetType(current.getPetType());
        req.setLimit(8);
        return productDao.search(req).stream()
                .filter(p -> p.getId() != current.getId())
                .limit(4)
                .toList();
    }

    public List<ProductView> featured() {
        return productDao.featured(8);
    }

    public List<ProductView> bestSellers() {
        return productDao.bestSellers(8);
    }

    public List<ProductView> newArrivals() {
        return productDao.newArrivals(8);
    }

    public List<SellerSummary> topSellers() {
        return productDao.topSellers(8);
    }

    public Optional<ProductView> findByViewId(long id) {
        return productDao.findViewById(id);
    }

    public long countBySeller(long sellerId) {
        return productDao.countBySeller(sellerId);
    }

    public List<ProductView> findViewsForSeller(long sellerId) {
        return productDao.listBySeller(sellerId);
    }
}