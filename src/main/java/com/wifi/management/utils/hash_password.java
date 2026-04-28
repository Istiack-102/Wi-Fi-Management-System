package com.wifi.management.utils;

import java.security.MessageDigest;

public class hash_password {

    // Returns hashed password (SHA-256)
    public static String hashPassword(String password) {

        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}