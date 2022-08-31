package com.validation;

import com.entity.Activity;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ActivityValidator {
    private static ActivityValidator instance= new ActivityValidator();
    private static final String NAME_FORMAT = "[\\p{L}\\s\\-]{2,40}";
    private static final String DESCRIPTION_FORMAT = "[\\p{N}\\p{L}\\p{P}\\s\\n\\r]{1,280}";
    private static final String PRICE_FORMAT = "[1-9]\\d{0,5}\\.?\\d{0,2}";


    private ActivityValidator() {

    }

    public static ActivityValidator getInstance(){return instance;}

    public boolean validateStatus(String status){
        return Arrays.stream(Activity.Status.values())
                .anyMatch(orderStatus -> orderStatus.getName().equalsIgnoreCase(status));
    }

    public boolean validateName(String name){
        Matcher matcher= Pattern.compile(NAME_FORMAT).matcher(name);
        return matcher.matches() && !name.trim().isEmpty();
    }

    public boolean validateDescription(String description){
        Matcher matcher= Pattern.compile(DESCRIPTION_FORMAT).matcher(description);
        return matcher.matches() && !description.trim().isEmpty();
    }

    public boolean validatePrice(String price){
        Matcher matcher= Pattern.compile(PRICE_FORMAT).matcher(price);
        return matcher.matches() && !price.trim().isEmpty();
    }
}
