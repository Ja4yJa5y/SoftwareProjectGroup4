/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library;
import java.sql.Connection;
import java.sql.DriverManager;

public class DerbyTest {
    public static void main(String[] args) throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Connection conn = DriverManager.getConnection("jdbc:derby:librarydb;create=true");
        System.out.println("Connected to Derby successfully!");
        conn.close();
    }
}
