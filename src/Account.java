public class Account {
    private final String accountNumber;
    private final String username;
    private double balance;

    public Account(String accountNumber, String username, double balance) {
        if (accountNumber == null || accountNumber.isBlank()
                || username == null || username.isBlank()
                || !Double.isFinite(balance) || balance < 0) {
            throw new IllegalArgumentException("Invalid account details.");
        }
        this.accountNumber = accountNumber;
        this.username = username;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getUsername() { return username; }
    public double getBalance() { return balance; }

    public boolean deposit(double amount) {
        if (!Double.isFinite(amount) || amount <= 0
                || !Double.isFinite(balance + amount)
                || balance + amount == balance) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (!Double.isFinite(amount) || amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public String toFileString() {
        return accountNumber + "|" + username + "|" + balance;
    }
}
