package library;

import javax.swing.*;
import java.awt.*;

public class CartPanel extends JPanel {

    private LibraryApp app;
    private Cart cart;
    private JLabel lblTotal;

    public CartPanel(LibraryApp app) {
        this.app = app;
        this.cart = new Cart();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        setBackground(new Color(245, 247, 250)); // light background

        // ===== Header =====
        JLabel header = new JLabel("Cart / Checkout", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(new Color(33, 37, 41));
        add(header, BorderLayout.NORTH);

        // ===== Center Container =====
        JPanel center = new JPanel(new BorderLayout(15, 15));
//        center.setBackground(getBackground());
//        center.setBackground(new Color(33, 37, 41));

        add(center, BorderLayout.CENTER);

        // ===== Image Section =====
        JLabel img = ImageUtils.createBackgroundLabel("b6.webp", 220, 220);
        img.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        center.add(img, BorderLayout.WEST);

        // ===== Right Panel =====
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
//        right.setBackground(Color.WHITE);
//        right.setBackground(new Color(33, 37, 41));
        right.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        center.add(right, BorderLayout.CENTER);

        lblTotal = new JLabel("Total: 0.0");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(lblTotal);

        right.add(Box.createVerticalStrut(20));

        // ===== Buttons =====
        JButton btnClear = createButton("Clear Cart", new Color(220, 53, 69));
        btnClear.setForeground(Color.BLACK);
        btnClear.addActionListener(e -> {
            cart.clearCart();
            refreshCart();
        });
        right.add(btnClear);

        right.add(Box.createVerticalStrut(10));

        JButton btnProceed = createButton("Proceed to Payment", new Color(40, 167, 69));
        btnProceed.addActionListener(e -> app.showScreen(ScreenNames.PAYMENT));
        btnProceed.setForeground(Color.BLACK);

        right.add(btnProceed);

        right.add(Box.createVerticalStrut(10));

        JButton btnBack = createButton("Back to Customer Menu", new Color(108, 117, 125));
        btnBack.setForeground(Color.BLACK);

        btnBack.addActionListener(e -> app.showScreen(ScreenNames.CUSTOMER_MENU));
        right.add(btnBack);
    }

    // ===== Button Style Helper =====
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(250, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        return btn;
    }

    public void refreshCart() {
        double total = cart.calculateTotal();
        lblTotal.setText("Total: " + total);
    }

    public Cart getCart() {
        return cart;
    }
}
