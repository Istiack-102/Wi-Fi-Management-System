package com.wifi.management.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // MySQL connection details
    private static final String URL = "jdbc:mysql://localhost:3306/wifi_management_utils";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // XAMPP default is empty

    public static Connection getConnection() {
        try {
            // Loading the MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
}