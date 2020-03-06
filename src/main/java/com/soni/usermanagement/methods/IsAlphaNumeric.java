package com.soni.usermanagement.methods;

import java.util.regex.Pattern;

public class IsAlphaNumeric {

    private static Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");

    public static boolean isAlphaNumeric(String s){
        return p.matcher(s).find();
    }
}