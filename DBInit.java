package library;

import java.sql.Connection;
import java.sql.Statement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
//ResultSet
public class DBInit {

    public static void initialize() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // USERS
            stmt.executeUpdate(
                "CREATE TABLE users (" +
                "user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "first_name VARCHAR(100)," +
                "last_name VARCHAR(100)," +
                "username VARCHAR(100) UNIQUE," +
                "password VARCHAR(100)," +
                "role VARCHAR(50))"
            );

        } catch (Exception e) {
            // Table already exists → ignore
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // BOOK / LIBRARY ITEMS
            stmt.executeUpdate(
                "CREATE TABLE library_items (" +
                "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "title VARCHAR(255)," +
                "author VARCHAR(255)," +
                "genre VARCHAR(100)," +
                "type VARCHAR(50)," +
                "price DOUBLE," +
                "availability BOOLEAN," +
                "pages INT," +
                "publish_year INT," +
                "language VARCHAR(50)," +
                "file_format VARCHAR(50)," +
                "download_link VARCHAR(255))"
            );

        } catch (Exception e) {}

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // BORROWED BOOKS
            stmt.executeUpdate(
                "CREATE TABLE borrowed_books (" +
                "borrow_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "user_id INT," +
                "book_id INT," +
                "borrow_date DATE," +
                "return_due_date DATE," +
                "status VARCHAR(50))"
            );

        } catch (Exception e) {}

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // PURCHASE
            stmt.executeUpdate(
                "CREATE TABLE purchase (" +
                "purchase_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "user_id INT," +
                "purchase_date DATE," +
                "payment_method VARCHAR(50)," +
                "payment_status VARCHAR(50)," +
                "total_cost DOUBLE)"
            );

        } catch (Exception e) {}
    }    
  public static void insertUsers(){

    String sql = "INSERT INTO users (first_name, last_name, username, password, role) " +
                 "VALUES (?, ?, ?, ?, ?)";
    String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

        // (first_name, last_name, username, password, role)
        String[][] users = {
            {"Ali","Manager","manager1","123","manager"},
            {"Fahad","Manager","manager2","123","manager"},
            {"Lina","Manager","manager3","123","manager"},

            {"Sara","Employee","employee1","123","employee"},
            {"Ahmed","Employee","employee2","123","employee"},
            {"Noor","Employee","employee3","123","employee"},

            {"Omar","Customer","customer1","123","customer"},
            {"Huda","Customer","customer2","123","customer"},
            {"Yousef","Customer","customer3","123","customer"},
            {"Maha","Customer","customer4","123","customer"}
        };

        int inserted = 0;

        for (String[] u : users) {

            //  check per username 
            checkPs.setString(1, u[2]); // username
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Skipped (already exists): " + u[2]);
                    continue; // تخطّى هذا المستخدم وكمل الباقي
                }
            }

            ps.setString(1, u[0]);
            ps.setString(2, u[1]);
            ps.setString(3, u[2]);
            ps.setString(4, u[3]);
            ps.setString(5, u[4]);
            ps.executeUpdate();
            inserted++;
        }

        System.out.println(inserted + " users inserted successfully.");

    } catch (Exception e) {
        System.out.println("Users already exist or error inserting users.");
        e.printStackTrace(); // 🔸 مفيد عشان تعرفين الخطأ الحقيقي لو صار
    }
}

    
}
