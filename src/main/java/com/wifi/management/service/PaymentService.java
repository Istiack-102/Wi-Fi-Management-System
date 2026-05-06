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

    /**
     * পেমেন্ট প্রসেস করার এবং সাবস্ক্রিপশন আপডেট করার মূল মেথড।
     */
    public String processNewSubscription(int userId, int planId, String cardNum, String expiryDate, String cvc, String method) {

        // ১. প্ল্যান ভ্যালিডেশন (ডাটাবেস থেকে সঠিক প্ল্যান খুঁজে বের করা)
        Plan selectedPlan = planDAO.getPlanById(planId);
        if (selectedPlan == null) {
            return "Error: Plan ID " + planId + " not found!";
        }

        double amount = selectedPlan.getMonthlyPrice();

        // ২. পেমেন্ট মেথড অনুযায়ী কার্ড ভ্যালিডেশন
        if ("Card".equalsIgnoreCase(method)) {
            // কার্ডের বিস্তারিত তথ্য (Number, Expiry, CVC) চেক করা
            if (!Card_Checker.isFullCardValid(cardNum, expiryDate, cvc)) {
                return "Invalid Card Details! Please check Card Number, Expiry (MM/YY), and CVC.";
            }
        } else if (cardNum == null || cardNum.trim().isEmpty()) {
            // bKash বা Nagad এর ক্ষেত্রে অন্তত একাউন্ট নাম্বার আছে কিনা চেক করা
            return "Please enter your Mobile Wallet account number.";
        }

        // ৩. পেমেন্ট অবজেক্ট তৈরি এবং ট্রানজেকশন আইডি জেনারেট করা
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);

        // ইউনিক ট্রানজেকশন আইডি জেনারেট (TransactionUtils ব্যবহার করে)
        String uniqueTxnId = TransactionUtils.generateTransactionId();
        payment.setTransactionId(uniqueTxnId);

        // ৪. ডাটাবেস অপারেশন (DAO কল করা)
        // এটি Transactions এ ডাটা ঢুকাবে এবং Subscriptions টেবিল Upsert করবে
        boolean isSuccess = paymentDAO.processPlanPurchase(payment, planId);

        if (isSuccess) {
            return "Payment Successful! Transaction ID: " + uniqueTxnId;
        } else {
            // এটি সাধারণত তখন হয় যখন subscriptions টেবিলে user_id UNIQUE করা নেই
            // অথবা ডাটাবেস কানেকশন ড্রপ করে।
            return "Transaction failed due to a database error. Please contact support.";
        }
    }
}