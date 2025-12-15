package library;

import library.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBooksListPanel extends JPanel {

    private final LibraryApp app;

    private JTable table;
    private DefaultTableModel model;

    public BorrowedBooksListPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("All Borrowed Books");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);

        JButton btnBack = new JButton("Back");
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> goBack());
        header.add(btnBack, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(
                new Object[]{"Borrow ID", "User ID", "Book ID", "Title", "Borrow Date", "Due Date", "Status"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom actions
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadAll());
        bottom.add(btnRefresh);

        add(bottom, BorderLayout.SOUTH);
    }

    private void goBack() {
        String role = app.getCurrentUser().getRole().toLowerCase();
        if (role.equals("manager")) {
            app.showScreen(ScreenNames.MANAGER_MENU);
        } else {
            app.showScreen(ScreenNames.EMPLOYEE_MENU);
        }
    }

    public void loadAll() {
        model.setRowCount(0);
        List<Object[]> rows = fetchBorrowedRows();
        for (Object[] r : rows) model.addRow(r);
    }

    private List<Object[]> fetchBorrowedRows() {
        List<Object[]> rows = new ArrayList<>();

        String sql =
                "SELECT b.borrow_id, b.user_id, b.book_id, i.title, " +
                "       b.borrow_date, b.return_due_date, b.status " +
                "FROM borrowed_books b " +
                "LEFT JOIN library_items i ON b.book_id = i.id " +
                "ORDER BY b.borrow_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("borrow_id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getDate("borrow_date"),
                        rs.getDate("return_due_date"),
                        rs.getString("status")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading borrowed books: " + e.getMessage());
        }

        return rows;
    }
}
