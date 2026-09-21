package com.lulumart.service;

import com.lulumart.dao.UserDao;
import com.lulumart.exception.BadRequestException;
import com.lulumart.exception.UnauthorizedException;
import com.lulumart.model.Role;
import com.lulumart.model.User;
import com.lulumart.util.PasswordUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest_Mockito {

    @Mock
    private UserDao userDao;

    @Test
    void loginRejectsUnknownEmail() {
        when(userDao.findByEmail("ghost@example.com")).thenReturn(java.util.Optional.empty());
        AuthService service = new AuthService(userDao);
        assertThrows(UnauthorizedException.class, () -> service.login("ghost@example.com", "Whatever@1"));
    }

    @Test
    void loginRejectsWrongPassword() {
        User user = User.of(14, "Riya", "buyer@lulumart.com", Role.BUYER);
        user.setPasswordHash(PasswordUtil.hash("Buyer@123"));
        when(userDao.findByEmail("buyer@lulumart.com")).thenReturn(java.util.Optional.of(user));
        AuthService service = new AuthService(userDao);
        assertThrows(UnauthorizedException.class, () -> service.login("buyer@lulumart.com", "Nope@1234"));
    }

    @Test
    void registerChecksDuplicateBeforeInsert() {
        when(userDao.emailExists(anyString())).thenReturn(true);
        AuthService service = new AuthService(userDao);
        assertThrows(BadRequestException.class,
                () -> service.register("Dup", "dup@example.com", "Buyer@2026", "Buyer@2026", "BUYER"));
    }

    @Test
    void registerUsesProvidedRole() {
        when(userDao.emailExists(anyString())).thenReturn(false);
        when(userDao.insert(org.mockito.ArgumentMatchers.any(User.class)))
                .thenAnswer(inv -> {
                    User u = inv.getArgument(0);
                    u.setId(999);
                    return u;
                });
        AuthService service = new AuthService(userDao);
        User created = service.register("Frank", "frank@example.com", "Frank#2026", "Frank#2026", "SELLER");
        assertEquals(999, created.getId());
        assertEquals(Role.SELLER, created.getRole());
    }
}