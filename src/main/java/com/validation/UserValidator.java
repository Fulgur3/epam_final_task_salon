package com.validation;
import com.entity.User;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private static UserValidator instance = new UserValidator();
    private static final String LOGIN_FORMAT = "[\\w]{4,20}";
    private static final String PASSWORD_FORMAT = "[\\w]{6,32}";
    private static final String EMAIL_FORMAT =
            "([\\w\\-\\.]+)@([\\w\\-\\.]+)\\.([a-zA-Z]{2,5})";
    private static final int MAX_EMAIL_LENGTH = 50;
    private static final String PHONE_NUMBER_FORMAT = "\\+(\\d{12})";
    private static final String USER_NAME_FORMAT = "[\\p{L}\\s]{2,40}";
    private static final String CARD_NUMBER_FORMAT = "\\d{16}";

    private UserValidator() {
    }

    public static UserValidator getInstance() {
        return instance;
    }

    public boolean validateUser(String login, String password, String email, String phoneNumber,
                                String userName, String cardNumber) {
        return validateLogin(login) && validatePassword(password) && validateEmail(email) &&
                validatePhoneNumber(phoneNumber) && validateUserName(userName) &&
                validateCardNumber(cardNumber);

    }

    public boolean validateLogin(String login) {
        Matcher matcher = Pattern.compile(LOGIN_FORMAT).matcher(login);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        Matcher matcher = Pattern.compile(PASSWORD_FORMAT).matcher(password);
        return matcher.matches();
    }

    public boolean validateEmail(String email) {
        Matcher matcher = Pattern.compile(EMAIL_FORMAT).matcher(email);
        return matcher.matches() && email.length() <= MAX_EMAIL_LENGTH;
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        Matcher matcher = Pattern.compile(PHONE_NUMBER_FORMAT).matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean validateUserName(String userName) {
        Matcher matcher = Pattern.compile(USER_NAME_FORMAT).matcher(userName);
        return userName.isEmpty() || (matcher.matches() && !userName.trim().isEmpty());
    }

    public boolean validateCardNumber(String cardNumber) {
        Matcher matcher = Pattern.compile(CARD_NUMBER_FORMAT).matcher(cardNumber);
        return matcher.matches();
    }

    public boolean validateStatus(String status) {
        return Arrays.stream(User.Status.values())
                .anyMatch(orderStatus -> orderStatus.getName().equalsIgnoreCase(status));
    }
}
