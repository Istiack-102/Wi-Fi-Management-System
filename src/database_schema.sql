CREATE DATABASE IF NOT EXISTS wifi_management_db;
USE wifi_management_db;

CREATE TABLE User (
                      userId INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      number VARCHAR (64) NOT NULL,
                      address VARCHAR (100) NOT NULL
);

CREATE TABLE Admin (
                       adminId INT PRIMARY KEY ,
                       name VARCHAR(100) NOT NULL,
                       password VARCHAR(100) NOT NULL
);

CREATE TABLE Plan (
                      planId INT PRIMARY KEY ,
                      planName VARCHAR(50) NOT NULL,
                      price DOUBLE NOT NULL,
                      speed VARCHAR(20)
);

CREATE TABLE Subscription (
                              subscriptionId INT PRIMARY KEY AUTO_INCREMENT,
                              userId INT,
                              planId INT,
                              startDate VARCHAR(50),
                              FOREIGN KEY (userId) REFERENCES User(userId),
                              FOREIGN KEY (planId) REFERENCES Plan(planId)
);

CREATE TABLE Usage_Track (
                             usageId INT PRIMARY KEY AUTO_INCREMENT,
                             dataAmount DOUBLE,
                             timeUsed VARCHAR(50),
                             userId INT,
                             FOREIGN KEY (userId) REFERENCES User(userId)
);

CREATE TABLE Payment (
                         paymentId INT PRIMARY KEY AUTO_INCREMENT,
                         paymentDate VARCHAR(50),
                         subscriptionId INT,
                         FOREIGN KEY (subscriptionId) REFERENCES Subscription(subscriptionId)
);