package org.example;
import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/atm_simulation?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "p@ssw0rd";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // explicitly load MySQL driver
            System.out.println("MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver NOT found.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("Connecting to database...");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
