CREATE DATABASE IF NOT EXISTS wifi_management_db;
USE wifi_management_db;

CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(20) UNIQUE NOT NULL
);
INSERT IGNORE INTO roles (role_id, role_name) VALUES (1, 'Admin');
INSERT IGNORE INTO roles (role_id, role_name) VALUES (2, 'Customer');
select * from roles;

CREATE TABLE plans (
    plan_id INT PRIMARY KEY AUTO_INCREMENT,
    plan_name VARCHAR(50) NOT NULL,
    speed_limit_mbps INT NOT NULL,
    monthly_price DECIMAL(10, 2) NOT NULL
);
INSERT INTO plans (plan_name, speed_limit_mbps, monthly_price) VALUES
('Starter Eco', 10, 500.00),
('Home Basic', 20, 800.00),
('Standard Family', 30, 1000.00),
('Premium Ultra', 50, 1500.00),
('Gamer Pro', 80, 2500.00),
('Business Dedicated', 100, 5000.00);
select * from plans;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);
select * from users;
insert  into users values ( 1, 'Istiack', '898a935bd7fc2852a9b3177f0aaeec13f41f31cb387fa9dc425c591541363cd2',1);

CREATE TABLE customer_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    full_name VARCHAR(100),
    phone VARCHAR(15),
    installation_address TEXT,
    mac_address VARCHAR(17) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
select * from customer_details;

UPDATE customer_details
SET mac_address = 'AA:BB:CC:DD:EE:FF'
WHERE user_id = 4;

CREATE TABLE subscriptions (
    sub_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    plan_id INT,
    expiry_date DATE,
    status ENUM('active', 'expired') DEFAULT 'active',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (plan_id) REFERENCES plans(plan_id)
);
select * from subscriptions;

CREATE TABLE transactions (
    transaction_id VARCHAR(50) PRIMARY KEY,
    user_id INT,
    amount DECIMAL(10, 2),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
select * from transactions;

CREATE TABLE usage_logs (
    usage_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    data_amount_gb DECIMAL(10, 2),
    time_used TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE OR REPLACE VIEW view_admin_search_user AS
SELECT
    u.user_id,
    u.username,
    cd.full_name,
    cd.phone,
    cd.installation_address,
    p.plan_name,
    s.expiry_date,
    s.status,
    cd.mac_address
FROM users u
JOIN customer_details cd ON u.user_id = cd.user_id
JOIN subscriptions s ON u.user_id = s.user_id
JOIN plans p ON s.plan_id = p.plan_id;
select * from view_admin_search_user;

DROP VIEW IF EXISTS view_customer_dashboard;

CREATE OR REPLACE VIEW view_customer_dashboard AS
SELECT
    u.user_id,
    u.username,
    u.role_id,
    cd.full_name,
    cd.phone,
    cd.installation_address,
    p.plan_name,
    p.speed_limit_mbps,
    p.monthly_price,
    s.expiry_date
FROM users u
LEFT JOIN customer_details cd ON u.user_id = cd.user_id
LEFT JOIN subscriptions s ON u.user_id = s.user_id
LEFT JOIN plans p ON s.plan_id = p.plan_id;
select  * from view_customer_dashboard;

CREATE TABLE connection_requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    plan_id INT NOT NULL,
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
    mac_address VARCHAR(20) DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (plan_id) REFERENCES plans(plan_id)
);
select * from connection_requests;

ALTER TABLE subscriptions ADD CONSTRAINT UNIQUE (user_id);
ALTER TABLE customer_details MODIFY COLUMN mac_address VARCHAR(20);

CREATE TABLE IF NOT EXISTS profile_update_history (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    field_name VARCHAR(50),
    old_value TEXT,
    new_value TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER $$

DROP TRIGGER IF EXISTS after_customer_profile_update$$

CREATE TRIGGER after_customer_profile_update
AFTER UPDATE ON customer_details
FOR EACH ROW
BEGIN
    IF OLD.full_name <> NEW.full_name THEN
        INSERT INTO profile_update_history (user_id, field_name, old_value, new_value)
        VALUES (NEW.user_id, 'full_name', OLD.full_name, NEW.full_name);
    END IF;

    IF OLD.phone <> NEW.phone THEN
        INSERT INTO profile_update_history (user_id, field_name, old_value, new_value)
        VALUES (NEW.user_id, 'phone', OLD.phone, NEW.phone);
    END IF;

    IF OLD.installation_address <> NEW.installation_address THEN
        INSERT INTO profile_update_history (user_id, field_name, old_value, new_value)
        VALUES (NEW.user_id, 'installation_address', OLD.installation_address, NEW.installation_address);
    END IF;
END$$

DELIMITER ;

CREATE TABLE IF NOT EXISTS subscription_plan_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    old_plan_id INT,
    new_plan_id INT,
    changed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER $$

DROP TRIGGER IF EXISTS after_subscription_plan_change$$

CREATE TRIGGER after_subscription_plan_change
AFTER UPDATE ON subscriptions
FOR EACH ROW
BEGIN
    IF OLD.plan_id <> NEW.plan_id THEN
        INSERT INTO subscription_plan_history (user_id, old_plan_id, new_plan_id)
        VALUES (NEW.user_id, OLD.plan_id, NEW.plan_id);
    END IF;
END$$

DELIMITER ;

CREATE TABLE IF NOT EXISTS deleted_users_log (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    username VARCHAR(50),
    deleted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER $$

DROP TRIGGER IF EXISTS before_user_delete$$

CREATE TRIGGER before_user_delete
BEFORE DELETE ON users
FOR EACH ROW
BEGIN
    INSERT INTO deleted_users_log (user_id, username)
    VALUES (OLD.user_id, OLD.username);
END$$

DELIMITER ;

CREATE OR REPLACE VIEW view_profile_history_logs AS
SELECT
    p.log_id,
    p.user_id,
    u.username,
    p.field_name,
    p.old_value,
    p.new_value,
    p.changed_at
FROM profile_update_history p
JOIN users u ON p.user_id = u.user_id;

SELECT * FROM view_profile_history_logs;