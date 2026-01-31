package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to cancel a booking for a customer.
 * Applies a cancellation fee based on the booking price.
 */
public class CancelBooking implements Command {

    private final int customerId;
    private final int flightId;

    /**
     * Creates a new CancelBooking command.
     * @param customerId the ID of the customer
     * @param flightId the ID of the outbound flight to cancel
     */
    public CancelBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight flight = flightBookingSystem.getFlightByID(flightId);

        // Find the booking to get return flight info before cancellation
        Booking bookingToCancel = null;
        for (Booking booking : customer.getBookings()) {
            if (!booking.isCancelled() && booking.getOutboundFlight().getId() == flightId) {
                bookingToCancel = booking;
                break;
            }
        }

        if (bookingToCancel == null) {
            throw new FlightBookingSystemException("Customer does not have an active booking for this flight.");
        }

        // Calculate and apply cancellation fee
        double cancellationFee = bookingToCancel.calculateCancellationFee(flightBookingSystem.getSystemDate());
        bookingToCancel.setCancellationFee(cancellationFee);
        bookingToCancel.setCancelled(true);

        // Remove customer from outbound flight's passengers
        flight.removePassenger(customer);

        // Remove customer from return flight's passengers if exists
        if (bookingToCancel.hasReturnFlight()) {
            bookingToCancel.getReturnFlight().removePassenger(customer);
        }

        System.out.println("Booking cancelled for customer #" + customerId + " for flight #" + flightId);
        System.out.printf("Cancellation fee: £%.2f%n", cancellationFee);
    }
}

