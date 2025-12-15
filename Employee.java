package library;

public class Employee extends Management {

    public Employee(String staffID, String staffName) {
        super(staffID, staffName);
    }

    @Override
    public void updateBook(int id, double newPrice, boolean newAvailability) {
        // Employee may also update book information
        LibraryItems.updateBook(id, newPrice, newAvailability);
    }
}
