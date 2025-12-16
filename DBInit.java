package library;

import java.sql.Connection;
import java.sql.Statement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
//ResultSet
public class DBInit {

       
    public static void insertUsers(){
             
        String sql = "INSERT INTO USERS (USERID,FIRSTNAME, LASTNAME, USERNAME, PASSWORD, ROLE) " +
                     "VALUES (?, ?, ?, ?, ?)";
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, "manager1");

                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // User already exists
                        System.out.println("User already exists. Insert skipped.");
                        return;
                    }
                }
            }

            try( PreparedStatement ps = conn.prepareStatement(sql)) {

            // ===== User 1: Manager =====
            ps.setString(1, "Ali");
            ps.setString(2, "Manager");
            ps.setString(3, "manager1");
            ps.setString(4, "123");
            ps.setString(5, "manager");
            ps.executeUpdate();

            // ===== User 2: Employee =====
            ps.setString(1, "Sara");
            ps.setString(2, "Employee");
            ps.setString(3, "employee1");
            ps.setString(4, "123");
            ps.setString(5, "employee");
            ps.executeUpdate();

            // ===== User 3: Customer =====
            ps.setString(1, "Omar");
            ps.setString(2, "Customer");
            ps.setString(3, "customer1");
            ps.setString(4, "123");
            ps.setString(5, "customer");
            ps.executeUpdate();

            System.out.println("3 users inserted successfully.");

            } catch (Exception e) {
                // If users already exist (unique username), ignore
                System.out.println("Users already exist or error inserting users.");
            }
        } catch (Exception e) {
            // If users already exist (unique username), ignore
            System.out.println("Users already exist or error inserting users.");
        }
    }
    
}
