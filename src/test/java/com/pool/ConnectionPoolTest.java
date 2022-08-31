package com.pool;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class ConnectionPoolTest {
    private static ConnectionPool pool = ConnectionPool.getInstance();
    private ProxyConnection connection;

    @Test
    public void initConnectionPool() {
        Assert.assertFalse(pool == null);
    }

    @Test
    public void getConnection() throws PoolException{
        connection = pool.takeConnection();
        int availableConnections = pool.getAvailableConnectionNumber();
        Assert.assertEquals(7, availableConnections);
    }

    @Test
    public void releaseConnection() throws PoolException{
        pool.releaseConnection(connection);
        int availableConnections = pool.getAvailableConnectionNumber();
        Assert.assertEquals(8, availableConnections);
    }

    @AfterClass
    public void closePool() throws PoolException{
        pool.closeConnectionPool();
    }
}
