package com.lulumart.util;

import com.lulumart.exception.BadRequestException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PASSWORD_STRENGTH_PATTERN =
            Pattern.compile("(?=.*[A-Za-z])(?=.*\\d)");

    private ValidationUtil() {
    }

    public static String require(String value, String field, int maxLen) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(field + " is required");
        }
        String v = value.trim();
        if (v.length() > maxLen) {
            throw new BadRequestException(field + " must be at most " + maxLen + " characters");
        }
        return v;
    }

    public static String optional(String value, int maxLen) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String v = value.trim();
        return v.length() > maxLen ? v.substring(0, maxLen) : v;
    }

    public static String requireEmail(String value) {
        String email = require(value, "Email", 150);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestException("Please enter a valid email address");
        }
        return email.toLowerCase();
    }

    public static String requirePassword(String value) {
        String pass = require(value, "Password", 72);
        if (pass.length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }
        if (!PASSWORD_STRENGTH_PATTERN.matcher(pass).find()) {
            throw new BadRequestException("Password must contain at least one letter and one number");
        }
        return pass;
    }

    public static BigDecimal requirePrice(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException("Price is required");
        }
        try {
            BigDecimal price = new BigDecimal(value.trim());
            if (price.signum() < 0) {
                throw new BadRequestException("Price cannot be negative");
            }
            return price.setScale(2, java.math.RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Price must be a valid number");
        }
    }

    public static int requireStock(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException("Stock quantity is required");
        }
        try {
            int stock = Integer.parseInt(value.trim());
            if (stock < 0) {
                throw new BadRequestException("Stock quantity cannot be negative");
            }
            return stock;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Stock quantity must be a whole number");
        }
    }

    public static int requirePositiveInt(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(field + " is required");
        }
        try {
            int n = Integer.parseInt(value.trim());
            if (n <= 0) {
                throw new BadRequestException(field + " must be greater than zero");
            }
            return n;
        } catch (NumberFormatException e) {
            throw new BadRequestException(field + " must be a whole number");
        }
    }

    public static long positiveLong(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(field + " is required");
        }
        try {
            long n = Long.parseLong(value.trim());
            if (n <= 0) {
                throw new BadRequestException(field + " must be greater than zero");
            }
            return n;
        } catch (NumberFormatException e) {
            throw new BadRequestException(field + " is invalid");
        }
    }

    public static int optionalPositiveInt(String value, int fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        try {
            return Math.max(Integer.parseInt(value.trim()), 1);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}