package com.wifi.management.utils;

public class Card_Checker {

    // Only validation method (returns true or false)
    public static boolean isValidCard(String cardNumber) {

        if (cardNumber == null) {
            return false;
        }

        // Remove spaces and dashes
        cardNumber = cardNumber.replaceAll("[\\s-]", "");

        // Must contain only digits
        if (!cardNumber.matches("\\d+")) {
            return false;
        }

        return luhnCheck(cardNumber);
    }

    // Luhn Algorithm (core validation)
    private static boolean luhnCheck(String number) {

        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {

            int digit = number.charAt(i) - '0';

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }
}