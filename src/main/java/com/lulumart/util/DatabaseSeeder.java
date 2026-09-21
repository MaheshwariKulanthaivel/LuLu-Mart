package com.lulumart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Runs schema.sql and seed.sql (idempotent) and injects real BCrypt hashes
 * for the built-in demo accounts.
 */
public final class DatabaseSeeder {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private DatabaseSeeder() {
    }

    public static synchronized void ensureSeeded(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            executeScript(conn, "/schema.sql");
            if (!isSeeded(conn)) {
                executeScript(conn, "/seed.sql");
                log.info("Seed data inserted");
            } else {
                log.info("Database already seeded, skipping seed");
            }
            applyDemoPasswords(conn);
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Failed to initialize database", e);
        }
    }

    private static boolean isSeeded(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) AS c FROM users")) {
            rs.next();
            return rs.getLong("c") > 0;
        }
    }

    private static void executeScript(Connection conn, String resource) throws SQLException, IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                DatabaseSeeder.class.getResourceAsStream(resource), StandardCharsets.UTF_8))) {
            StringBuilder current = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                    continue;
                }
                current.append(line).append('\n');
                if (current.toString().trim().endsWith(";")) {
                    try (Statement st = conn.createStatement()) {
                        st.execute(current.toString());
                    }
                    current.setLength(0);
                }
            }
            if (!current.toString().trim().isEmpty()) {
                throw new SQLException("Unterminated SQL statement in " + resource);
            }
        }
        log.debug("Executed {}", resource);
    }

    private static String readResource(String resource) throws IOException {
        try (InputStream in = DatabaseSeeder.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resource);
            }
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            }
            return sb.toString();
        }
    }

    /** Secret demo accounts: Admin@123, Seller@123, Buyer@123. */
    private static void applyDemoPasswords(Connection conn) {
        updatePasswordForRole(conn, "ADMIN", "Admin@123");
        updatePasswordForRole(conn, "SELLER", "Seller@123");
        updatePasswordForRole(conn, "BUYER", "Buyer@123");
    }

    private static void updatePasswordForRole(Connection conn, String role, String rawPassword) {
        String hash = PasswordUtil.hash(rawPassword);
        String sql = "UPDATE users SET password_hash = ? WHERE role = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setString(2, role);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to set demo password for role {}", role, e);
        }
    }
}