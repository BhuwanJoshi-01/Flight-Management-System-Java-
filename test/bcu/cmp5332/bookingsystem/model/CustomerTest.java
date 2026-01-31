package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;
    private Flight flight;
    private Booking booking;

    @BeforeEach
    void setUp() {
        customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
        flight = new Flight(1, "BA123", "London", "Paris", LocalDate.of(2026, 6, 15), 100, 150.0);
        booking = new Booking(customer, flight, LocalDate.now());
    }

    @Test
    @DisplayName("Customer constructor should initialize all fields correctly")
    void testCustomerConstructor() {
        assertEquals(1, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals("1234567890", customer.getPhone());
        assertEquals("john@example.com", customer.getEmail());
    }

    @Test
    @DisplayName("Customer should have empty bookings initially")
    void testInitialBookingsEmpty() {
        assertTrue(customer.getBookings().isEmpty());
    }

    @Test
    @DisplayName("Adding booking should increase bookings count")
    void testAddBooking() throws FlightBookingSystemException {
        customer.addBooking(booking);
        assertEquals(1, customer.getBookings().size());
    }

    @Test
    @DisplayName("Adding duplicate booking for same flight should throw exception")
    void testAddDuplicateBookingThrowsException() throws FlightBookingSystemException {
        customer.addBooking(booking);
        Booking duplicateBooking = new Booking(customer, flight, LocalDate.now());
        assertThrows(FlightBookingSystemException.class, () -> customer.addBooking(duplicateBooking));
    }

    @Test
    @DisplayName("Cancelling booking should remove it from customer")
    void testCancelBooking() throws FlightBookingSystemException {
        customer.addBooking(booking);
        customer.cancelBookingForFlight(flight);
        assertTrue(customer.getBookings().isEmpty());
    }

    @Test
    @DisplayName("Cancelling non-existent booking should throw exception")
    void testCancelNonExistentBookingThrowsException() {
        assertThrows(FlightBookingSystemException.class, () -> customer.cancelBookingForFlight(flight));
    }

    @Test
    @DisplayName("getDetailsShort should return formatted string")
    void testGetDetailsShort() {
        String details = customer.getDetailsShort();
        assertTrue(details.contains("John Doe"));
        assertTrue(details.contains("1234567890"));
        assertTrue(details.contains("john@example.com"));
    }

    @Test
    @DisplayName("getDetailsLong should include all customer information")
    void testGetDetailsLong() {
        String details = customer.getDetailsLong();
        assertTrue(details.contains("Customer #1"));
        assertTrue(details.contains("John Doe"));
        assertTrue(details.contains("1234567890"));
        assertTrue(details.contains("john@example.com"));
    }

    @Test
    @DisplayName("Setters should update customer properties")
    void testSetters() {
        customer.setName("Jane Doe");
        customer.setPhone("0987654321");
        customer.setEmail("jane@example.com");

        assertEquals("Jane Doe", customer.getName());
        assertEquals("0987654321", customer.getPhone());
        assertEquals("jane@example.com", customer.getEmail());
    }

    @Test
    @DisplayName("Bookings list should be unmodifiable")
    void testBookingsListUnmodifiable() throws FlightBookingSystemException {
        customer.addBooking(booking);
        assertThrows(UnsupportedOperationException.class, () -> customer.getBookings().add(booking));
    }

    @Test
    @DisplayName("Customer should not be deleted by default")
    void testCustomerNotDeletedByDefault() {
        assertFalse(customer.isDeleted());
    }

    @Test
    @DisplayName("Setting deleted flag should work")
    void testSetDeleted() {
        customer.setDeleted(true);
        assertTrue(customer.isDeleted());
    }
}

