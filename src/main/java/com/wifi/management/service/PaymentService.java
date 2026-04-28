package com.wifi.management.service;

import com.wifi.management.utils.Card_Checker;

public class PaymentService {

    // Method to validate payment card details
    public static String validateCardPayment(String cardHolderName,
                                             String cardNumber,
                                             String expiryDate,
                                             String cvv) {

        // Check empty fields
        if (cardHolderName == null || cardHolderName.isEmpty() ||
                cardNumber == null || cardNumber.isEmpty() ||
                expiryDate == null || expiryDate.isEmpty() ||
                cvv == null || cvv.isEmpty()) {
            return "❌ All fields are required";
        }

        // Validate card number using Luhn algorithm
        boolean isCardValid = Card_Checker.isValidCard(cardNumber);

        if (!isCardValid) {
            return "❌ Invalid card number";
        }

        // CVV check (basic: 3 or 4 digits)
        if (!cvv.matches("\\d{3,4}")) {
            return "❌ Invalid CVV";
        }

        // Expiry format check (basic MM/YY)
        if (!expiryDate.matches("(0[1-9]|1[0-2])\\/\\d{2}")) {
            return "❌ Invalid expiry date format (MM/YY)";
        }

        // If everything is correct
        return "✅ Card is valid and payment can proceed";
    }

    // Test method
    public static void main(String[] args) {

        String result = validateCardPayment(
                "John Doe",
                "4539 1488 0343 6467",
                "12/28",
                "123"
        );

        System.out.println(result);
    }
}