package com.soni.usermanagement.methods;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PasswordValidator
 */
public class PasswordValidator {
 
    private static final String PASSWORD_REGEX = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,20})";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    // Be between 8 and 20 characters long
    // Contain at least one digit.
    // Contain at least one lower case character.
    // Contain at least one upper case character.
    // Contain at least one special character from [ @ # $ % ! . ]

    public static boolean validate(final String password) {
        
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
 
    }
}