package com.util;

import java.util.ResourceBundle;

public class MailComposer {
    private static MailComposer instance = new MailComposer();
    private static final String BUNDLE_NAME = "message";
    private static ResourceBundle bundle;
    private static final String RESET_PASSWORD_MESSAGE_NAME = "message.cbb.password.reset.message";
    private static final String RESET_PASSWORD_MESSAGE_THEME_NAME = "message.cbb.password.reset.theme";
    private static final String ORDER_REMINDER_MESSAGE_NAME = "message.cbb.order.reminder.message";
    private static final String ORDER_REMINDER_MESSAGE_THEME_NAME = "message.cbb.order.reminder.theme";


    private MailComposer() {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    }

    public static MailComposer getInstance() {
        return instance;
    }

    public static String getResetPasswordMessage(String newPassword) {
        return String.format(bundle.getString(RESET_PASSWORD_MESSAGE_NAME), newPassword);
    }

    public static String getResetPasswordMessageTheme() {
        return bundle.getString(RESET_PASSWORD_MESSAGE_THEME_NAME);
    }

    public static String getOrderReminderMessage(String userLogin) {
        return String.format(bundle.getString(ORDER_REMINDER_MESSAGE_NAME), userLogin);
    }

    public static String getOrderReminderMessageTheme() {
        return bundle.getString(ORDER_REMINDER_MESSAGE_THEME_NAME);
    }

}
