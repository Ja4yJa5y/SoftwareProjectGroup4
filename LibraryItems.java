/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryItems {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String type;           // "Book" or "EBook"
    private double price;
    private boolean availability;

    // Constructors
    public LibraryItems() {
    }

    public LibraryItems(int id, String title, String author, String genre,
                        String type, double price, boolean availability) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.type = type;
        this.price = price;
        this.availability = availability;
    }

    // Getters and setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public boolean isAvailability() { return availability; }

    public void setAvailability(boolean availability) { this.availability = availability; }

    @Override
    public String toString() {
        return "LibraryItems{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                '}';
    }

    // ========= Database Helper Methods ===========

    public static LibraryItems findById(int id) {
        String sql = "SELECT * FROM library_items WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToLibraryItem(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<LibraryItems> findByTitle(String title) {
        String sql = "SELECT * FROM library_items WHERE LOWER(title) LIKE ?";
        List<LibraryItems> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + title.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToLibraryItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<LibraryItems> findByAuthor(String author) {
        String sql = "SELECT * FROM library_items WHERE LOWER(author) LIKE ?";
        List<LibraryItems> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + author.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToLibraryItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<LibraryItems> findByGenre(String genre) {
        String sql = "SELECT * FROM library_items WHERE LOWER(genre) LIKE ?";
        List<LibraryItems> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + genre.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToLibraryItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Insert method (used by Manager.addBook)
    public static void insertBook(String title, String author, String genre,
                                  String type, double price, boolean availability,
                                  Integer pages, Integer publishYear, String language,
                                  String fileFormat, String downloadLink) {
        String sql = "INSERT INTO library_items " +
                "(title, author, genre, type, price, availability, " +
                "pages, publish_year, language, file_format, download_link) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, genre);
            ps.setString(4, type);
            ps.setDouble(5, price);
            ps.setBoolean(6, availability);

            if (pages != null) ps.setInt(7, pages); else ps.setNull(7, Types.INTEGER);
            if (publishYear != null) ps.setInt(8, publishYear); else ps.setNull(8, Types.INTEGER);
            if (language != null) ps.setString(9, language); else ps.setNull(9, Types.VARCHAR);
            if (fileFormat != null) ps.setString(10, fileFormat); else ps.setNull(10, Types.VARCHAR);
            if (downloadLink != null) ps.setString(11, downloadLink); else ps.setNull(11, Types.VARCHAR);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update only price + availability
    public static void updateBook(int id, double newPrice, boolean newAvailability) {
        String sql = "UPDATE library_items SET price = ?, availability = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setBoolean(2, newAvailability);
            ps.setInt(3, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBook(int id) {
        String sql = "DELETE FROM library_items WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static LibraryItems mapRowToLibraryItem(ResultSet rs) throws SQLException {
        LibraryItems item = new LibraryItems();
        item.setId(rs.getInt("id"));
        item.setTitle(rs.getString("title"));
        item.setAuthor(rs.getString("author"));
        item.setGenre(rs.getString("genre"));
        item.setType(rs.getString("type"));
        item.setPrice(rs.getDouble("price"));
        item.setAvailability(rs.getBoolean("availability"));
        return item;
    }

public static List<LibraryItems> findAll() {
        String sql = "SELECT * FROM library_items ORDER BY id DESC";
        List<LibraryItems> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LibraryItems item = new LibraryItems();
                item.setId(rs.getInt("id"));
                item.setTitle(rs.getString("title"));
                item.setAuthor(rs.getString("author"));
                item.setGenre(rs.getString("genre"));
                item.setType(rs.getString("type"));
                item.setPrice(rs.getDouble("price"));
                item.setAvailability(rs.getBoolean("availability"));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateFull(int id, String title, String author, String genre,
                                  String type, double price, boolean availability) {
        String sql = "UPDATE library_items SET title=?, author=?, genre=?, type=?, price=?, availability=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, genre);
            ps.setString(4, type);
            ps.setDouble(5, price);
            ps.setBoolean(6, availability);
            ps.setInt(7, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
