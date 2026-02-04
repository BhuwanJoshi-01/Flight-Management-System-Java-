package bcu.cmp5332.bookingsystem.utils;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Utility class for validating user input.
 * Provides RFC-compliant email validation, password strength checks, and phone validation.
 */
public class ValidationUtils {

    // RFC 5322 compliant email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?" +
        "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );

    // Phone pattern: allows +, digits, spaces, dashes, and parentheses
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[(]?[0-9]{1,4}[)]?[-\\s./0-9]*$"
    );

    // Minimum password requirements
    private static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Validates an email address using RFC 5322 compliant regex.
     * 
     * @param email the email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates password strength.
     * Requirements:
     * - At least 8 characters
     * - Contains at least one letter
     * - Contains at least one digit
     * 
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (hasLetter && hasDigit) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a password validation error message.
     * 
     * @param password the password to check
     * @return error message, or null if password is valid
     */
    public static String getPasswordError(String password) {
        if (password == null || password.isEmpty()) {
            return "Password is required.";
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.";
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        if (!hasLetter) {
            return "Password must contain at least one letter.";
        }
        if (!hasDigit) {
            return "Password must contain at least one digit.";
        }
        return null; // Password is valid
    }

    /**
     * Validates a phone number.
     * 
     * @param phone the phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleaned = phone.trim().replaceAll("[\\s-()]", "");
        if (cleaned.length() < 7 || cleaned.length() > 15) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validates a name (not empty, at least 2 characters).
     * 
     * @param name the name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2;
    }

    /**
     * Validates a date of birth (must be in the past, person must be at least 18 for booking).
     * 
     * @param dob the date of birth
     * @return true if valid, false otherwise
     */
    public static boolean isValidDateOfBirth(LocalDate dob) {
        if (dob == null) {
            return true; // Optional field
        }
        LocalDate now = LocalDate.now();
        return dob.isBefore(now) && dob.isAfter(now.minusYears(120));
    }

    /**
     * Checks if person is at least 18 years old.
     * 
     * @param dob the date of birth
     * @return true if 18 or older
     */
    public static boolean isAdult(LocalDate dob) {
        if (dob == null) {
            return true; // Assume adult if not specified
        }
        return dob.isBefore(LocalDate.now().minusYears(18).plusDays(1));
    }
}

