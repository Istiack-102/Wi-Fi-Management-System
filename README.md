# Project Name: WiFi Management System

A Java Swing-based desktop application for managing WiFi ISP operations including users, plans, subscriptions, payments, and usage tracking.

---

## 🚀 Features

### 👨‍💼 Admin Panel
- Manage Internet Plans (CRUD-ready structure)
- View Customers with full details
- Track Payments (Transactions)
- Manage Subscriptions
- Monitor Usage Logs
- Dashboard-based UI navigation

### 👤 User Panel
- View active plan
- Payment history
- Usage details
- Simple user dashboard

---

## 🏗️ Tech Stack

- Java (Swing GUI)
- MySQL (Database)
- JDBC (Database Connection)
- XAMPP (Local Server)
- NetBeans / IntelliJ (Recommended IDE)

---

## 🗄️ Database Structure

Main tables:

- `roles`
- `users`
- `customer_details`
- `plans`
- `subscriptions`
- `transactions`
- `usage_logs`

### Views Used:
- `view_admin_search_user`
- `view_customer_dashboard`

---

## ⚙️ Setup Instructions

### 1. Clone Repository
```bash
git clone https://github.com/your-username/wifi-management-system.git

2. Setup Database
- Open XAMPP
- Start Apache & MySQL
- Import wifi_management_utils.sql

3. Configure DB Connection
String url = "jdbc:mysql://localhost:3306/wifi_management_db";
String user = "root";
String password = "";

▶️ Run Project
src/com/wifi/management/Main.java
