package library;

import java.sql.*;
import java.util.Date;

public class Purchase {
    private int purchaseId;
    private int userId;
    private int bookId;
    private Date purchaseDate;
    private String paymentMethod;
    private String paymentStatus;
    private double totalCost;

    public Purchase() {}

    // Getters/setters omitted for brevity
    public void setUserId(int userId) { this.userId = userId; }
    public void setPurchaseDate(java.sql.Date purchaseDate) { this.purchaseDate = purchaseDate; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public int getPurchaseId() { return purchaseId; }


    public String getPurchaseDetails() {
        return "Purchase{" +
                "purchaseId=" + purchaseId +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", purchaseDate=" + purchaseDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", totalCost=" + totalCost +
                '}';
    }

     public void save() {
        // Matches your DB table: purchase (NOT purchases)
        String sql = "INSERT INTO purchase (user_id, purchase_date, payment_method, payment_status, total_cost) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

            ps.setInt(1, userId);
            ps.setDate(2, today);
            ps.setString(3, paymentMethod);
            ps.setString(4, paymentStatus);
            ps.setDouble(5, totalCost);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                purchaseId = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Saving purchase failed: " + e.getMessage(), e);
        }
    }
    // Example method to save purchase in DB
//    public void save() {
//        String sql = "INSERT INTO purchases (user_id, book_id, purchase_date, payment_method, payment_status, total_cost) " +
//                "VALUES (?,?,?,?,?,?)";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
//            ps.setInt(1, userId);
//            ps.setInt(2, bookId);
//            ps.setDate(3, today);
//            ps.setString(4, paymentMethod);
//            ps.setString(5, paymentStatus);
//            ps.setDouble(6, totalCost);
//
//            ps.executeUpdate();
//            ResultSet rs = ps.getGeneratedKeys();
//            if (rs.next()) {
//                this.purchaseId = rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    void setBookId(int bookIdByTitle) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
