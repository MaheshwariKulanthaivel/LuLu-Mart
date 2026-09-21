package com.lulumart.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lulumart.dto.OrderView;
import com.lulumart.exception.AppException;
import com.lulumart.model.User;
import com.lulumart.service.OrderService;
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
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/orders/*")
public class ApiOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();
        try {
            if (path.equals("/") || path.isEmpty()) {
                List<OrderView> orders = orderService.ordersForBuyer(user.getId());
                WebUtil.writeOk(resp, "Orders fetched", orders);
            } else {
                long id = Long.parseLong(path.replace("/", "").trim());
                OrderView view = user.isAdmin() ? orderService.viewForAdmin(id)
                        : user.isSeller() ? orderService.viewForSeller(id, user.getId())
                        : orderService.viewForBuyer(id, user.getId());
                WebUtil.writeOk(resp, "Order fetched", view);
            }
        } catch (AppException e) {
            WebUtil.writeFail(resp, e.getStatusCode(), e.getMessage());
        } catch (NumberFormatException e) {
            WebUtil.writeFail(resp, 404, "Order not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        if (!user.isBuyer()) {
            WebUtil.writeFail(resp, 403, "Only buyers can place orders");
            return;
        }
        try {
            JsonObject body = parseBody(req);
            String address = string(body, "shippingAddress");
            String paymentMethod = string(body, "paymentMethod");
            var order = orderService.checkout(user.getId(), address, paymentMethod);
            Map<String, Object> data = new HashMap<>();
            data.put("id", order.getId());
            data.put("orderRef", order.getOrderRef());
            data.put("totalAmount", order.getTotalAmount().toPlainString());
            data.put("status", order.getStatus());
            data.put("paymentStatus", order.getPaymentStatus());
            WebUtil.writeOk(resp, "Order placed successfully", data);
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