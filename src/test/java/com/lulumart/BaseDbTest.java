package com.lulumart;

import com.lulumart.util.DatabaseSeeder;
import com.lulumart.util.DbUtil;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Base class for integration-style tests that share an in-memory H2 database
 * seeded with the same schema.sql / seed.sql used at runtime. The database is
 * dropped and reseeded before every test method so mutations never leak.
 */
public abstract class BaseDbTest {

    protected static final long BUYER_ID = 14;
    protected static final long SELLER_ID = 2;
    protected static final long ADMIN_ID = 1;

    @BeforeAll
    static void setUpPool() {
        DbUtil.buildPool("jdbc:h2:mem:lulumart_test;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");
    }

    @BeforeEach
    void resetAndSeed() {
        try (Connection conn = DbUtil.getConnection();
             Statement st = conn.createStatement()) {
            st.execute("DROP ALL OBJECTS");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DatabaseSeeder.ensureSeeded(DbUtil.dataSource());
    }

    @AfterAll
    static void tearDownDatabase() {
        DbUtil.close();
    }

    protected int update(String sql, Object... args) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             var ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        }
    }

    protected Long queryLong(String sql, Object... args) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             var ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }
}