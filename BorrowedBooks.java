package library;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDate;


public class BorrowedBooks {

    private int borrowId;
    private int userId;
    private int bookId;
    private Date borrowDate;
    private Date returnDueDate;
    private String status;

    // Constructors, getters, setters omitted for brevity

    public void borrowBook(String title) {
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Find available book by title
            String findSql = "SELECT id FROM library_items WHERE LOWER(title) = ? AND availability = TRUE FETCH FIRST ROW ONLY";
            try (PreparedStatement ps = conn.prepareStatement(findSql)) {
                ps.setString(1, title.toLowerCase());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    System.out.println("No available copy for title: " + title);
                    return;
                }
                int bookId = rs.getInt("id");

                // 2. Insert into borrowed_books
                String insertSql = "INSERT INTO borrowed_books (user_id, book_id, borrow_date, return_due_date, status) " +
                        "VALUES (?,?,?,?,?)";
                try (PreparedStatement ps2 = conn.prepareStatement(insertSql)) {
                    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                    long dueMillis = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000); // 7 days
                    java.sql.Date dueDate = new java.sql.Date(dueMillis);

                    ps2.setInt(1, this.userId);
                    ps2.setInt(2, bookId);
                    ps2.setDate(3, today);
                    ps2.setDate(4, dueDate);
                    ps2.setString(5, "BORROWED");
                    ps2.executeUpdate();
                }

                // 3. Update book availability
                LibraryItems.updateBook(bookId, LibraryItems.findById(bookId).getPrice(), false);
                System.out.println("Book borrowed: " + title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
public void borrowBook() {

        String insertSql =
                "INSERT INTO borrowed_books (user_id, book_id, borrow_date, return_due_date, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        String updateAvailabilitySql =
                "UPDATE library_items SET availability = FALSE WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1) Prepare values using setters
            // 1. Convert LocalDate.now() to java.util.Date
            Date borrowDate = Date.from(
                LocalDate.now()
                         .atStartOfDay(ZoneId.systemDefault())
                         .toInstant()
            );
            setBorrowDate(borrowDate);

            // 2. Convert LocalDate 7 days later to java.util.Date
            Date returnDueDate = Date.from(
                LocalDate.now()
                         .plusDays(7)
                         .atStartOfDay(ZoneId.systemDefault())
                         .toInstant()
            );
            setReturnDueDate(returnDueDate);
            setStatus("BORROWED");

            
            // Get today's date using the modern Java 8+ API
            LocalDate localToday = LocalDate.now();

            // Convert the LocalDate to a java.sql.Date. This is the correct, cleaner way.
            java.sql.Date borrowDate1 = java.sql.Date.valueOf(localToday);

            // Calculate the return due date (e.g., 7 days from today)
            java.sql.Date returnDueDate1 = java.sql.Date.valueOf(localToday.plusDays(7));            
            // 2) Insert borrowed_books record
            try (PreparedStatement ps = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setInt(2, bookId);
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

                ps.setDate(3, borrowDate1);
                ps.setDate(4, returnDueDate1);

                ps.setString(5, status);
                ps.executeUpdate();

                // get generated borrow_id
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    setBorrowId(rs.getInt(1));
                }
            }

            // 3) Update book availability
            try (PreparedStatement ps2 = conn.prepareStatement(updateAvailabilitySql)) {
                ps2.setInt(1, bookId);
                ps2.executeUpdate();
            }

            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException("Borrow book failed: " + e.getMessage(), e);
        }
}
    public void returnBook(int bookId) {
        try (Connection conn = DBConnection.getConnection()) {
            // Update borrowed_books status
            String sql = "UPDATE borrowed_books SET status = 'RETURNED' WHERE book_id = ? AND status = 'BORROWED'";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }
            // Set book availability to true
            LibraryItems item = LibraryItems.findById(bookId);
            if (item != null) {
                LibraryItems.updateBook(bookId, item.getPrice(), true);
            }
            System.out.println("Book returned: id = " + bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayBorrowedBooks() {
        String sql = "SELECT b.borrow_id, b.user_id, b.book_id, b.borrow_date, b.return_due_date, b.status, " +
                     "i.title FROM borrowed_books b JOIN library_items i ON b.book_id = i.id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("BorrowID: " + rs.getInt("borrow_id")
                        + ", UserID: " + rs.getInt("user_id")
                        + ", BookID: " + rs.getInt("book_id")
                        + ", Title: " + rs.getString("title")
                        + ", BorrowDate: " + rs.getDate("borrow_date")
                        + ", DueDate: " + rs.getDate("return_due_date")
                        + ", Status: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // You can add full getters/setters here
    
    
   // ===== Getters =====
    public int getBorrowId() { return borrowId; }
    public int getUserId() { return userId; }
    public int getBookId() { return bookId; }
    public Date getBorrowDate() { return borrowDate; }
    public Date getReturnDueDate() { return returnDueDate; }
    public String getStatus() { return status; }

    // ===== Setters =====
    public void setBorrowId(int borrowId) { this.borrowId = borrowId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }
    public void setReturnDueDate(Date returnDueDate) { this.returnDueDate = returnDueDate; }
    public void setStatus(String status) { this.status = status; }    
}
