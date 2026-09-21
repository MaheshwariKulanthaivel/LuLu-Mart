package com.lulumart.controller;

import com.lulumart.dto.OrderView;
import com.lulumart.model.User;
import com.lulumart.service.OrderService;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        List<OrderView> orders = orderService.ordersForBuyer(user.getId());
        req.setAttribute("orders", orders);
        req.setAttribute("pageTitle", "My Orders");
        req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req, resp);
    }
}