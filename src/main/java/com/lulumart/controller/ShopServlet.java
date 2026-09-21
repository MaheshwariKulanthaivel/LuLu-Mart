package com.lulumart.controller;

import com.lulumart.dto.ProductSearchRequest;
import com.lulumart.dto.ProductView;
import com.lulumart.model.User;
import com.lulumart.service.ProductService;
import com.lulumart.service.WishlistService;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet("/shop")
public class ShopServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final WishlistService wishlistService = new WishlistService();

    public static final List<String> PET_TYPES = List.of(
            "Dogs", "Cats", "Birds", "Rabbits", "Hamsters", "Guinea Pigs",
            "Fish", "Turtles", "Reptiles", "Other");
    public static final List<String> CATEGORIES = List.of(
            "Food", "Treats", "Toys", "Accessories", "Grooming", "Care",
            "Beds & Comfort", "Cages / Habitats", "Cleaning", "Training",
            "Health & Wellness", "Clothing / Pet Wear");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String q = WebUtil.param(req, "q");
        String petType = WebUtil.param(req, "petType");
        String category = WebUtil.param(req, "category");
        String minPrice = WebUtil.param(req, "minPrice");
        String maxPrice = WebUtil.param(req, "maxPrice");
        String availability = WebUtil.param(req, "availability");
        String sort = WebUtil.param(req, "sort");

        ProductSearchRequest search = ProductSearchRequest.fromParams(
                q, petType, category, minPrice, maxPrice, availability, sort);
        search.setLimit(60);
        String sellerParam = WebUtil.param(req, "seller");
        if (sellerParam != null && !sellerParam.isBlank()) {
            try {
                search.setSellerId(Long.parseLong(sellerParam));
            } catch (NumberFormatException ignored) {
                // ignore malformed seller filter
            }
        }

        List<ProductView> results = productService.search(search);
        long total = productService.searchCount(search);
        User user = WebUtil.currentUser(req);
        Set<Long> wishlisted = new java.util.HashSet<>();
        if (user != null) {
            for (ProductView pv : results) {
                if (wishlistService.contains(user.getId(), pv.getId())) {
                    wishlisted.add(pv.getId());
                }
            }
        }

        req.setAttribute("results", results);
        req.setAttribute("resultCount", total);
        req.setAttribute("petTypes", PET_TYPES);
        req.setAttribute("categories", CATEGORIES);
        req.setAttribute("fq", q);
        req.setAttribute("fpet", petType);
        req.setAttribute("fcat", category);
        req.setAttribute("fmin", minPrice);
        req.setAttribute("fmax", maxPrice);
        req.setAttribute("favail", availability);
        req.setAttribute("fsort", sort);
        req.setAttribute("fseller", sellerParam);
        req.setAttribute("wishlisted", wishlisted);
        req.setAttribute("pageTitle", "Shop Pet Products");
        req.getRequestDispatcher("/WEB-INF/views/shop.jsp").forward(req, resp);
    }
}