package library;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterUserPanel extends JPanel {

    private final LibraryApp app;

    private JTextField txtFirstName, txtLastName, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JLabel lblMessage;

    public RegisterUserPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        JLabel header = new JLabel("Create New User", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setForeground(new Color(33, 37, 41));
        add(header, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fields
        txtFirstName = new JTextField(18);
        txtLastName  = new JTextField(18);
        txtUsername  = new JTextField(18);
        txtPassword  = new JPasswordField(18);

        styleField(txtFirstName);
        styleField(txtLastName);
        styleField(txtUsername);
        styleField(txtPassword);

        cmbRole = new JComboBox<>(new String[]{"customer", "employee", "manager"});
        cmbRole.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbRole.setBackground(Color.WHITE);
        cmbRole.setForeground(Color.BLACK);
        cmbRole.setBorder(new LineBorder(new Color(210, 210, 210), 1, true));

        int row = 0;

        addRow(card, gbc, row++, "First Name", txtFirstName);
        addRow(card, gbc, row++, "Last Name", txtLastName);
        addRow(card, gbc, row++, "Username", txtUsername);
        addRow(card, gbc, row++, "Password", txtPassword);
        addRow(card, gbc, row++, "Role", cmbRole);

        // Buttons row
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        actions.setBackground(Color.WHITE);

        JButton btnCreate = createButton("Create User");
        btnCreate.addActionListener(this::handleCreate);

        JButton btnBack = createButton("Back");
        btnBack.addActionListener(e -> app.showScreen(ScreenNames.ROLE_SELECTION));

        actions.add(btnCreate);
        actions.add(btnBack);

        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 8, 8, 8);
        card.add(actions, gbc);

        // Message
        lblMessage = new JLabel(" ", SwingConstants.CENTER);
        lblMessage.setForeground(new Color(220, 53, 69));
        lblMessage.setFont(new Font("SansSerif", Font.PLAIN, 13));

        gbc.gridy = row + 1;
        gbc.insets = new Insets(4, 8, 4, 8);
        card.add(lblMessage, gbc);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setBackground(getBackground());
        centerWrap.add(card);
        add(centerWrap, BorderLayout.CENTER);
    }

    private void addRow(JPanel parent, GridBagConstraints gbc, int row, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setForeground(new Color(73, 80, 87));

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row;
        parent.add(lbl, gbc);

        gbc.gridx = 1;
        parent.add(field, gbc);
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(Color.BLACK);
        field.setBackground(Color.WHITE);
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setBackground(new Color(232, 236, 241));
        btn.setForeground(Color.BLACK); // requested
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return btn;
    }

    private void handleCreate(ActionEvent e) {
        String first = txtFirstName.getText().trim();
        String last  = txtLastName.getText().trim();
        String user  = txtUsername.getText().trim();
        String pass  = new String(txtPassword.getPassword()).trim();
        String role  = (String) cmbRole.getSelectedItem();

        if (first.isEmpty() || last.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            lblMessage.setText("Please fill all fields.");
            return;
        }

        // Role is enforced by combo box, but keep a safe validation:
        if (!(role.equals("customer") || role.equals("employee") || role.equals("manager"))) {
            lblMessage.setText("Invalid role selected.");
            return;
        }

        // TODO: Insert into DB using your SQL table
        // Suggested call:
         User u = new User(); // // User newUser = new User(first, last, user, pass, role);
        
         u.setFirstName(first);
         u.setLastName(last);
         u.setUsername(user);
         u.setPassword(pass);
         u.setRole(role);
         boolean ok = u.register();  // implement in User class
         if (ok) { 
            app.onLoginSuccess(u);
         }

        lblMessage.setForeground(new Color(25, 135, 84));
        lblMessage.setText("User created (connect this to DB insert).");
    }
}
