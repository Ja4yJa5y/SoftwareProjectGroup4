package library;

import library.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // ===== Header (banner + title + logout) =====
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // If you want, you can use another banner image; b4 is fine as consistent admin theme
        JLabel banner = ImageUtils.createBackgroundLabel("b4.jpg", 1000, 140);
        header.add(banner, BorderLayout.NORTH);

        JLabel title = new JLabel("Employee Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> app.showScreen(ScreenNames.LOGIN));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(btnLogout, BorderLayout.EAST);

        header.add(titleRow, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // ===== Center: Books Table =====
        booksModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Genre", "Type", "Price", "Available"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblBooks = new JTable(booksModel);
        tblBooks.setRowHeight(26);
        tblBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tblBooks);
        scroll.setBorder(BorderFactory.createTitledBorder("Books List"));

        add(scroll, BorderLayout.CENTER);

        // ===== Right: Actions =====
        JPanel actions = new JPanel(new GridLayout(0, 1, 10, 10));
        actions.setBorder(BorderFactory.createTitledBorder("Actions"));

        JButton btnRefresh = new JButton("Refresh Books");
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadBooks());

        JButton btnUpdate = new JButton("Update Selected Book");
        btnUpdate.setFocusPainted(false);
        btnUpdate.addActionListener(e -> {
            LibraryItems selected = getSelectedBook();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a book first.");
                return;
            }
            openBookForm(selected); // opens the same dialog, but employee uses update only
        });

        JButton btnBorrowed = new JButton("View All Borrowed Books");
        btnBorrowed.setFocusPainted(false);
        btnBorrowed.addActionListener(e -> {
            app.getBorrowedBooksListPanel().loadAll();
            app.showScreen(ScreenNames.BORROWED_LIST);
        });

        actions.add(btnRefresh);
        actions.add(btnUpdate);
        actions.add(btnBorrowed);

        add(actions, BorderLayout.EAST);
    }

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
        return LibraryItems.findById(id); // refetch to ensure latest data
    }

    private void openBookForm(LibraryItems book) {
        Window window = SwingUtilities.getWindowAncestor(this);
        BookFormDialog dialog = new BookFormDialog(window, book, this::loadBooks);
        dialog.setVisible(true);
    }
}
