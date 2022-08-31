package com.dao;

import com.entity.Order;
import com.entity.Activity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface OrderDao extends AbstractDao<Order>{

    void addOrder(Order order) throws DaoException;

    void changeOrderStatus(int orderId, String status) throws DaoException;

    void cancelOrder(int orderId) throws DaoException;

    Optional<Order> findOrderById(int id) throws DaoException;

    List<Order> findAllOrders(int startPosition, int numberOfRecords) throws DaoException;

    List<Order> findOrdersByUser(int userId, int startPosition, int numberOfRecords) throws DaoException;

    Optional<Order> findOrderByUserAndTime(int userId, Timestamp timestamp) throws DaoException;

    List<Activity> findActivitiesByOrderId(int id) throws DaoException;

    List<String> findEmailsForUpcomingOrders() throws DaoException;

    void payForOrder(int orderId) throws DaoException;

    int countOrders() throws DaoException;

    int countUserOrders(int userId) throws DaoException;

    void cancelUnconfirmedOutdatedOrders() throws DaoException;
}
