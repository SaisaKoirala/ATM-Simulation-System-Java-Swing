package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignupFrame extends JFrame {
    JTextField cardField, nameField;
    JPasswordField pinField;
    JButton signupBtn, loginBtn;

    public SignupFrame() {
        setTitle("ATM - Sign Up");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true); // for modern clean look

        // Gradient background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(0, 123, 255);
                Color color2 = new Color(32, 201, 151);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Header panel with heading label and exit button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel heading = new JLabel("ATM Signup");
        heading.setFont(new Font("Segoe UI Black", Font.BOLD, 40));
        heading.setForeground(Color.WHITE);
        headerPanel.add(heading, BorderLayout.WEST);

        // Exit button (top-right)
        JButton exitBtn = new JButton("X");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(new Color(0, 0, 0, 0)); // transparent background
        exitBtn.setFocusPainted(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setContentAreaFilled(false);
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitBtn.setPreferredSize(new Dimension(50, 50));

        exitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitBtn.setOpaque(true);
                exitBtn.setBackground(Color.RED);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                exitBtn.setOpaque(false);
                exitBtn.setBackground(new Color(0, 0, 0, 0));
            }
        });

        exitBtn.addActionListener(e -> System.exit(0));

        headerPanel.add(exitBtn, BorderLayout.EAST);

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel setup
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        cardLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(cardLabel, gbc);

        cardField = new JTextField(20);
        cardField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        formPanel.add(cardField, gbc);

        JLabel pinLabel = new JLabel("PIN (4 digits):");
        pinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        pinLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(pinLabel, gbc);

        pinField = new JPasswordField(20);
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        formPanel.add(pinField, gbc);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        nameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Buttons - login (left) and signup (right)
        loginBtn = createButton("Go to Login", new Color(255, 193, 7), Color.BLACK);
        signupBtn = createButton("Sign Up", new Color(220, 53, 69), Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(loginBtn, gbc);
        gbc.gridx = 1;
        formPanel.add(signupBtn, gbc);

        backgroundPanel.add(formPanel, BorderLayout.CENTER);

        // Events
        signupBtn.addActionListener(e -> registerUser());
        loginBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(btn.getBackground());
                g2d.fillRoundRect(0, 0, btn.getWidth(), btn.getHeight(), 40, 40);
                super.paint(g2d, c);
                g2d.dispose();
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(btn.getBackground().darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    private void registerUser() {
        String card = cardField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();
        String name = nameField.getText().trim();

        if (card.length() != 16 || !card.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Enter a valid 16-digit card number.");
            return;
        }
        if (pin.length() != 4 || !pin.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "PIN must be 4 digits.");
            return;
        }
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO accounts (card_number, pin, name, balance) VALUES (?, ?, ?, 0)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, card);
            ps.setString(2, pin);
            ps.setString(3, name);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Signup successful!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Signup failed: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new SignupFrame();
    }
}
