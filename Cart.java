package library;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Double> bookPrices = new ArrayList<>();

    public void addBook(double price) {
        bookPrices.add(price);
    }

    public double calculateTotal() {
        double total = 0.0;
        for (double p : bookPrices) {
            total += p;
        }
        return total;
    }

    public void clearCart() {
        bookPrices.clear();
    }
}
