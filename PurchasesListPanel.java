package library;

import library.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchasesListPanel extends JPanel {

    private final LibraryApp app;

    private JTable table;
    private DefaultTableModel model;

    public PurchasesListPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("All Purchases");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);

        JButton btnBack = new JButton("Back");
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> app.showScreen(ScreenNames.MANAGER_MENU));
        header.add(btnBack, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(
//                new Object[]{"Purchase ID", "User ID", "Book ID", "Title", "Date", "Method", "Status", "Total Cost"}
                new Object[]{"Purchase ID", "User ID", "Date", "Method", "Status", "Total"}

                , 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadAll());
        bottom.add(btnRefresh);

        add(bottom, BorderLayout.SOUTH);
    }

    public void loadAll() {
        model.setRowCount(0);
        List<Object[]> rows = fetchPurchaseRows();
        for (Object[] r : rows) model.addRow(r);
    }
private List<Object[]> fetchPurchaseRows() {
    List<Object[]> rows = new ArrayList<>();

    String sql =
            "SELECT purchase_id, user_id, purchase_date, payment_method, payment_status, total_cost " +
            "FROM purchase " +
            "ORDER BY purchase_id DESC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            rows.add(new Object[]{
                    rs.getInt("purchase_id"),
                    rs.getInt("user_id"),
                    rs.getDate("purchase_date"),
                    rs.getString("payment_method"),
                    rs.getString("payment_status"),
                    rs.getDouble("total_cost")
            });
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading purchases: " + e.getMessage());
    }

    return rows;
}

    private List<Object[]> fetchesPurchaseRows() {
        List<Object[]> rows = new ArrayList<>();

        String sql =
                "SELECT p.purchase_id, p.user_id, p.book_id, i.title, " +
                "       p.purchase_date, p.payment_method, p.payment_status, p.total_cost " +
                "FROM purchases p " +
                "LEFT JOIN library_items i ON p.book_id = i.id " +
                "ORDER BY p.purchase_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("purchase_id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getDate("purchase_date"),
                        rs.getString("payment_method"),
                        rs.getString("payment_status"),
                        rs.getDouble("total_cost")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading purchases: " + e.getMessage());
        }

        return rows;
    }
}
