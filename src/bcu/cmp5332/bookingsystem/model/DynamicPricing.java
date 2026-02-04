package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for calculating dynamic flight prices based on time until departure
 * and current capacity utilization.
 */
public class DynamicPricing {

    // Default sensitivity parameters
    private static final double DEFAULT_ALPHA = 0.15;  // time sensitivity
    private static final double DEFAULT_BETA = 0.20;   // capacity sensitivity
    private static final int DEFAULT_MAX_BOOKING_WINDOW = 30;  // days

    // Configurable parameters (mutable for admin settings)
    private static double timeSensitivity = DEFAULT_ALPHA;
    private static double capacitySensitivity = DEFAULT_BETA;
    private static int bookingWindowDays = DEFAULT_MAX_BOOKING_WINDOW;

    // Getters
    public static double getTimeSensitivity() { return timeSensitivity; }
    public static double getCapacitySensitivity() { return capacitySensitivity; }
    public static int getBookingWindowDays() { return bookingWindowDays; }

    // Setters
    public static void setTimeSensitivity(double alpha) {
        timeSensitivity = Math.max(0, Math.min(1, alpha));
    }

    public static void setCapacitySensitivity(double beta) {
        capacitySensitivity = Math.max(0, Math.min(1, beta));
    }

    public static void setBookingWindowDays(int days) {
        bookingWindowDays = Math.max(1, days);
    }

    // Reset to defaults
    public static void resetToDefaults() {
        timeSensitivity = DEFAULT_ALPHA;
        capacitySensitivity = DEFAULT_BETA;
        bookingWindowDays = DEFAULT_MAX_BOOKING_WINDOW;
    }

    /**
     * Calculates the dynamic price for a flight.
     * 
     * @param basePrice the base price of the flight
     * @param daysUntilDeparture number of days until the flight departs
     * @param maxBookingWindow maximum booking window in days
     * @param bookedSeats number of seats already booked
     * @param totalCapacity total capacity of the flight
     * @param alpha time sensitivity factor (0.0 to 1.0)
     * @param beta capacity sensitivity factor (0.0 to 1.0)
     * @return the calculated dynamic price
     */
    public static double calculatePrice(
            double basePrice,
            int daysUntilDeparture,
            int maxBookingWindow,
            int bookedSeats,
            int totalCapacity,
            double alpha,
            double beta
    ) {
        // Time factor: increases as departure date approaches
        // When daysUntilDeparture = 0, timeFactor = 1
        // When daysUntilDeparture = maxBookingWindow, timeFactor = 0
        double timeFactor = 1.0 - ((double) daysUntilDeparture / maxBookingWindow);
        timeFactor = Math.max(0, Math.min(1, timeFactor)); // Clamp between 0 and 1

        // Capacity factor: increases as more seats are booked
        double capacityFactor = (double) bookedSeats / totalCapacity;
        capacityFactor = Math.max(0, Math.min(1, capacityFactor)); // Clamp between 0 and 1

        // Calculate final price
        double price = basePrice * (1 + alpha * timeFactor) * (1 + beta * capacityFactor);

        return Math.round(price * 100.0) / 100.0; // Round to 2 decimal places
    }

    /**
     * Calculates the dynamic price for a flight using default sensitivity parameters.
     * 
     * @param flight the flight to calculate price for
     * @param systemDate the current system date
     * @return the calculated dynamic price
     */
    public static double calculatePrice(Flight flight, LocalDate systemDate) {
        int daysUntilDeparture = (int) ChronoUnit.DAYS.between(systemDate, flight.getDepartureDate());
        if (daysUntilDeparture < 0) {
            daysUntilDeparture = 0;
        }
        
        int bookedSeats = flight.getCapacity() - flight.getAvailableSeats();
        
        return calculatePrice(
            flight.getPrice(),
            daysUntilDeparture,
            bookingWindowDays,
            bookedSeats,
            flight.getCapacity(),
            timeSensitivity,
            capacitySensitivity
        );
    }
}

