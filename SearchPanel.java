package library;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SearchPanel extends JPanel {

    private final LibraryApp app;

    private JTextField txtTitle, txtAuthor, txtGenre;
    private JComboBox<String> cmbType;
    private JTable table;

    public SearchPanel(LibraryApp app) {
        this.app = app;
        initComponents();
        loadAllItems();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(new Color(245, 247, 250));

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Search / View Catalog");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(33, 37, 41));
        header.add(title, BorderLayout.WEST);

        JButton btnBackTop = createButton("Back to Menu");
        btnBackTop.addActionListener(e -> app.showScreen(ScreenNames.CUSTOMER_MENU));
        header.add(btnBackTop, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== Main Content =====
        JPanel content = new JPanel(new BorderLayout(14, 14));
        content.setOpaque(false);
        add(content, BorderLayout.CENTER);

        // ===== Left: Filters Card =====
        JPanel filtersCard = createCardPanel();
        filtersCard.setLayout(new GridBagLayout());
        filtersCard.setPreferredSize(new Dimension(320, 10));

        JLabel filtersTitle = new JLabel("Search Filters");
        filtersTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        filtersTitle.setForeground(new Color(33, 37, 41));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: title label as section title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        filtersCard.add(filtersTitle, gbc);

        gbc.gridwidth = 1;

        // Inputs
        txtTitle = createTextField();
        txtAuthor = createTextField();
        txtGenre = createTextField();
        cmbType = new JComboBox<>(new String[]{"All", "Book", "EBook", "Journal", "Magazine"});
        cmbType.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbType.setForeground(Color.BLACK);
        cmbType.setBackground(Color.WHITE);

        int y = 1;
        addFilterRow(filtersCard, gbc, y++, "Title:", txtTitle);
        addFilterRow(filtersCard, gbc, y++, "Author:", txtAuthor);
        addFilterRow(filtersCard, gbc, y++, "Genre:", txtGenre);
        addFilterRow(filtersCard, gbc, y++, "Type:", cmbType);

        // Buttons
        JButton btnSearch = createButton("Search");
        btnSearch.addActionListener(this::doSearch);

        JButton btnClear = createButton("Clear");
        btnClear.addActionListener(e -> {
            txtTitle.setText("");
            txtAuthor.setText("");
            txtGenre.setText("");
            cmbType.setSelectedIndex(0);
            loadAllItems();
        });

        JPanel filterBtns = new JPanel(new GridLayout(1, 2, 10, 0));
        filterBtns.setOpaque(false);
        filterBtns.add(btnSearch);
        filterBtns.add(btnClear);

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        filtersCard.add(filterBtns, gbc);

        content.add(filtersCard, BorderLayout.WEST);

        // ===== Center: Results Card =====
        JPanel resultsCard = createCardPanel();
        resultsCard.setLayout(new BorderLayout(10, 10));

        JLabel resultsTitle = new JLabel("Results");
        resultsTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        resultsTitle.setForeground(new Color(33, 37, 41));
        resultsCard.add(resultsTitle, BorderLayout.NORTH);

        table = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Genre", "Type", "Price", "Available"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        resultsCard.add(scrollPane, BorderLayout.CENTER);

        JButton btnViewDetails = createButton("View Book Details");
        btnViewDetails.addActionListener(this::openBookDetails);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setOpaque(false);
        bottom.add(btnViewDetails);

        resultsCard.add(bottom, BorderLayout.SOUTH);

        content.add(resultsCard, BorderLayout.CENTER);
    }

    // ===== Simple UI helpers (same style family) =====

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

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(Color.BLACK);
        field.setBackground(Color.WHITE);
        field.setColumns(18); // enough width inside filter panel
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setForeground(Color.BLACK);
        return lbl;
    }

    private void addFilterRow(JPanel parent, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.0;
        parent.add(createLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        parent.add(field, gbc);
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

    // ===== Data =====

    public void loadAllItems() {
        Search search = new Search();
        List<LibraryItems> items = search.byTitle(""); // all
        fillTable(items);
    }

    private void doSearch(ActionEvent e) {
        Search search = new Search();
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String genre = txtGenre.getText().trim();
        String type = cmbType.getSelectedItem().toString();

        List<LibraryItems> items;

        if (!title.isEmpty()) {
            items = search.byTitle(title);
        } else if (!author.isEmpty()) {
            items = search.byAuthor(author);
        } else if (!genre.isEmpty()) {
            items = search.byGenre(genre);
        } else {
            items = search.byTitle("");
        }

        if (!type.equalsIgnoreCase("All")) {
            items.removeIf(i -> !i.getType().equalsIgnoreCase(type));
        }

        fillTable(items);
    }

    private void fillTable(List<LibraryItems> items) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (LibraryItems item : items) {
            model.addRow(new Object[]{
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

    private void openBookDetails(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book first.");
            return;
        }
        int id = (int) table.getValueAt(row, 0);
        LibraryItems item = LibraryItems.findById(id);
        if (item != null) {
            app.getBookDetailsPanel().showBook(item);
            app.showScreen(ScreenNames.BOOK_DETAILS);
        }
    }
}
