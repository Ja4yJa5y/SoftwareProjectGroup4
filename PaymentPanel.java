package library;

import library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class PaymentPanel extends JPanel {

    private LibraryApp app;
    private JLabel lblAmount;
    private String selectedPaymentType = "Visa"; // default

    public PaymentPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JLabel header = new JLabel("Payment", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10,10));
        add(center, BorderLayout.CENTER);

        lblAmount = new JLabel("Amount: 0.0", SwingConstants.CENTER);
        lblAmount.setFont(new Font("SansSerif", Font.BOLD, 18));
        center.add(lblAmount, BorderLayout.NORTH);

        JPanel payments = new JPanel(new FlowLayout());
        JButton btnVisa = new JButton("Visa", ImageUtils.loadIcon("visa.png", 50, 30));
        btnVisa.addActionListener(e -> selectType("Visa"));

        JButton btnMada = new JButton("Mada", ImageUtils.loadIcon("mada.png", 50, 30));
        btnMada.addActionListener(e -> selectType("Mada"));

        JButton btnApplePay = new JButton("Apple Pay", ImageUtils.loadIcon("applepay.png", 50, 30));
        btnApplePay.addActionListener(e -> selectType("Apple Pay"));

        payments.add(btnVisa);
        payments.add(btnMada);
        payments.add(btnApplePay);

        center.add(payments, BorderLayout.CENTER);

        JButton btnPay = new JButton("Pay Now");
        btnPay.addActionListener(this::processPayment);
        center.add(btnPay, BorderLayout.SOUTH);

        JButton btnBack = new JButton("Back to Cart");
        btnBack.addActionListener(e -> app.showScreen(ScreenNames.CART));
        add(btnBack, BorderLayout.SOUTH);
    }

    public void updateAmount(double amount) {
        lblAmount.setText("Amount: " + amount);
    }

    private void selectType(String type) {
        selectedPaymentType = type;
        JOptionPane.showMessageDialog(this, "Payment type selected: " + type);
    }

    private void processPayment(ActionEvent e) {
        double amount = app.getCartPanel().getCart().calculateTotal();
        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }

        Payment payment = new Payment();
        payment.processPayment(amount, selectedPaymentType);

        // Create and save purchase
        Purchase purchase = new Purchase();

        purchase.setUserId(app.getCurrentUser().getUserId());
        purchase.setPurchaseDate(new java.sql.Date(System.currentTimeMillis()));
        purchase.setPaymentMethod(selectedPaymentType);
        purchase.setPaymentStatus("PAID");
        purchase.setTotalCost(amount);
        purchase.save();

        JOptionPane.showMessageDialog(this,
                "Payment successful.\nAmount: " + amount +
                "\nType: " + selectedPaymentType +
                "\nDate: " + new Date());

        // Clear cart and go back to customer menu
        app.getCartPanel().getCart().clearCart();
        app.getCartPanel().refreshCart();
        app.showScreen(ScreenNames.CUSTOMER_MENU);
    }
}
