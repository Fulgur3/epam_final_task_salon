package com.dao;

import com.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserDao extends AbstractDao<User> {
    enum UniqueUserInfo {
        LOGIN, EMAIL, PHONE_NUMBER, CARD_NUMBER
    }


   boolean propertyExists(UniqueUserInfo property, String value) throws DaoException;

    User register(User user) throws DaoException;

    Optional<User> findUserByLoginAndPassword(String login,String password) throws DaoException;

    Optional<User> findUserByIdAndCardNumber(int userId, String cardNumber) throws DaoException;

    Optional<User> findUserById(int id) throws DaoException;

    Optional<User> findUserByEmail(String email) throws DaoException;

    List<User> findAllUsers() throws DaoException;

    void changeUserStatus(int userId, String status) throws DaoException;

    void updateUser(int id, String password, String userName)throws DaoException;

    void updateUserName(int id, String userName) throws DaoException;

    void addMoneyToCard(String cardNumber, BigDecimal amount) throws DaoException;

    BigDecimal findMoneyByCardNumber(String cardNumber) throws DaoException;
  }