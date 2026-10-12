import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final Path USERS_FILE = Path.of("users.txt");
    private static final Path ACCOUNTS_FILE = Path.of("accounts.txt");
    private static final Path TRANSACTIONS_FILE = Path.of("transactions.txt");

    public static List<User> loadUsers() throws IOException {
        List<User> users = new ArrayList<>();
        if (!Files.exists(USERS_FILE)) return users;
        for (String line : Files.readAllLines(USERS_FILE, StandardCharsets.UTF_8)) {
            try {
                String[] p = line.split("\\|", -1);
                if (p.length == 3) users.add(User.fromFile(p[0], p[1], p[2]));
            } catch (RuntimeException e) {
                System.err.println("Skipping invalid user record.");
            }
        }
        return users;
    }

    public static List<Account> loadAccounts() throws IOException {
        List<Account> accounts = new ArrayList<>();
        if (!Files.exists(ACCOUNTS_FILE)) return accounts;
        for (String line : Files.readAllLines(ACCOUNTS_FILE, StandardCharsets.UTF_8)) {
            try {
                String[] p = line.split("\\|", -1);
                if (p.length == 3) accounts.add(new Account(p[0], p[1], Double.parseDouble(p[2])));
            } catch (RuntimeException e) {
                System.err.println("Skipping invalid account record.");
            }
        }
        return accounts;
    }

    public static List<Transaction> loadTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        if (!Files.exists(TRANSACTIONS_FILE)) return transactions;
        for (String line : Files.readAllLines(TRANSACTIONS_FILE, StandardCharsets.UTF_8)) {
            try {
                String[] p = line.split("\\|", -1);
                if (p.length == 4) {
                    transactions.add(new Transaction(p[0], p[1], Double.parseDouble(p[2]), p[3]));
                }
            } catch (RuntimeException e) {
                System.err.println("Skipping invalid transaction record.");
            }
        }
        return transactions;
    }

    public static void saveUsers(List<User> users) throws IOException {
        List<String> lines = new ArrayList<>();
        for (User user : users) lines.add(user.toFileString());
        Files.write(USERS_FILE, lines, StandardCharsets.UTF_8);
    }

    public static void saveAccounts(List<Account> accounts) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Account account : accounts) lines.add(account.toFileString());
        Files.write(ACCOUNTS_FILE, lines, StandardCharsets.UTF_8);
    }

    public static void saveTransactions(List<Transaction> transactions) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Transaction transaction : transactions) lines.add(transaction.toFileString());
        Files.write(TRANSACTIONS_FILE, lines, StandardCharsets.UTF_8);
    }
}
