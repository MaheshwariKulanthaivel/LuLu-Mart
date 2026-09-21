package com.lulumart.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages the HikariCP connection pool and the H2 database lifecycle.
 * Configuration is read from db.properties and can be overridden for tests.
 */
public final class DbUtil {

    private static final Logger log = LoggerFactory.getLogger(DbUtil.class);
    private static HikariDataSource dataSource;

    private DbUtil() {
    }

    public static synchronized HikariDataSource dataSource() {
        if (dataSource == null) {
            configureFromProperties();
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource().getConnection();
    }

    private static synchronized void configureFromProperties() {
        Properties props = new Properties();
        try (InputStream in = DbUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("db.properties not found on classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read db.properties", e);
        }
        String url = expand(props.getProperty("db.url"));
        String user = props.getProperty("db.username", "sa");
        String pass = props.getProperty("db.password", "");
        buildPool(url, user, pass);
        log.info("HikariCP pool created for database {}", url);
    }

    /** Allows tests to point the pool at an in-memory H2 instance. */
    public static synchronized void buildPool(String jdbcUrl, String user, String password) {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password == null ? "" : password);
        config.setDriverClassName("org.h2.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30_000);
        config.setPoolName("LuluMartPool");
        dataSource = new HikariDataSource(config);
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    private static String expand(String value) {
        if (value == null) {
            return value;
        }
        String result = value;
        int start;
        while ((start = result.indexOf("${")) >= 0) {
            int end = result.indexOf('}', start);
            if (end < 0) {
                break;
            }
            String key = result.substring(start + 2, end);
            String replacement = System.getProperty(key, "");
            result = result.substring(0, start) + replacement + result.substring(end + 1);
        }
        return result;
    }
}