package com.lulumart.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lulumart.exception.AppException;
import com.lulumart.model.Role;
import com.lulumart.model.User;
import com.lulumart.service.AuthService;
import com.lulumart.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/api/v1/auth/*")
public class ApiAuthServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        try {
            JsonObject body = parseBody(req);
            switch (path) {
                case "/login" -> doLogin(req, resp, body);
                case "/register" -> doRegister(req, resp, body);
                case "/logout" -> doLogout(req, resp);
                default -> WebUtil.writeFail(resp, 404, "Endpoint not found");
            }
        } catch (AppException e) {
            WebUtil.writeFail(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            WebUtil.writeFail(resp, 400, "Invalid JSON request body");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        if ("/me".equals(path)) {
            User user = WebUtil.currentUser(req);
            if (user == null) {
                WebUtil.writeFail(resp, 401, "Not logged in");
            } else {
                WebUtil.writeOk(resp, "Logged in", userInfo(user));
            }
            return;
        }
        WebUtil.writeFail(resp, 404, "Endpoint not found");
    }

    private void doLogin(HttpServletRequest req, HttpServletResponse resp, JsonObject body) throws IOException {
        String email = string(body, "email");
        String password = string(body, "password");
        User user = authService.login(email, password);
        establishSession(req, user);
        WebUtil.writeOk(resp, "Logged in successfully", userInfo(user));
    }

    private void doRegister(HttpServletRequest req, HttpServletResponse resp, JsonObject body) throws IOException {
        User user = authService.register(
                string(body, "name"),
                string(body, "email"),
                string(body, "password"),
                string(body, "confirmPassword"),
                string(body, "role") == null ? "BUYER" : string(body, "role"));
        establishSession(req, user);
        WebUtil.writeOk(resp, "Account created successfully", userInfo(user));
    }

    private void doLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        WebUtil.writeOk(resp, "Logged out");
    }

    private void establishSession(HttpServletRequest req, User user) {
        HttpSession session = req.getSession(true);
        session.setAttribute(WebUtil.SESSION_USER, user);
        session.setMaxInactiveInterval(60 * 60 * 8);
        req.changeSessionId();
    }

    private Map<String, Object> userInfo(User user) {
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().name());
    }

    private JsonObject parseBody(HttpServletRequest req) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) {
            throw new com.lulumart.exception.BadRequestException("Request body must be valid JSON");
        }
    }

    private String string(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : null;
    }
}