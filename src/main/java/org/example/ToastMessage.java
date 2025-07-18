package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ToastMessage extends JDialog {
    public static final int SUCCESS = new Color(76, 175, 80).getRGB();    // Green
    public static final int ERROR = new Color(244, 67, 54).getRGB();      // Red
    public static final int WARNING = new Color(255, 193, 7).getRGB();    // Amber
    public static final int INFO = new Color(33, 150, 243).getRGB();      // Blue

    private static final int WIDTH = 350;
    private static final int HEIGHT = 60;
    private static final int DISPLAY_TIME = 2000; // milliseconds

    public ToastMessage(JFrame parent, String message, int bgColor) {
        super(parent);
        setUndecorated(true);
        setSize(WIDTH, HEIGHT);
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());

        // Set rounded corners
        setShape(new RoundRectangle2D.Double(0, 0, WIDTH, HEIGHT, 30, 30));

        // Message label styling
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().setBackground(new Color(bgColor));
        add(label, BorderLayout.CENTER);

        // Position the toast near bottom center of parent frame
        int x = parent.getX() + (parent.getWidth() - WIDTH) / 2;
        int y = parent.getY() + parent.getHeight() - HEIGHT - 50;
        setLocation(x, y);

        // Show non-modal toast (does NOT block UI)
        setModal(false);
        setVisible(true);

        // Auto-close after DISPLAY_TIME milliseconds
        new javax.swing.Timer(DISPLAY_TIME, e -> dispose()).start();
    }

    public static void showToast(JFrame parent, String message, int type) {
        // Run on Event Dispatch Thread (safe Swing call)
        SwingUtilities.invokeLater(() -> new ToastMessage(parent, message, type));
    }
}
