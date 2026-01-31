package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.util.*;

public class FlightBookingSystem {
    
    private final LocalDate systemDate = LocalDate.parse("2024-11-11");
    
    private final Map<Integer, Customer> customers = new TreeMap<>();
    private final Map<Integer, Flight> flights = new TreeMap<>();

    public LocalDate getSystemDate() {
        return systemDate;
    }

    /**
     * Returns a list of all non-deleted flights.
     * @return unmodifiable list of active flights
     */
    public List<Flight> getFlights() {
        List<Flight> out = new ArrayList<>();
        for (Flight flight : flights.values()) {
            if (!flight.isDeleted()) {
                out.add(flight);
            }
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Returns a list of all flights including deleted ones.
     * Used for data persistence.
     * @return unmodifiable list of all flights
     */
    public List<Flight> getAllFlights() {
        return Collections.unmodifiableList(new ArrayList<>(flights.values()));
    }

    /**
     * Gets a flight by its ID.
     * @param id the flight ID
     * @return the flight with the given ID
     * @throws FlightBookingSystemException if no flight exists with that ID or if it's deleted
     */
    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        if (!flights.containsKey(id)) {
            throw new FlightBookingSystemException("There is no flight with that ID.");
        }
        Flight flight = flights.get(id);
        if (flight.isDeleted()) {
            throw new FlightBookingSystemException("This flight has been deleted.");
        }
        return flight;
    }

    /**
     * Returns a list of all non-deleted customers.
     * @return unmodifiable list of active customers
     */
    public List<Customer> getCustomers() {
        List<Customer> out = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (!customer.isDeleted()) {
                out.add(customer);
            }
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Returns a list of all customers including deleted ones.
     * Used for data persistence.
     * @return unmodifiable list of all customers
     */
    public List<Customer> getAllCustomers() {
        return Collections.unmodifiableList(new ArrayList<>(customers.values()));
    }

    /**
     * Gets a customer by their ID.
     * @param id the customer ID
     * @return the customer with the given ID
     * @throws FlightBookingSystemException if no customer exists with that ID or if they're deleted
     */
    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        if (!customers.containsKey(id)) {
            throw new FlightBookingSystemException("There is no customer with that ID.");
        }
        Customer customer = customers.get(id);
        if (customer.isDeleted()) {
            throw new FlightBookingSystemException("This customer has been deleted.");
        }
        return customer;
    }

    /**
     * Adds a new flight to the system.
     * @param flight the flight to add
     * @throws FlightBookingSystemException if a flight with the same ID or same number/date exists
     */
    public void addFlight(Flight flight) throws FlightBookingSystemException {
        if (flights.containsKey(flight.getId())) {
            throw new IllegalArgumentException("Duplicate flight ID.");
        }
        for (Flight existing : flights.values()) {
            if (!existing.isDeleted() &&
                existing.getFlightNumber().equals(flight.getFlightNumber())
                && existing.getDepartureDate().isEqual(flight.getDepartureDate())) {
                throw new FlightBookingSystemException("There is a flight with same "
                        + "number and departure date in the system");
            }
        }
        flights.put(flight.getId(), flight);
    }

    /**
     * Adds a new customer to the system.
     * @param customer the customer to add
     * @throws FlightBookingSystemException if a customer with the same ID exists
     */
    public void addCustomer(Customer customer) throws FlightBookingSystemException {
        if (customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Duplicate customer ID.");
        }
        customers.put(customer.getId(), customer);
    }

    /**
     * Soft deletes a flight by setting its deleted flag.
     * @param flightId the ID of the flight to delete
     * @throws FlightBookingSystemException if the flight doesn't exist
     */
    public void deleteFlight(int flightId) throws FlightBookingSystemException {
        Flight flight = getFlightByID(flightId);
        flight.setDeleted(true);
    }

    /**
     * Soft deletes a customer by setting their deleted flag.
     * @param customerId the ID of the customer to delete
     * @throws FlightBookingSystemException if the customer doesn't exist
     */
    public void deleteCustomer(int customerId) throws FlightBookingSystemException {
        Customer customer = getCustomerByID(customerId);
        customer.setDeleted(true);
    }
}
