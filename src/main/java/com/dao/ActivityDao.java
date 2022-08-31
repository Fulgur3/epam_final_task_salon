package com.dao;

import com.entity.Activity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

public interface ActivityDao extends AbstractDao<Activity> {

    void addActivity(Activity activity)throws DaoException;

    void updateActivity(int id, String name, String description, BigDecimal price, String status) throws DaoException;

    Optional<Activity> findActivityById(int id) throws DaoException;

    Optional<Activity> findActivityByName(String name) throws DaoException;

    List<Activity> findAllActivities() throws DaoException;

    List<Activity> findAvailableActivities() throws DaoException;
}
