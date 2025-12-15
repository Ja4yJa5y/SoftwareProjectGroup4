package library;

import library.ScreenNames;
import library.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SearchPanel extends JPanel {

    private LibraryApp app;
    private JTextField txtTitle, txtAuthor, txtGenre;
    private JComboBox<String> cmbType;
    private JTable table;

    public SearchPanel(LibraryApp app) {
        this.app = app;
        initComponents();
        loadAllItems();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel banner = ImageUtils.createBackgroundLabel("b2.jpg", 1000, 160);
        add(banner, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search Filters"));
        add(filterPanel, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        txtTitle = new JTextField(12);
        filterPanel.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        txtAuthor = new JTextField(12);
        filterPanel.add(txtAuthor, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        filterPanel.add(new JLabel("Genre:"), gbc);
        gbc.gridx = 1;
        txtGenre = new JTextField(12);
        filterPanel.add(txtGenre, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        filterPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        cmbType = new JComboBox<>(new String[]{"All", "Book", "EBook", "Journal", "Magazine"});
        filterPanel.add(cmbType, gbc);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(this::doSearch);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        filterPanel.add(btnSearch, gbc);

        JButton btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> app.showScreen(ScreenNames.CUSTOMER_MENU));
        gbc.gridy = 5;
        filterPanel.add(btnBack, gbc);

        // Table
        table = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Genre", "Type", "Price", "Available"}, 0));
        JScrollPane scrollPane = new JScrollPane(table);

        // Center panel with table and "View Details" button
        JPanel center = new JPanel(new BorderLayout());
        center.add(scrollPane, BorderLayout.CENTER);

        JButton btnViewDetails = new JButton("View Book Details");
        btnViewDetails.addActionListener(this::openBookDetails);
        JPanel bottom = new JPanel();
        bottom.add(btnViewDetails);
        center.add(bottom, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

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
            items = search.byTitle(""); // all
        }

        // Filter on type if not "All"
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
