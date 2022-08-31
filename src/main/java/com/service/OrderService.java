package com.service;

import com.dao.DaoException;
import com.dao.OrderDao;
import com.entity.Order;
import com.entity.Activity;
import com.dao.impl.OrderDaoImpl;
import com.validation.OrderValidator;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private static final OrderDao orderDao = new OrderDaoImpl();

    OrderService() {
    }

    public boolean addOrder(int userId, Timestamp time, List<Activity> activityList)
            throws ServiceException {
        if (!OrderValidator.getInstance().validateOrderTimeAfterNow(time) ||
                activityList.isEmpty()) {
            return false;
        }
        Order order;
        try {
            order = new Order();
            order.setUserId(userId);
            order.setDateTime(time);
            for (Activity activity : activityList) {
                order.addActivity(activity);
            }
            orderDao.addOrder(order);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Order> findOrderById(int id) throws ServiceException {
        try {
            return orderDao.findOrderById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findOrdersByUser(int userId, int startPosition, int numberOfRecords)
            throws ServiceException {
        try {
            return orderDao.findOrdersByUser(userId, startPosition, numberOfRecords);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean orderExists(int userId, String date, String time) throws ServiceException {
        try {
            Optional<Order> found = orderDao.findOrderByUserAndTime(userId, buildTimestamp(date, time));
            if (found.isPresent()) {
                return !Order.Status.CANCELLED.getName().equalsIgnoreCase(found.get().getStatus());
            }
            return false;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean orderExists(int userId, Timestamp timestamp) throws ServiceException {
        try {
            Optional<Order> found = orderDao.findOrderByUserAndTime(userId, timestamp);
            return found.isPresent();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Order> findAllOrders(int startPosition, int numberOfRecords) throws ServiceException {
        try {
            return orderDao.findAllOrders(startPosition, numberOfRecords);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean changeOrderStatus(int id, String status) throws ServiceException {
        try {
            if (OrderValidator.getInstance().validateStatus(status)) {
                orderDao.changeOrderStatus(id, status);
                return true;
            }
            return false;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void cancelOrder(int id) throws ServiceException {
        try {
            Optional<Order> orderOptional = orderDao.findOrderById(id);
            if (orderOptional.isPresent() &&
                    OrderValidator.getInstance().validateOrderTimeAfterNow
                            (orderOptional.get().getDateTime())) {
                orderDao.cancelOrder(id);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

    }

    public void payForOrder(int orderId) throws ServiceException {
        try {
            Optional<Order> orderOptional = orderDao.findOrderById(orderId);
            if (orderOptional.isPresent() && OrderValidator.getInstance().validateOrderTimeAfterNow
                    (orderOptional.get().getDateTime())) {
                orderDao.payForOrder(orderId);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public int countOrders() throws ServiceException {
        try {
            return orderDao.countOrders();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public int countUserOrders(int userId) throws ServiceException {
        try {
            return orderDao.countUserOrders(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }


    /**
     * Creates a valid timestamp from given date and time
     *
     * @param date Date of the order
     * @param time Time of the order
     *
     * @return Valid timestamp
     */
    public Timestamp buildTimestamp(String date, String time) {
        String orderTime = date + " " + time;
        if (StringUtils.countMatches(orderTime, ":") == 1) {
            orderTime += ":00";
        }
        return Timestamp.valueOf(orderTime.replace("T", " "));
    }
}

