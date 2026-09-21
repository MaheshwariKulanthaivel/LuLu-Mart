package com.lulumart.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lulumart.dto.Stats;
import com.lulumart.exception.AppException;
import com.lulumart.model.User;
import com.lulumart.service.OrderService;
import com.lulumart.service.ProductService;
import com.lulumart.service.SellerService;
import com.lulumart.util.ValidationUtil;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/seller/*")
public class ApiSellerServlet extends HttpServlet {

    private final SellerService sellerService = new SellerService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();
        switch (path) {
            case "/stats" -> {
                Stats stats = sellerService.dashboardStats(user.getId());
                WebUtil.writeOk(resp, "Seller stats fetched", stats);
            }
            case "/products" -> WebUtil.writeOk(resp, "Products fetched", productService.findViewsForSeller(user.getId()));
            case "/orders" -> WebUtil.writeOk(resp, "Orders fetched", orderService.ordersForSeller(user.getId()));
            default -> WebUtil.writeFail(resp, 404, "Endpoint not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();
        try {
            JsonObject body = parseBody(req);
            switch (path) {
                case "/products" -> {
                    var product = productService.create(user.getId(),
                            string(body, "petType"), string(body, "name"), string(body, "description"),
                            string(body, "price"), string(body, "stockQty"), string(body, "category"),
                            string(body, "imageUrl"));
                    WebUtil.writeOk(resp, "Product created", Map.of("id", product.getId()));
                }
                case "/stock" -> {
                    long id = ValidationUtil.positiveLong(string(body, "id"), "Product id");
                    int stock = ValidationUtil.requireStock(string(body, "stockQty"));
                    productService.updateStock(user.getId(), id, stock);
                    WebUtil.writeOk(resp, "Stock updated");
                }
                default -> WebUtil.writeFail(resp, 404, "Endpoint not found");
            }
        } catch (AppException e) {
            WebUtil.writeFail(resp, e.getStatusCode(), e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        try {
            String path = req.getPathInfo() == null ? "" : req.getPathInfo();
            if (!path.startsWith("/products/")) {
                WebUtil.writeFail(resp, 404, "Endpoint not found");
                return;
            }
            long id = Long.parseLong(path.replace("/products/", "").trim());
            JsonObject body = parseBody(req);
            productService.update(user.getId(), id,
                    string(body, "petType"), string(body, "name"), string(body, "description"),
                    string(body, "price"), string(body, "stockQty"), string(body, "category"),
                    string(body, "imageUrl"));
            WebUtil.writeOk(resp, "Product updated");
        } catch (AppException e) {
            WebUtil.writeFail(resp, e.getStatusCode(), e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        try {
            String path = req.getPathInfo() == null ? "" : req.getPathInfo();
            if (!path.startsWith("/products/")) {
                WebUtil.writeFail(resp, 404, "Endpoint not found");
                return;
            }
            long id = Long.parseLong(path.replace("/products/", "").trim());
            productService.delete(user.getId(), id, false);
            WebUtil.writeOk(resp, "Product deleted");
        } catch (AppException e) {
            WebUtil.writeFail(resp, e.getStatusCode(), e.getMessage());
        }
    }

    private JsonObject parseBody(HttpServletRequest req) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) {
            throw new com.lulumart.exception.BadRequestException("Request body must be valid JSON");
        }
    }

    private String string(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : null;
    }
}