package com.controller.listener;

import com.dao.DaoException;
import com.dao.OrderDao;
import com.dao.impl.OrderDaoImpl;
import com.dao.UserDao;
import com.dao.impl.UserDaoImpl;
import com.entity.User;
import com.util.MailSenderThread;
import  com.util.MailComposer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class OrderReminderThread implements Runnable {
    private static Logger logger = LogManager.getLogger();
    private static final OrderDao orderDao = new OrderDaoImpl();
    private static final UserDao userDao = new UserDaoImpl();

    /**
     * Looks for upcoming orders in 1 day interval and
     * retrieves users emails and names or logins from the database
     * Then runs mail sender threads
     */
    @Override
    public void run() {
        try {
            List<String> emailList = orderDao.findEmailsForUpcomingOrders();
            List<String> userLoginList = new ArrayList<>();
            for (String email : emailList) {
                Optional<User> userOptional = userDao.findUserByEmail(email);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    userLoginList.add((user.getUserName() != null && !user.getUserName().isEmpty()) ?
                            user.getUserName() : user.getLogin());
                }
            }
            for (int i = 0; i < emailList.size(); i++) {
                new MailSenderThread(emailList.get(i), MailComposer.getOrderReminderMessageTheme(),
                        MailComposer.getOrderReminderMessage(userLoginList.get(i))).start();
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Couldn't retrieve email list for upcoming orders: " + e.getMessage());
        }
    }
}