package library;

import java.util.Scanner;

public class Payment {

    public String selectPaymentType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select payment type (Cash/Credit): ");
        String type = scanner.nextLine();
        return type;
    }

    public void processPayment(double amount, String paymentType) {
        // Basic simulation
        System.out.println("Processing payment of " + amount + " using " + paymentType);
        System.out.println("Payment successful.");
    }
}
