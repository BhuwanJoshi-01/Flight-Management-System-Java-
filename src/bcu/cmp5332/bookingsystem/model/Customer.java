package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {

    // Enums for customer preferences and tier
    public enum Gender { MALE, FEMALE, OTHER, NOT_SPECIFIED }
    public enum SeatPreference { WINDOW, AISLE, MIDDLE, NO_PREFERENCE }
    public enum MealPreference { STANDARD, VEGETARIAN, VEGAN, HALAL, KOSHER }
    public enum CustomerTier { BRONZE, SILVER, GOLD, PLATINUM }

    private int id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private boolean deleted;
    private final List<Booking> bookings = new ArrayList<>();

    // New fields for enhanced customer profile
    private LocalDate dateOfBirth;
    private Gender gender;
    private int rewardPoints;
    private CustomerTier tier;
    private SeatPreference preferredSeat;
    private MealPreference mealPreference;
    private boolean emailNotifications;
    private boolean smsNotifications;
    private LocalDate createdAt;

    // Original constructor for backward compatibility
    public Customer(int id, String name, String phone) {
        this(id, name, phone, "", "");
    }

    // Constructor with email (backward compatibility)
    public Customer(int id, String name, String phone, String email) {
        this(id, name, phone, email, "");
    }

    // New constructor with email and password
    public Customer(int id, String name, String phone, String email, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.deleted = false;

        // Initialize new fields with defaults
        this.dateOfBirth = null;
        this.gender = Gender.NOT_SPECIFIED;
        this.rewardPoints = 0;
        this.tier = CustomerTier.BRONZE;
        this.preferredSeat = SeatPreference.NO_PREFERENCE;
        this.mealPreference = MealPreference.STANDARD;
        this.emailNotifications = true;
        this.smsNotifications = false;
        this.createdAt = LocalDate.now();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    // New getters and setters for enhanced fields
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    /**
     * Adds reward points and updates tier accordingly.
     * @param points the points to add
     */
    public void addRewardPoints(int points) {
        this.rewardPoints += points;
        updateTier();
    }

    /**
     * Updates the customer tier based on reward points.
     * Bronze: 0-999 points
     * Silver: 1000-4999 points
     * Gold: 5000-9999 points
     * Platinum: 10000+ points
     */
    private void updateTier() {
        if (rewardPoints >= 10000) {
            tier = CustomerTier.PLATINUM;
        } else if (rewardPoints >= 5000) {
            tier = CustomerTier.GOLD;
        } else if (rewardPoints >= 1000) {
            tier = CustomerTier.SILVER;
        } else {
            tier = CustomerTier.BRONZE;
        }
    }

    public CustomerTier getTier() {
        return tier;
    }

    public void setTier(CustomerTier tier) {
        this.tier = tier;
    }

    public SeatPreference getPreferredSeat() {
        return preferredSeat;
    }

    public void setPreferredSeat(SeatPreference preferredSeat) {
        this.preferredSeat = preferredSeat;
    }

    public MealPreference getMealPreference() {
        return mealPreference;
    }

    public void setMealPreference(MealPreference mealPreference) {
        this.mealPreference = mealPreference;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the total number of active (non-cancelled) bookings.
     */
    public int getTotalBookings() {
        int count = 0;
        for (Booking booking : bookings) {
            if (!booking.isCancelled()) {
                count++;
            }
        }
        return count;
    }

    public String getDetailsShort() {
        return "Customer #" + id + " - " + name + " - " + phone + " - " + email;
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer #").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Phone: ").append(phone).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("--------------------------\n");
        sb.append("Bookings:\n");

        if (bookings.isEmpty()) {
            sb.append("No bookings.\n");
        } else {
            for (Booking booking : bookings) {
                Flight outboundFlight = booking.getOutboundFlight();
                sb.append(" * Booking date: ").append(booking.getBookingDate());
                sb.append(" for Flight #").append(outboundFlight.getId());
                sb.append(" - ").append(outboundFlight.getFlightNumber());
                sb.append(" - ").append(outboundFlight.getOrigin());
                sb.append(" to ").append(outboundFlight.getDestination());
                sb.append(" on ").append(outboundFlight.getDepartureDate().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                // Show return flight if exists
                if (booking.getReturnFlight() != null) {
                    Flight returnFlight = booking.getReturnFlight();
                    sb.append("\n   Return: Flight #").append(returnFlight.getId());
                    sb.append(" - ").append(returnFlight.getFlightNumber());
                    sb.append(" - ").append(returnFlight.getOrigin());
                    sb.append(" to ").append(returnFlight.getDestination());
                    sb.append(" on ").append(returnFlight.getDepartureDate().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                sb.append("\n");
            }
        }
        sb.append(bookings.size()).append(" booking(s)");
        return sb.toString();
    }

    public void addBooking(Booking booking) throws FlightBookingSystemException {
        // Check if customer already has a booking for the same outbound flight
        for (Booking existingBooking : bookings) {
            if (existingBooking.getOutboundFlight().getId() == booking.getOutboundFlight().getId()) {
                throw new FlightBookingSystemException("Customer already has a booking for this flight.");
            }
        }
        bookings.add(booking);
    }

    public void cancelBookingForFlight(Flight flight) throws FlightBookingSystemException {
        Booking bookingToRemove = null;
        for (Booking booking : bookings) {
            if (booking.getOutboundFlight().getId() == flight.getId()) {
                bookingToRemove = booking;
                break;
            }
        }

        if (bookingToRemove == null) {
            throw new FlightBookingSystemException("Customer does not have a booking for this flight.");
        }

        bookings.remove(bookingToRemove);
    }
}
