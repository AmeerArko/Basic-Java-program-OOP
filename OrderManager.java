import java.io.*;

public class OrderManager {

    // Simulated stock and product information
    static String[] productIDs = {"P1001", "P1002", "P1003"};
    static int[] stock = {10, 0, 5}; // Product 2 is out of stock
    static double[] prices = {100.50, 200.75, 150.25};

    // Custom exceptions
    static class InvalidProductIDException extends Exception {
        public InvalidProductIDException(String message) {
            super(message);
        }
    }

    static class OutOfStockException extends Exception {
        public OutOfStockException(String message) {
            super(message);
        }
    }

    static class PaymentFailedException extends Exception {
        public PaymentFailedException(String message) {
            super(message);
        }
    }

    // Validate the product ID
    public static void validateProduct(String productID) throws InvalidProductIDException {
        boolean found = false;
        for (String id : productIDs) {
            if (id.equals(productID)) {
                found = true;
                break;
            } else {
                throw new InvalidProductIDException("Invalid product ID: " + productID);
            }
        }
    }


    public static void checkStock(String productID) throws OutOfStockException {
        int index = getProductIndex(productID);
        if (stock[index] <= 0) {
            throw new OutOfStockException("Product " + productID + " is out of stock.");
        }
    }


    public static void processPayment(double amount) throws PaymentFailedException {
        if (amount > 500) { // Simulate a payment failure for amounts over 500
            throw new PaymentFailedException("Payment failed: Amount exceeds $500.");
        }
        System.out.println("Payment of $" + amount + " \nprocessed successfully.");
    }

    // Get product index based on product ID
    private static int getProductIndex(String productID) {
        for (int i = 0; i < productIDs.length; i++) {
            if (productIDs[i].equals(productID)) {
                return i;
            }
        }
        return -1; // Just a placeholder to avoid error
    }

    // Log the order to a file
    public static void logOrder(String productID, double amount) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("order_log.txt", true))) {
            writer.write("Order: Product ID = " + productID + ", Amount = $" + amount + "\n");
        }
        System.out.println("Order logged successfully.");
    }

    // Main method
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            // Step 1: Get Product ID
            System.out.print("Enter Product ID: ");
            String productID = reader.readLine();

            // Step 2: Validate product
            validateProduct(productID);

            // Step 3: Check stock
            checkStock(productID);

            // Step 4: Get Quantity and Price
            int index = getProductIndex(productID);
            double price = prices[index];

            // Step 5: Input the quantity
            while (true) {
                try {
                    System.out.print("Enter quantity: ");
                    int quantity = Integer.parseInt(reader.readLine());

                    // Step 6: Calculate total cost
                    double totalCost = price * quantity;
                    System.out.println("Total cost for " + quantity + " items: $" + totalCost);

                    // Step 7: Process payment
                    processPayment(totalCost);

                    // Step 8: Log order to file
                    logOrder(productID, totalCost);

                    break; // Exit the loop if the order is successful
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: Please enter a valid integer for the quantity.");
                }
            }

        } catch (InvalidProductIDException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (OutOfStockException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (PaymentFailedException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}