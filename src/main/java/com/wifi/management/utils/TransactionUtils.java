package com.wifi.management.utils;

import java.util.UUID;
import java.time.Instant;

public class TransactionUtils {
    public static String generateTransactionId() {
        long timestamp = Instant.now().toEpochMilli();
        String randomPart = UUID.randomUUID().toString().substring(0, 8);
        return "TXN-" + timestamp + "-" + randomPart.toUpperCase();
    }
}