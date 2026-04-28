package com.wifi.management.utils;

import java.util.ArrayList;
import java.util.List;

public class Strong_password_check {

    public static List<String> checkPasswordStrength(String password) {

        List<String> missing = new ArrayList<>();

        if (password == null) {
            missing.add("Password cannot be null");
            return missing;
        }

        // Length check
        if (password.length() < 8) {
            missing.add("At least 8 characters");
        }

        // Lowercase check
        if (!password.matches(".*[a-z].*")) {
            missing.add("At least one lowercase letter (a-z)");
        }

        // Uppercase check
        if (!password.matches(".*[A-Z].*")) {
            missing.add("At least one uppercase letter (A-Z)");
        }

        // Digit check
        if (!password.matches(".*\\d.*")) {
            missing.add("At least one digit (0-9)");
        }

        // Special character check
        if (!password.matches(".*[@$!%*?&].*")) {
            missing.add("At least one special character (@$!%*?&)");
        }

        // Space check
        if (password.contains(" ")) {
            missing.add("No spaces allowed");
        }

        return missing;
    }

    // Test method
    public static void main(String[] args) {

        String password = "abc123";

        List<String> result = checkPasswordStrength(password);

        if (result.isEmpty()) {
            System.out.println("Password is STRONG ✔️");
        } else {
            System.out.println("Password is WEAK ❌");
            System.out.println("Fix the following:");

            for (String msg : result) {
                System.out.println("- " + msg);
            }
        }
    }
}