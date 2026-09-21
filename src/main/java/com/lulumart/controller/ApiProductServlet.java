package com.lulumart.controller;

import com.lulumart.dto.ProductSearchRequest;
import com.lulumart.dto.ProductView;
import com.lulumart.exception.NotFoundException;
import com.lulumart.service.ProductService;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/products/*")
public class ApiProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String path = req.getPathInfo() == null ? "/" : req.getPathInfo();
            switch (path) {
                case "/", "//" -> list(req, resp);
                case "/featured" -> respond(resp, productService.featured());
                case "/bestsellers" -> respond(resp, productService.bestSellers());
                case "/new-arrivals" -> respond(resp, productService.newArrivals());
                default -> detail(resp, path);
            }
        } catch (NotFoundException e) {
            WebUtil.writeFail(resp, 404, e.getMessage());
        } catch (Exception e) {
            WebUtil.writeFail(resp, 400, "Invalid request");
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ProductSearchRequest search = ProductSearchRequest.fromParams(
                WebUtil.param(req, "q"),
                WebUtil.param(req, "petType"),
                WebUtil.param(req, "category"),
                WebUtil.param(req, "minPrice"),
                WebUtil.param(req, "maxPrice"),
                WebUtil.param(req, "availability"),
                WebUtil.param(req, "sort"));
        String seller = WebUtil.param(req, "seller");
        if (seller != null && !seller.isBlank()) {
            try {
                search.setSellerId(Long.parseLong(seller.trim()));
            } catch (NumberFormatException ignored) {
                // ignore malformed seller filter
            }
        }
        String limit = WebUtil.param(req, "limit");
        if (limit != null) {
            try {
                search.setLimit(Math.min(Integer.parseInt(limit), 200));
            } catch (NumberFormatException ignored) {
                search.setLimit(200);
            }
        }
        List<ProductView> results = productService.search(search);
        long total = productService.searchCount(search);
        WebUtil.writeOk(resp, "Products fetched",
                Map.of("results", results, "count", total));
    }

    private void detail(HttpServletResponse resp, String path) throws IOException {
        String idPart = path.replace("/", "").trim();
        long id = Long.parseLong(idPart);
        ProductView product = productService.findByViewId(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        WebUtil.writeOk(resp, "Product fetched", product);
    }

    private void respond(HttpServletResponse resp, List<ProductView> products) throws IOException {
        WebUtil.writeOk(resp, "Products fetched", products);
    }
}