package library;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BookDetailsPanel extends JPanel {

    private LibraryApp app;
    private LibraryItems currentItem;

    private JLabel lblTitle, lblAuthor, lblGenre, lblType, lblPrice, lblAvailability,
            lblLanguage, lblFileFormat, lblDownloadLink;

    private JButton btnBorrow, btnAddToCart, btnBack;

    public BookDetailsPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        // ===== Header =====
        JLabel header = new JLabel("Book Details", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(new Color(33, 37, 41));
        add(header, BorderLayout.NORTH);

        // ===== Center Container =====
        JPanel center = new JPanel(new BorderLayout(15, 15));
        center.setBackground(getBackground());
        add(center, BorderLayout.CENTER);

        // ===== Details Card =====
        JPanel detailsCard = new JPanel();
        detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));
        detailsCard.setBackground(Color.WHITE);
        detailsCard.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        lblTitle = createInfoLabel("Title: -");
        lblAuthor = createInfoLabel("Author: -");
        lblGenre = createInfoLabel("Genre: -");
        lblType = createInfoLabel("Type: -");
        lblPrice = createInfoLabel("Price: -");
        lblAvailability = createInfoLabel("Availability: -");
        lblLanguage = createInfoLabel("Language: -");
        lblFileFormat = createInfoLabel("File Format: -");
        lblDownloadLink = createInfoLabel("Download Link: -");

        detailsCard.add(lblTitle);
        detailsCard.add(Box.createVerticalStrut(6));
        detailsCard.add(lblAuthor);
        detailsCard.add(Box.createVerticalStrut(6));
        detailsCard.add(lblGenre);
        detailsCard.add(Box.createVerticalStrut(6));
        detailsCard.add(lblType);
        detailsCard.add(Box.createVerticalStrut(6));
        detailsCard.add(lblPrice);
        detailsCard.add(Box.createVerticalStrut(6));
        detailsCard.add(lblAvailability);

        detailsCard.add(Box.createVerticalStrut(12));
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 230, 230));
        detailsCard.add(sep);
        detailsCard.add(Box.createVerticalStrut(12));

        detailsCard.add(lblLanguage);
        detailsCard.add(Box.createVerticalStrut(6));
        detailsCard.add(lblFileFormat);
        detailsCard.add(Box.createVerticalStrut(6));
        detailsCard.add(lblDownloadLink);

        center.add(detailsCard, BorderLayout.CENTER);


        // ===== Bottom Actions =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottom.setBackground(getBackground());

        btnBorrow = createButton("Borrow Book", new Color(13, 110, 253)); // blue
        btnBorrow.addActionListener(this::borrowBook);

        btnAddToCart = createButton("Add to Cart", new Color(40, 167, 69)); // green
        btnAddToCart.addActionListener(this::addToCart);

        btnBack = createButton("Back to Search", new Color(108, 117, 125)); // gray
        btnBack.addActionListener(e -> app.showScreen(ScreenNames.SEARCH));

        bottom.add(btnBorrow);
        bottom.add(btnAddToCart);
        bottom.add(btnBack);

        add(bottom, BorderLayout.SOUTH);
    }

    private JLabel createInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setForeground(new Color(33, 37, 41));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setBackground(bg);
//        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        return btn;
    }

    public void showBook(LibraryItems item) {
        this.currentItem = item;

        lblTitle.setText("Title: " + item.getTitle());
        lblAuthor.setText("Author: " + item.getAuthor());
        lblGenre.setText("Genre: " + item.getGenre());
        lblType.setText("Type: " + item.getType());
        lblPrice.setText("Price: " + item.getPrice());

        boolean available = item.isAvailability();
        lblAvailability.setText("Availability: " + (available ? "Available" : "Not available"));
        lblAvailability.setForeground(available ? new Color(25, 135, 84) : new Color(220, 53, 69));

        lblLanguage.setText("Language: -");
        lblFileFormat.setText("File Format: -");
        lblDownloadLink.setText("Download Link: -");

        if (item instanceof EBook) {
            EBook eb = (EBook) item;
            lblFileFormat.setText("File Format: " + eb.getFileFormat());
            lblDownloadLink.setText("Download: " + eb.getDownloadLink());
        }
    }

    private void borrowBook(ActionEvent e) {
        if (currentItem == null || !currentItem.isAvailability()) {
            JOptionPane.showMessageDialog(this, "Book is not available.");
            return;
        }
        BorrowedBooks bb = new BorrowedBooks();
        bb.setUserId(app.getCurrentUser().getUserId());
        bb.setBookId(currentItem.getId());

        bb.borrowBook();

        JOptionPane.showMessageDialog(this, "Borrow book functionality: connect to BorrowedBooks.borrowBook().");
    }

    private void addToCart(ActionEvent e) {

        if (currentItem == null || !currentItem.isAvailability()) {
            JOptionPane.showMessageDialog(this, "Book is not available.");
            return;
        }

        app.getCartPanel().getCart().addBook(currentItem.getPrice());
        app.getCartPanel().refreshCart();
        
        

        JOptionPane.showMessageDialog(this, "Book added to cart.");
    }
}
