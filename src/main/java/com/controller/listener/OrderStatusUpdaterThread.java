package com.controller.listener;

import com.dao.DaoException;
import com.dao.OrderDao;
import com.dao.impl.OrderDaoImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class OrderStatusUpdaterThread implements Runnable {
    private static Logger logger = LogManager.getLogger();
    private static final OrderDao orderDao = new OrderDaoImpl();

    /**
     * Cancels outdated orders that have 'pending' status
     */
    @Override
    public void run() {
        logger.log(Level.INFO, "Cancelling unconfirmed outdated orders...");
        try {
            orderDao.cancelUnconfirmedOutdatedOrders();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Couldn't cancel outdated orders: " + e.getMessage());
        }
    }
}