package com.lulumart.controller;

import com.lulumart.dto.ProductView;
import com.lulumart.model.Review;
import com.lulumart.model.User;
import com.lulumart.service.ProductService;
import com.lulumart.service.ReviewService;
import com.lulumart.service.WishlistService;
import com.lulumart.util.ValidationUtil;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = ValidationUtil.positiveLong(WebUtil.param(req, "id"), "Product id");
        ProductView product = productService.findByViewId(id)
                .orElseThrow(() -> new com.lulumart.exception.NotFoundException("Product not found"));

        List<Review> reviews = reviewService.reviewsFor(id);
        List<ProductView> related = productService.related(product);
        User user = WebUtil.currentUser(req);

        boolean inWishlist = user != null && wishlistService.contains(user.getId(), id);
        Set<Long> wishlisted = new HashSet<>();
        if (user != null) {
            for (ProductView pv : related) {
                if (wishlistService.contains(user.getId(), pv.getId())) {
                    wishlisted.add(pv.getId());
                }
            }
        }
        boolean eligibleToReview = user != null && user.isBuyer()
                && !reviewService.reviewsFor(id).stream()
                        .anyMatch(r -> r.getUserId() == user.getId())
                && !new com.lulumart.dao.ReviewDao().eligibleOrderItemIds(user.getId(), id).isEmpty();

        req.setAttribute("product", product);
        req.setAttribute("reviews", reviews);
        req.setAttribute("related", related);
        req.setAttribute("wishlisted", wishlisted);
        req.setAttribute("inWishlist", inWishlist);
        req.setAttribute("canReview", eligibleToReview);
        req.setAttribute("avgRating", reviewService.avgRating(id));
        req.setAttribute("reviewCount", reviewService.reviewCount(id));
        req.setAttribute("pageTitle", product.getName());
        req.getRequestDispatcher("/WEB-INF/views/product.jsp").forward(req, resp);
    }
}