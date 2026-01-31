package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to soft delete a flight from the system.
 * The flight is marked as deleted but not removed from storage.
 */
public class DeleteFlight implements Command {

    private final int flightId;

    /**
     * Creates a new DeleteFlight command.
     * @param flightId the ID of the flight to delete
     */
    public DeleteFlight(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Executes the delete flight command.
     * @param flightBookingSystem the flight booking system
     * @throws FlightBookingSystemException if the flight doesn't exist
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        flightBookingSystem.deleteFlight(flightId);
        System.out.println("Flight #" + flightId + " has been deleted.");
    }
}

