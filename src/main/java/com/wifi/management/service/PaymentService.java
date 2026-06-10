package com.wifi.management.service;

import com.wifi.management.database_operation.PaymentDAO;
import com.wifi.management.database_operation.PlanDAO;
import com.wifi.management.database_operation.UserDAO;
import com.wifi.management.model.Payment;
import com.wifi.management.model.Plan;
import com.wifi.management.utils.Card_Checker;
import com.wifi.management.utils.TransactionUtils;

public class PaymentService {

    private PaymentDAO paymentDAO;
    private PlanDAO planDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.planDAO = new PlanDAO();
    }

    public String processNewSubscription(int userId, int planId, String cardNum, String expiryDate, String cvc, String method) {
        UserDAO userDAO = new UserDAO();
        if (!userDAO.isExistingCustomer(userId)) {
            return "Error: No registered device (MAC Address) found for this user. Payment denied.";
        }

        Plan selectedPlan = planDAO.getPlanById(planId);
        if (selectedPlan == null) {
            return "Error: Plan ID " + planId + " not found!";
        }

        double amount = selectedPlan.getMonthlyPrice();

        if ("Card".equalsIgnoreCase(method)) {
            if (!Card_Checker.isFullCardValid(cardNum, expiryDate, cvc)) {
                return "Invalid Card Details! Please check Card Number, Expiry (MM/YY), and CVC.";
            }
        } else if (cardNum == null || cardNum.trim().isEmpty()) {
            return "Please enter your Mobile Wallet account number.";
        }

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);

        String uniqueTxnId = TransactionUtils.generateTransactionId();
        payment.setTransactionId(uniqueTxnId);

        boolean isSuccess = paymentDAO.processPlanPurchase(payment, planId);

        if (isSuccess) {
            return "Payment Successful! Transaction ID: " + uniqueTxnId;
        } else {
            return "Transaction failed due to a database error. Please contact support.";
        }
    }
}