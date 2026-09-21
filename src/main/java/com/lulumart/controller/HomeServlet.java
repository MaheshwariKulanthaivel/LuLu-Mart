package com.lulumart.controller;

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

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        var featured = productService.featured();
        var bestSellers = productService.bestSellers();
        var newArrivals = productService.newArrivals();
        req.setAttribute("featured", featured);
        req.setAttribute("bestSellers", bestSellers);
        req.setAttribute("newArrivals", newArrivals);
        req.setAttribute("topSellers", productService.topSellers());

        User user = WebUtil.currentUser(req);
        java.util.Set<Long> wishlisted = new java.util.HashSet<>();
        if (user != null) {
            for (var pv : featured) {
                if (wishlistService.contains(user.getId(), pv.getId())) {
                    wishlisted.add(pv.getId());
                }
            }
            for (var pv : bestSellers) {
                if (wishlistService.contains(user.getId(), pv.getId())) {
                    wishlisted.add(pv.getId());
                }
            }
            for (var pv : newArrivals) {
                if (wishlistService.contains(user.getId(), pv.getId())) {
                    wishlisted.add(pv.getId());
                }
            }
        }
        req.setAttribute("wishlisted", wishlisted);
        req.setAttribute("pageActive", "home");
        req.setAttribute("pageTitle", "Everything Your Pet Loves, Delivered.");
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}