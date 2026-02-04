package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a booking in the flight booking system.
 * Supports both one-way and return flight bookings.
 */
public class Booking {

    /** Default cancellation fee percentage (10%) */
    public static final double CANCELLATION_FEE_PERCENTAGE = 0.10;

    /** Booking status enumeration */
    public enum BookingStatus {
        CONFIRMED,
        CANCELLED,
        COMPLETED,
        PENDING
    }

    /** Counter for generating unique booking references */
    private static final AtomicInteger referenceCounter = new AtomicInteger(1000);

    private Customer customer;
    private Flight outboundFlight;
    private Flight returnFlight;  // Can be null for one-way bookings
    private LocalDate bookingDate;
    private boolean cancelled;
    private double cancellationFee;

    // New fields
    private String reference;
    private int passengerCount;
    private BookingStatus status;
    private String specialRequests;

    /**
     * Constructor for one-way booking.
     * @param customer the customer making the booking
     * @param outboundFlight the outbound flight
     * @param bookingDate the date of booking
     */
    public Booking(Customer customer, Flight outboundFlight, LocalDate bookingDate) {
        this.customer = customer;
        this.outboundFlight = outboundFlight;
        this.returnFlight = null;
        this.bookingDate = bookingDate;
        this.cancelled = false;
        this.cancellationFee = 0.0;
        this.reference = generateReference();
        this.passengerCount = 1;
        this.status = BookingStatus.CONFIRMED;
        this.specialRequests = "";
    }

    /**
     * Constructor for return booking.
     * @param customer the customer making the booking
     * @param outboundFlight the outbound flight
     * @param returnFlight the return flight
     * @param bookingDate the date of booking
     */
    public Booking(Customer customer, Flight outboundFlight, Flight returnFlight, LocalDate bookingDate) {
        this.customer = customer;
        this.outboundFlight = outboundFlight;
        this.returnFlight = returnFlight;
        this.bookingDate = bookingDate;
        this.cancelled = false;
        this.cancellationFee = 0.0;
        this.reference = generateReference();
        this.passengerCount = 1;
        this.status = BookingStatus.CONFIRMED;
        this.specialRequests = "";
    }

    /**
     * Generates a unique booking reference.
     * Format: SKY-YYYYMMDD-XXXX where XXXX is an incrementing number
     */
    private static String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int counter = referenceCounter.incrementAndGet();
        return String.format("SKY-%s-%04d", datePart, counter % 10000);
    }

    /**
     * Sets the reference counter to a specific value.
     * Used when loading data to ensure unique references.
     */
    public static void setReferenceCounter(int value) {
        referenceCounter.set(value);
    }

    /**
     * Gets the current reference counter value.
     */
    public static int getReferenceCounter() {
        return referenceCounter.get();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getOutboundFlight() {
        return outboundFlight;
    }

    public void setOutboundFlight(Flight outboundFlight) {
        this.outboundFlight = outboundFlight;
    }

    public Flight getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(Flight returnFlight) {
        this.returnFlight = returnFlight;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public boolean hasReturnFlight() {
        return returnFlight != null;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        if (cancelled) {
            this.status = BookingStatus.CANCELLED;
        }
    }

    public double getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    // New getters and setters
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    /**
     * Gets a display-friendly status string.
     */
    public String getStatusDisplay() {
        if (cancelled) {
            return "Cancelled";
        }
        switch (status) {
            case CONFIRMED: return "Confirmed";
            case COMPLETED: return "Completed";
            case PENDING: return "Pending";
            case CANCELLED: return "Cancelled";
            default: return "Unknown";
        }
    }

    /**
     * Calculates the total price of this booking.
     * @param systemDate the current system date for dynamic pricing
     * @return the total price including outbound and return flights
     */
    public double getTotalPrice(LocalDate systemDate) {
        double total = DynamicPricing.calculatePrice(outboundFlight, systemDate);
        if (hasReturnFlight()) {
            total += DynamicPricing.calculatePrice(returnFlight, systemDate);
        }
        return total * passengerCount;
    }

    /**
     * Calculates the cancellation fee for this booking.
     * @param systemDate the current system date for dynamic pricing
     * @return the cancellation fee (percentage of total price)
     */
    public double calculateCancellationFee(LocalDate systemDate) {
        return getTotalPrice(systemDate) * CANCELLATION_FEE_PERCENTAGE;
    }
}
