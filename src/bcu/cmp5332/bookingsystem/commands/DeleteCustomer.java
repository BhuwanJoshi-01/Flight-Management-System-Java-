package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to soft delete a customer from the system.
 * The customer is marked as deleted but not removed from storage.
 */
public class DeleteCustomer implements Command {

    private final int customerId;

    /**
     * Creates a new DeleteCustomer command.
     * @param customerId the ID of the customer to delete
     */
    public DeleteCustomer(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Executes the delete customer command.
     * @param flightBookingSystem the flight booking system
     * @throws FlightBookingSystemException if the customer doesn't exist
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        flightBookingSystem.deleteCustomer(customerId);
        System.out.println("Customer #" + customerId + " has been deleted.");
    }
}

