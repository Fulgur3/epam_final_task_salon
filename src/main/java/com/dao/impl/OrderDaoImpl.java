package com.dao.impl;

import com.entity.*;
import com.dao.OrderDao;
import com.pool.ConnectionPool;
import com.pool.ProxyConnection;
import com.pool.PoolException;
import com.dao.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionPool pool = ConnectionPool.getInstance();
    private static final String DB_ORDER_ID_FIELD = "order_id";
    private static final String DB_ORDER_USER_ID_FIELD = "user_id";
    private static final String DB_ORDER_STATUS_FIELD = "order_status";
    private static final String DB_ORDER_TIME_FIELD = "order_time";
    private static final String DB_ORDER_PRICE_FIELD = "order_price";
    private static final String DB_ORDER_PAID_FIELD = "paid";
    private static final String DB_ACTIVITY_ID_FIELD = "service_id";
    private static final String DB_ACTIVITY_NAME_FIELD = "service_name";
    private static final String DB_ACTIVITY_DESCRIPTION_FIELD = "service_description";
    private static final String DB_ACTIVITY_PRICE_FIELD = "service_price";
    private static final String DB_ACTIVITY_STATUS_FIELD = "service_status";
    private static final String DB_USER_EMAIL_FIELD = "user_email";
    private static final String DB_USER_LOGIN_FIELD = "login";
    private static final String INSERT_ORDER_INFO = "INSERT INTO order_info " +
            "(user_id, order_time) " +
            "VALUES (?, ?)";
    private static final String INSERT_ORDER_ACTIVITIES = "INSERT INTO order_services " +
            "(order_id, service_id) " +
            "VALUES (?, ?)";
    private static final String CANCEL_ORDER = "UPDATE order_info " +
            "SET order_status = 'cancelled', order_time = order_time " +
            "WHERE order_id = ? AND DATE(order_time) >= CURRENT_DATE";
    private static final String UPDATE_ORDER_STATUS = "UPDATE order_info " +
            "SET order_status = ?, order_time = order_time WHERE order_id = ?";
    private static final String FIND_ALL_ORDERS = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, " +
            "order_info.order_time, order_info.order_price, order_info.paid, user.login " +
            "FROM order_info " +
            "JOIN user " +
            "ON order_info.user_id = user.user_id " +
            "ORDER BY order_info.order_id ASC LIMIT ?,?";
    private static final String FIND_ORDER_BY_ID = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time, order_info.order_price, order_info.paid " +
            "FROM order_info " +
            "WHERE order_info.order_id = ?";
    private static final String FIND_ORDERS_BY_USER_ID = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time, order_info.order_price, order_info.paid " +
            "FROM order_info " +
            "WHERE order_info.user_id = ? ";
    private static final String FIND_ORDER_BY_USER_ID_AND_TIME = "SELECT order_info.order_id, " +
            "order_info.user_id, order_info.order_status, order_info.order_time, order_info.order_price, order_info.paid " +
            "FROM order_info " +
            "WHERE order_info.user_id = ? AND " +
            "DATE(order_info.order_time) = DATE(?) AND order_status != 'cancelled'";
    private static final String FIND_ORDERS_BY_USER_ID_LIMITED = FIND_ORDERS_BY_USER_ID +
            " LIMIT ?,? ";
    private static final String FIND_ACTIVITIES_BY_ORDER_ID = "SELECT service.service_id, " +
            "service_name, service_description, service_status, service_price FROM service " +
            "JOIN order_services ON service.service_id = order_services.service_id " +
            "WHERE order_id = ?";
    private static final String FIND_EMAILS_FOR_UPCOMING_ORDERS =
            "SELECT DISTINCT user.user_email, order_info.order_id " +
                    "FROM user " +
                    "LEFT JOIN order_info " +
                    "ON order_info.user_id = user.user_id " +
                    "WHERE DATE(order_time) <= CURRENT_DATE + INTERVAL 1 DAY " +
                    "AND order_info.order_status = 'confirmed' AND order_info.reminded = 0";
    private static final String SET_ORDER_REMINDED = "UPDATE order_info " +
            "SET reminded = 1, order_time = order_time WHERE order_id = ?";
    private static final String SET_ORDER_PRICE = "UPDATE order_info " +
            "SET order_price = ?, order_time = order_time WHERE order_id = ?";
    private static final String RETURN_ORDER_MONEY = "UPDATE card " +
            "JOIN order_info " +
            "ON order_info.order_id = ? " +
            "SET paid = 0, money = money + order_price, order_info.order_time = order_info.order_time " +
            "WHERE card.user_id = order_info.user_id " +
            "AND order_info.paid = 1 " +
            "AND order_info.order_status = 'confirmed'";
    private static final String PAY_FOR_ORDER = "UPDATE card " +
            "JOIN order_info " +
            "ON order_info.order_id = ? " +
            "SET paid = 1, money = money - order_price, " +
            "order_info.order_time = order_info.order_time," +
            "order_info.order_status = 'confirmed'  " +
            "WHERE card.user_id = order_info.user_id " +
            "AND paid = 0 " +
            "AND DATE(order_time) >= CURRENT_DATE " +
            "AND (order_status = 'pending' " +
            "OR order_status = 'confirmed')";
    private static final String COUNT_ORDERS = "SELECT DISTINCT COUNT(*) FROM order_info";
    private static final String COUNT_USER_ORDERS = "SELECT DISTINCT COUNT(*) FROM order_info " +
            "WHERE user_id = ?";
    private static final String CANCEL_UNCONFIRMED_OUTDATED_ORDERS = "UPDATE order_info " +
            "SET order_status = 'cancelled', order_time = order_time " +
            "WHERE order_time <= UTC_TIMESTAMP() AND order_status = 'pending'";

    @Override
    public void addOrder(Order order) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);
            int userId = order.getUserId();
            Timestamp time = order.getDateTime();
            List<Activity> activityList = new ArrayList<>();
            for (int i = 0; i < order.activityListSize(); i++) {
                activityList.add(order.getActivity(i));
            }
            preparedStatement = connection.prepareStatement(INSERT_ORDER_INFO,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, userId);
            preparedStatement.setTimestamp(2, time);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int orderId = resultSet.getInt(1);
                order.setId(orderId);
            }
            for (Activity activity : activityList) {
                preparedStatement = connection.prepareStatement(INSERT_ORDER_ACTIVITIES);
                preparedStatement.setInt(1, order.getId());
                preparedStatement.setInt(2, activity.getId());
                preparedStatement.executeUpdate();
            }
            preparedStatement = connection.prepareStatement(SET_ORDER_PRICE);
            preparedStatement.setBigDecimal(1, calculateOrderPrice(order.getActivityList()));
            preparedStatement.setInt(2, order.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    logger.log(Level.INFO, "Encountered an error, made a rollback");
                }
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Couldn't rollback connection: " + ex.getMessage(), ex);
            }
            throw new DaoException("Failed to add order" + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void changeOrderStatus(int id, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);
            if (Order.Status.CANCELLED.getName().equalsIgnoreCase(status)) {
                preparedStatement = connection.prepareStatement(RETURN_ORDER_MONEY);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
            preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    logger.log(Level.INFO, "Encountered an error, made a rollback");
                }
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Couldn't rollback connection: " + ex.getMessage(), ex);
            }
            throw new DaoException("Failed to change order status" + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void cancelOrder(int id) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(RETURN_ORDER_MONEY);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(CANCEL_ORDER);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    logger.log(Level.INFO, "Encountered an error, made a rollback");
                }
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Couldn't rollback connection: " + ex.getMessage(), ex);
            }
            throw new DaoException("Failed to change order status" + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public List<Order> findAllOrders(int startPosition, int numberOfRecords) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(FIND_ALL_ORDERS);
            preparedStatement.setInt(1, startPosition);
            preparedStatement.setInt(2, numberOfRecords);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int currentOrderId = resultSet.getInt(DB_ORDER_ID_FIELD);
                List<Activity> activityList = findActivitiesByOrderId(currentOrderId);
                orderList.add(new OrderBuilder()
                        .setId(currentOrderId)
                        .setUserId(resultSet.getInt(DB_ORDER_USER_ID_FIELD))
                        .setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD))
                        .setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD))
                        .setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD))
                        .setPaid(resultSet.getBoolean(DB_ORDER_PAID_FIELD))
                        .setActivityList(activityList)
                        .setUserLogin(resultSet.getString(DB_USER_LOGIN_FIELD))
                        .create());
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders" + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return orderList;
    }

    @Override
    public Optional<Order> findOrderById(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<Order> result = Optional.empty();
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(FIND_ORDER_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                List<Activity> activityList = findActivitiesByOrderId(id);
                result = Optional.of(new OrderBuilder()
                        .setId(id)
                        .setUserId(resultSet.getInt(DB_ORDER_USER_ID_FIELD))
                        .setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD))
                        .setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD))
                        .setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD))
                        .setPaid(resultSet.getBoolean(DB_ORDER_PAID_FIELD))
                        .setActivityList(activityList)
                        .create());
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find order by id" + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public List<Order> findOrdersByUser(int userId, int startPosition, int numberOfRecords)
            throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(FIND_ORDERS_BY_USER_ID_LIMITED);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, startPosition);
            preparedStatement.setInt(3, numberOfRecords);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int currentOrderId = resultSet.getInt(DB_ORDER_ID_FIELD);
                List<Activity> activityList = findActivitiesByOrderId(currentOrderId);
                orderList.add(new OrderBuilder()
                        .setId(currentOrderId)
                        .setUserId(resultSet.getInt(DB_ORDER_USER_ID_FIELD))
                        .setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD))
                        .setDateTime(resultSet.getTimestamp(DB_ORDER_TIME_FIELD))
                        .setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD))
                        .setPaid(resultSet.getBoolean(DB_ORDER_PAID_FIELD))
                        .setActivityList(activityList)
                        .create());
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders by user" + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return orderList;
    }

    private BigDecimal calculateOrderPrice(List<Activity> activityList) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Activity activity : activityList) {
            totalPrice = totalPrice.add(activity.getPrice());
        }
        return totalPrice;
    }

    @Override
    public List<Activity> findActivitiesByOrderId(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Activity> activityList = new ArrayList<>();
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(FIND_ACTIVITIES_BY_ORDER_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                activityList.add(new ActivityBuilder()
                        .setId(resultSet.getInt(DB_ACTIVITY_ID_FIELD))
                        .setName(resultSet.getString(DB_ACTIVITY_NAME_FIELD))
                        .setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD).trim())
                        .setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD))
                        .setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD))
                        .create());
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find activities by order id" + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return activityList;
    }

    @Override
    public List<String> findEmailsForUpcomingOrders() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        Statement statement;
        PreparedStatement preparedStatement = null;
        List<String> emailList = new ArrayList<>();
        List<Integer> orderIdList = new ArrayList<>();
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_EMAILS_FOR_UPCOMING_ORDERS);
            while (resultSet.next()) {
                emailList.add(resultSet.getString(DB_USER_EMAIL_FIELD));
                orderIdList.add(resultSet.getInt(DB_ORDER_ID_FIELD));
            }
            if (!emailList.isEmpty()) {
                preparedStatement = connection.prepareStatement(SET_ORDER_REMINDED);
                for (int orderId : orderIdList) {
                    preparedStatement.setInt(1, orderId);
                    preparedStatement.executeUpdate();
                }
            } else {
                logger.log(Level.INFO, "No confirmed orders to remind of");
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    logger.log(Level.INFO, "Encountered an error, made a rollback");
                }
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Couldn't rollback connection: " + ex.getMessage(), ex);
            }
            throw new DaoException("Failed to find emails for upcoming orders" + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return emailList;
    }

    public void payForOrder(int orderId) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(PAY_FOR_ORDER);
            preparedStatement.setInt(1, orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to pay for order: " + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int countOrders() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        Statement statement = null;
        int result = 0;
        try {
            connection = pool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(COUNT_ORDERS);
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders" + e.getMessage(), e);
        } finally {
            closeStatement(statement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public int countUserOrders(int userId) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        int result = 0;
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(COUNT_USER_ORDERS);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find orders" + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public void cancelUnconfirmedOutdatedOrders() throws DaoException {
        ProxyConnection connection = null;
        Statement statement = null;
        try {
            connection = pool.takeConnection();
            statement = connection.createStatement();
            statement.executeUpdate(CANCEL_UNCONFIRMED_OUTDATED_ORDERS);
        } catch (SQLException e) {
            throw new DaoException("Failed to cancel unconfirmed orders: " + e.getMessage(), e);
        } finally {
            closeStatement(statement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Optional<Order> findOrderByUserAndTime(int userId, Timestamp timestamp)
            throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(FIND_ORDER_BY_USER_ID_AND_TIME);
            preparedStatement.setInt(1, userId);
            preparedStatement.setTimestamp(2, timestamp);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new OrderBuilder()
                        .setId(resultSet.getInt(DB_ORDER_ID_FIELD))
                        .setUserId(userId)
                        .setStatus(resultSet.getString(DB_ORDER_STATUS_FIELD))
                        .setDateTime(timestamp)
                        .setPrice(resultSet.getBigDecimal(DB_ORDER_PRICE_FIELD))
                        .setPaid(resultSet.getBoolean(DB_ORDER_PAID_FIELD))
                        .create());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to find order by user id and time: " + e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }
}

