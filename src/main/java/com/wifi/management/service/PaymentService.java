package com.wifi.management.service;

import com.wifi.management.database_operation.PaymentDAO;
import com.wifi.management.database_operation.PlanDAO;
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

        Plan selectedPlan = planDAO.getPlanById(planId);
        if (selectedPlan == null) {
            return "Plan not found!";
        }

        double amount = selectedPlan.getMonthlyPrice();

        if (method.equalsIgnoreCase("Card")) {
            if (!Card_Checker.isFullCardValid(cardNum, expiryDate, cvc)) {
                return "Invalid Card Details! Please check Card Number, Expiry (MM/YY), and CVC.";
            }
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
            return "Transaction failed due to a database error.";
        }
    }
}