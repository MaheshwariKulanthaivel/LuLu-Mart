package com.lulumart.controller;

import com.lulumart.exception.AppException;
import com.lulumart.model.Role;
import com.lulumart.model.User;
import com.lulumart.service.AuthService;
import com.lulumart.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AuthServlet.class);
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        switch (path) {
            case "/login" -> req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            case "/register" -> req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        switch (path) {
            case "/login" -> doLogin(req, resp);
            case "/register" -> doRegister(req, resp);
            case "/logout" -> doLogout(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void doLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            User user = authService.login(email, password);
            establishSession(req, user);
            redirectAfterLogin(req, resp, user);
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            forwardSafe(req, resp, "/WEB-INF/views/login.jsp");
        }
    }

    private void doRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");
        String role = req.getParameter("role");
        try {
            User user = authService.register(name, email, password, confirm, role);
            establishSession(req, user);
            log.info("New account registered: {} ({})", user.getEmail(), user.getRole());
            redirectAfterLogin(req, resp, user);
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            forwardSafe(req, resp, "/WEB-INF/views/register.jsp");
        }
    }

    private void doLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }

    private void establishSession(HttpServletRequest req, User user) {
        HttpSession session = req.getSession(true);
        session.setAttribute(WebUtil.SESSION_USER, user);
        try {
            session.setMaxInactiveInterval(60 * 60 * 8);
            req.changeSessionId();
        } catch (Exception e) {
            log.warn("Session ID regeneration skipped: {}", e.getMessage());
        }
    }

    private void redirectAfterLogin(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        String next = req.getParameter("next");
        if (next != null && !next.isBlank() && next.startsWith(req.getContextPath() + "/")
                && !next.contains("//") && !next.contains("/auth/")) {
            resp.sendRedirect(next);
            return;
        }
        if (user.getRole() == Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else if (user.getRole() == Role.SELLER) {
            resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/shop");
        }
    }

    private void forwardSafe(HttpServletRequest req, HttpServletResponse resp, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher(view).forward(req, resp);
    }
}