package com.lulumart.controller;

import com.lulumart.dto.OrderView;
import com.lulumart.exception.NotFoundException;
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

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = ValidationUtil.positiveLong(WebUtil.param(req, "id"), "Order id");
        User user = WebUtil.currentUser(req);
        OrderView view;
        try {
            if (user.isAdmin()) {
                view = orderService.viewForAdmin(id);
            } else if (user.isSeller()) {
                view = orderService.viewForSeller(id, user.getId());
            } else {
                view = orderService.viewForBuyer(id, user.getId());
            }
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("order", view);
        req.setAttribute("justPlaced", "1".equals(req.getParameter("placed")));
        req.setAttribute("canReview", user.isBuyer());
        req.setAttribute("pageTitle", "Order " + view.getOrderRef());
        req.getRequestDispatcher("/WEB-INF/views/order-detail.jsp").forward(req, resp);
    }
}