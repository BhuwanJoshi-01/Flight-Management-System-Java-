package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

/**
 * Represents a booking in the flight booking system.
 * Supports both one-way and return flight bookings.
 */
public class Booking {

    /** Default cancellation fee percentage (10%) */
    public static final double CANCELLATION_FEE_PERCENTAGE = 0.10;

    private Customer customer;
    private Flight outboundFlight;
    private Flight returnFlight;  // Can be null for one-way bookings
    private LocalDate bookingDate;
    private boolean cancelled;
    private double cancellationFee;

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
    }

    public double getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
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
        return total;
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
