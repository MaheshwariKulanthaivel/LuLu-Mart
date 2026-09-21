package com.lulumart.controller;

import com.lulumart.dao.UserDao;
import com.lulumart.exception.AppException;
import com.lulumart.model.User;
import com.lulumart.service.AdminService;
import com.lulumart.service.OrderService;
import com.lulumart.service.ProductService;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/admin/*")
public class ApiAdminServlet extends HttpServlet {

    private final AdminService adminService = new AdminService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();
        switch (path) {
            case "/stats" -> WebUtil.writeOk(resp, "Stats fetched", adminService.dashboardStats());
            case "/users" -> WebUtil.writeOk(resp, "Users fetched", userDao.findAll());
            case "/products" -> WebUtil.writeOk(resp, "Products fetched", new com.lulumart.dao.ProductDao().listAllViews());
            case "/orders" -> WebUtil.writeOk(resp, "Orders fetched", orderService.allOrders());
            default -> WebUtil.writeFail(resp, 404, "Endpoint not found");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String path = req.getPathInfo() == null ? "" : req.getPathInfo();
            if (!path.startsWith("/products/")) {
                WebUtil.writeFail(resp, 404, "Endpoint not found");
                return;
            }
            long id = Long.parseLong(path.replace("/products/", "").trim());
            productService.delete(0, id, true);
            WebUtil.writeOk(resp, "Listing removed");
        } catch (AppException e) {
            WebUtil.writeFail(resp, e.getStatusCode(), e.getMessage());
        }
    }
}