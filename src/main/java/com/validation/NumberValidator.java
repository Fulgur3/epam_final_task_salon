package com.validation;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidator {
    private static NumberValidator instance = new NumberValidator();
    private static final String MONEY_FORMAT = "[1-9]\\d{0,5}\\.?\\d{0,2}";
    private static final String NUMBER_FORMAT = "[1-9]\\d{0,2}";
    private static final BigDecimal MIN_MONEY = BigDecimal.valueOf(0.01);
    private static final int MIN_NUMBER = 1;

    private NumberValidator() {
    }

    public static NumberValidator getInstance() {
        return instance;
    }

    public boolean validateMoney(String money) {
        Matcher matcher = Pattern.compile(MONEY_FORMAT).matcher(money);
        return matcher.matches() && new BigDecimal(money).compareTo(MIN_MONEY) >= 0;
    }

    public boolean validateNumber(String value) {
        Matcher matcher = Pattern.compile(NUMBER_FORMAT).matcher(value);
        if (!matcher.matches()) {
            return false;
        }
        int intId = Integer.parseInt(value);
        return intId >= MIN_NUMBER;
    }
}
