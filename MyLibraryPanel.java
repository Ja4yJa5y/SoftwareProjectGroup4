package library;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(new Color(245, 247, 250));

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("My Library");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(33, 37, 41));
        header.add(title, BorderLayout.WEST);

        JButton btnBackTop = createButton("Back to Menu");
        btnBackTop.addActionListener(e -> app.showScreen(ScreenNames.CUSTOMER_MENU));
        header.add(btnBackTop, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== Center: Two Cards =====
        JPanel center = new JPanel(new GridLayout(1, 2, 14, 14));
        center.setOpaque(false);

        // ================= Borrowed Card =================
        JPanel borrowedCard = createCardPanel();
        borrowedCard.setLayout(new BorderLayout(10, 10));

        JLabel borrowedTitle = new JLabel("Borrowed Books");
        borrowedTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        borrowedTitle.setForeground(new Color(33, 37, 41));
        borrowedCard.add(borrowedTitle, BorderLayout.NORTH);

        borrowedModel = new DefaultTableModel(
                new Object[]{"Borrow ID", "Book ID", "Title", "Borrow Date", "Due Date", "Status"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        tblBorrowed = new JTable(borrowedModel);
        styleTable(tblBorrowed);

        JScrollPane borrowedScroll = new JScrollPane(tblBorrowed);
        borrowedScroll.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        borrowedScroll.getViewport().setBackground(Color.WHITE);

        borrowedCard.add(borrowedScroll, BorderLayout.CENTER);

        JButton btnReturn = createButton("Return Selected Book");
        btnReturn.addActionListener(e -> returnSelectedBook());

        JPanel borrowedBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        borrowedBottom.setOpaque(false);
        borrowedBottom.add(btnReturn);
        borrowedCard.add(borrowedBottom, BorderLayout.SOUTH);

        // ================= Purchased Card =================
        JPanel purchasedCard = createCardPanel();
        purchasedCard.setLayout(new BorderLayout(10, 10));

        JLabel purchasedTitle = new JLabel("Purchased Items");
        purchasedTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        purchasedTitle.setForeground(new Color(33, 37, 41));
        purchasedCard.add(purchasedTitle, BorderLayout.NORTH);

        purchasedModel = new DefaultTableModel(
                new Object[]{"Purchase ID", "Date", "Status", "Total", "Method"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        tblPurchased = new JTable(purchasedModel);
        styleTable(tblPurchased);

        JScrollPane purchasedScroll = new JScrollPane(tblPurchased);
        purchasedScroll.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        purchasedScroll.getViewport().setBackground(Color.WHITE);

        purchasedCard.add(purchasedScroll, BorderLayout.CENTER);

        center.add(borrowedCard);
        center.add(purchasedCard);

        add(center, BorderLayout.CENTER);
    }

    // ===== Style helpers (same as Manager/Employee) =====

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
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(232, 236, 241));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(28);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setGridColor(new Color(230, 230, 230));
        t.setShowVerticalLines(false);
        t.setBackground(Color.WHITE);

        JTableHeader th = t.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 13));
        th.setForeground(new Color(33, 37, 41));
        th.setBackground(new Color(248, 249, 250));
    }

    // ===== Data loading =====

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

    // ===== Return logic (same as yours) =====

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

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

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

            String sql2 = "UPDATE library_items SET availability=TRUE WHERE id=?";
            try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                ps2.setInt(1, bookId);
                ps2.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Book returned successfully.");

            if (currentUser != null) {
                loadDataForUser(currentUser);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error returning book: " + e.getMessage());
        }
    }
}
