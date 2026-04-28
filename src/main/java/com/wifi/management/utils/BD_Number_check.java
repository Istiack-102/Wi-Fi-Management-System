package com.wifi.management.utils;

import java.util.regex.Pattern;

public class BD_Number_check {

    // Regex for BD phone numbers
    private static final String BD_NUMBER_REGEX = "^(\\+8801[3-9]\\d{8}|8801[3-9]\\d{8}|01[3-9]\\d{8})$";

    private static final Pattern pattern = Pattern.compile(BD_NUMBER_REGEX);

    // Method to validate number
    public static boolean isValidBDNumber(String number) {
        if (number == null) {
            return false;
        }
        return pattern.matcher(number).matches();
    }

    // Test main method
//    public static void main(String[] args) {
//
//        String[] testNumbers = {
//                "01712345678",
//                "+8801712345678",
//                "8801812345678",
//                "01234567890",      // invalid
//                "0171234567",       // invalid (too short)
//                "+8801212345678"    // invalid operator
//        };
//
//        for (String num : testNumbers) {
//            System.out.println(num + " -> " + isValidBDNumber(num));
//        }
//    }
}