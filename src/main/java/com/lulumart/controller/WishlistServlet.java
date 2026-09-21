package com.lulumart.controller;

import com.lulumart.model.User;
import com.lulumart.service.WishlistService;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        req.setAttribute("items", wishlistService.items(user.getId()));
        req.setAttribute("pageTitle", "My Wishlist");
        req.getRequestDispatcher("/WEB-INF/views/wishlist.jsp").forward(req, resp);
    }
}