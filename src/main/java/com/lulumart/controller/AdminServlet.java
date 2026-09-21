package com.lulumart.controller;

import com.lulumart.dao.UserDao;
import com.lulumart.dto.OrderView;
import com.lulumart.dto.ProductView;
import com.lulumart.dto.Stats;
import com.lulumart.exception.AppException;
import com.lulumart.model.Role;
import com.lulumart.model.User;
import com.lulumart.service.AdminService;
import com.lulumart.service.OrderService;
import com.lulumart.service.ProductService;
import com.lulumart.util.ValidationUtil;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private final AdminService adminService = new AdminService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "/dashboard" : req.getPathInfo();
        switch (path) {
            case "/dashboard" -> {
                Stats stats = adminService.dashboardStats();
                req.setAttribute("stats", stats);
                req.setAttribute("pageTitle", "Admin Dashboard");
                req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
            }
            case "/users" -> {
                String roleFilter = WebUtil.param(req, "role");
                List<User> users = userDao.findAll();
                if (roleFilter != null && !roleFilter.isEmpty()) {
                    users = users.stream().filter(u -> u.getRole().name().equalsIgnoreCase(roleFilter)).toList();
                }
                req.setAttribute("users", users);
                req.setAttribute("roleFilter", roleFilter);
                req.setAttribute("pageTitle", "Manage Users");
                req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
            }
            case "/products" -> {
                List<ProductView> products = new com.lulumart.dao.ProductDao().listAllViews();
                req.setAttribute("products", products);
                req.setAttribute("pageTitle", "Moderate Listings");
                req.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(req, resp);
            }
            case "/orders" -> {
                List<OrderView> orders = orderService.allOrders();
                req.setAttribute("orders", orders);
                req.setAttribute("pageTitle", "All Orders");
                req.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(req, resp);
            }
            case "/order" -> {
                long id = ValidationUtil.positiveLong(WebUtil.param(req, "id"), "Order id");
                req.setAttribute("order", orderService.viewForAdmin(id));
                req.setAttribute("pageTitle", "Order Detail");
                req.getRequestDispatcher("/WEB-INF/views/admin/order-detail.jsp").forward(req, resp);
            }
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        try {
            switch (path) {
                case "/products/delete" -> {
                    long id = ValidationUtil.positiveLong(req.getParameter("id"), "Product id");
                    productService.delete(0, id, true);
                    resp.sendRedirect(req.getContextPath() + "/admin/products?msg=Listing removed");
                }
                case "/orders/status" -> {
                    long id = ValidationUtil.positiveLong(req.getParameter("id"), "Order id");
                    String status = ValidationUtil.require(req.getParameter("status"), "Status", 20);
                    orderService.updateStatus(id, status);
                    resp.sendRedirect(req.getContextPath() + "/admin/orders?msg=Order updated");
                }
                case "/users/delete" -> {
                    long id = ValidationUtil.positiveLong(req.getParameter("id"), "User id");
                    User target = userDao.findById(id).orElseThrow(() -> new AppException("User not found", 404));
                    if (target.getRole() == Role.ADMIN) {
                        throw new AppException("The administrator account cannot be deleted", 400);
                    }
                    userDao.delete(id);
                    resp.sendRedirect(req.getContextPath() + "/admin/users?msg=User removed");
                }
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (AppException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}