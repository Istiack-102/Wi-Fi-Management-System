package com.wifi.management.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/wifi_management_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to: wifi_management_db");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed! Check if the database name is correct.");
            e.printStackTrace();
        }
        return conn;
    }
}