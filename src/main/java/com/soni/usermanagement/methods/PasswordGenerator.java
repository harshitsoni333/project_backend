package com.soni.usermanagement.methods;

import java.util.Random;

/**
 * PasswordGenerator
 */
public class PasswordGenerator {

    public static String generatePassword() {
        
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$%";
        String numbers = "1234567890";
        Integer passwordLength = 12;

        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[passwordLength];
  
        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));
  
        for(int i = 4; i< passwordLength ; i++) {
           password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(password);
     }
}