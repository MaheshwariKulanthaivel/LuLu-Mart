package com.lulumart.controller;

import com.lulumart.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/sellers")
public class SellersServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("sellers", userDao.listSellers());
        req.setAttribute("pageTitle", "Trusted Sellers");
        req.setAttribute("pageActive", "sellers");
        req.getRequestDispatcher("/WEB-INF/views/sellers.jsp").forward(req, resp);
    }
}