import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String accountNumber;
    private final String type;
    private final double amount;
    private final LocalDateTime dateTime;

    public Transaction(String accountNumber, String type, double amount) {
        this(accountNumber, type, amount, LocalDateTime.now());
    }

    public Transaction(String accountNumber, String type, double amount, String dateTime) {
        this(accountNumber, type, amount, LocalDateTime.parse(dateTime, FORMATTER));
    }

    private Transaction(String accountNumber, String type, double amount,
                        LocalDateTime dateTime) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDateTime() { return dateTime.format(FORMATTER); }

    public String toFileString() {
        return accountNumber + "|" + type + "|" + amount + "|" + getDateTime();
    }

    @Override
    public String toString() {
        return getDateTime() + " | " + type + " | $" + String.format("%.2f", amount);
    }
}
