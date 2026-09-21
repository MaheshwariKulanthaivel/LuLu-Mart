package com.lulumart.controller;

import com.lulumart.dto.ProductView;
import com.lulumart.dto.Stats;
import com.lulumart.exception.AppException;
import com.lulumart.exception.NotFoundException;
import com.lulumart.model.Product;
import com.lulumart.model.User;
import com.lulumart.service.OrderService;
import com.lulumart.service.ProductService;
import com.lulumart.service.SellerService;
import com.lulumart.util.ValidationUtil;
import com.lulumart.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/seller/*")
public class SellerServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SellerServlet.class);
    private final SellerService sellerService = new SellerService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "/dashboard" : req.getPathInfo();
        User user = WebUtil.currentUser(req);
        switch (path) {
            case "/dashboard" -> {
                Stats stats = sellerService.dashboardStats(user.getId());
                req.setAttribute("stats", stats);
                req.setAttribute("pageTitle", "Seller Dashboard");
                req.getRequestDispatcher("/WEB-INF/views/seller/dashboard.jsp").forward(req, resp);
            }
            case "/products" -> {
                List<ProductView> products = productService.findViewsForSeller(user.getId());
                req.setAttribute("products", products);
                req.setAttribute("pageTitle", "My Products");
                req.getRequestDispatcher("/WEB-INF/views/seller/products.jsp").forward(req, resp);
            }
            case "/product-form" -> {
                long id = req.getParameter("id") == null ? 0 : ValidationUtil.positiveLong(req.getParameter("id"), "Product id");
                Product product = null;
                if (id > 0) {
                    product = productService.requireOwned(user.getId(), id);
                }
                req.setAttribute("product", product);
                req.setAttribute("petTypes", ShopServlet.PET_TYPES);
                req.setAttribute("categories", ShopServlet.CATEGORIES);
                req.setAttribute("pageTitle", product == null ? "Add Product" : "Edit Product");
                req.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(req, resp);
            }
            case "/orders" -> {
                req.setAttribute("orders", orderService.ordersForSeller(user.getId()));
                req.setAttribute("pageTitle", "Incoming Orders");
                req.getRequestDispatcher("/WEB-INF/views/seller/orders.jsp").forward(req, resp);
            }
            case "/order" -> {
                long id = ValidationUtil.positiveLong(WebUtil.param(req, "id"), "Order id");
                try {
                    req.setAttribute("order", orderService.viewForSeller(id, user.getId()));
                } catch (NotFoundException e) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                req.setAttribute("pageTitle", "Order Detail");
                req.getRequestDispatcher("/WEB-INF/views/seller/order-detail.jsp").forward(req, resp);
            }
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        User user = WebUtil.currentUser(req);
        try {
            switch (path) {
                case "/product-form" -> saveProduct(req, resp, user);
                case "/delete" -> deleteProduct(req, resp, user);
                case "/stock" -> updateStock(req, resp, user);
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (AppException e) {
            resp.sendRedirect(req.getContextPath() + "/seller/products?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void saveProduct(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        String idParam = req.getParameter("id");
        long id = idParam == null || idParam.isBlank() ? 0 : ValidationUtil.positiveLong(idParam, "Product id");
        String petType = ValidationUtil.require(req.getParameter("petType"), "Pet type", 50);
        String name = ValidationUtil.require(req.getParameter("name"), "Product name", 150);
        String description = ValidationUtil.require(req.getParameter("description"), "Description", 2000);
        String price = req.getParameter("price");
        String stock = req.getParameter("stockQty");
        String category = ValidationUtil.require(req.getParameter("category"), "Category", 50);
        String image = req.getParameter("imageUrl");

        if (id > 0) {
            productService.update(user.getId(), id, petType, name, description, price, stock, category, image);
        } else {
            productService.create(user.getId(), petType, name, description, price, stock, category, image);
        }
        resp.sendRedirect(req.getContextPath() + "/seller/products?msg=Product saved");
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        long id = ValidationUtil.positiveLong(req.getParameter("id"), "Product id");
        productService.delete(user.getId(), id, false);
        resp.sendRedirect(req.getContextPath() + "/seller/products?msg=Product deleted");
    }

    private void updateStock(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        long id = ValidationUtil.positiveLong(req.getParameter("id"), "Product id");
        int newStock = ValidationUtil.requireStock(req.getParameter("stockQty"));
        productService.updateStock(user.getId(), id, newStock);
        resp.sendRedirect(req.getContextPath() + "/seller/products?msg=Stock updated");
    }
}