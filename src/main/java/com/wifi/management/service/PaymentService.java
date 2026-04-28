package com.wifi.management.service;

import com.wifi.management.database_operation.PaymentDAO;
import com.wifi.management.model.Payment;
import com.wifi.management.utils.Card_Checker;
import com.wifi.management.utils.TransactionUtils;

public class PaymentService {

    private PaymentDAO paymentDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }

    public String processNewSubscription(int userId, int planId, double amount, String cardNum, String method) {

        // Check if card number is valid using Luhn Algorithm utility
        if (method.equalsIgnoreCase("Card") && !Card_Checker.isValidCard(cardNum)) {
            return "Invalid Card Details! Please check your card number.";
        }

        // Create a new Payment object
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);

        // Generate a unique transaction ID using the utility
        String uniqueTxnId = TransactionUtils.generateTransactionId();
        payment.setTransactionId(uniqueTxnId);

        // Call DAO to perform the database transaction (ACID)
        boolean isSuccess = paymentDAO.processPlanPurchase(payment, planId);

        if (isSuccess) {
            return "Payment Successful! Transaction ID: " + uniqueTxnId;
        } else {
            return "Transaction failed due to a database error.";
        }
    }
}