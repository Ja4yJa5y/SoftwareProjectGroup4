package library;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CustomerMenuPanel extends JPanel {

    private final LibraryApp app;

    public CustomerMenuPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(new Color(245, 247, 250));

        // ===== Header (card style) =====
        JPanel headerCard = createCardPanel();
        headerCard.setLayout(new BorderLayout(10, 10));

//        JLabel logo = new JLabel(ImageUtils.loadIcon("b1.png", 60, 60));
//        headerCard.add(logo, BorderLayout.WEST);

        JLabel title = new JLabel("Customer Main Menu", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(33, 37, 41));
        headerCard.add(title, BorderLayout.CENTER);

        JButton btnLogout = createButton("Logout");
        btnLogout.addActionListener(e -> app.showScreen(ScreenNames.LOGIN));
        headerCard.add(btnLogout, BorderLayout.EAST);

        add(headerCard, BorderLayout.NORTH);

        // ===== Center (buttons card) =====
        JPanel centerCard = createCardPanel();
        centerCard.setLayout(new BorderLayout(10, 10));

        JLabel actionsTitle = new JLabel("Choose an option");
        actionsTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        actionsTitle.setForeground(new Color(33, 37, 41));
        centerCard.add(actionsTitle, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 2, 12, 12));
        grid.setOpaque(false);

        JButton btnSearch = createButton("Search / View Catalog");
        btnSearch.addActionListener(e -> app.showScreen(ScreenNames.SEARCH));

        JButton btnBorrow = createButton("Borrow a Book");
        btnBorrow.addActionListener(this::openBorrowFromSearch);

        JButton btnAddToCart = createButton("Add Book to Cart");
        btnAddToCart.addActionListener(this::openCart);

        JButton btnViewCart = createButton("View Cart");
        btnViewCart.addActionListener(this::openCart);

        JButton btnCheckout = createButton("Checkout");
        btnCheckout.addActionListener(e -> app.showScreen(ScreenNames.CART));

        JButton btnMyLibrary = createButton("My Library (Borrowed & Purchased)");
        btnMyLibrary.addActionListener(e -> {
            app.getMyLibraryPanel().loadDataForUser(app.getCurrentUser());
            app.showScreen(ScreenNames.MY_LIBRARY);
        });

        grid.add(btnSearch);
        grid.add(btnBorrow);
        grid.add(btnAddToCart);
        grid.add(btnViewCart);
        grid.add(btnCheckout);
        grid.add(btnMyLibrary);

        centerCard.add(grid, BorderLayout.CENTER);

        add(centerCard, BorderLayout.CENTER);
    }

    // ===== Styling helpers (same style as Manager/Employee) =====

    private JPanel createCardPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return p;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.BLACK); // requested
        btn.setBackground(new Color(232, 236, 241));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ===== Navigation =====

    private void openBorrowFromSearch(ActionEvent e) {
        app.showScreen(ScreenNames.SEARCH);
        // Borrow is triggered from BookDetails UI.
    }

    private void openCart(ActionEvent e) {
        app.getCartPanel().refreshCart();
        app.showScreen(ScreenNames.CART);
    }
}
