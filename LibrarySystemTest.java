package library;

import org.junit.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class LibrarySystemTest {

    @BeforeClass
    public static void setup() throws Exception {
        DBInit.insertUsers();
    }

    @Test
    public void TC01_LoginSuccess() {
        User u = new User();
        boolean ok = u.login("customer1", "123");
        assertTrue(ok);
        assertTrue(u.getUserId() > 0);
    }

  @Test
    public void TC02_RoleBasedAccessDenied() {
        User u = new User();
        boolean ok = u.login("customer1", "123");
        assertTrue(ok);
        assertNotNull(u.getRole());
        assertFalse(u.getRole().equalsIgnoreCase("manager"));
    }

    @Test
    public void TC03_SearchBooks() {
        LibraryItems.insertBook(
                "Literature Test",
                "Author A",
                "Literature",
                "Book",
                15.0,
                true,
                120,
                2023,
                "English",
                null,
                null
        );

        List<LibraryItems> results = LibraryItems.findByTitle("Literature");
        assertNotNull(results);
        assertTrue(results.size() > 0);
    }

    @Test
    public void TC04_BorrowBook() throws Exception {
        int userId = getUserIdByUsername("customer1");
        int bookId = insertOrGetAvailableBookId();

        BorrowedBooks bb = new BorrowedBooks();
        bb.setUserId(userId);
        bb.setBookId(bookId);
        bb.borrowBook();

        assertTrue(countRows(
                "borrowed_books",
                "user_id=" + userId + " AND book_id=" + bookId + " AND status='BORROWED'"
        ) >= 1);

        assertFalse(getAvailability(bookId));
    }

    @Test
    public void TC05_PaymentProcessing() throws Exception {
        int userId = getUserIdByUsername("customer1");

        Purchase p = new Purchase();
        p.setUserId(userId);
        p.setPurchaseDate(new java.sql.Date(System.currentTimeMillis()));
        p.setPaymentMethod("Visa");
        p.setPaymentStatus("PAID");
        p.setTotalCost(50.0);
        p.save();

        assertTrue(countRows(
                "purchase",
                "user_id=" + userId + " AND payment_status='PAID'"
        ) >= 1);
    }

    @Test
    public void TC06_PurchaseCreationAndStorage() throws Exception {
        int userId = getUserIdByUsername("customer1");

        Purchase p = new Purchase();
        p.setUserId(userId);
        p.setPurchaseDate(new java.sql.Date(System.currentTimeMillis()));
        p.setPaymentMethod("MasterCard");
        p.setPaymentStatus("PAID");
        p.setTotalCost(99.99);
        p.save();

        assertTrue(countRows(
                "purchase",
                "user_id=" + userId
        ) >= 1);
    }

    private static int getUserIdByUsername(String username) throws Exception {
        String sql = "SELECT user_id FROM users WHERE username=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return -1;
        }
    }

    private static int insertOrGetAvailableBookId() throws Exception {
        String findSql = "SELECT id FROM library_items WHERE availability=TRUE FETCH FIRST ROW ONLY";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(findSql)) {
            if (rs.next()) return rs.getInt(1);
        }

        LibraryItems.insertBook(
                "Borrow Test Book",
                "Borrow Author",
                "General",
                "Book",
                10.0,
                true,
                100,
                2022,
                "English",
                null,
                null
        );

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(findSql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    private static boolean getAvailability(int bookId) throws Exception {
        String sql = "SELECT availability FROM library_items WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getBoolean(1);
        }
    }

    private static int countRows(String table, String where) throws Exception {
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE " + where;
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        }
    }
}