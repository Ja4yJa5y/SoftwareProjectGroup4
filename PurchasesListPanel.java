package library;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(new Color(245, 247, 250));

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);

        JLabel title = new JLabel("All Purchases");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        header.add(title, BorderLayout.WEST);

        JPanel headerBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerBtns.setOpaque(false);

        JButton btnRefreshTop = createButton("Refresh");
        btnRefreshTop.addActionListener(e -> loadAll());

        JButton btnBack = createButton("Back");
        btnBack.addActionListener(e -> app.showScreen(ScreenNames.MANAGER_MENU));

        headerBtns.add(btnRefreshTop);
        headerBtns.add(btnBack);
        header.add(headerBtns, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== Table =====
        model = new DefaultTableModel(
                new Object[]{"Purchase ID", "User Name", "Date", "Method", "Status", "Total"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(Color.WHITE);
        tableWrap.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        tableWrap.add(scroll, BorderLayout.CENTER);

        add(tableWrap, BorderLayout.CENTER);

        // ===== Bottom =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setOpaque(false);

        JButton btnRefresh = createButton("Refresh");
        btnRefresh.addActionListener(e -> loadAll());
        bottom.add(btnRefresh);

        add(bottom, BorderLayout.SOUTH);
    }

    // ===== Style helpers =====

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.BLACK);
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

    // ===== Data loading =====

    public void loadAll() {
        model.setRowCount(0);
        List<Object[]> rows = fetchPurchaseRows();
        for (Object[] r : rows) model.addRow(r);
    }

    private List<Object[]> fetchPurchaseRows() {
        List<Object[]> rows = new ArrayList<>();

        // NOTE: your table name is "purchase" (singular) and it has NO book_id
        String sql =
                "SELECT p.purchase_id, " +
                "       u.first_name || ' ' || u.last_name AS user_name, " +
                "       p.purchase_date, p.payment_method, p.payment_status, p.total_cost " +
                "FROM purchase p " +
                "JOIN users u ON p.user_id = u.user_id " +
                "ORDER BY p.purchase_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("purchase_id"),
                        rs.getString("user_name"),
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
