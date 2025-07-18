package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    JTextField cardField;
    JPasswordField pinField;
    JButton loginBtn, signupBtn;

    public LoginFrame() {
        setTitle("ATM - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel heading = new JLabel("ATM Login");
        heading.setFont(new Font("Segoe UI Black", Font.BOLD, 40));
        heading.setForeground(Color.WHITE);
        headerPanel.add(heading, BorderLayout.WEST);

        JButton exitBtn = new JButton("X");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(new Color(0, 0, 0, 0));
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

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

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

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        pinLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(pinLabel, gbc);

        pinField = new JPasswordField(20);
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        formPanel.add(pinField, gbc);

        signupBtn = createButton("Go to Signup", new Color(255, 193, 7), Color.BLACK);
        loginBtn = createButton("Login", new Color(40, 167, 69), Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(signupBtn, gbc);
        gbc.gridx = 1;
        formPanel.add(loginBtn, gbc);

        backgroundPanel.add(formPanel, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> login());
        signupBtn.addActionListener(e -> {
            dispose();
            new SignupFrame();
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

    private void login() {
        String card = cardField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if (card.length() != 16 || !card.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Enter a valid 16-digit card number.");
            return;
        }
        if (pin.length() != 4 || !pin.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "PIN must be 4 digits.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM accounts WHERE card_number = ? AND pin = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, card);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                ToastMessage.showToast(this, "Welcome, " + name + "!", ToastMessage.SUCCESS);
                dispose();
                new ATMFrame(card);
            } else {
                ToastMessage.showToast(this, "Invalid card number or PIN.", ToastMessage.ERROR);
            }

        } catch (SQLException ex) {
            ToastMessage.showToast(this, "Login error: " + ex.getMessage(), 2);
        }
    }
}
