package com.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConnectionManager {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionManager instance = new ConnectionManager();
    private static final String NUMERICAL_PATTERN = "^[1-9]\\d?$";
    static final int INITIAL_POOL_SIZE = 8;
    static final int MAX_POOL_SIZE = 32;
    private static final String BASE_NAME = "database";
    private static final String DATABASE_USER = BASE_NAME + ".user";
    private static final String DATABASE_PASSWORD = BASE_NAME + ".password";
    private static final String DATABASE_POOL_SIZE = BASE_NAME + ".poolSize";
    private static final String DATABASE_URL = BASE_NAME + ".url";
    private String url;
    private String user;
    private String password;
    private int poolSize;

    private ConnectionManager() {
    }

    static ConnectionManager getInstance() {
        return instance;
    }

    void buildPool() {
        register();
    }

    private void register() {
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle(BASE_NAME);
        } catch (MissingResourceException e) {
            logger.fatal("Couldn't process DB property file");
            throw new RuntimeException("Couldn't process DB property file");
        }
        url = bundle.getString(DATABASE_URL);
        user = bundle.getString(DATABASE_USER);
        password = bundle.getString(DATABASE_PASSWORD);
        if (url == null || user == null || password == null) {
            logger.fatal("Not enough data to init connection pool");
            throw new RuntimeException("Not enough data to init connection pool");
        }
        String poolSizeString = bundle.getString(DATABASE_POOL_SIZE);
        Pattern pattern = Pattern.compile(NUMERICAL_PATTERN);
        Matcher matcher = pattern.matcher(poolSizeString);
        poolSize = (matcher.matches() && Integer.parseInt(poolSizeString) <= MAX_POOL_SIZE)
                ? Integer.parseInt(poolSizeString) : INITIAL_POOL_SIZE;
        logger.log(Level.INFO, "Initial connection pool size: " + poolSize);
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            logger.fatal("Couldn't register driver", e);
            throw new RuntimeException("Couldn't register driver", e);
        }
    }

    ProxyConnection createConnection() throws SQLException {
        return new ProxyConnection(DriverManager.getConnection(url, user, password));
    }

    int getPoolSize() {
        return poolSize;
    }
}
