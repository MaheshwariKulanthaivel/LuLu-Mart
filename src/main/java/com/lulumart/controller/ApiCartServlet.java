package com.lulumart.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lulumart.dto.CartView;
import com.lulumart.exception.AppException;
import com.lulumart.model.User;
import com.lulumart.service.CartService;
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
import java.util.Map;

@WebServlet("/api/v1/cart/*")
public class ApiCartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        CartView cart = cartService.viewCart(user.getId());
        WebUtil.writeOk(resp, "Cart fetched", cart);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doUpdateQuantity(req, resp);
            return;
        }
        super.service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        try {
            JsonObject body = parseBody(req);
            long productId = ValidationUtil.positiveLong(string(body, "productId"), "Product id");
            int quantity = ValidationUtil.optionalPositiveInt(string(body, "quantity"), 1);
            cartService.add(user.getId(), productId, quantity);
            long count = cartService.cartCount(user.getId());
            WebUtil.writeOk(resp, "Added to cart", Map.of("cartCount", count));
        } catch (AppException e) {
            WebUtil.writeFail(resp, e.getStatusCode(), e.getMessage());
        }
    }

    private void doUpdateQuantity(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        try {
            long itemId = idFromPath(req.getPathInfo());
            JsonObject body = parseBody(req);
            int newQty = ValidationUtil.requirePositiveInt(string(body, "quantity"), "Quantity");
            cartService.updateQuantity(user.getId(), itemId, newQty);
            CartView cart = cartService.viewCart(user.getId());
            WebUtil.writeOk(resp, "Quantity updated", cart);
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
            if (path.equals("/") || path.isEmpty()) {
                cartService.clear(user.getId());
                WebUtil.writeOk(resp, "Cart cleared");
            } else {
                long itemId = idFromPath(path);
                cartService.remove(user.getId(), itemId);
                long count = cartService.cartCount(user.getId());
                WebUtil.writeOk(resp, "Item removed from cart", Map.of("cartCount", count));
            }
        } catch (AppException e) {
            WebUtil.writeFail(resp, e.getStatusCode(), e.getMessage());
        }
    }

    private long idFromPath(String path) {
        String id = path.replace("/", "").trim();
        return Long.parseLong(id);
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