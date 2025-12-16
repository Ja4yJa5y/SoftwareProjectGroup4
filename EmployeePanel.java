package library;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class EmployeePanel extends JPanel {

    private final LibraryApp app;

    private JTable tblBooks;
    private DefaultTableModel booksModel;

    public EmployeePanel(LibraryApp app) {
        this.app = app;
        initComponents();
        loadBooks();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(new Color(245, 247, 250));

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Employee Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(33, 37, 41));

        JButton btnLogout = createButton("Logout");
        btnLogout.addActionListener(e -> app.showScreen(ScreenNames.LOGIN));

        header.add(title, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== Main Content =====
        JPanel content = new JPanel(new BorderLayout(14, 14));
        content.setOpaque(false);
        add(content, BorderLayout.CENTER);

        // ===== Table Card =====
        JPanel tableCard = createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

        JLabel tblTitle = new JLabel("Books List");
        tblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        tblTitle.setForeground(new Color(33, 37, 41));
        tableCard.add(tblTitle, BorderLayout.NORTH);

        booksModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Genre", "Type", "Price", "Available"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblBooks = new JTable(booksModel);
        styleTable(tblBooks);

        JScrollPane scroll = new JScrollPane(tblBooks);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        scroll.getViewport().setBackground(Color.WHITE);

        tableCard.add(scroll, BorderLayout.CENTER);
        content.add(tableCard, BorderLayout.CENTER);

        // ===== Actions Card =====
        JPanel actionsCard = createCardPanel();
        actionsCard.setLayout(new BorderLayout(8, 8));
        actionsCard.setPreferredSize(new Dimension(260, 10));

        JLabel actionsTitle = new JLabel("Actions");
        actionsTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        actionsTitle.setForeground(new Color(33, 37, 41));
        actionsCard.add(actionsTitle, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(0, 1, 10, 10));
        actions.setOpaque(false);

        JButton btnRefresh = createButton("Refresh Books");
        btnRefresh.addActionListener(e -> loadBooks());

        JButton btnUpdate = createButton("Update Selected Book");
        btnUpdate.addActionListener(e -> {
            LibraryItems selected = getSelectedBook();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a book first.");
                return;
            }
            openBookForm(selected);
        });

        JButton btnBorrowed = createButton("View All Borrowed Books");
        btnBorrowed.addActionListener(e -> {
            app.getBorrowedBooksListPanel().loadAll();
            app.showScreen(ScreenNames.BORROWED_LIST);
        });

        actions.add(btnRefresh);
        actions.add(btnUpdate);
        actions.add(btnBorrowed);

        actionsCard.add(actions, BorderLayout.CENTER);
        content.add(actionsCard, BorderLayout.EAST);
    }

    // ===== Styling helpers (same as Manager) =====

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

    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 13));
        th.setForeground(new Color(33, 37, 41));
        th.setBackground(new Color(248, 249, 250));
    }

    // ===== Logic =====

    private void loadBooks() {
        booksModel.setRowCount(0);
        List<LibraryItems> items = LibraryItems.findAll();
        for (LibraryItems item : items) {
            booksModel.addRow(new Object[]{
                    item.getId(),
                    item.getTitle(),
                    item.getAuthor(),
                    item.getGenre(),
                    item.getType(),
                    item.getPrice(),
                    item.isAvailability() ? "Yes" : "No"
            });
        }
    }

    private LibraryItems getSelectedBook() {
        int row = tblBooks.getSelectedRow();
        if (row == -1) return null;

        int id = (int) booksModel.getValueAt(row, 0);
        return LibraryItems.findById(id);
    }

    private void openBookForm(LibraryItems book) {
        Window window = SwingUtilities.getWindowAncestor(this);
        BookFormDialog dialog = new BookFormDialog(window, book, this::loadBooks);
        dialog.setVisible(true);
    }
}
