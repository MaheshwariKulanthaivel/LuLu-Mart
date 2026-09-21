package com.lulumart.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error/*")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (req.getAttribute("javax.servlet.error.status_code") == null) {
            resp.setStatus(switch (path) {
                case "/403" -> HttpServletResponse.SC_FORBIDDEN;
                case "/404" -> HttpServletResponse.SC_NOT_FOUND;
                default -> HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            });
        }
        switch (path) {
            case "/404" -> {
                req.setAttribute("pageTitle", "Page Not Found");
                req.getRequestDispatcher("/WEB-INF/views/error/404.jsp").forward(req, resp);
            }
            case "/403" -> {
                req.setAttribute("pageTitle", "Access Denied");
                req.getRequestDispatcher("/WEB-INF/views/error/403.jsp").forward(req, resp);
            }
            default -> {
                req.setAttribute("pageTitle", "Something Went Wrong");
                req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
            }
        }
    }
}