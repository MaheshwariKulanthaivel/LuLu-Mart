package com.lulumart.filter;

import com.lulumart.model.Role;
import com.lulumart.model.User;
import com.lulumart.util.WebUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication + role authorization gate.
 * Public pages pass through; buyer/seller/admin areas are enforced
 * both here and again inside the servlets (defense in depth).
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.isEmpty()) {
            path = "/";
        }

        RequiredRole required = requiredRole(path);
        if (required == RequiredRole.PUBLIC) {
            chain.doFilter(request, response);
            return;
        }

        User user = WebUtil.currentUser(req);
        if (required == RequiredRole.AUTHENTICATED && user != null) {
            chain.doFilter(request, response);
            return;
        }
        if (required == RequiredRole.AUTHENTICATED) {
            deny(req, resp, 401, "Please log in to continue");
            return;
        }

        // Role-specific areas
        if (user == null) {
            deny(req, resp, 401, "Please log in to continue");
            return;
        }
        boolean allowed = switch (required) {
            case BUYER -> user.getRole() == Role.BUYER;
            case SELLER -> user.getRole() == Role.SELLER;
            case ADMIN -> user.getRole() == Role.ADMIN;
            default -> false;
        };
        if (!allowed) {
            deny(req, resp, 403, "You do not have permission to access this area");
            return;
        }
        chain.doFilter(request, response);
    }

    private void deny(HttpServletRequest req, HttpServletResponse resp, int status, String message)
            throws IOException {
        if (req.getRequestURI().contains("/api/")) {
            WebUtil.writeFail(resp, status, message);
        } else {
            if (status == 401) {
                String next = req.getRequestURI()
                        + (req.getQueryString() == null ? "" : "?" + req.getQueryString());
                resp.sendRedirect(req.getContextPath() + "/auth/login?next=" + urlEncode(next));
            } else {
                resp.sendRedirect(req.getContextPath() + "/error/403");
            }
        }
    }

    private String urlEncode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    private RequiredRole requiredRole(String path) {
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")
                || path.startsWith("/favicon") || path.equals("/api/v1/products")
                || path.startsWith("/api/v1/products/")
                || path.startsWith("/api/v1/auth")
                || path.startsWith("/auth")
                || path.equals("/") || path.equals("/home") || path.equals("/index.jsp")
                || path.equals("/shop") || path.equals("/product") || path.startsWith("/pets")
                || path.startsWith("/categories") || path.startsWith("/sellers")
                || path.startsWith("/about") || path.startsWith("/error")) {
            return RequiredRole.PUBLIC;
        }
        if (path.startsWith("/api/v1/cart") || path.startsWith("/api/v1/wishlist")
                || path.startsWith("/api/v1/reviews")
                || path.startsWith("/cart") || path.startsWith("/checkout")
                || path.startsWith("/orders") || path.startsWith("/profile")) {
            return RequiredRole.BUYER;
        }
        if (path.startsWith("/api/v1/seller") || path.startsWith("/seller")) {
            return RequiredRole.SELLER;
        }
        if (path.startsWith("/api/v1/admin") || path.startsWith("/admin")) {
            return RequiredRole.ADMIN;
        }
        if (path.startsWith("/api/v1/orders") || path.startsWith("/order")) {
            return RequiredRole.AUTHENTICATED;
        }
        return RequiredRole.PUBLIC;
    }

    private enum RequiredRole {
        PUBLIC, AUTHENTICATED, BUYER, SELLER, ADMIN
    }
}