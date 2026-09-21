package com.lulumart.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lulumart.exception.AppException;
import com.lulumart.model.User;
import com.lulumart.service.ReviewService;
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

@WebServlet("/api/v1/reviews/*")
public class ApiReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            long productId = ValidationUtil.positiveLong(WebUtil.param(req, "productId"), "Product id");
            var reviews = reviewService.reviewsFor(productId);
            WebUtil.writeOk(resp, "Reviews fetched", Map.of(
                    "reviews", reviews,
                    "avgRating", reviewService.avgRating(productId),
                    "count", reviewService.reviewCount(productId)));
        } catch (AppException e) {
            WebUtil.writeFail(resp, e.getStatusCode(), e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        if (!user.isBuyer()) {
            WebUtil.writeFail(resp, 403, "Only buyers can post reviews");
            return;
        }
        try {
            JsonObject body = parseBody(req);
            long productId = ValidationUtil.positiveLong(string(body, "productId"), "Product id");
            int rating = string(body, "rating") == null ? 0 : Integer.parseInt(string(body, "rating"));
            String comment = string(body, "comment");
            reviewService.submit(user.getId(), productId, rating, comment);
            WebUtil.writeOk(resp, "Thank you! Your review has been posted");
        } catch (NumberFormatException e) {
            WebUtil.writeFail(resp, 400, "Rating must be a number between 1 and 5");
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