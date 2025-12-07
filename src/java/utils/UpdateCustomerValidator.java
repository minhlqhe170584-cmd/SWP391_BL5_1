/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Acer
 */
public class UpdateCustomerValidator {
        public static List<String> validate(String fullName, String email, String phone, String password) {
        List<String> errors = new ArrayList<>();
        
        if (!isValidFullName(fullName)) {
            errors.add("Full name is required");
        }
        
        if (!isValidEmail(email)) {
            errors.add("Email must contain @ and domain");
        }
        
        if (!isValidPhone(phone)) {
            errors.add("Phone must be 10 digits");
        }
        
        if (!isValidPassword(password)) {
            errors.add("Password must be 6+ characters with 1 uppercase and 1 special character");
        }
        
        return errors;
    }
    
    private static boolean isValidFullName(String name) {
        return name != null && !name.trim().isEmpty();
    }
    
    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        int atIndex = email.indexOf('@');
        int dotIndex = email.lastIndexOf('.');
        return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < email.length() - 1;
    }
    
    private static boolean isValidPhone(String phone) {
        if (phone == null || phone.length() != 10) return false;
        for (char c : phone.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
    
    private static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) return false;
        
        boolean hasUpper = false;
        boolean hasSpecial = false;
        String specialChars = "!@#$%^&*()_+-=[]{}|;':,.<>?/";
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (specialChars.indexOf(c) >= 0) hasSpecial = true;
        }
        
        return hasUpper && hasSpecial;
    }
}
