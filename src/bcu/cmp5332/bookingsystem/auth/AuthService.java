package bcu.cmp5332.bookingsystem.auth;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

/**
 * Authentication service handling login, registration, and password management.
 * Uses SHA-256 hashing for password security.
 */
public class AuthService {

    // Admin credentials (hardcoded as per requirements)
    private static final String ADMIN_EMAIL = "admin@flights.com";
    private static final String ADMIN_PASSWORD_HASH = hashPassword("admin123");

    private final FlightBookingSystem flightBookingSystem;

    public AuthService(FlightBookingSystem flightBookingSystem) {
        this.flightBookingSystem = flightBookingSystem;
    }

    /**
     * Authenticates a customer with email and password.
     * 
     * @param email    The customer's email
     * @param password The plain text password
     * @return The authenticated Customer object
     * @throws FlightBookingSystemException if authentication fails
     */
    public Customer authenticateCustomer(String email, String password) throws FlightBookingSystemException {
        if (email == null || email.trim().isEmpty()) {
            throw new FlightBookingSystemException("Email is required.");
        }
        if (password == null || password.isEmpty()) {
            throw new FlightBookingSystemException("Password is required.");
        }

        String hashedPassword = hashPassword(password);

        for (Customer customer : flightBookingSystem.getCustomers()) {
            if (customer.getEmail().equalsIgnoreCase(email.trim())) {
                if (customer.getPassword().equals(hashedPassword)) {
                    return customer;
                } else {
                    throw new FlightBookingSystemException("Invalid password.");
                }
            }
        }

        throw new FlightBookingSystemException("No account found with this email.");
    }

    /**
     * Authenticates an admin with email and password.
     * 
     * @param email    The admin's email
     * @param password The plain text password
     * @return true if admin authentication successful
     * @throws FlightBookingSystemException if authentication fails
     */
    public boolean authenticateAdmin(String email, String password) throws FlightBookingSystemException {
        if (email == null || email.trim().isEmpty()) {
            throw new FlightBookingSystemException("Email is required.");
        }
        if (password == null || password.isEmpty()) {
            throw new FlightBookingSystemException("Password is required.");
        }

        String hashedPassword = hashPassword(password);

        if (ADMIN_EMAIL.equalsIgnoreCase(email.trim()) && ADMIN_PASSWORD_HASH.equals(hashedPassword)) {
            return true;
        }

        throw new FlightBookingSystemException("Invalid admin credentials.");
    }

    /**
     * Registers a new customer.
     * 
     * @param name     Customer's full name
     * @param email    Customer's email (used for login)
     * @param phone    Customer's phone number
     * @param password Plain text password
     * @return The newly created Customer object
     * @throws FlightBookingSystemException if registration fails
     */
    public Customer registerCustomer(String name, String email, String phone, String password) 
            throws FlightBookingSystemException {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            throw new FlightBookingSystemException("Name is required.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new FlightBookingSystemException("Email is required.");
        }
        if (!isValidEmail(email)) {
            throw new FlightBookingSystemException("Invalid email format.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new FlightBookingSystemException("Phone number is required.");
        }
        if (password == null || password.length() < 6) {
            throw new FlightBookingSystemException("Password must be at least 6 characters.");
        }

        // Check if email already exists
        for (Customer customer : flightBookingSystem.getCustomers()) {
            if (customer.getEmail().equalsIgnoreCase(email.trim())) {
                throw new FlightBookingSystemException("An account with this email already exists.");
            }
        }

        // Check if email is admin email
        if (ADMIN_EMAIL.equalsIgnoreCase(email.trim())) {
            throw new FlightBookingSystemException("This email cannot be used for registration.");
        }

        // Generate new ID
        int newId = generateNewCustomerId();

        // Hash password
        String hashedPassword = hashPassword(password);

        // Create and add customer
        Customer customer = new Customer(newId, name.trim(), phone.trim(), email.trim(), hashedPassword);
        flightBookingSystem.addCustomer(customer);

        return customer;
    }

    /**
     * Checks if a password reset can be initiated for the given email.
     * In a real application, this would send an email.
     * 
     * @param email The email to check
     * @return true if a customer with this email exists
     * @throws FlightBookingSystemException if email not found
     */
    public boolean initiatePasswordReset(String email) throws FlightBookingSystemException {
        if (email == null || email.trim().isEmpty()) {
            throw new FlightBookingSystemException("Email is required.");
        }

        for (Customer customer : flightBookingSystem.getCustomers()) {
            if (customer.getEmail().equalsIgnoreCase(email.trim())) {
                // In a real application, send password reset email here
                return true;
            }
        }

        throw new FlightBookingSystemException("No account found with this email.");
    }

    /**
     * Updates a customer's password.
     * 
     * @param customer    The customer whose password to update
     * @param newPassword The new plain text password
     * @throws FlightBookingSystemException if password update fails
     */
    public void updatePassword(Customer customer, String newPassword) throws FlightBookingSystemException {
        if (newPassword == null || newPassword.length() < 6) {
            throw new FlightBookingSystemException("Password must be at least 6 characters.");
        }
        customer.setPassword(hashPassword(newPassword));
    }

    /**
     * Generates a new unique customer ID.
     */
    private int generateNewCustomerId() {
        if (flightBookingSystem.getAllCustomers().isEmpty()) {
            return 1;
        }
        int maxId = Collections.max(flightBookingSystem.getAllCustomers(), 
            (c1, c2) -> Integer.compare(c1.getId(), c2.getId())).getId();
        return maxId + 1;
    }

    /**
     * Validates email format.
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Hashes a password using SHA-256.
     * 
     * @param password The plain text password
     * @return The hashed password as a hex string
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is always available, this should never happen
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Gets the admin email for display purposes (e.g., demo credentials).
     */
    public static String getAdminEmail() {
        return ADMIN_EMAIL;
    }
}
