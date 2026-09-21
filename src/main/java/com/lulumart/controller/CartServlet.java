package com.lulumart.controller;

import com.lulumart.dto.CartView;
import com.lulumart.exception.AppException;
import com.lulumart.model.User;
import com.lulumart.service.CartService;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        CartView cart = cartService.viewCart(user.getId());
        req.setAttribute("cart", cart);
        req.setAttribute("pageTitle", "Your Cart");
        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = WebUtil.param(req, "action");
        User user = WebUtil.currentUser(req);
        try {
            if ("clear".equals(action)) {
                cartService.clear(user.getId());
            }
            resp.sendRedirect(req.getContextPath() + "/cart");
        } catch (AppException e) {
            resp.sendRedirect(req.getContextPath() + "/cart?error=" + e.getMessage());
        }
    }
}