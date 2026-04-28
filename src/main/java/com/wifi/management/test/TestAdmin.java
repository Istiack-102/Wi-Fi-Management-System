package com.wifi.management.test;

import com.wifi.management.model.Admin;

public class TestAdmin {
    public static void main(String[] args) {
        Admin admin = new Admin(1, "1234", "admin@gmail.com");

        System.out.println("Admin ID: " + admin.getAdminId());
        System.out.println("Email: " + admin.getEmail());
        System.out.println("Password: " + admin.getPassword());
    }
}
