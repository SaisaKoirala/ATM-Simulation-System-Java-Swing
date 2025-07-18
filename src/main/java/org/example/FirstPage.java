package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FirstPage extends JFrame {

    JButton loginBtn, signupBtn, adminBtn;

    public FirstPage() {
        setTitle("ATM - Welcome");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true); // No window border

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

        // Heading label
        JLabel heading = new JLabel("Welcome to ATM", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI Black", Font.BOLD, 48));
        heading.setForeground(Color.WHITE);
        heading.setBorder(BorderFactory.createEmptyBorder(80, 0, 60, 0));
        backgroundPanel.add(heading, BorderLayout.NORTH);

        // Button panel with transparent background
        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Buttons
        signupBtn = createButton("Sign Up", new Color(255, 193, 7), Color.BLACK);
        loginBtn = createButton("Login", new Color(220, 53, 69), Color.WHITE);
        adminBtn = createButton("Admin", new Color(108, 117, 125), Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        btnPanel.add(signupBtn, gbc);

        gbc.gridx = 1;
        btnPanel.add(loginBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        btnPanel.add(adminBtn, gbc);

        backgroundPanel.add(btnPanel, BorderLayout.CENTER);

        // Button actions
        signupBtn.addActionListener(e -> {
            dispose();
            new SignupFrame();
        });

        loginBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        adminBtn.addActionListener(e -> {
            dispose();
            new AdminLoginFrame();
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

    public static void main(String[] args) {
        new FirstPage();
    }
}
