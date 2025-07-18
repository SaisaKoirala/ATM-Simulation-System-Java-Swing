    package org.example;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.sql.*;

    public class ATMFrame extends JFrame {
        private String cardNumber;
        private double balance;

        private JLabel balanceLabel;
        private JTextField amountField;
        private JButton checkBalanceBtn, depositBtn, withdrawBtn, logoutBtn;

        public ATMFrame(String cardNumber) {
            this.cardNumber = cardNumber;

            setTitle("ATM Dashboard");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setUndecorated(true);

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

            // Header with welcome and exit button
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            header.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

            JLabel heading = new JLabel("Welcome, Card: " + cardNumber);
            heading.setFont(new Font("Segoe UI Black", Font.BOLD, 36));
            heading.setForeground(Color.WHITE);
            header.add(heading, BorderLayout.WEST);

            JButton exitBtn = createExitButton();
            header.add(exitBtn, BorderLayout.EAST);

            backgroundPanel.add(header, BorderLayout.NORTH);

            // Main content panel
            JPanel contentPanel = new JPanel(new GridBagLayout());
            contentPanel.setOpaque(false);
            backgroundPanel.add(contentPanel, BorderLayout.CENTER);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Balance label
            balanceLabel = new JLabel("Balance: Loading...");
            balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            balanceLabel.setForeground(Color.WHITE);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            contentPanel.add(balanceLabel, gbc);

            // Amount input
            JLabel amountLabel = new JLabel("Enter amount:");
            amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            amountLabel.setForeground(Color.WHITE);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            contentPanel.add(amountLabel, gbc);

            amountField = new JTextField(15);
            amountField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            gbc.gridx = 1;
            contentPanel.add(amountField, gbc);

            // Buttons
            checkBalanceBtn = createButton("Check Balance", new Color(40, 167, 69), Color.WHITE);
            depositBtn = createButton("Deposit", new Color(0, 123, 255), Color.WHITE);
            withdrawBtn = createButton("Withdraw", new Color(220, 53, 69), Color.WHITE);
            logoutBtn = createButton("Logout", new Color(108, 117, 125), Color.WHITE);

            gbc.gridx = 0;
            gbc.gridy = 2;
            contentPanel.add(checkBalanceBtn, gbc);

            gbc.gridx = 1;
            contentPanel.add(depositBtn, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            contentPanel.add(withdrawBtn, gbc);

            gbc.gridx = 1;
            contentPanel.add(logoutBtn, gbc);

            // Load balance on startup
            loadBalance();

            // Button actions
            checkBalanceBtn.addActionListener(e -> showBalance());
            depositBtn.addActionListener(e -> deposit());
            withdrawBtn.addActionListener(e -> withdraw());
            logoutBtn.addActionListener(e -> logout());

            setVisible(true);
        }

        private JButton createButton(String text, Color bgColor, Color fgColor) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
            btn.setBackground(bgColor);
            btn.setForeground(fgColor);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(btn.getBackground());
                    g2d.fillRoundRect(0, 0, btn.getWidth(), btn.getHeight(), 30, 30);
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

        private JButton createExitButton() {
            JButton exitBtn = new JButton("X");
            exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 28));
            exitBtn.setForeground(Color.WHITE);
            exitBtn.setBackground(new Color(0,0,0,0));
            exitBtn.setFocusPainted(false);
            exitBtn.setBorderPainted(false);
            exitBtn.setContentAreaFilled(false);
            exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            exitBtn.setPreferredSize(new Dimension(60, 60));
            exitBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    exitBtn.setOpaque(true);
                    exitBtn.setBackground(Color.RED);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    exitBtn.setOpaque(false);
                    exitBtn.setBackground(new Color(0,0,0,0));
                }
            });
            exitBtn.addActionListener(e -> System.exit(0));
            return exitBtn;
        }

        private void loadBalance() {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT balance FROM accounts WHERE card_number = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, cardNumber);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    balance = rs.getDouble("balance");
                    balanceLabel.setText(String.format("Balance: Rs. %.2f", balance));
                } else {
                    balanceLabel.setText("Balance: Account not found!");
                }
            } catch (SQLException ex) {
                ToastMessage.showToast(this, "Error fetching balance: " + ex.getMessage(), ToastMessage.ERROR);
            }
        }

        private void showBalance() {
            ToastMessage.showToast(this, String.format("Your current balance is Rs. %.2f", balance), ToastMessage.INFO);
        }

        private void deposit() {
            String amountStr = amountField.getText().trim();
            if (!isValidAmount(amountStr)) return;

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                ToastMessage.showToast(this, "Enter a positive amount to deposit.", ToastMessage.WARNING);
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE accounts SET balance = balance + ? WHERE card_number = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDouble(1, amount);
                ps.setString(2, cardNumber);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    balance += amount;
                    balanceLabel.setText(String.format("Balance: Rs. %.2f", balance));
                    ToastMessage.showToast(this, "Deposit successful!", ToastMessage.SUCCESS);
                    amountField.setText("");
                } else {
                    ToastMessage.showToast(this, "Deposit failed, account not found.", ToastMessage.ERROR);
                }
            } catch (SQLException ex) {
                ToastMessage.showToast(this, "Deposit error: " + ex.getMessage(), ToastMessage.ERROR);
            }
        }

        private void withdraw() {
            String amountStr = amountField.getText().trim();
            if (!isValidAmount(amountStr)) return;

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                ToastMessage.showToast(this, "Enter a positive amount to withdraw.", ToastMessage.WARNING);
                return;
            }
            if (amount > balance) {
                ToastMessage.showToast(this, "Insufficient balance.", ToastMessage.WARNING);
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE accounts SET balance = balance - ? WHERE card_number = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDouble(1, amount);
                ps.setString(2, cardNumber);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    balance -= amount;
                    balanceLabel.setText(String.format("Balance: Rs. %.2f", balance));
                    ToastMessage.showToast(this, "Withdrawal successful!", ToastMessage.SUCCESS);
                    amountField.setText("");
                } else {
                    ToastMessage.showToast(this, "Withdrawal failed, account not found.", ToastMessage.ERROR);
                }
            } catch (SQLException ex) {
                ToastMessage.showToast(this, "Withdrawal error: " + ex.getMessage(), ToastMessage.ERROR);
            }
        }

        private boolean isValidAmount(String amountStr) {
            if (amountStr.isEmpty()) {
                ToastMessage.showToast(this, "Please enter an amount.", ToastMessage.WARNING);
                return false;
            }
            try {
                Double.parseDouble(amountStr);
                return true;
            } catch (NumberFormatException e) {
                ToastMessage.showToast(this, "Enter a valid number.", ToastMessage.WARNING);
                return false;
            }
        }

        private void logout() {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        }
    }
