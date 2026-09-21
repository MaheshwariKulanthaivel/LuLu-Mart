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

@WebServlet("/pets")
public class PetsServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (String pet : ShopServlet.PET_TYPES) {
            var search = new com.lulumart.dto.ProductSearchRequest();
            search.setPetType(pet);
            counts.put(pet, productService.searchCount(search));
        }
        req.setAttribute("counts", counts);
        req.setAttribute("pageTitle", "Shop by Pet");
        req.getRequestDispatcher("/WEB-INF/views/pets.jsp").forward(req, resp);
    }
}