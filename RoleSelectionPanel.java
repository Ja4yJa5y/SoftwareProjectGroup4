package library;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class RoleSelectionPanel extends JPanel {

    private final LibraryApp app;

    public RoleSelectionPanel(LibraryApp app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        // Header
        JLabel header = new JLabel("Welcome to Library System", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setForeground(new Color(33, 37, 41));
        add(header, BorderLayout.NORTH);

        // Card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));

        JLabel subtitle = new JLabel("Select an option", SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(73, 80, 87));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(18));

        JButton btnCustomer = createButton("Login as Customer");
        btnCustomer.addActionListener(e -> app.showScreen(ScreenNames.LOGIN));
        card.add(btnCustomer);
        card.add(Box.createVerticalStrut(10));

        JButton btnEmployee = createButton("Login as Employee");
        btnEmployee.addActionListener(e -> app.showScreen(ScreenNames.LOGIN));
        card.add(btnEmployee);
        card.add(Box.createVerticalStrut(10));

        JButton btnManager = createButton("Login as Manager");
        btnManager.addActionListener(e -> app.showScreen(ScreenNames.LOGIN));
        card.add(btnManager);
        card.add(Box.createVerticalStrut(16));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 230, 230));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));

        JButton btnCreate = createButton("Create New User");
        btnCreate.addActionListener(e -> app.showScreen(ScreenNames.REGISTER));
        card.add(btnCreate);

        // Centering wrapper
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setBackground(getBackground());
        centerWrap.add(card);
        add(centerWrap, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(260, 44));
        btn.setMaximumSize(new Dimension(260, 44));

        // Style
        btn.setBackground(new Color(232, 236, 241));
        btn.setForeground(Color.BLACK); // requested
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        return btn;
    }
}
