package com.wifi.management.utils;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Card_Checker {

    public static boolean isFullCardValid(String cardNumber, String expiryDate, String cvc) {
        return isValidCard(cardNumber) && isValidExpiryDate(expiryDate) && isValidCVC(cvc);
    }

    public static boolean isValidCard(String cardNumber) {
        if (cardNumber == null) return false;
        cardNumber = cardNumber.replaceAll("[\\s-]", "");
        if (!cardNumber.matches("\\d{13,19}")) return false; // সাধারণত ১৩-১৯ ডিজিট হয়
        return luhnCheck(cardNumber);
    }
    public static boolean isValidExpiryDate(String expiryDate) {
        if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiry = YearMonth.parse(expiryDate, formatter);
            // কার্ডের মেয়াদ বর্তমান মাসের সমান বা বেশি হতে হবে
            return expiry.isAfter(YearMonth.now()) || expiry.equals(YearMonth.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidCVC(String cvc) {
        if (cvc == null) return false;
        return cvc.matches("\\d{3,4}");
    }

    private static boolean luhnCheck(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = number.charAt(i) - '0';
            if (alternate) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }
}