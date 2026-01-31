package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class AddBooking implements Command {

    private final int customerId;
    private final int outboundFlightId;
    private final int returnFlightId;  // -1 if no return flight

    // Constructor for one-way booking
    public AddBooking(int customerId, int outboundFlightId) {
        this.customerId = customerId;
        this.outboundFlightId = outboundFlightId;
        this.returnFlightId = -1;
    }
    
    // Constructor for return booking
    public AddBooking(int customerId, int outboundFlightId, int returnFlightId) {
        this.customerId = customerId;
        this.outboundFlightId = outboundFlightId;
        this.returnFlightId = returnFlightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight outboundFlight = flightBookingSystem.getFlightByID(outboundFlightId);
        
        Flight returnFlight = null;
        if (returnFlightId != -1) {
            returnFlight = flightBookingSystem.getFlightByID(returnFlightId);
        }
        
        // Create booking with system date
        Booking booking;
        if (returnFlight != null) {
            booking = new Booking(customer, outboundFlight, returnFlight, flightBookingSystem.getSystemDate());
        } else {
            booking = new Booking(customer, outboundFlight, flightBookingSystem.getSystemDate());
        }
        
        // Add booking to customer's list
        customer.addBooking(booking);
        
        // Add customer to outbound flight's passengers
        outboundFlight.addPassenger(customer);
        
        // Add customer to return flight's passengers if exists
        if (returnFlight != null) {
            returnFlight.addPassenger(customer);
        }
        
        if (returnFlight != null) {
            System.out.println("Booking issued for customer #" + customerId + 
                " for outbound flight #" + outboundFlightId + 
                " and return flight #" + returnFlightId);
        } else {
            System.out.println("Booking issued for customer #" + customerId + 
                " for flight #" + outboundFlightId);
        }
    }
}

