package library;

public class Manager extends Management {

    public Manager(String staffID, String staffName) {
        super(staffID, staffName);
    }

    // addBook(...)
    public void addBook(String title, String author, String genre,
                        String type, double price, boolean availability) {
        // For simple case, we add a basic book without pages/year/language info
        LibraryItems.insertBook(title, author, genre, type, price, availability,
                null, null, null, null, null);
    }

    @Override
    public void updateBook(int id, double newPrice, boolean newAvailability) {
        // Manager can use the same logic as Management
        LibraryItems.updateBook(id, newPrice, newAvailability);
    }

    public void deleteBook(int id) {
        LibraryItems.deleteBook(id);
    }

    public double viewProfit(double revenue, double expenses) {
        return revenue - expenses;
    }
}
