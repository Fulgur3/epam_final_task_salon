package com.util;

import org.apache.commons.text.RandomStringGenerator;

public class PasswordGenerator {
    private static final int DEFAULT_LENGTH = 16;

    public static String generatePassword(){
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .build();
        return generator.generate(DEFAULT_LENGTH);
    }

    private PasswordGenerator(){}
}