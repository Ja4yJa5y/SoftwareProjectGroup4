package library;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class BookFormDialog extends JDialog {

    private final LibraryItems book;         // null = add, not null = update
    private final Runnable onSuccessRefresh;

    private JTextField txtTitle, txtAuthor, txtGenre, txtPrice;
    private JComboBox<String> cmbType;   // <-- changed
    private JCheckBox chkAvailable;

    public BookFormDialog(Window owner, LibraryItems book, Runnable onSuccessRefresh) {
        super(owner, (book == null ? "Add New Book" : "Update Book"), ModalityType.APPLICATION_MODAL);
        this.book = book;
        this.onSuccessRefresh = onSuccessRefresh;

        initComponents();
        if (book != null) fillForm(book);

        setSize(650, 380);
        setLocationRelativeTo(owner);
        setResizable(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        add(form, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);

        chkAvailable = new JCheckBox("Available");
        chkAvailable.setBackground(Color.WHITE);
        chkAvailable.setForeground(Color.BLACK);
        chkAvailable.setFont(new Font("SansSerif", Font.BOLD, 13));

        txtTitle = createTextField();
        txtAuthor = createTextField();
        txtGenre = createTextField();
        txtPrice = createTextField();

        // ===== Type ComboBox (instead of JTextField) =====
        cmbType = new JComboBox<>(new String[]{"All", "Book", "EBook", "Journal", "Magazine"});
        cmbType.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbType.setForeground(Color.BLACK);
        cmbType.setBackground(Color.WHITE);
        cmbType.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(6, 8, 6, 8)
        ));

        int y = 0;

        addRow(form, gbc, y++, "Title:", txtTitle, labelFont);
        addRow(form, gbc, y++, "Author:", txtAuthor, labelFont);
        addRow(form, gbc, y++, "Genre:", txtGenre, labelFont);

        // Add combo row (Type)
        addRow(form, gbc, y++, "Type:", cmbType, labelFont);

        addRow(form, gbc, y++, "Price:", txtPrice, labelFont);

        // Checkbox row
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.weightx = 1.0;
        form.add(chkAvailable, gbc);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = new JButton(book == null ? "Add Book" : "Update Book");
        btnSave.setForeground(Color.BLACK);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> save());

        buttons.add(btnCancel);
        buttons.add(btnSave);
        add(buttons, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnSave);
    }

    // Overload addRow to support both JTextField and JComboBox
    private void addRow(JPanel form, GridBagConstraints gbc, int row,
                        String labelText, JComponent field, Font labelFont) {

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(labelFont);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        form.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;   // field expands
        form.add(field, gbc);
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txt.setForeground(Color.BLACK);
        txt.setBackground(Color.WHITE);
        txt.setColumns(30);
        txt.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(9, 10, 9, 10)
        ));
        txt.setCaretColor(Color.BLACK);
        return txt;
    }

    private void fillForm(LibraryItems b) {
        txtTitle.setText(b.getTitle());
        txtAuthor.setText(b.getAuthor());
        txtGenre.setText(b.getGenre());
        txtPrice.setText(String.valueOf(b.getPrice()));
        chkAvailable.setSelected(b.isAvailability());

        // Select the correct type in combo
        String t = b.getType();
        if (t == null || t.trim().isEmpty()) {
            cmbType.setSelectedItem("All");
        } else {
            // If value is not in list, keep it safe:
            boolean found = false;
            for (int i = 0; i < cmbType.getItemCount(); i++) {
                if (cmbType.getItemAt(i).equalsIgnoreCase(t)) {
                    cmbType.setSelectedIndex(i);
                    found = true;
                    break;
                }
            }
            if (!found) cmbType.setSelectedItem("Book"); // default fallback
        }
    }

    private void save() {
        try {
            String title = txtTitle.getText().trim();
            String author = txtAuthor.getText().trim();
            String genre = txtGenre.getText().trim();

            String type = (String) cmbType.getSelectedItem(); // <-- changed
            if (type != null) type = type.trim();

            double price = Double.parseDouble(txtPrice.getText().trim());
            boolean available = chkAvailable.isSelected();

            // In the form, "All" should not be saved as a book type
            if ("All".equalsIgnoreCase(type)) {
                JOptionPane.showMessageDialog(this, "Please select a valid Type (not All).");
                return;
            }

            if (title.isEmpty() || author.isEmpty() || type.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title, Author, and Type are required.");
                return;
            }

            if (book == null) {
                LibraryItems.insertBook(title, author, genre, type, price, available,
                        null, null, null, null, null);
                JOptionPane.showMessageDialog(this, "Book added successfully.");
            } else {
                LibraryItems.updateFull(book.getId(), title, author, genre, type, price, available);
                JOptionPane.showMessageDialog(this, "Book updated successfully.");
            }

            if (onSuccessRefresh != null) onSuccessRefresh.run();
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price. Please enter a number.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
