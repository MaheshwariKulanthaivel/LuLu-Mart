package com.lulumart.util;

import com.lulumart.exception.BadRequestException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationUtilTest {

    @Test
    void requireRejectsBlank() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.require("  ", "Name", 100));
    }

    @Test
    void requireRejectsTooLong() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.require("x".repeat(101), "Name", 100));
    }

    @Test
    void requireEmailValidatesFormat() {
        assertEquals("a@b.co", ValidationUtil.requireEmail(" a@b.co "));
        assertThrows(BadRequestException.class, () -> ValidationUtil.requireEmail("not-an-email"));
    }

    @Test
    void requirePasswordEnforcesStrength() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.requirePassword("short"));
        assertThrows(BadRequestException.class, () -> ValidationUtil.requirePassword("abcdefgh"));
        assertEquals("Pets#2026", ValidationUtil.requirePassword("Pets#2026"));
    }

    @Test
    void requirePriceAcceptsValidDecimals() {
        assertEquals(new java.math.BigDecimal("2450.00"), ValidationUtil.requirePrice("2450"));
        assertThrows(BadRequestException.class, () -> ValidationUtil.requirePrice("-5"));
        assertThrows(BadRequestException.class, () -> ValidationUtil.requirePrice("abc"));
    }

    @Test
    void requireStockRejectsNegative() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.requireStock("-1"));
        assertEquals(12, ValidationUtil.requireStock("12"));
    }

    @Test
    void positiveLongRejectsZeroAndNonNumbers() {
        assertThrows(BadRequestException.class, () -> ValidationUtil.positiveLong("0", "Id"));
        assertThrows(BadRequestException.class, () -> ValidationUtil.positiveLong("abc", "Id"));
        assertEquals(42L, ValidationUtil.positiveLong("42", "Id"));
    }

    @Test
    void optionalTruncates() {
        assertEquals("abc", ValidationUtil.optional("  abc  ", 10));
        assertNull(ValidationUtil.optional("  ", 10));
        assertTrue(ValidationUtil.optional("abc".repeat(20), 10).length() <= 10);
    }
}