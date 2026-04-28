CREATE DATABASE IF NOT EXISTS wifi_management_db;
USE wifi_management_db;

CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE plans (
    plan_id INT PRIMARY KEY AUTO_INCREMENT,
    plan_name VARCHAR(50) NOT NULL,
    speed_limit_mbps INT NOT NULL,
    monthly_price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE customer_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    full_name VARCHAR(100),
    phone VARCHAR(15),
    installation_address TEXT,
    mac_address VARCHAR(17) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE subscriptions (
    sub_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    plan_id INT,
    expiry_date DATE,
    status ENUM('active', 'expired') DEFAULT 'active',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (plan_id) REFERENCES plans(plan_id)
);

CREATE TABLE transactions (
    transaction_id VARCHAR(50) PRIMARY KEY,
    user_id INT,
    amount DECIMAL(10, 2),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE usage_logs (
    usage_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    data_amount_gb DECIMAL(10, 2),
    time_used TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);



CREATE VIEW view_admin_search_user AS
SELECT
    u.user_id,
    cd.full_name,
    cd.phone,
    p.plan_name,
    s.expiry_date,
    s.status,
    cd.mac_address
FROM users u
JOIN customer_details cd ON u.user_id = cd.user_id
JOIN subscriptions s ON u.user_id = s.user_id
JOIN plans p ON s.plan_id = p.plan_id;

CREATE VIEW view_customer_dashboard AS
SELECT
    u.user_id,
    cd.full_name,
    p.plan_name,
    p.speed_limit_mbps,
    p.monthly_price,
    s.expiry_date
FROM users u
JOIN customer_details cd ON u.user_id = cd.user_id
JOIN subscriptions s ON u.user_id = s.user_id
JOIN plans p ON s.plan_id = p.plan_id;