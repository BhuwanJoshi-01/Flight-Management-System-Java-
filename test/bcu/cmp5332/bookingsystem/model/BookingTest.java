package bcu.cmp5332.bookingsystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    private Customer customer;
    private Flight outboundFlight;
    private Flight returnFlight;
    private LocalDate bookingDate;

    @BeforeEach
    void setUp() {
        customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
        outboundFlight = new Flight(1, "BA123", "London", "Paris", LocalDate.of(2026, 6, 15), 100, 150.0);
        returnFlight = new Flight(2, "BA124", "Paris", "London", LocalDate.of(2026, 6, 20), 100, 150.0);
        bookingDate = LocalDate.of(2026, 1, 10);
    }

    @Test
    @DisplayName("One-way booking constructor should initialize correctly")
    void testOneWayBookingConstructor() {
        Booking booking = new Booking(customer, outboundFlight, bookingDate);
        
        assertEquals(customer, booking.getCustomer());
        assertEquals(outboundFlight, booking.getOutboundFlight());
        assertNull(booking.getReturnFlight());
        assertEquals(bookingDate, booking.getBookingDate());
        assertFalse(booking.hasReturnFlight());
    }

    @Test
    @DisplayName("Return booking constructor should initialize correctly")
    void testReturnBookingConstructor() {
        Booking booking = new Booking(customer, outboundFlight, returnFlight, bookingDate);
        
        assertEquals(customer, booking.getCustomer());
        assertEquals(outboundFlight, booking.getOutboundFlight());
        assertEquals(returnFlight, booking.getReturnFlight());
        assertEquals(bookingDate, booking.getBookingDate());
        assertTrue(booking.hasReturnFlight());
    }

    @Test
    @DisplayName("hasReturnFlight should return false for one-way booking")
    void testHasReturnFlightFalse() {
        Booking booking = new Booking(customer, outboundFlight, bookingDate);
        assertFalse(booking.hasReturnFlight());
    }

    @Test
    @DisplayName("hasReturnFlight should return true for return booking")
    void testHasReturnFlightTrue() {
        Booking booking = new Booking(customer, outboundFlight, returnFlight, bookingDate);
        assertTrue(booking.hasReturnFlight());
    }

    @Test
    @DisplayName("Setters should update booking properties")
    void testSetters() {
        Booking booking = new Booking(customer, outboundFlight, bookingDate);
        
        Customer newCustomer = new Customer(2, "Jane Doe", "0987654321", "jane@example.com");
        Flight newOutbound = new Flight(3, "BA125", "Manchester", "Berlin", LocalDate.of(2026, 7, 1), 100, 200.0);
        LocalDate newDate = LocalDate.of(2026, 2, 15);
        
        booking.setCustomer(newCustomer);
        booking.setOutboundFlight(newOutbound);
        booking.setBookingDate(newDate);
        booking.setReturnFlight(returnFlight);
        
        assertEquals(newCustomer, booking.getCustomer());
        assertEquals(newOutbound, booking.getOutboundFlight());
        assertEquals(newDate, booking.getBookingDate());
        assertEquals(returnFlight, booking.getReturnFlight());
    }
}

