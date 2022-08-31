package com.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Collection;

public class ConnectionPool {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionPool instance;
    private static AtomicBoolean isCreated = new AtomicBoolean(false);
    private static ReentrantLock lock = new ReentrantLock();
    private BlockingQueue<ProxyConnection> availableConnections;
    private Deque<ProxyConnection> unavailableConnections;

    private ConnectionPool() {
        ConnectionManager.getInstance().buildPool();
        initPool();
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                    isCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    /**
     * Creates connections containers and creates available connections
     * If available connections number is smaller than initial pool size, tries to create missing
     * connections
     * If no connections were created, throws runtime exception
     */
    private void initPool() {
        availableConnections = new LinkedBlockingQueue<>();
        unavailableConnections = new ArrayDeque<>();
        for (int i = 0; i < ConnectionManager.getInstance().getPoolSize(); i++) {
            try {
                createConnection();
            } catch (SQLException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
        if (availableConnections.size() < ConnectionManager.INITIAL_POOL_SIZE) {
            for (int i = availableConnections.size() - 1;
                 i < ConnectionManager.getInstance().getPoolSize(); i++) {
                try {
                    createConnection();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
        }
        if (availableConnections.isEmpty()) {
            logger.fatal("Couldn't create any connections");
            throw new RuntimeException("Couldn't create any connections");
        } else if (availableConnections.size() == ConnectionManager.INITIAL_POOL_SIZE) {
            logger.log(Level.INFO, "Successfully initialized connection pool");
        }
    }

    /**
     * If no connections are currently available but number of connections doesn't exceed the maximum,
     * creates a new connection
     * Moves connection from available to unavailable and returns it
     *
     * @return
     *
     * @throws PoolException
     */
    public ProxyConnection takeConnection() throws PoolException {
        ProxyConnection connection = null;
        if (availableConnections.isEmpty() &&
                unavailableConnections.size() >= ConnectionManager.INITIAL_POOL_SIZE &&
                unavailableConnections.size() < ConnectionManager.MAX_POOL_SIZE) {
            createConnection();
        }
        try {
            connection = availableConnections.take();
            unavailableConnections.add(connection);
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    /**
     * Creates a new connection and adds it to available
     *
     * @throws PoolException
     */
    private void createConnection() throws PoolException {
        try {
            availableConnections.add(ConnectionManager.getInstance().createConnection());
        } catch (SQLException e) {
            throw new PoolException("Couldn't create connection: " + e.getMessage(), e);
        }
    }

    /**
     * Removes connection from unavailable and adds to available
     *
     * @param connection
     *
     * @throws PoolException
     */
    public void releaseConnection(ProxyConnection connection) throws PoolException {
        if (connection != null) {
            try {
                if (!connection.getAutoCommit()) {
                    connection.setAutoCommit(true);
                }
                unavailableConnections.remove(connection);
                availableConnections.put(connection);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, e);
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
                throw new PoolException("Couldn't release connection: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Closes all connections in the pool
     *
     * @throws PoolException
     */
    public void closeConnectionPool() throws PoolException {
        ProxyConnection connection;
        int currentPoolSize = availableConnections.size() + unavailableConnections.size();
        for (int i = 0; i < currentPoolSize; i++) {
            try {
                connection = availableConnections.take();
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
                connection.closeConnection();
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, e);
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
                throw new PoolException("Couldn't close connection: " + e.getMessage(), e);
            }
        }
        deregisterDrivers();
    }

    /**
     * Deregisters drivers
     */
    private void deregisterDrivers() {
        DriverManager.drivers().forEach(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("Couldn't deregister driver: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Returns number of available connections
     *
     * @return
     */
    public int getAvailableConnectionNumber() {
        return availableConnections.size();
    }
}
