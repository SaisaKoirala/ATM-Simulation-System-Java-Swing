package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    private JTable accountTable;
    private DefaultTableModel tableModel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Gradient background
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(0, 123, 255);
                Color color2 = new Color(32, 201, 151);
                g2d.setPaint(new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(null);
        setContentPane(background);

        JLabel title = new JLabel("Admin Dashboard - User Accounts", JLabel.CENTER);
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 40, getWidth(), 60);
        background.add(title);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Card Number", "Name", "Balance"}, 0);
        accountTable = new JTable(tableModel);
        accountTable.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        accountTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setBounds(300, 150, 1000, 400);
        background.add(scrollPane);

        // Load account data
        loadAccountData();

        // Delete Button
        JButton deleteBtn = new JButton("Remove Selected Account");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        deleteBtn.setBounds(500, 580, 300, 60);
        deleteBtn.setBackground(new Color(255, 87, 34));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> deleteSelectedAccount());
        background.add(deleteBtn);

        // Edit Button
        JButton editBtn = new JButton("Edit Selected User Name");
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        editBtn.setBounds(850, 580, 300, 60);
        editBtn.setBackground(new Color(255, 193, 7));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> editSelectedUserName());
        background.add(editBtn);

        // Go Back Button
        JButton backBtn = new JButton("← Go Back to Main Page");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        backBtn.setBounds(20, 20, 260, 50);
        backBtn.setBackground(new Color(52, 58, 64));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dispose();
            new FirstPage(); // Assuming you have this class
        });
        background.add(backBtn);

        // Exit Button
        JButton closeBtn = new JButton("X");
        closeBtn.setBounds(getWidth() - 80, 20, 50, 40);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(220, 53, 69));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setOpaque(true);
        closeBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit the dashboard?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        background.add(closeBtn);

        setVisible(true);
    }

    private void loadAccountData() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT card_number, name, balance FROM accounts";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("card_number"),
                        rs.getString("name"),
                        rs.getDouble("balance")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load account data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedAccount() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account to delete.");
            return;
        }

        String cardNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete account of '" + name + "' (" + cardNumber + ")?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "DELETE FROM accounts WHERE card_number = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, cardNumber);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Account deleted successfully.");
                loadAccountData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to delete account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSelectedUserName() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
            return;
        }

        String cardNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String oldName = (String) tableModel.getValueAt(selectedRow, 1);

        String newName = JOptionPane.showInputDialog(this, "Enter new name for " + oldName + ":", oldName);
        if (newName != null && !newName.trim().isEmpty()) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE accounts SET name = ? WHERE card_number = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, newName);
                pst.setString(2, cardNumber);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Name updated successfully.");
                loadAccountData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}
