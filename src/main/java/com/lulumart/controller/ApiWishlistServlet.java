package com.lulumart.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lulumart.exception.AppException;
import com.lulumart.model.User;
import com.lulumart.service.WishlistService;
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

@WebServlet("/api/v1/wishlist/*")
public class ApiWishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        WebUtil.writeOk(resp, "Wishlist fetched", wishlistService.items(user.getId()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();
        try {
            JsonObject body = parseBody(req);
            long productId = ValidationUtil.positiveLong(string(body, "productId"), "Product id");
            boolean added;
            switch (path) {
                case "/toggle" -> added = wishlistService.toggle(user.getId(), productId);
                case "/add" -> {
                    wishlistService.add(user.getId(), productId);
                    added = true;
                }
                case "/remove" -> {
                    wishlistService.remove(user.getId(), productId);
                    added = false;
                }
                default -> throw new com.lulumart.exception.BadRequestException("Unknown action");
            }
            WebUtil.writeOk(resp, added ? "Added to wishlist" : "Removed from wishlist",
                    Map.of("added", added));
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