package library;

import library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CustomerMenuPanel extends JPanel {

    private LibraryApp app;

    public CustomerMenuPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        JLabel logo = new JLabel(ImageUtils.loadIcon("b1.png", 70, 70));
        header.add(logo, BorderLayout.WEST);

        JLabel title = new JLabel("Customer Main Menu");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.add(title, BorderLayout.CENTER);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> app.showScreen(ScreenNames.LOGIN));
        header.add(btnLogout, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Center buttons
        JPanel center = new JPanel();
        center.setLayout(new GridLayout(3, 2, 15, 15));

        JButton btnSearch = new JButton("Search / View Catalog");
        btnSearch.addActionListener(e -> app.showScreen(ScreenNames.SEARCH));

        JButton btnBorrow = new JButton("Borrow a Book");
        btnBorrow.addActionListener(this::openBorrowFromSearch);

        JButton btnAddToCart = new JButton("Add Book to Cart");
        btnAddToCart.addActionListener(this::openCart);

        JButton btnViewCart = new JButton("View Cart");
        btnViewCart.addActionListener(this::openCart);

        JButton btnCheckout = new JButton("Checkout");
        btnCheckout.addActionListener(e -> app.showScreen(ScreenNames.CART));

        JButton btnMyLibrary = new JButton("My Library (Borrowed & Purchased)");
        btnMyLibrary.addActionListener(e -> {
            app.getMyLibraryPanel().loadDataForUser(app.getCurrentUser());
            app.showScreen(ScreenNames.MY_LIBRARY);
        });

        center.add(btnSearch);
        center.add(btnBorrow);
        center.add(btnAddToCart);
        center.add(btnViewCart);
        center.add(btnCheckout);
        center.add(btnMyLibrary);

        add(center, BorderLayout.CENTER);
    }

    private void openBorrowFromSearch(ActionEvent e) {
        app.showScreen(ScreenNames.SEARCH);
        // The borrow functionality will be triggered from BookDetails UI.
    }

    private void openCart(ActionEvent e) {
        app.getCartPanel().refreshCart();
        app.showScreen(ScreenNames.CART);
    }
}
