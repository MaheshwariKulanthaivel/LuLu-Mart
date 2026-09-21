package com.lulumart.controller;

import com.lulumart.dto.CartView;
import com.lulumart.exception.AppException;
import com.lulumart.model.Order;
import com.lulumart.model.User;
import com.lulumart.service.CartService;
import com.lulumart.service.OrderService;
import com.lulumart.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(CheckoutServlet.class);
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        CartView cart = cartService.viewCart(user.getId());
        req.setAttribute("cart", cart);
        req.setAttribute("pageTitle", "Checkout");
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = WebUtil.currentUser(req);
        String address = req.getParameter("shippingAddress");
        String paymentMethod = req.getParameter("paymentMethod");
        try {
            Order order = orderService.checkout(user.getId(), address, paymentMethod);
            log.info("Order {} placed by buyer {}", order.getOrderRef(), user.getEmail());
            resp.sendRedirect(req.getContextPath() + "/order?id=" + order.getId() + "&placed=1");
        } catch (AppException e) {
            resp.sendRedirect(req.getContextPath() + "/checkout?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}