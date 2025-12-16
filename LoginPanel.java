package library;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {

    private LibraryApp app;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblMessage;

    public LoginPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));

        // ===== Top banner =====
//        JLabel banner = ImageUtils.createBackgroundLabel("b5.jpg", 1000, 280);
//        banner.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
//        add(banner, BorderLayout.NORTH);

        // ===== Center wrapper (to center the card) =====
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setBackground(getBackground());
        add(centerWrap, BorderLayout.CENTER);

        // ===== Login card =====
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("Library Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Username
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblUser.setForeground(new Color(73, 80, 87));
        gbc.gridx = 0; gbc.gridy = 1;
        card.add(lblUser, gbc);

        txtUsername = new JTextField(18);
        styleTextField(txtUsername);
        gbc.gridx = 1; gbc.gridy = 1;
        card.add(txtUsername, gbc);

        // Password
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblPass.setForeground(new Color(73, 80, 87));
        gbc.gridx = 0; gbc.gridy = 2;
        card.add(lblPass, gbc);

        txtPassword = new JPasswordField(18);
        styleTextField(txtPassword);
        gbc.gridx = 1; gbc.gridy = 2;
        card.add(txtPassword, gbc);

        // Login button (full width)
        JButton btnLogin = createButton("Login", new Color(13, 110, 253));
        btnLogin.addActionListener(this::handleLogin);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 8, 8, 8);
        card.add(btnLogin, gbc);

        // Message
        lblMessage = new JLabel(" ", SwingConstants.CENTER);
        lblMessage.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblMessage.setForeground(new Color(220, 53, 69));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(4, 8, 4, 8);
        card.add(lblMessage, gbc);

        // Add card to center wrapper
        GridBagConstraints wrap = new GridBagConstraints();
        wrap.anchor = GridBagConstraints.CENTER;
        centerWrap.add(card, wrap);

        // Optional: Enter key triggers login
//        getRootPane().setDefaultButton(btnLogin);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(33, 37, 41));
        field.setCaretColor(new Color(33, 37, 41));
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        btn.setPreferredSize(new Dimension(260, 42));
        return btn;
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter username and password.");
            return;
        }

        User u = new User();
        boolean success = u.login(username, password);

        if (success) {
            lblMessage.setText(" ");
            app.onLoginSuccess(u);
        } else {
            lblMessage.setText("Invalid username or password.");
        }
    }
}
