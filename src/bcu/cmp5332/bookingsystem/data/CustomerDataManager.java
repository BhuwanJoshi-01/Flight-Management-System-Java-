package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Customer.Gender;
import bcu.cmp5332.bookingsystem.model.Customer.SeatPreference;
import bcu.cmp5332.bookingsystem.model.Customer.MealPreference;
import bcu.cmp5332.bookingsystem.model.Customer.CustomerTier;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class CustomerDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/customers.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] properties = line.split(SEPARATOR, -1);
                try {
                    int id = Integer.parseInt(properties[0]);
                    String name = properties[1];
                    String phone = properties[2];

                    // Handle email, password and deleted flag (with defaults for backward compatibility)
                    String email = "";
                    String password = "";
                    boolean deleted = false;
                    if (properties.length > 3 && !properties[3].isEmpty()) {
                        email = properties[3];
                    }
                    if (properties.length > 4 && !properties[4].isEmpty()) {
                        password = properties[4];
                    }
                    if (properties.length > 5 && !properties[5].isEmpty()) {
                        deleted = Boolean.parseBoolean(properties[5]);
                    }

                    Customer customer = new Customer(id, name, phone, email, password);
                    customer.setDeleted(deleted);

                    // Load new fields with backward compatibility (indices 6-14)
                    // dateOfBirth (6)
                    if (properties.length > 6 && !properties[6].isEmpty()) {
                        customer.setDateOfBirth(LocalDate.parse(properties[6]));
                    }
                    // gender (7)
                    if (properties.length > 7 && !properties[7].isEmpty()) {
                        customer.setGender(Gender.valueOf(properties[7]));
                    }
                    // rewardPoints (8)
                    if (properties.length > 8 && !properties[8].isEmpty()) {
                        customer.setRewardPoints(Integer.parseInt(properties[8]));
                    }
                    // tier (9)
                    if (properties.length > 9 && !properties[9].isEmpty()) {
                        customer.setTier(CustomerTier.valueOf(properties[9]));
                    }
                    // preferredSeat (10)
                    if (properties.length > 10 && !properties[10].isEmpty()) {
                        customer.setPreferredSeat(SeatPreference.valueOf(properties[10]));
                    }
                    // mealPreference (11)
                    if (properties.length > 11 && !properties[11].isEmpty()) {
                        customer.setMealPreference(MealPreference.valueOf(properties[11]));
                    }
                    // emailNotifications (12)
                    if (properties.length > 12 && !properties[12].isEmpty()) {
                        customer.setEmailNotifications(Boolean.parseBoolean(properties[12]));
                    }
                    // smsNotifications (13)
                    if (properties.length > 13 && !properties[13].isEmpty()) {
                        customer.setSmsNotifications(Boolean.parseBoolean(properties[13]));
                    }
                    // createdAt (14)
                    if (properties.length > 14 && !properties[14].isEmpty()) {
                        customer.setCreatedAt(LocalDate.parse(properties[14]));
                    }

                    fbs.addCustomer(customer);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse customer id " + properties[0] + " on line " + line_idx
                        + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            // Store ALL customers including deleted ones
            for (Customer customer : fbs.getAllCustomers()) {
                out.print(customer.getId() + SEPARATOR);
                out.print(customer.getName() + SEPARATOR);
                out.print(customer.getPhone() + SEPARATOR);
                out.print(customer.getEmail() + SEPARATOR);
                out.print(customer.getPassword() + SEPARATOR);
                out.print(customer.isDeleted() + SEPARATOR);
                // New fields
                out.print((customer.getDateOfBirth() != null ? customer.getDateOfBirth().toString() : "") + SEPARATOR);
                out.print(customer.getGender().name() + SEPARATOR);
                out.print(customer.getRewardPoints() + SEPARATOR);
                out.print(customer.getTier().name() + SEPARATOR);
                out.print(customer.getPreferredSeat().name() + SEPARATOR);
                out.print(customer.getMealPreference().name() + SEPARATOR);
                out.print(customer.isEmailNotifications() + SEPARATOR);
                out.print(customer.isSmsNotifications() + SEPARATOR);
                out.print((customer.getCreatedAt() != null ? customer.getCreatedAt().toString() : "") + SEPARATOR);
                out.println();
            }
        }
    }
}
