package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PurchaseItem {

    public static void insert(Connection conn, int purchaseId, int bookId)
            throws SQLException {

        String sql =
                "INSERT INTO purchase_items (purchaseid, bookid) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, purchaseId);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }
    }
}