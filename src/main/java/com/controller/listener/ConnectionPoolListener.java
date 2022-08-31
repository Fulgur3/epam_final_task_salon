package com.controller.listener;

import com.pool.ConnectionPool;
import com.pool.PoolException;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConnectionPoolListener implements ServletContextListener {
    private static Logger logger = LogManager.getLogger();

    /*
            * Calls connection pool instance to initialize it
     *
             * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.log(Level.INFO, "Creating connection pool...");
        ConnectionPool.getInstance();
    }

    /*
            * Destroys connection pool
     *
             * @param servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            ConnectionPool.getInstance().closeConnectionPool();
        } catch (PoolException e) {
            logger.fatal("Can't close connection pool", e);
            throw new RuntimeException("Can't close connection pool", e);
        }
        AbandonedConnectionCleanupThread.checkedShutdown();
    }

}