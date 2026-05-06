package com.wifi.management.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * UniqueMacAddress utility class to retrieve the hardware address of the device.
 */
public class UniqueMacAddress {

    /**
     * বর্তমান ডিভাইসের ইউনিক MAC Address খুঁজে বের করে এবং হেক্সাডেসিমেল ফরম্যাটে রিটার্ন করে।
     * @return String (e.g., "00-1A-2B-3C-4D-5E") অথবা এরর হলে null।
     */
    public static String getMac() {
        try {
            // ১. লোকাল হোস্টের আইপি অ্যাড্রেস সংগ্রহ
            InetAddress ip = InetAddress.getLocalHost();

            // ২. বর্তমান আইপির সাথে যুক্ত নেটওয়ার্ক ইন্টারফেস খুঁজে বের করা
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            // যদি আইপি দিয়ে ইন্টারফেস না পাওয়া যায় (যেমন অনেক সময় ওয়াইফাই বা ইথারনেট আলাদা থাকে)
            if (network == null) {
                return "00-00-00-00-00-00";
            }

            // ৩. হার্ডওয়্যার (MAC) অ্যাড্রেস বাইট অ্যারে হিসেবে নেওয়া
            byte[] mac = network.getHardwareAddress();

            if (mac == null) {
                return null;
            }

            // ৪. বাইট অ্যারেকে স্ট্যান্ডার্ড রিডেবল ফরম্যাটে কনভার্ট করা
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }

            return sb.toString();

        } catch (UnknownHostException | SocketException e) {
            System.err.println("Error retrieving MAC address: " + e.getMessage());
            return null;
        }
    }
}