package com.validation;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ReviewValidator {
    private static ReviewValidator instance = new ReviewValidator();
    private static final String MESSAGE_FORMAT = "[\\p{N}\\p{L}\\p{P}\\s\\n\\r]{1,280}";
    private static final String MARK_FORMAT = "\\d{1,2}";
    private static final int MAX_MARK = 10;
    private static final int MIN_MARK = 1;

    private ReviewValidator() {
    }

    public static ReviewValidator getInstance() {
        return instance;
    }

    public boolean validateMark(String mark) {
        Matcher matcher = Pattern.compile(MARK_FORMAT).matcher(mark);
        if (!matcher.matches()) {
            return false;
        }
        int intMark = Integer.parseInt(mark);
        return intMark >= MIN_MARK && intMark <= MAX_MARK;
    }

    public boolean validateMessage(String message) {
        Matcher matcher = Pattern.compile(MESSAGE_FORMAT).matcher(message);
        return (matcher.matches() && !message.trim().isEmpty()) || message.isEmpty();
    }
}
