package com.lulumart.controller;

import com.lulumart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/categories")
public class CategoriesServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (String cat : ShopServlet.CATEGORIES) {
            var search = new com.lulumart.dto.ProductSearchRequest();
            search.setCategory(cat);
            counts.put(cat, productService.searchCount(search));
        }
        req.setAttribute("counts", counts);
        req.setAttribute("pageTitle", "Shop by Category");
        req.getRequestDispatcher("/WEB-INF/views/categories.jsp").forward(req, resp);
    }
}