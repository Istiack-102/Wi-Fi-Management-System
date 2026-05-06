package com.wifi.management.utils;

import java.util.Random;

public class UniqueMacAddress { // ক্লাসের নাম

    // মেথডের নাম পরিবর্তন করা হয়েছে যাতে ক্লাসের নামের সাথে না মিলে যায়
    public static String generateRandomMac() {
        Random rand = new Random();
        byte[] mac = new byte[6];
        rand.nextBytes(mac);

        // প্রথম বাইটটিকে লোকাল অ্যাডমিনিস্ট্রেটেড সেট করা
        mac[0] = (byte) (mac[0] & (byte) 254);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }
}