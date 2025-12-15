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

        JButton btnSignUp = createButton("Create Account", new Color(25, 135, 84));
btnSignUp.addActionListener(e -> openSignUpDialog());

gbc.gridx = 0; gbc.gridy = 4;
gbc.gridwidth = 2;
gbc.insets = new Insets(6, 8, 8, 8);
card.add(btnSignUp, gbc);


        // Message
        lblMessage = new JLabel(" ", SwingConstants.CENTER);
        lblMessage.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblMessage.setForeground(new Color(220, 53, 69));
      gbc.gridx = 0; gbc.gridy = 5;
gbc.insets = new Insets(4, 8, 4, 8);
card.add(lblMessage, gbc);


        // Add card to center wrapper
        GridBagConstraints wrap = new GridBagConstraints();
        wrap.anchor = GridBagConstraints.CENTER;
        centerWrap.add(card, wrap);

        // Optional: Enter key triggers login
//        getRootPane().setDefaultButton(btnLogin);
    }
    
    private void openSignUpDialog() {
    JTextField tfName = new JTextField();
    JTextField tfEmail = new JTextField();
    JPasswordField pfPass = new JPasswordField();

    String[] roles = {"Customer", "Employee", "Manager"};
    JComboBox<String> cbRole = new JComboBox<>(roles);

    JPanel p = new JPanel(new GridLayout(0, 1, 8, 8));
    p.add(new JLabel("Name:"));
    p.add(tfName);
    p.add(new JLabel("Email (or Username):"));
    p.add(tfEmail);
    p.add(new JLabel("Password:"));
    p.add(pfPass);
    p.add(new JLabel("Role:"));
    p.add(cbRole);

    int res = JOptionPane.showConfirmDialog(this, p, "Create Account",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (res != JOptionPane.OK_OPTION) return;

    String name = tfName.getText().trim();
    String email = tfEmail.getText().trim();
    String pass = new String(pfPass.getPassword()).trim();
    String role = (String) cbRole.getSelectedItem();

    if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields.");
        return;
    }

    // ✅ هنا تحفظين في الداتابيس أو قائمة داخل البرنامج
    boolean ok = registerUser(name, email, pass, role);

    if (ok) {
        JOptionPane.showMessageDialog(this, "Account created successfully. You can login now.");
    } else {
        JOptionPane.showMessageDialog(this, "Failed to create account (maybe username already exists).");
    }
}

   private boolean registerUser(String name, String email, String pass, String role) {
    try {
        String[] parts = name.trim().split("\\s+", 2);
        String first = parts[0];
        String last = (parts.length > 1) ? parts[1] : "";

        User u = new User();
        u.setFirstName(first);
        u.setLastName(last);
        u.setUsername(email); // هنا اعتبرناه username
        u.setPassword(pass);
        u.setRole(role.toUpperCase());
        u.register();

        return true;
    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
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
        btn.setForeground(Color.WHITE);
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

        // ✅ هنا فقط نجيب role بعد نجاح login
        String role = u.getRole();

        if ("MANAGER".equalsIgnoreCase(role)) {
            app.showScreen(ScreenNames.MANAGER_PANEL);
        } else if ("EMPLOYEE".equalsIgnoreCase(role)) {
            app.showScreen(ScreenNames.EMPLOYEE_PANEL);
        } else {
            app.showScreen(ScreenNames.CUSTOMER_MENU); // أو SEARCH حسب مشروعك
        }

        app.onLoginSuccess(u);
    } else {
        lblMessage.setText("Invalid username or password.");
    }
}

}
