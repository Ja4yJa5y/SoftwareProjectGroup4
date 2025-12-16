/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library;

public class Library {
    
    public static void main(String[] args) {
        
        DBInit.insertUsers();// insertUsers  if they don't exist

        javax.swing.SwingUtilities.invokeLater(() -> {
            new LibraryApp().setVisible(true); // ManagerPanel
//            new ManagerPanel().setVisible(true); // ManagerPanel

        });
    }

}
