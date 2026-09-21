package com.lulumart.service;

import com.lulumart.dao.UserDao;
import com.lulumart.exception.BadRequestException;
import com.lulumart.exception.UnauthorizedException;
import com.lulumart.model.Role;
import com.lulumart.model.User;
import com.lulumart.util.PasswordUtil;
import com.lulumart.util.ValidationUtil;

public class AuthService {

    private final UserDao userDao;

    public AuthService() {
        this(new UserDao());
    }

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(String name, String email, String password, String confirmPassword, String role) {
        String cleanName = ValidationUtil.require(name, "Name", 100);
        String cleanEmail = ValidationUtil.requireEmail(email);
        String cleanPassword = ValidationUtil.requirePassword(password);
        if (!cleanPassword.equals(confirmPassword)) {
            throw new BadRequestException("Passwords do not match");
        }
        if (!"BUYER".equals(role) && !"SELLER".equals(role)) {
            throw new BadRequestException("Admin accounts cannot be created through registration");
        }
        if (userDao.emailExists(cleanEmail)) {
            throw new BadRequestException("An account with this email already exists");
        }

        User user = new User();
        user.setName(cleanName);
        user.setEmail(cleanEmail);
        user.setPasswordHash(PasswordUtil.hash(cleanPassword));
        user.setRole(Role.valueOf(role));
        return userDao.insert(user);
    }

    public User login(String email, String password) {
        String cleanEmail = ValidationUtil.requireEmail(email);
        if (password == null || password.isEmpty()) {
            throw new BadRequestException("Password is required");
        }
        return userDao.findByEmail(cleanEmail)
                .filter(u -> PasswordUtil.matches(password, u.getPasswordHash()))
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
    }
}