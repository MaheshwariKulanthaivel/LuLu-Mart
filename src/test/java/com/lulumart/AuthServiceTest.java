package com.lulumart;

import com.lulumart.exception.BadRequestException;
import com.lulumart.exception.UnauthorizedException;
import com.lulumart.model.User;
import com.lulumart.service.AuthService;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceTest extends BaseDbTest {

    private final AuthService authService = new AuthService();

    @Test
    void registersNewBuyerAndLogsIn() {
        User created = authService.register("Test Buyer", "test.buyer@example.com", "Buyer@2026", "Buyer@2026", "BUYER");
        assertNotNull(created.getId());
        assertEquals("BUYER", created.getRole().name());

        User loggedIn = authService.login("test.buyer@example.com", "Buyer@2026");
        assertEquals(created.getId(), loggedIn.getId());
    }

    @Test
    void rejectsDuplicateEmail() {
        assertThrows(BadRequestException.class,
                () -> authService.register("Dup", "buyer@lulumart.com", "Buyer@2026", "Buyer@2026", "BUYER"));
    }

    @Test
    void rejectsPasswordMismatch() {
        assertThrows(BadRequestException.class,
                () -> authService.register("Mismatch", "mm@example.com", "Buyer@2026", "Different@2026", "BUYER"));
    }

    @Test
    void rejectsAdminRegistration() {
        assertThrows(BadRequestException.class,
                () -> authService.register("Hacker", "hacker@example.com", "Buyer@2026", "Buyer@2026", "ADMIN"));
    }

    @Test
    void rejectsWrongPassword() {
        assertThrows(UnauthorizedException.class, () -> authService.login("buyer@lulumart.com", "wrong-password"));
    }
}