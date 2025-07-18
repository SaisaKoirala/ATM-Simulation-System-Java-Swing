# 💳 ATM Simulation System with Admin Dashboard

This is a Java-based ATM simulation system built with Java Swing and MySQL. It includes both **user functionality** (login, withdraw, deposit, check balance) and a full-featured **admin dashboard** (view users, edit name, remove accounts).

---

## 🚀 Features

### ✅ User Features
- User Login via card number and PIN
- Withdraw & Deposit with validations
- Check Account Balance
- Sound effect on transaction success
- UI built with Java Swing

### 🛠️ Admin Features
- Admin Login with custom credentials
- Full-screen Admin Dashboard
- View all user accounts and balances
- Edit user name
- Delete any user account
- Go back to Main Page option
- Stylish Close/Exit buttons
- Gradient UI Theme

---

## 🛠️ Technologies Used

- Java (Swing GUI)
- JDBC for MySQL Database Connection
- MySQL Database
- IntelliJ IDEA / Eclipse
- Sound using Java's `Clip` and `AudioSystem`

---

## 🧩 Database Schema (MySQL)

```sql
CREATE DATABASE IF NOT EXISTS atm_simulation;
USE atm_simulation;

CREATE TABLE IF NOT EXISTS accounts (
    card_number VARCHAR(16) PRIMARY KEY,
    pin VARCHAR(6) NOT NULL,
    name VARCHAR(100),
    balance DOUBLE DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(16),
    type VARCHAR(10),
    amount DOUBLE,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_number) REFERENCES accounts(card_number)
);

-- Sample account
INSERT INTO accounts (card_number, pin, name, balance)
VALUES ('1234567890123456', '1234', 'Saisa Koirala', 10000);
```

---

## 🔌 JDBC Dependency (Add to your `pom.xml` if using Maven)

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

---

## 📦 How to Run

1. Clone or download the project.
2. Create the database using the above schema.
3. Make sure MySQL is running and accessible.
4. Update DB credentials in `DBConnection.java`.
5. Run `FirstPage.java` to get started.

---

## 👨‍💻 Author

- Developed by **Saisa Koirala**

---

## 📸 Screenshots

<img width="1237" height="1051" alt="image" src="https://github.com/user-attachments/assets/838ba06e-8624-4374-9c40-6bfd083ae3f7" />
<img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/44294e67-4732-40e0-a080-36e7653dc8fd" />
<img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/46cbda57-faa4-4df1-86de-a2518a8d7ad9" />


---

Feel free to customize and extend this project!
