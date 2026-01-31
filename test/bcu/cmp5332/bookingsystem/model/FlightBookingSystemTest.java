package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FlightBookingSystemTest {

    private FlightBookingSystem fbs;
    private Flight flight;
    private Customer customer;

    @BeforeEach
    void setUp() {
        fbs = new FlightBookingSystem();
        flight = new Flight(1, "BA123", "London", "Paris", LocalDate.of(2026, 6, 15), 100, 150.0);
        customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
    }

    @Test
    @DisplayName("New FlightBookingSystem should have empty flights list")
    void testInitialFlightsEmpty() {
        assertTrue(fbs.getFlights().isEmpty());
    }

    @Test
    @DisplayName("New FlightBookingSystem should have empty customers list")
    void testInitialCustomersEmpty() {
        assertTrue(fbs.getCustomers().isEmpty());
    }

    @Test
    @DisplayName("Adding flight should increase flights count")
    void testAddFlight() throws FlightBookingSystemException {
        fbs.addFlight(flight);
        assertEquals(1, fbs.getFlights().size());
    }

    @Test
    @DisplayName("Adding customer should increase customers count")
    void testAddCustomer() throws FlightBookingSystemException {
        fbs.addCustomer(customer);
        assertEquals(1, fbs.getCustomers().size());
    }

    @Test
    @DisplayName("getFlightByID should return correct flight")
    void testGetFlightByID() throws FlightBookingSystemException {
        fbs.addFlight(flight);
        Flight retrieved = fbs.getFlightByID(1);
        assertEquals(flight, retrieved);
    }

    @Test
    @DisplayName("getFlightByID should throw exception for non-existent flight")
    void testGetFlightByIDThrowsException() {
        assertThrows(FlightBookingSystemException.class, () -> fbs.getFlightByID(999));
    }

    @Test
    @DisplayName("getCustomerByID should return correct customer")
    void testGetCustomerByID() throws FlightBookingSystemException {
        fbs.addCustomer(customer);
        Customer retrieved = fbs.getCustomerByID(1);
        assertEquals(customer, retrieved);
    }

    @Test
    @DisplayName("getCustomerByID should throw exception for non-existent customer")
    void testGetCustomerByIDThrowsException() {
        assertThrows(FlightBookingSystemException.class, () -> fbs.getCustomerByID(999));
    }

    @Test
    @DisplayName("Adding duplicate flight should throw exception")
    void testAddDuplicateFlightThrowsException() throws FlightBookingSystemException {
        fbs.addFlight(flight);
        Flight duplicateFlight = new Flight(1, "BA456", "Manchester", "Berlin", LocalDate.of(2026, 7, 1), 100, 200.0);
        assertThrows(FlightBookingSystemException.class, () -> fbs.addFlight(duplicateFlight));
    }

    @Test
    @DisplayName("Adding duplicate customer should throw exception")
    void testAddDuplicateCustomerThrowsException() throws FlightBookingSystemException {
        fbs.addCustomer(customer);
        Customer duplicateCustomer = new Customer(1, "Jane Doe", "0987654321", "jane@example.com");
        assertThrows(FlightBookingSystemException.class, () -> fbs.addCustomer(duplicateCustomer));
    }
}

