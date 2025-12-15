package library;


import javax.swing.*;
import java.awt.*;

public class BookFormDialog extends JDialog {

    private final LibraryItems book;         // null = add, not null = update
    private final Runnable onSuccessRefresh;

    private JTextField txtTitle, txtAuthor, txtGenre, txtType, txtPrice;
    private JCheckBox chkAvailable;

    public BookFormDialog(Window owner, LibraryItems book, Runnable onSuccessRefresh) {
        super(owner, (book == null ? "Add New Book" : "Update Book"), ModalityType.APPLICATION_MODAL);
        this.book = book;
        this.onSuccessRefresh = onSuccessRefresh;

        initComponents();
        if (book != null) fillForm(book);

        setSize(520, 360);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(form, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTitle = new JTextField(20);
        txtAuthor = new JTextField(20);
        txtGenre = new JTextField(20);
        txtType = new JTextField(20);
        txtPrice = new JTextField(20);
        chkAvailable = new JCheckBox("Available");

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y; form.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; form.add(txtTitle, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; form.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; form.add(txtAuthor, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; form.add(new JLabel("Genre:"), gbc);
        gbc.gridx = 1; form.add(txtGenre, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; form.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; form.add(txtType, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; form.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; form.add(txtPrice, gbc); y++;

        gbc.gridx = 1; gbc.gridy = y; form.add(chkAvailable, gbc);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnSave = new JButton(book == null ? "Add Book" : "Update Book");
        btnSave.addActionListener(e -> save());

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        buttons.add(btnCancel);
        buttons.add(btnSave);

        add(buttons, BorderLayout.SOUTH);
    }

    private void fillForm(LibraryItems b) {
        txtTitle.setText(b.getTitle());
        txtAuthor.setText(b.getAuthor());
        txtGenre.setText(b.getGenre());
        txtType.setText(b.getType());
        txtPrice.setText(String.valueOf(b.getPrice()));
        chkAvailable.setSelected(b.isAvailability());
    }

    private void save() {
        try {
            String title = txtTitle.getText().trim();
            String author = txtAuthor.getText().trim();
            String genre = txtGenre.getText().trim();
            String type = txtType.getText().trim();
            double price = Double.parseDouble(txtPrice.getText().trim());
            boolean available = chkAvailable.isSelected();

            if (title.isEmpty() || author.isEmpty() || type.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title, Author, and Type are required.");
                return;
            }

            if (book == null) {
                // ADD
                LibraryItems.insertBook(title, author, genre, type, price, available,
                        null, null, null, null, null);
                JOptionPane.showMessageDialog(this, "Book added successfully.");
            } else {
                // UPDATE (full)
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
