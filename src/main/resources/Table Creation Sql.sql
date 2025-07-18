CREATE DATABASE IF NOT EXISTS atm_simulation;
USE atm_simulation;

-- User account table
CREATE TABLE IF NOT EXISTS accounts (
    card_number VARCHAR(16) PRIMARY KEY,
    pin VARCHAR(6) NOT NULL,
    name VARCHAR(100),
    balance DOUBLE DEFAULT 0
);

-- Transaction table
CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(16),
    type VARCHAR(10),
    amount DOUBLE,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_number) REFERENCES accounts(card_number)
);

-- Insert sample account
INSERT INTO accounts (card_number, pin, name, balance) VALUES
('1234567890123456', '1234', 'Saisa Koirala', 10000);
