import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SecureRandom random = new SecureRandom();
    private static List<User> users;
    private static List<Account> accounts;
    private static List<Transaction> transactions;

    public static void main(String[] args) {
        try {
            users = FileManager.loadUsers();
            accounts = FileManager.loadAccounts();
            transactions = FileManager.loadTransactions();
            System.out.println("================================");
            System.out.println("     SECURE BANKING SYSTEM");
            System.out.println("================================");
            boolean running = true;
            while (running) {
                System.out.println("\n1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                int choice = readInt("Choose an option: ");
                switch (choice) {
                    case 1 -> register();
                    case 2 -> {
                        User user = login();
                        if (user != null) bankingMenu(user);
                    }
                    case 3 -> {
                        saveAll();
                        running = false;
                        System.out.println("Thank you for using our bank.");
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load or save data: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void register() throws IOException {
        System.out.println("\n--- User Registration ---");
        String username = readLine("Create username: ").trim();
        if (!username.matches("[A-Za-z0-9_.-]{3,30}")) {
            System.out.println("Use 3-30 letters, numbers, dots, underscores or hyphens.");
            return;
        }
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                System.out.println("That username already exists.");
                return;
            }
        }
        String password = readLine("Create password (at least 8 characters): ");
        if (password.length() < 8) {
            System.out.println("Password is too short.");
            return;
        }
        users.add(new User(username, password));
        FileManager.saveUsers(users);
        System.out.println("Registration successful. You can now log in.");
    }

    private static User login() {
        System.out.println("\n--- Login ---");
        String username = readLine("Username: ").trim();
        String password = readLine("Password: ");
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.verifyPassword(password)) {
                System.out.println("Login successful. Welcome, " + user.getUsername() + "!");
                return user;
            }
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    private static void bankingMenu(User user) throws IOException {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Banking Menu ---");
            System.out.println("1. Create bank account");
            System.out.println("2. View balance");
            System.out.println("3. Deposit funds");
            System.out.println("4. Withdraw funds");
            System.out.println("5. View transaction history");
            System.out.println("6. Logout");
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> createAccount(user);
                case 2 -> viewBalance(user);
                case 3 -> deposit(user);
                case 4 -> withdraw(user);
                case 5 -> viewHistory(user);
                case 6 -> {
                    saveAll();
                    loggedIn = false;
                    System.out.println("You have logged out.");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void createAccount(User user) throws IOException {
        for (Account account : accounts) {
            if (account.getUsername().equalsIgnoreCase(user.getUsername())) {
                System.out.println("You already have an account: " + account.getAccountNumber());
                return;
            }
        }
        String accountNumber;
        do {
            accountNumber = String.format("%010d", random.nextInt(1_000_000_000));
        } while (accountExists(accountNumber));
        Account account = new Account(accountNumber, user.getUsername(), 0.0);
        accounts.add(account);
        FileManager.saveAccounts(accounts);
        System.out.println("Account created successfully.");
        System.out.println("Account number: " + accountNumber);
        System.out.println("Opening balance: $0.00");
    }

    private static boolean accountExists(String number) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(number)) return true;
        }
        return false;
    }

    private static Account findAccount(User user) {
        for (Account account : accounts) {
            if (account.getUsername().equalsIgnoreCase(user.getUsername())) return account;
        }
        return null;
    }

    private static void viewBalance(User user) {
        Account account = findAccount(user);
        if (account == null) {
            System.out.println("Create an account first.");
            return;
        }
        System.out.println("Account: " + account.getAccountNumber());
        System.out.printf("Balance: $%.2f%n", account.getBalance());
    }

    private static void deposit(User user) throws IOException {
        Account account = findAccount(user);
        if (account == null) {
            System.out.println("Create an account first.");
            return;
        }
        double amount = readDouble("Amount to deposit: $");
        if (!account.deposit(amount)) {
            System.out.println("Invalid deposit amount.");
            return;
        }
        transactions.add(new Transaction(account.getAccountNumber(), "DEPOSIT", amount));
        saveAll();
        System.out.printf("Deposit successful. Balance: $%.2f%n", account.getBalance());
    }

    private static void withdraw(User user) throws IOException {
        Account account = findAccount(user);
        if (account == null) {
            System.out.println("Create an account first.");
            return;
        }
        double amount = readDouble("Amount to withdraw: $");
        if (!account.withdraw(amount)) {
            System.out.println("Withdrawal failed. Check the amount and your balance.");
            return;
        }
        transactions.add(new Transaction(account.getAccountNumber(), "WITHDRAWAL", amount));
        saveAll();
        System.out.printf("Withdrawal successful. Balance: $%.2f%n", account.getBalance());
    }

    private static void viewHistory(User user) {
        Account account = findAccount(user);
        if (account == null) {
            System.out.println("Create an account first.");
            return;
        }
        System.out.println("\n--- Transaction History ---");
        boolean found = false;
        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber().equals(account.getAccountNumber())) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) System.out.println("No transactions recorded yet.");
    }

    private static void saveAll() throws IOException {
        FileManager.saveUsers(users);
        FileManager.saveAccounts(accounts);
        FileManager.saveTransactions(transactions);
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt).trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt).trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }
}
