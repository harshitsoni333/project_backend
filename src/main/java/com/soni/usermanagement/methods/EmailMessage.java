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
                        "Hope you have a great day ahead.\nBest Regards,\nSUP Team",

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
                        "Hope you have a great day ahead.\nBest Regards,\nSUP Team",

                        user.getFirstName(), 
                        user.getFirstName(), user.getLastName(),
                        user.getEmail(),
                        user.getProfile()
                    ));
        
            default: return("Sample email message");
        }
    }
}