package com.dao.impl;

import com.entity.Activity;
import com.entity.ActivityBuilder;
import com.dao.ActivityDao;
import com.pool.ConnectionPool;
import com.pool.ProxyConnection;
import com.pool.PoolException;
import com.dao.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ActivityDaoImpl implements ActivityDao {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionPool pool = ConnectionPool.getInstance();
    private static final String DB_ACTIVITY_ID_FIELD = "service_id";
    private static final String DB_ACTIVITY_NAME_FIELD = "service_name";
    private static final String DB_ACTIVITY_DESCRIPTION_FIELD = "service_description";
    private static final String DB_ACTIVITY_PRICE_FIELD = "service_price";
    private static final String DB_ACTIVITY_STATUS_FIELD = "service_status";
    private static final String INSERT_ACTIVITY = "INSERT INTO service " +
            "(service_name, service_description, service_price) " +
            "VALUES (?, ?, ?)";
    private static final String FIND_ACTIVITY_BY_NAME = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service " +
            "WHERE service.service_name = ?";
    private static final String FIND_ACTIVITY_BY_ID = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service " +
            "WHERE service.service_id = ?";
    private static final String FIND_ALL_ACTIVITIES = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service ";
    private static final String FIND_AVAILABLE_ACTIVITIES = "SELECT service.service_id, " +
            "service.service_name, service.service_description, service.service_price, service.service_status " +
            "FROM service " +
            "WHERE service.service_status = 'available'";
    private static final String UPDATE_ACTIVITY = "UPDATE service " +
            "SET service_name = ?, service_description = ?, service_price = ?, service_status = ? " +
            "WHERE service_id = ?";

    @Override
    public void addActivity(Activity activity) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            String name = activity.getName();
            String description = activity.getDescription();
            BigDecimal price = activity.getPrice();
            preparedStatement = connection.prepareStatement(INSERT_ACTIVITY);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setBigDecimal(3, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to add activity: " + e.getMessage(), e);
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
    public Optional<Activity> findActivityById(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<Activity> result = Optional.empty();
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(FIND_ACTIVITY_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = Optional.of(new ActivityBuilder()
                        .setId(id)
                        .setName(resultSet.getString(DB_ACTIVITY_NAME_FIELD))
                        .setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD).trim())
                        .setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD))
                        .setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD))
                        .create());
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find activity by id" + e.getMessage(), e);
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
    public Optional<Activity> findActivityByName(String name) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<Activity> result = Optional.empty();
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(FIND_ACTIVITY_BY_NAME);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = Optional.of(new ActivityBuilder()
                        .setId(resultSet.getInt(DB_ACTIVITY_ID_FIELD))
                        .setName(name)
                        .setDescription(resultSet.getString(DB_ACTIVITY_DESCRIPTION_FIELD).trim())
                        .setPrice(resultSet.getBigDecimal(DB_ACTIVITY_PRICE_FIELD))
                        .setStatus(resultSet.getString(DB_ACTIVITY_STATUS_FIELD))
                        .create()
                );
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find activity by name" + e.getMessage(), e);
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
    public List<Activity> findAllActivities() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        Statement statement = null;
        List<Activity> activityList = new ArrayList<>();
        try {
            connection = pool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_ACTIVITIES);
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
            throw new DaoException("Failed to find activities" + e.getMessage(), e);
        } finally {
            closeStatement(statement);
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
    public List<Activity> findAvailableActivities() throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        Statement statement = null;
        List<Activity> activityList = new LinkedList<>();
        try {
            connection = pool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_AVAILABLE_ACTIVITIES);
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
            throw new DaoException("Failed to find available activities" + e.getMessage(), e);
        } finally {
            closeStatement(statement);
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
    public void updateActivity(int id, String name, String description,
                               BigDecimal price, String status) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_ACTIVITY);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setBigDecimal(3, price);
            preparedStatement.setString(4, status);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to update activity:  " + e.getMessage(), e);
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

