package com.lulumart.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lulumart.dto.ApiResponse;
import com.lulumart.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public final class WebUtil {

    public static final String SESSION_USER = "authUser";

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private static final SecureRandom RANDOM = new SecureRandom();

    private WebUtil() {
    }

    public static Gson gson() {
        return GSON;
    }

    public static void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter out = resp.getWriter()) {
            out.print(GSON.toJson(body));
        }
    }

    public static void writeOk(HttpServletResponse resp, String message) throws IOException {
        writeJson(resp, 200, ApiResponse.ok(message));
    }

    public static void writeOk(HttpServletResponse resp, String message, Object data) throws IOException {
        writeJson(resp, 200, ApiResponse.ok(message, data));
    }

    public static void writeFail(HttpServletResponse resp, int status, String message) throws IOException {
        writeJson(resp, status, ApiResponse.fail(message));
    }

    public static User currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute(SESSION_USER);
    }

    public static String param(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    /** Generates an order reference like LM1000042. */
    public static String generateOrderRef() {
        int num = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "LM" + num;
    }
}