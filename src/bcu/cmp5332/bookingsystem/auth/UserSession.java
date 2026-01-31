package bcu.cmp5332.bookingsystem.auth;

import bcu.cmp5332.bookingsystem.model.Customer;

/**
 * Singleton class to manage user session state.
 * Tracks the currently logged-in user and their role.
 */
public class UserSession {

    /**
     * User roles in the system.
     */
    public enum UserRole {
        CUSTOMER,
        ADMIN
    }

    private static UserSession instance;

    private Customer currentCustomer;
    private UserRole currentRole;
    private boolean isLoggedIn;

    private UserSession() {
        this.isLoggedIn = false;
        this.currentCustomer = null;
        this.currentRole = null;
    }

    /**
     * Gets the singleton instance of UserSession.
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Logs in a customer.
     * 
     * @param customer The customer to log in
     */
    public void loginAsCustomer(Customer customer) {
        this.currentCustomer = customer;
        this.currentRole = UserRole.CUSTOMER;
        this.isLoggedIn = true;
    }

    /**
     * Logs in as admin.
     */
    public void loginAsAdmin() {
        this.currentCustomer = null;
        this.currentRole = UserRole.ADMIN;
        this.isLoggedIn = true;
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        this.currentCustomer = null;
        this.currentRole = null;
        this.isLoggedIn = false;
    }

    /**
     * Checks if a user is currently logged in.
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Checks if the current user is an admin.
     */
    public boolean isAdmin() {
        return isLoggedIn && currentRole == UserRole.ADMIN;
    }

    /**
     * Checks if the current user is a customer.
     */
    public boolean isCustomer() {
        return isLoggedIn && currentRole == UserRole.CUSTOMER;
    }

    /**
     * Gets the currently logged-in customer.
     * 
     * @return The Customer object, or null if not logged in as customer
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * Gets the current user's role.
     * 
     * @return The UserRole, or null if not logged in
     */
    public UserRole getCurrentRole() {
        return currentRole;
    }

    /**
     * Gets a display name for the current user.
     */
    public String getDisplayName() {
        if (!isLoggedIn) {
            return "Guest";
        }
        if (currentRole == UserRole.ADMIN) {
            return "Administrator";
        }
        if (currentCustomer != null) {
            return currentCustomer.getName();
        }
        return "Unknown";
    }

    /**
     * Resets the singleton instance (useful for testing).
     */
    public static synchronized void reset() {
        instance = null;
    }
}
