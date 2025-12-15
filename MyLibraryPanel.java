package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyLibraryPanel extends JPanel {

    private final LibraryApp app;
    private JTable tblBorrowed;
    private JTable tblPurchased;

    private DefaultTableModel borrowedModel;
    private DefaultTableModel purchasedModel;

    private User currentUser;

    public MyLibraryPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JLabel header = new JLabel("My Library", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1,2,10,10));

        // ================= Borrowed =================
        JPanel borrowedPanel = new JPanel(new BorderLayout());
        borrowedPanel.setBorder(BorderFactory.createTitledBorder("Borrowed Books"));

        borrowedModel = new DefaultTableModel(
                new Object[]{"Borrow ID", "Book ID", "Title", "Borrow Date", "Due Date", "Status"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        tblBorrowed = new JTable(borrowedModel);
        tblBorrowed.setRowHeight(26);
        borrowedPanel.add(new JScrollPane(tblBorrowed), BorderLayout.CENTER);

        JButton btnReturn = new JButton("Return Selected Book");
        btnReturn.addActionListener(e -> returnSelectedBook());
        borrowedPanel.add(btnReturn, BorderLayout.SOUTH);

        // ================= Purchased =================
        JPanel purchasedPanel = new JPanel(new BorderLayout());
        purchasedPanel.setBorder(BorderFactory.createTitledBorder("Purchased Items"));

        purchasedModel = new DefaultTableModel(
                new Object[]{"Purchase ID", "Date", "Status", "Total", "Method"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
//new Object[]{"Purchase ID", "Book ID", "Title", "Date", "Status", "Total", "Method"}

        tblPurchased = new JTable(purchasedModel);
        tblPurchased.setRowHeight(26);
        purchasedPanel.add(new JScrollPane(tblPurchased), BorderLayout.CENTER);

        center.add(borrowedPanel);
        center.add(purchasedPanel);

        add(center, BorderLayout.CENTER);

        JButton btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> app.showScreen(ScreenNames.CUSTOMER_MENU));
        add(btnBack, BorderLayout.SOUTH);
        

    }

    public void loadDataForUser(User user) {
        this.currentUser = user;
        loadBorrowedForUser(user.getUserId());
        loadPurchasesForUser(user.getUserId());
    }

    private void loadBorrowedForUser(int userId) {
        borrowedModel.setRowCount(0);

        String sql =
                "SELECT b.borrow_id, b.book_id, i.title, b.borrow_date, b.return_due_date, b.status " +
                "FROM borrowed_books b " +
                "LEFT JOIN library_items i ON b.book_id = i.id " +
                "WHERE b.user_id = ? " +
                "ORDER BY b.borrow_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    borrowedModel.addRow(new Object[]{
                            rs.getInt("borrow_id"),
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getDate("borrow_date"),
                            rs.getDate("return_due_date"),
                            rs.getString("status")
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading borrowed books: " + e.getMessage());
        }
    }

    private void loadPurchasesForUser(int userId) {
    purchasedModel.setRowCount(0);

    String sql =
        "SELECT purchase_id, purchase_date, payment_status, total_cost, payment_method " +
        "FROM purchase " +
        "WHERE user_id = ? " +
        "ORDER BY purchase_id DESC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, userId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                purchasedModel.addRow(new Object[]{
                    rs.getInt("purchase_id"),
                    rs.getDate("purchase_date"),
                    rs.getString("payment_status"),
                    rs.getDouble("total_cost"),
                    rs.getString("payment_method")
                });
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading purchases: " + e.getMessage());
    }
}

//    private void loadPurchasesForUser(int userId) {
//        purchasedModel.setRowCount(0);
//
//        String sql =
//                "SELECT p.purchase_id, p.book_id, i.title, p.purchase_date, p.payment_status, p.total_cost, p.payment_method " +
//                "FROM purchases p " +
//                "LEFT JOIN library_items i ON p.book_id = i.id " +
//                "WHERE p.user_id = ? " +
//                "ORDER BY p.purchase_id DESC";
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, userId);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    purchasedModel.addRow(new Object[]{
//                            rs.getInt("purchase_id"),
//                            rs.getInt("book_id"),
//                            rs.getString("title"),
//                            rs.getDate("purchase_date"),
//                            rs.getString("payment_status"),
//                            rs.getDouble("total_cost"),
//                            rs.getString("payment_method")
//                    });
//                }
//            }
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Error loading purchases: " + e.getMessage());
//        }
//    }

    private void returnSelectedBook() {
        int row = tblBorrowed.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a borrowed book first.");
            return;
        }

        int borrowId = (int) tblBorrowed.getValueAt(row, 0);
        int bookId = (int) tblBorrowed.getValueAt(row, 1);
        String status = String.valueOf(tblBorrowed.getValueAt(row, 5));

        if (!status.equalsIgnoreCase("BORROWED")) {
            JOptionPane.showMessageDialog(this, "This book is already returned.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Return this book now?",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        // Transaction: update borrowed_books + update availability
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1) Update borrowed_books status
            String sql1 = "UPDATE borrowed_books SET status='RETURNED' WHERE borrow_id=? AND status='BORROWED'";
            try (PreparedStatement ps1 = conn.prepareStatement(sql1)) {
                ps1.setInt(1, borrowId);
                int updated = ps1.executeUpdate();
                if (updated == 0) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Return failed. Record not in BORROWED status.");
                    return;
                }
            }

            // 2) Set availability TRUE in library_items
            String sql2 = "UPDATE library_items SET availability=TRUE WHERE id=?";
            try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                ps2.setInt(1, bookId);
                ps2.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Book returned successfully.");

            // Refresh tables
            if (currentUser != null) {
                loadDataForUser(currentUser);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error returning book: " + e.getMessage());
        }
    }
}
