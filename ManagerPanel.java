package library;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManagerPanel extends JPanel {

    private final LibraryApp app;

    private JTable tblBooks;
    private DefaultTableModel booksModel;

    public ManagerPanel(LibraryApp app) {
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

        JLabel banner = ImageUtils.createBackgroundLabel("b4.jpg", 1000, 140);
        header.add(banner, BorderLayout.NORTH);

        JLabel title = new JLabel("Manager Dashboard");
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
                return false; // read-only table
            }
        };

        tblBooks = new JTable(booksModel);
        tblBooks.setRowHeight(26);
        tblBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tblBooks);
        scroll.setBorder(BorderFactory.createTitledBorder("Books List"));

        add(scroll, BorderLayout.CENTER);

        // ===== Right: Actions Panel =====
        JPanel actions = new JPanel();
        actions.setLayout(new GridLayout(0, 1, 10, 10));
        actions.setBorder(BorderFactory.createTitledBorder("Actions"));

        JButton btnRefresh = new JButton("Refresh Books");
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadBooks());

        JButton btnAdd = new JButton("Add New Book");
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> openBookForm(null));

        JButton btnUpdate = new JButton("Update Selected Book");
        btnUpdate.setFocusPainted(false);
        btnUpdate.addActionListener(e -> {
            LibraryItems selected = getSelectedBook();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a book first.");
                return;
            }
            openBookForm(selected);
        });

        JButton btnDelete = new JButton("Delete Selected Book");
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteSelectedBook());

        JButton btnBorrowed = new JButton("View All Borrowed Books");
        btnBorrowed.setFocusPainted(false);
        btnBorrowed.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Next step: open BorrowedBooks list dialog/panel.")
        );
        btnBorrowed.addActionListener(e -> {
            app.getBorrowedBooksListPanel().loadAll();
            app.showScreen(ScreenNames.BORROWED_LIST);
        });

        JButton btnPurchases = new JButton("View All Purchases");
        btnPurchases.setFocusPainted(false);

        btnPurchases.addActionListener(e -> {
            app.getPurchasesListPanel().loadAll();
            app.showScreen(ScreenNames.PURCHASES_LIST);
        });

        
        
        actions.add(btnRefresh);
        actions.add(btnAdd);
        actions.add(btnUpdate);
        actions.add(btnDelete);
        actions.add(btnBorrowed);
        actions.add(btnPurchases);

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
        // Best: re-fetch from DB so you always get latest record
        return LibraryItems.findById(id);
    }

    private void openBookForm(LibraryItems book) {
        Window window = SwingUtilities.getWindowAncestor(this);
        BookFormDialog dialog = new BookFormDialog(window, book, () -> loadBooks());
        dialog.setVisible(true);
    }

    private void deleteSelectedBook() {
        LibraryItems selected = getSelectedBook();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a book first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete book: " + selected.getTitle() + " ?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        Manager manager = new Manager(
                app.getCurrentUser().getUserId() + "",
                app.getCurrentUser().getFirstName()
        );
        manager.deleteBook(selected.getId());
        JOptionPane.showMessageDialog(this, "Book deleted successfully.");
        loadBooks();
    }
}
