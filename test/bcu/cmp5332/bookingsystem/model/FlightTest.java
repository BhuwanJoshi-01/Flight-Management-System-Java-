package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {

    private Flight flight;
    private Customer customer;

    @BeforeEach
    void setUp() {
        flight = new Flight(1, "BA123", "London", "Paris", LocalDate.of(2026, 6, 15), 100, 150.0);
        customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
    }

    @Test
    @DisplayName("Flight constructor should initialize all fields correctly")
    void testFlightConstructor() {
        assertEquals(1, flight.getId());
        assertEquals("BA123", flight.getFlightNumber());
        assertEquals("London", flight.getOrigin());
        assertEquals("Paris", flight.getDestination());
        assertEquals(LocalDate.of(2026, 6, 15), flight.getDepartureDate());
        assertEquals(100, flight.getCapacity());
        assertEquals(150.0, flight.getPrice());
    }

    @Test
    @DisplayName("Flight should have correct available seats initially")
    void testInitialAvailableSeats() {
        assertEquals(100, flight.getAvailableSeats());
    }

    @Test
    @DisplayName("Adding passenger should decrease available seats")
    void testAddPassengerDecreasesAvailableSeats() throws FlightBookingSystemException {
        flight.addPassenger(customer);
        assertEquals(99, flight.getAvailableSeats());
    }

    @Test
    @DisplayName("Adding same passenger twice should throw exception")
    void testAddDuplicatePassengerThrowsException() throws FlightBookingSystemException {
        flight.addPassenger(customer);
        assertThrows(FlightBookingSystemException.class, () -> flight.addPassenger(customer));
    }

    @Test
    @DisplayName("Removing passenger should increase available seats")
    void testRemovePassengerIncreasesAvailableSeats() throws FlightBookingSystemException {
        flight.addPassenger(customer);
        flight.removePassenger(customer);
        assertEquals(100, flight.getAvailableSeats());
    }

    @Test
    @DisplayName("Removing non-existent passenger should throw exception")
    void testRemoveNonExistentPassengerThrowsException() {
        assertThrows(FlightBookingSystemException.class, () -> flight.removePassenger(customer));
    }

    @Test
    @DisplayName("getDetailsShort should return formatted string")
    void testGetDetailsShort() {
        String details = flight.getDetailsShort();
        assertTrue(details.contains("BA123"));
        assertTrue(details.contains("London"));
        assertTrue(details.contains("Paris"));
        assertTrue(details.contains("150.00"));
    }

    @Test
    @DisplayName("getDetailsLong should include all flight information")
    void testGetDetailsLong() {
        String details = flight.getDetailsLong();
        assertTrue(details.contains("Flight #1"));
        assertTrue(details.contains("BA123"));
        assertTrue(details.contains("London"));
        assertTrue(details.contains("Paris"));
        assertTrue(details.contains("100 seats"));
        assertTrue(details.contains("150.00"));
    }

    @Test
    @DisplayName("Setters should update flight properties")
    void testSetters() {
        flight.setFlightNumber("BA456");
        flight.setOrigin("Manchester");
        flight.setDestination("Berlin");
        flight.setCapacity(200);
        flight.setPrice(250.0);

        assertEquals("BA456", flight.getFlightNumber());
        assertEquals("Manchester", flight.getOrigin());
        assertEquals("Berlin", flight.getDestination());
        assertEquals(200, flight.getCapacity());
        assertEquals(250.0, flight.getPrice());
    }

    @Test
    @DisplayName("Flight should not be deleted by default")
    void testFlightNotDeletedByDefault() {
        assertFalse(flight.isDeleted());
    }

    @Test
    @DisplayName("Setting deleted flag should work")
    void testSetDeleted() {
        flight.setDeleted(true);
        assertTrue(flight.isDeleted());
    }

    @Test
    @DisplayName("Adding passenger to full flight should throw exception")
    void testAddPassengerToFullFlight() throws FlightBookingSystemException {
        Flight smallFlight = new Flight(2, "BA999", "London", "Rome", LocalDate.of(2026, 7, 1), 1, 100.0);
        Customer customer1 = new Customer(1, "John", "123", "john@test.com");
        Customer customer2 = new Customer(2, "Jane", "456", "jane@test.com");

        smallFlight.addPassenger(customer1);
        assertThrows(FlightBookingSystemException.class, () -> smallFlight.addPassenger(customer2));
    }
}

