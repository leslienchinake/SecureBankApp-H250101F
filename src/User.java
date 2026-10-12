import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;

public class User {
    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH = 256;

    private final String username;
    private final String salt;
    private final String passwordHash;

    public User(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        this.username = username;
        this.salt = createSalt();
        this.passwordHash = hashPassword(password, this.salt);
    }

    private User(String username, String salt, String passwordHash) {
        this.username = username;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    public static User fromFile(String username, String salt, String passwordHash) {
        return new User(username, salt, passwordHash);
    }

    private static String createSalt() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String hashPassword(String password, String salt) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        }
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(),
                Base64.getDecoder().decode(salt), ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return Base64.getEncoder().encodeToString(
                    factory.generateSecret(spec).getEncoded());
        } catch (Exception e) {
            throw new IllegalStateException("Could not hash password.", e);
        } finally {
            spec.clearPassword();
        }
    }

    public String getUsername() { return username; }

    public boolean verifyPassword(String password) {
        if (password == null || password.length() < 8) return false;
        try {
            byte[] expected = Base64.getDecoder().decode(passwordHash);
            byte[] actual = Base64.getDecoder().decode(hashPassword(password, salt));
            try {
                return java.security.MessageDigest.isEqual(expected, actual);
            } finally {
                Arrays.fill(actual, (byte) 0);
                Arrays.fill(expected, (byte) 0);
            }
        } catch (RuntimeException e) {
            return false;
        }
    }

    public String toFileString() {
        return username + "|" + salt + "|" + passwordHash;
    }
}
