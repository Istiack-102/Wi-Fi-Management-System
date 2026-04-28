package com.wifi.management.utils;

import java.util.UUID;
import java.time.Instant;

public class TransactionUtils {

    /**
     * Generates a unique Transaction ID.
     * Format: TXN-Timestamp-RandomUUID
     */
    public static String generateTransactionId() {
        // Get current timestamp in milliseconds for chronological sorting
        long timestamp = Instant.now().toEpochMilli();

        // Generate a random UUID and take the first 8 characters for brevity
        String randomPart = UUID.randomUUID().toString().substring(0, 8);

        // Combine them: e.g., TXN-1714300000-a1b2c3d4
        return "TXN-" + timestamp + "-" + randomPart.toUpperCase();
    }
}