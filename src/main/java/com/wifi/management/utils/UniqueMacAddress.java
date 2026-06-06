package com.wifi.management.utils;

import java.util.Random;

public class UniqueMacAddress {

    public static String generateRandomMac() {
        Random rand = new Random();
        byte[] mac = new byte[6];
        rand.nextBytes(mac);

        mac[0] = (byte) (mac[0] & (byte) 254);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }
}