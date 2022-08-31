package com.service;

import com.dao.DaoException;
import com.dao.UserDao;
import com.dao.impl.UserDaoImpl;
import com.entity.User;
import com.entity.UserBuilder;
import com.validation.NumberValidator;
import com.validation.UserValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static final UserDao userDao = new UserDaoImpl();

    UserService() {
    }

    public Optional<User> registerUser(String login, String password, String email,
                                       String phoneNumber, String userName, String cardNumber)
            throws ServiceException {
        if (!UserValidator.getInstance().validateUser(login, password, email, phoneNumber,
                userName, cardNumber)) {
            return Optional.empty();
        }
        try {
            return Optional.of(userDao.register(new UserBuilder()
                    .setLogin(login)
                    .setPassword(password)
                    .setEmail(email)
                    .setUserName(userName)
                    .setPhoneNumber(phoneNumber)
                    .setCardNumber(cardNumber)
                    .create()));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserByLoginAndPassword(String login, String password)
            throws ServiceException {
        try {
            return userDao.findUserByLoginAndPassword(login, password);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean loginExists(String login) throws ServiceException {
        try {
            return userDao.propertyExists(UserDaoImpl.UniqueUserInfo.LOGIN, login);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean emailExists(String email) throws ServiceException {
        try {
            return userDao.propertyExists(UserDaoImpl.UniqueUserInfo.EMAIL, email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean phoneNumberExists(String phoneNumber) throws ServiceException {
        try {
            return userDao.propertyExists(UserDaoImpl.UniqueUserInfo.PHONE_NUMBER, phoneNumber);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean cardNumberExists(String cardNumber) throws ServiceException {
        try {
            return userDao.propertyExists(UserDaoImpl.UniqueUserInfo.CARD_NUMBER, cardNumber);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserByEmail(String email) throws ServiceException {
        try {
            return userDao.findUserByEmail(email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserById(int id) throws ServiceException {
        try {
            return userDao.findUserById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<User> findAllUsers() throws ServiceException {
        try {
            return userDao.findAllUsers();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean changeUserStatus(int userId, String status) throws ServiceException {
        if (!UserValidator.getInstance().validateStatus(status)) {
            return false;
        }
        try {
            userDao.changeUserStatus(userId, status);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updateUser(int id, String newPassword, String newUserName)
            throws ServiceException {
        if (!UserValidator.getInstance().validatePassword(newPassword) ||
                !UserValidator.getInstance().validateUserName(newUserName)) {
            return false;
        }
        try {
            userDao.updateUser(id, newPassword, newUserName);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updateUserName(int id, String newUserName) throws ServiceException {
        if (!UserValidator.getInstance().validateUserName(newUserName)) {
            return false;
        }
        try {
            userDao.updateUserName(id, newUserName);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<User> findUserByIdAndCard(int userId, String cardNumber)
            throws ServiceException {
        try {
            return userDao.findUserByIdAndCardNumber(userId, cardNumber);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean addMoneyToCard(String cardNumber, String money) throws ServiceException {
        if (!NumberValidator.getInstance().validateMoney(money)) {
            return false;
        }
        try {
            userDao.addMoneyToCard(cardNumber, new BigDecimal(money));
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public BigDecimal findMoneyByCardNumber(String cardNumber) throws ServiceException {
        try {
            return userDao.findMoneyByCardNumber(cardNumber);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
