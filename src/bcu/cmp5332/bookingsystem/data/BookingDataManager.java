package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Booking.BookingStatus;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class BookingDataManager implements DataManager {

    public final String RESOURCE = "./resources/data/bookings.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        int maxRefCounter = 1000;

        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    continue;  // Skip empty lines
                }
                String[] properties = line.split(SEPARATOR, -1);
                try {
                    int customerId = Integer.parseInt(properties[0]);
                    int outboundFlightId = Integer.parseInt(properties[1]);
                    int returnFlightId = Integer.parseInt(properties[2]);  // -1 if no return flight
                    LocalDate bookingDate = LocalDate.parse(properties[3]);

                    // Handle cancelled and fee (with defaults for backward compatibility)
                    boolean cancelled = false;
                    double cancellationFee = 0.0;
                    if (properties.length > 4 && !properties[4].isEmpty()) {
                        cancelled = Boolean.parseBoolean(properties[4]);
                    }
                    if (properties.length > 5 && !properties[5].isEmpty()) {
                        cancellationFee = Double.parseDouble(properties[5]);
                    }

                    // Get customer - need to access even if deleted for loading bookings
                    Customer customer = null;
                    for (Customer c : fbs.getAllCustomers()) {
                        if (c.getId() == customerId) {
                            customer = c;
                            break;
                        }
                    }
                    if (customer == null) {
                        throw new FlightBookingSystemException("Customer not found: " + customerId);
                    }

                    // Get outbound flight - need to access even if deleted
                    Flight outboundFlight = null;
                    for (Flight f : fbs.getAllFlights()) {
                        if (f.getId() == outboundFlightId) {
                            outboundFlight = f;
                            break;
                        }
                    }
                    if (outboundFlight == null) {
                        throw new FlightBookingSystemException("Outbound flight not found: " + outboundFlightId);
                    }

                    Booking booking;
                    if (returnFlightId != -1) {
                        Flight returnFlight = null;
                        for (Flight f : fbs.getAllFlights()) {
                            if (f.getId() == returnFlightId) {
                                returnFlight = f;
                                break;
                            }
                        }
                        if (returnFlight == null) {
                            throw new FlightBookingSystemException("Return flight not found: " + returnFlightId);
                        }
                        booking = new Booking(customer, outboundFlight, returnFlight, bookingDate);
                        // Add customer to both flights only if booking is not cancelled
                        if (!cancelled) {
                            outboundFlight.addPassenger(customer);
                            returnFlight.addPassenger(customer);
                        }
                    } else {
                        booking = new Booking(customer, outboundFlight, bookingDate);
                        // Add customer to outbound flight only if booking is not cancelled
                        if (!cancelled) {
                            outboundFlight.addPassenger(customer);
                        }
                    }

                    booking.setCancelled(cancelled);
                    booking.setCancellationFee(cancellationFee);

                    // Load new fields with backward compatibility (indices 6-9)
                    // reference (6)
                    if (properties.length > 6 && !properties[6].isEmpty()) {
                        booking.setReference(properties[6]);
                        // Extract counter from reference to maintain uniqueness
                        try {
                            String[] refParts = properties[6].split("-");
                            if (refParts.length >= 3) {
                                int refNum = Integer.parseInt(refParts[2]);
                                if (refNum > maxRefCounter) {
                                    maxRefCounter = refNum;
                                }
                            }
                        } catch (NumberFormatException ignore) {}
                    }
                    // passengerCount (7)
                    if (properties.length > 7 && !properties[7].isEmpty()) {
                        booking.setPassengerCount(Integer.parseInt(properties[7]));
                    }
                    // status (8)
                    if (properties.length > 8 && !properties[8].isEmpty()) {
                        booking.setStatus(BookingStatus.valueOf(properties[8]));
                    }
                    // specialRequests (9)
                    if (properties.length > 9 && !properties[9].isEmpty()) {
                        booking.setSpecialRequests(properties[9]);
                    }

                    customer.addBooking(booking);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse booking data on line " + line_idx
                        + "\nError: " + ex);
                }
                line_idx++;
            }
        }

        // Set the reference counter to ensure unique references for new bookings
        Booking.setReferenceCounter(maxRefCounter + 1);
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            // Store ALL bookings including those from deleted customers
            for (Customer customer : fbs.getAllCustomers()) {
                for (Booking booking : customer.getBookings()) {
                    out.print(customer.getId() + SEPARATOR);
                    out.print(booking.getOutboundFlight().getId() + SEPARATOR);
                    // Store return flight id or -1 if no return flight
                    int returnFlightId = booking.hasReturnFlight() ? booking.getReturnFlight().getId() : -1;
                    out.print(returnFlightId + SEPARATOR);
                    out.print(booking.getBookingDate() + SEPARATOR);
                    out.print(booking.isCancelled() + SEPARATOR);
                    out.print(booking.getCancellationFee() + SEPARATOR);
                    // New fields
                    out.print(booking.getReference() + SEPARATOR);
                    out.print(booking.getPassengerCount() + SEPARATOR);
                    out.print(booking.getStatus().name() + SEPARATOR);
                    out.print(booking.getSpecialRequests() + SEPARATOR);
                    out.println();
                }
            }
        }
    }
}
