package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLoginFrame() {
        setTitle("Admin Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel backgroundPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                g2d.setPaint(new GradientPaint(0, 0, new Color(0, 123, 255), w, h, new Color(32, 201, 151)));
                g2d.fillRect(0, 0, w, h);
            }
        };
        setContentPane(backgroundPanel);

        // Close button
        JButton closeBtn = new JButton("X");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        closeBtn.setBounds( getWidth() - 100, 20, 60, 40);  // Initial placeholder, adjust below
        closeBtn.setBackground(new Color(220, 53, 69));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setOpaque(true);

        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(new Color(200, 40, 50));
            }

            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(new Color(220, 53, 69));
            }
        });

        closeBtn.addActionListener(e -> System.exit(0));
        backgroundPanel.add(closeBtn);

        // Components
        JLabel titleLabel = new JLabel("Admin Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 100, getWidth(), 60);
        backgroundPanel.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(600, 250, 150, 50);
        backgroundPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        usernameField.setBounds(760, 250, 300, 50);
        backgroundPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(600, 330, 150, 50);
        backgroundPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        passwordField.setBounds(760, 330, 300, 50);
        backgroundPanel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 28));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(new Color(255, 87, 34));
        loginBtn.setBounds(720, 420, 200, 60);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());

            if ("admin".equals(user) && "admin".equals(pass)) {
                dispose();
                new AdminDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        backgroundPanel.add(loginBtn);

        // Update closeBtn position AFTER getWidth() is properly set (via resize listener)
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                closeBtn.setBounds(getWidth() - 80, 20, 50, 40);
                titleLabel.setBounds(0, 100, getWidth(), 60);
            }
        });

        setVisible(true);
    }
}
