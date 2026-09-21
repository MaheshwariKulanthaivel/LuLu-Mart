package com.lulumart.listener;

import com.lulumart.util.DatabaseSeeder;
import com.lulumart.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Initializes the connection pool and provisions schema + seed data on startup. */
@WebListener
public class AppListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DbUtil.dataSource();
            DatabaseSeeder.ensureSeeded(DbUtil.dataSource());
            log.info("Lulu Mart database initialized and seeded");
        } catch (Exception e) {
            log.error("Lulu Mart failed to initialize the database", e);
            throw new IllegalStateException("Database initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DbUtil.close();
        log.info("Lulu Mart shut down; connection pool closed");
    }
}