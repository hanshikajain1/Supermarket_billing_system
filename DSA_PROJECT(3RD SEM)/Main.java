import java.io.*;
import java.util.*;

/**
 * Supermarket Billing System (PS 12) - Data Structures and Algorithms project.
 *
 * Author : Hanshika Jain (ERP ID 10543)
 * Guide  : Ms. Naina Singh
 *
 * Data structures used
 *   Array    - PRODUCT_NAMES / PRODUCT_PRICES hold the catalogue and MENU holds the menu text
 *   HashMap  - priceList: product name -> price (O(1) average lookup)
 *   Queue    - billingQueue: customers wait in FIFO order (LinkedList as Queue)
 *   Stack    - cartStack: items scanned for the customer being billed, so the
 *              last scanned item can be removed (undo) in LIFO order
 *   File I/O - bills.txt is loaded at start and saved after every bill and on exit
 */
public class Main {

    // ---------- Array: product catalogue ----------
    static final String[] PRODUCT_NAMES  = {"Rice", "Milk", "Bread"};
    static final int[]    PRODUCT_PRICES = {60, 30, 40};

    static final String[] MENU = {
        "1. Add customer",
        "2. Bill next customer",
        "3. View queue",
        "4. View saved bills",
        "5. Exit"
    };

    static final String BILL_FILE = "bills.txt";

    // ---------- HashMap, Queue, saved bills ----------
    static final Map<String, Integer> priceList = new HashMap<>();
    static final Queue<String> billingQueue = new LinkedList<>();
    static final List<String> savedBills = new ArrayList<>();

    /** One scanned line in a customer's cart. */
    static class CartItem {
        final String name;
        final int qty;
        final int price;

        CartItem(String name, int qty, int price) {
            this.name = name;
            this.qty = qty;
            this.price = price;
        }

        int amount() {
            return qty * price;
        }

        @Override
        public String toString() {
            return name + " x" + qty + " = " + amount();
        }
    }

    static Scanner in;

    // ------------------------------------------------------------------
    public static void main(String[] args) {
        in = new Scanner(System.in);
        loadPriceList();
        loadBills();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Choice: ");
            if (choice == null) break;            // input ended
            switch (choice.trim()) {
                case "1": addCustomer();       break;
                case "2": billNextCustomer();  break;
                case "3": viewQueue();         break;
                case "4": viewSavedBills();    break;
                case "5": running = false;     break;
                default:  System.out.println("Invalid choice. Please enter 1-5.");
            }
        }
        saveBills();
        System.out.println("Bills saved to " + BILL_FILE + ". Goodbye!");
    }

    // ---------- setup ----------
    static void loadPriceList() {
        for (int i = 0; i < PRODUCT_NAMES.length; i++) {
            priceList.put(PRODUCT_NAMES[i], PRODUCT_PRICES[i]);
        }
    }

    static void printMenu() {
        System.out.println();
        System.out.println("===== SUPERMARKET BILLING =====");
        for (String line : MENU) System.out.println(line);
    }

    // ---------- feature 1: add customer (Queue.offer) ----------
    static void addCustomer() {
        String name = readLine("Name: ");
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        billingQueue.offer(name.trim());
        System.out.println(name.trim() + " added to billing queue. Position: " + billingQueue.size());
    }

    // ---------- feature 2: bill next customer (Queue.poll + Stack undo) ----------
    static void billNextCustomer() {
        if (billingQueue.isEmpty()) {
            System.out.println("Queue is empty. Cannot bill.");
            return;
        }
        String customer = billingQueue.peek();            // removed only after the bill is made
        Stack<CartItem> cartStack = new Stack<>();
        System.out.println("Billing: " + customer);
        System.out.println("Enter item like 'Milk x2' (or 'Milk 2'), 'Undo' to remove the last item, 'Done' to finish.");

        while (true) {
            String line = readLine("Item: ");
            if (line == null) return;                     // input ended, customer stays queued
            line = line.trim();
            if (line.equalsIgnoreCase("done")) {
                if (cartStack.isEmpty()) {
                    System.out.println("No items scanned. " + customer + " stays in the queue.");
                    return;
                }
                break;
            }
            if (line.equalsIgnoreCase("undo")) {
                undoLastItem(cartStack);
                continue;
            }
            scanItem(line, cartStack);
        }

        billingQueue.poll();                              // customer served
        String bill = buildBill(customer, cartStack);
        System.out.println(bill);
        savedBills.add(bill);
        saveBills();                                      // every bill is saved to file
        if (billingQueue.isEmpty()) System.out.println("Queue is now empty.");
    }

    /** Parses "Milk x2", "Milk 2" or "Milk", looks the price up in the HashMap and pushes the item. */
    static void scanItem(String line, Stack<CartItem> cartStack) {
        if (line.isEmpty()) return;
        int qty = 1;
        String name = line;

        String[] parts = line.split("\\s+");
        String last = parts[parts.length - 1].toLowerCase();
        if (parts.length > 1 && last.matches("x?\\d+")) {
            qty = Integer.parseInt(last.replace("x", ""));
            name = String.join(" ", Arrays.copyOf(parts, parts.length - 1));
        }

        String key = capitalise(name);
        Integer price = priceList.get(key);               // O(1) average lookup
        if (price == null) {
            System.out.println("Item " + name + " not found in price list.");
            return;
        }
        if (qty <= 0) {
            System.out.println("Quantity must be at least 1.");
            return;
        }
        cartStack.push(new CartItem(key, qty, price));
        System.out.println("Added " + key + " x" + qty);
    }

    // ---------- feature 3: undo last item (Stack.pop) ----------
    static void undoLastItem(Stack<CartItem> cartStack) {
        if (cartStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        CartItem removed = cartStack.pop();
        System.out.println("Last item " + removed.name + " removed.");
    }

    static String buildBill(String customer, Stack<CartItem> cartStack) {
        StringBuilder sb = new StringBuilder("Bill for " + customer + ": ");
        int total = 0;
        for (int i = 0; i < cartStack.size(); i++) {      // oldest scanned item first
            CartItem item = cartStack.get(i);
            if (i > 0) sb.append(", ");
            sb.append(item);
            total += item.amount();
        }
        sb.append(" | Total ").append(total);
        return sb.toString();
    }

    // ---------- feature 4: view queue ----------
    static void viewQueue() {
        if (billingQueue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        int position = 1;
        for (String name : billingQueue) {
            System.out.println(position++ + ". " + name);
        }
    }

    static void viewSavedBills() {
        if (savedBills.isEmpty()) {
            System.out.println("No bills saved yet.");
            return;
        }
        for (int i = 0; i < savedBills.size(); i++) {
            System.out.println((i + 1) + ") " + savedBills.get(i));
        }
    }

    // ---------- file handling ----------
    static void loadBills() {
        File file = new File(BILL_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) savedBills.add(line);
            }
            System.out.println("Loaded " + savedBills.size() + " saved bill(s) from " + BILL_FILE + ".");
        } catch (IOException e) {
            System.out.println("Could not read " + BILL_FILE + ": " + e.getMessage());
        }
    }

    static void saveBills() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BILL_FILE))) {
            for (String bill : savedBills) pw.println(bill);
        } catch (IOException e) {
            System.out.println("Could not write " + BILL_FILE + ": " + e.getMessage());
        }
    }

    // ---------- helpers ----------
    static String readLine(String prompt) {
        System.out.print(prompt);
        return in.hasNextLine() ? in.nextLine() : null;
    }

    static String capitalise(String s) {
        s = s.trim();
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }
}
