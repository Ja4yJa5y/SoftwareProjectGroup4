package library;


import javax.swing.*;
import java.awt.*;

public class LibraryApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panels
    private LoginPanel loginPanel;
    private CustomerMenuPanel customerMenuPanel;
    private ManagerPanel managerPanel;
    private EmployeePanel employeePanel;
    private SearchPanel searchPanel;
    private BookDetailsPanel bookDetailsPanel;
    private CartPanel cartPanel;
    private PaymentPanel paymentPanel;
    private MyLibraryPanel myLibraryPanel;
    private BorrowedBooksListPanel borrowedBooksListPanel;
    private PurchasesListPanel purchasesListPanel;
    private RoleSelectionPanel roleSelectionPanel;          // RegisterUserPanel
    private RegisterUserPanel registerUserPanel;          // RegisterUserPanel

    // Logged-in user
    private User currentUser;

    public LibraryApp() {
        super("Library Management System");


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize screens
        initScreens();

        setContentPane(mainPanel);
    }

    private void initScreens() {
        // Create panels
        loginPanel = new LoginPanel(this);
        customerMenuPanel = new CustomerMenuPanel(this);
        managerPanel = new ManagerPanel(this);
        employeePanel = new EmployeePanel(this);
        searchPanel = new SearchPanel(this);
        bookDetailsPanel = new BookDetailsPanel(this);
        cartPanel = new CartPanel(this);
        paymentPanel = new PaymentPanel(this);
        myLibraryPanel = new MyLibraryPanel(this);
        borrowedBooksListPanel = new BorrowedBooksListPanel(this);
        purchasesListPanel = new PurchasesListPanel(this);
        roleSelectionPanel = new    RoleSelectionPanel(this);  // RegisterUserPanel
        registerUserPanel = new    RegisterUserPanel(this);  // RegisterUserPanel

        // Add to CardLayout
        mainPanel.add(roleSelectionPanel, ScreenNames.ROLE_SELECTION);
        mainPanel.add(registerUserPanel, ScreenNames.REGISTER);
        mainPanel.add(loginPanel, ScreenNames.LOGIN);
        mainPanel.add(customerMenuPanel, ScreenNames.CUSTOMER_MENU);
        mainPanel.add(managerPanel, ScreenNames.MANAGER_MENU);
        mainPanel.add(employeePanel, ScreenNames.EMPLOYEE_MENU);
        mainPanel.add(searchPanel, ScreenNames.SEARCH);
        mainPanel.add(bookDetailsPanel, ScreenNames.BOOK_DETAILS);
        mainPanel.add(cartPanel, ScreenNames.CART);
        mainPanel.add(paymentPanel, ScreenNames.PAYMENT);
        mainPanel.add(myLibraryPanel, ScreenNames.MY_LIBRARY);
        mainPanel.add(borrowedBooksListPanel, ScreenNames.BORROWED_LIST);
        mainPanel.add(purchasesListPanel, ScreenNames.PURCHASES_LIST);


//        showScreen(ScreenNames.CUSTOMER_MENU);
        showScreen(ScreenNames.ROLE_SELECTION);


    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public void onLoginSuccess(User user) {
        this.currentUser = user;

        switch (user.getRole().toLowerCase()) {
            case "manager":
                showScreen(ScreenNames.MANAGER_MENU);
                break;
            case "employee":
                showScreen(ScreenNames.EMPLOYEE_MENU);
                break;
            default:{
                showScreen(ScreenNames.CUSTOMER_MENU);
            
            }
                break;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Global access to other panels if needed
    public SearchPanel getSearchPanel() { return searchPanel; }
    public BookDetailsPanel getBookDetailsPanel() { return bookDetailsPanel; }
    public CartPanel getCartPanel() { return cartPanel; }
    public MyLibraryPanel getMyLibraryPanel() { return myLibraryPanel; }
    public BorrowedBooksListPanel getBorrowedBooksListPanel() { return borrowedBooksListPanel; }
    public PurchasesListPanel getPurchasesListPanel() { return purchasesListPanel; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibraryApp().setVisible(true);
        });
    }
}
