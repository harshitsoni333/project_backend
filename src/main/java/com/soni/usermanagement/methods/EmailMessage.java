package com.soni.usermanagement.methods;

import com.soni.usermanagement.model.UserManagement;

/**
 * EmailMessage
 */
public class EmailMessage {

    public static String makeSubjectFor(String request, String recipient) {

        switch (request) {

            case "update": return("Account details updated successfully");
            case "create": return("Welcome " + recipient + "!");
            case "delete": return("Account deleted successfully");
            case "login": return("Here are your login details.");
            case "changePassword": return("Your password has been changed!");

            default: return("Sample Default Subject");
        }
    }

    public static String makeMessageFor(String request, UserManagement user) {
        switch (request) {

            case "create":
                return(
                    String.format(
                        "Hello %s! \n"+
                        "Your account has been successfully created. \n\n"+
                        "Here are your details: \n"+
                        "Name: %s %s \n" +
                        "E-mail: %s \n" +
                        "Profile/Designation: %s \n\n" +
                        "We look forward to working with you.\nBest Regards,\nSUP Team",

                        user.getFirstName(), 
                        user.getFirstName(), user.getLastName(),
                        user.getEmail(),
                        user.getProfile()
                    ));
            
            case "update": 
                return(
                    String.format(
                        "Hello %s! \n"+
                        "Your account details have been successfully updated. \n\n"+
                        "Here are your new details: \n"+
                        "Name: %s %s \n" +
                        "E-mail: %s \n" +
                        "Profile/Designation: %s \n\n" +
                        "Hope you have a great day ahead.\nBest Regards,\nSUP Team",

                        user.getFirstName(), 
                        user.getFirstName(), user.getLastName(),
                        user.getEmail(),
                        user.getProfile()
                    ));

            case "delete": 
                return(
                    String.format(
                        "Hello %s! \n"+
                        "Your account has been successfully deleted. \n\n"+
                        "Your account details were: \n"+
                        "Name: %s %s \n" +
                        "E-mail: %s \n" +
                        "Profile/Designation: %s \n\n" +
                        "We will miss you.\nBest Regards,\nSUP Team",

                        user.getFirstName(), 
                        user.getFirstName(), user.getLastName(),
                        user.getEmail(),
                        user.getProfile()
                    ));
        
            default: return("Sample email message");
        }
    }

    public static String makePasswordMessageFor(String request, String name, String userName, String password) {

        switch(request) {

            case "login":
                return(
                    String.format(
                        "Hello %s! \n" +
                        "Welcome to Smart Utilities Portal. \n\n" +
                        "Here are your login credentials: \n" +
                        "Username: %s \n" +
                        "Password: %s \n\n" +
                        "We look forward to working with you.\nBest Regards,\nSUP Team", 
                        
                        name, userName, password));
            
            case "changePassword":
                return(
                    String.format(
                        "Hello %s! \n" +
                        "Your password has been changed successfully. \n\n" +
                        "Here are your new login credentials: \n" +
                        "Username: %s \n" +
                        "Password: %s \n\n" +
                        "We look forward to working with you.\nBest Regards,\nSUP Team", 
                        
                        name, userName, password));
            
            default: return("sample login details body");
        }
    }
}