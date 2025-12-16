package library;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(new Color(245, 247, 250));

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);

        JLabel title = new JLabel("All Borrowed Books");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        header.add(title, BorderLayout.WEST);

        JPanel headerBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerBtns.setOpaque(false);

        JButton btnBack = createButton("Back");
        btnBack.addActionListener(e -> goBack());

        JButton btnRefreshTop = createButton("Refresh");
        btnRefreshTop.addActionListener(e -> loadAll());

        headerBtns.add(btnRefreshTop);
        headerBtns.add(btnBack);
        header.add(headerBtns, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== Table =====
        model = new DefaultTableModel(
//                new Object[]{"Borrow ID", "User ID", "Book ID", "Title", "Borrow Date", "Due Date", "Status"}
                new Object[]{
                "Borrow ID",
                "User Name",
                "Book Title",
                "Borrow Date",
                "Due Date",
                "Status"
        }
                , 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        scroll.getViewport().setBackground(Color.WHITE);

        // Put table into a simple white container for nicer look
        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(Color.WHITE);
        tableWrap.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        tableWrap.add(scroll, BorderLayout.CENTER);

        add(tableWrap, BorderLayout.CENTER);

        // ===== Bottom actions =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setOpaque(false);

        JButton btnRefresh = createButton("Refresh");
        btnRefresh.addActionListener(e -> loadAll());
        bottom.add(btnRefresh);

        add(bottom, BorderLayout.SOUTH);
    }

    // ===== Simple styling helpers =====

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.BLACK); // requested
        btn.setBackground(new Color(232, 236, 241));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 14, 8, 14)
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
        th.setForeground(Color.BLACK);
        th.setBackground(new Color(248, 249, 250));
    }

    // ===== Navigation =====

    private void goBack() {
        String role = app.getCurrentUser().getRole().toLowerCase();
        if (role.equals("manager")) {
            app.showScreen(ScreenNames.MANAGER_MENU);
        } else {
            app.showScreen(ScreenNames.EMPLOYEE_MENU);
        }
    }

    // ===== Data loading =====

    public void loadAll() {
        model.setRowCount(0);
        List<Object[]> rows = fetchBorrowedRows();
        for (Object[] r : rows) model.addRow(r);
    }

    private List<Object[]> fetchBorrowedRows() {
    List<Object[]> rows = new ArrayList<>();

    String sql =
        "SELECT b.borrow_id, " +
        "       u.first_name || ' ' || u.last_name AS user_name, " +
        "       i.title AS book_title, " +
        "       b.borrow_date, " +
        "       b.return_due_date, " +
        "       b.status " +
        "FROM borrowed_books b " +
        "JOIN users u ON b.user_id = u.user_id " +
        "JOIN library_items i ON b.book_id = i.id " +
        "ORDER BY b.borrow_id DESC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            rows.add(new Object[]{
                    rs.getInt("borrow_id"),
                    rs.getString("user_name"),
                    rs.getString("book_title"),
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

    private List<Object[]> fetchsBorrowedRows() {
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
