package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to edit/update a booking by changing the flight.
 * A rebooking fee is applied (same as cancellation fee).
 */
public class EditBooking implements Command {

    private final int bookingId;  // This is the outbound flight ID of the existing booking
    private final int newFlightId;

    /**
     * Creates a new EditBooking command.
     * @param bookingId the ID of the outbound flight in the existing booking
     * @param newFlightId the ID of the new flight to book
     */
    public EditBooking(int bookingId, int newFlightId) {
        this.bookingId = bookingId;
        this.newFlightId = newFlightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Flight newFlight = flightBookingSystem.getFlightByID(newFlightId);
        
        // Find the booking across all customers
        Booking bookingToEdit = null;
        Customer bookingCustomer = null;
        
        for (Customer customer : flightBookingSystem.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                if (!booking.isCancelled() && booking.getOutboundFlight().getId() == bookingId) {
                    bookingToEdit = booking;
                    bookingCustomer = customer;
                    break;
                }
            }
            if (bookingToEdit != null) break;
        }
        
        if (bookingToEdit == null) {
            throw new FlightBookingSystemException("No active booking found with outbound flight ID: " + bookingId);
        }
        
        Flight oldFlight = bookingToEdit.getOutboundFlight();
        
        // Check if new flight is the same as old
        if (oldFlight.getId() == newFlightId) {
            throw new FlightBookingSystemException("New flight is the same as the current flight.");
        }
        
        // Calculate rebooking fee (same as cancellation fee)
        double rebookingFee = bookingToEdit.calculateCancellationFee(flightBookingSystem.getSystemDate());
        
        // Remove from old flight
        oldFlight.removePassenger(bookingCustomer);
        
        // Add to new flight (this will check capacity)
        newFlight.addPassenger(bookingCustomer);
        
        // Update the booking
        bookingToEdit.setOutboundFlight(newFlight);
        bookingToEdit.setCancellationFee(rebookingFee);  // Store the rebooking fee
        
        System.out.println("Booking updated successfully.");
        System.out.println("Customer #" + bookingCustomer.getId() + " moved from flight #" + oldFlight.getId() 
            + " to flight #" + newFlightId);
        System.out.printf("Rebooking fee: £%.2f%n", rebookingFee);
    }
}

