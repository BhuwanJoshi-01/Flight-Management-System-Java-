package bcu.cmp5332.bookingsystem.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DynamicPricingTest {

    @Test
    @DisplayName("Price should equal base price when flight is far away and empty")
    void testBasePriceWhenFarAndEmpty() {
        double price = DynamicPricing.calculatePrice(
            100.0,  // base price
            30,     // days until departure (max window)
            30,     // max booking window
            0,      // booked seats
            100,    // total capacity
            0.5,    // alpha
            0.8     // beta
        );
        // timeFactor = 1 - 30/30 = 0, capacityFactor = 0/100 = 0
        // price = 100 * (1 + 0.5*0) * (1 + 0.8*0) = 100
        assertEquals(100.0, price, 0.01);
    }

    @Test
    @DisplayName("Price should increase when departure is imminent")
    void testPriceIncreasesNearDeparture() {
        double price = DynamicPricing.calculatePrice(
            100.0,  // base price
            0,      // days until departure (departure day)
            30,     // max booking window
            0,      // booked seats
            100,    // total capacity
            0.5,    // alpha
            0.8     // beta
        );
        // timeFactor = 1 - 0/30 = 1, capacityFactor = 0/100 = 0
        // price = 100 * (1 + 0.5*1) * (1 + 0.8*0) = 100 * 1.5 = 150
        assertEquals(150.0, price, 0.01);
    }

    @Test
    @DisplayName("Price should increase when flight is nearly full")
    void testPriceIncreasesWhenNearlyFull() {
        double price = DynamicPricing.calculatePrice(
            100.0,  // base price
            30,     // days until departure (max window)
            30,     // max booking window
            100,    // booked seats (full)
            100,    // total capacity
            0.5,    // alpha
            0.8     // beta
        );
        // timeFactor = 1 - 30/30 = 0, capacityFactor = 100/100 = 1
        // price = 100 * (1 + 0.5*0) * (1 + 0.8*1) = 100 * 1.8 = 180
        assertEquals(180.0, price, 0.01);
    }

    @Test
    @DisplayName("Price should be maximum when departure is imminent and flight is full")
    void testMaximumPrice() {
        double price = DynamicPricing.calculatePrice(
            100.0,  // base price
            0,      // days until departure
            30,     // max booking window
            100,    // booked seats (full)
            100,    // total capacity
            0.5,    // alpha
            0.8     // beta
        );
        // timeFactor = 1, capacityFactor = 1
        // price = 100 * (1 + 0.5*1) * (1 + 0.8*1) = 100 * 1.5 * 1.8 = 270
        assertEquals(270.0, price, 0.01);
    }

    @Test
    @DisplayName("Calculate price from Flight object")
    void testCalculatePriceFromFlight() {
        Flight flight = new Flight(1, "BA123", "London", "Paris", LocalDate.of(2024, 12, 11), 100, 100.0);
        LocalDate systemDate = LocalDate.of(2024, 11, 11);  // 30 days before
        
        double price = DynamicPricing.calculatePrice(flight, systemDate);
        // With default alpha=0.5, beta=0.8, 30 days out, 0 passengers
        // timeFactor = 0, capacityFactor = 0
        // price = 100 * 1 * 1 = 100
        assertEquals(100.0, price, 0.01);
    }
}

