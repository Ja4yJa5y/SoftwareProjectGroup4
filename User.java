package library;

import java.sql.*;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;

    // Constructors, getters, setters omitted for brevity

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.userId = rs.getInt("user_id");
                this.firstName = rs.getString("first_name");
                this.lastName = rs.getString("last_name");
                this.username = rs.getString("username");
                this.password = rs.getString("password");
                this.role = rs.getString("role");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register() {
        String sql = "INSERT INTO users (first_name, last_name, username, password, role) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, this.firstName);
            ps.setString(2, this.lastName);
            ps.setString(3, this.username);
            ps.setString(4, this.password);
            ps.setString(5, this.role);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                this.userId = rs.getInt(1);
                return true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return false;
        
    }

    // Add setters so you can set before register()
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public String getFirstName() {
    return firstName;
}

public String getLastName() {
    return lastName;
}
public int getUserId(){
    return userId;
}
public String getUsername() {
    return username;
}

public String getPassword() {
    return password;
}

public String getRole() {
    return role;
}
    
}
