package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Flight {

    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private int capacity;
    private double price;
    private boolean deleted;

    private final Set<Customer> passengers;

    // Original constructor for backward compatibility
    public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate) {
        this(id, flightNumber, origin, destination, departureDate, 100, 0.0);
    }

    // New constructor with capacity and price
    public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate, int capacity, double price) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.price = price;
        this.deleted = false;

        passengers = new HashSet<>();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public List<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableSeats() {
        return capacity - passengers.size();
    }

    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to "
                + destination + " on " + departureDate.format(dtf)
                + " - £" + String.format("%.2f", price)
                + " - " + getAvailableSeats() + "/" + capacity + " seats";
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        sb.append("Flight #").append(id).append("\n");
        sb.append("Flight No: ").append(flightNumber).append("\n");
        sb.append("Origin: ").append(origin).append("\n");
        sb.append("Destination: ").append(destination).append("\n");
        sb.append("Departure Date: ").append(departureDate.format(dtf)).append("\n");
        sb.append("Capacity: ").append(capacity).append(" seats\n");
        sb.append("Price: £").append(String.format("%.2f", price)).append("\n");
        sb.append("Available Seats: ").append(getAvailableSeats()).append("\n");
        sb.append("---------------------------\n");
        sb.append("Passengers:\n");

        if (passengers.isEmpty()) {
            sb.append("No passengers.\n");
        } else {
            for (Customer passenger : passengers) {
                sb.append(" * Id: ").append(passenger.getId());
                sb.append(" - ").append(passenger.getName());
                sb.append(" - ").append(passenger.getPhone()).append("\n");
            }
        }
        sb.append(passengers.size()).append(" passenger(s)");

        return sb.toString();
    }

    /**
     * Adds a passenger to this flight.
     * @param passenger the customer to add as a passenger
     * @throws FlightBookingSystemException if the passenger is already on the flight or if the flight is full
     */
    public void addPassenger(Customer passenger) throws FlightBookingSystemException {
        if (passengers.contains(passenger)) {
            throw new FlightBookingSystemException("Passenger is already on this flight.");
        }
        if (passengers.size() >= capacity) {
            throw new FlightBookingSystemException("Flight is fully booked. No available seats.");
        }
        passengers.add(passenger);
    }

    /**
     * Removes a passenger from this flight.
     * @param passenger the customer to remove
     * @throws FlightBookingSystemException if the passenger is not on the flight
     */
    public void removePassenger(Customer passenger) throws FlightBookingSystemException {
        if (!passengers.contains(passenger)) {
            throw new FlightBookingSystemException("Passenger is not on this flight.");
        }
        passengers.remove(passenger);
    }
}
