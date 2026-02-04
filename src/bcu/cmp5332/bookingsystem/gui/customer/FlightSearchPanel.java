package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Flight Search Panel with filters and booking functionality.
 */
public class FlightSearchPanel extends JPanel {

    private final CustomerMainWindow parentWindow;
    private final FlightBookingSystem fbs;
    private final Customer customer;

    // Filter components
    private JComboBox<String> originCombo;
    private JComboBox<String> destinationCombo;
    private JTextField departureDateField;
    private JTextField maxPriceField;
    private JComboBox<String> sortByCombo;

    // Results display
    private JPanel flightsContainer;
    private List<Flight> currentResults;

    public FlightSearchPanel(CustomerMainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.fbs = parentWindow.getFlightBookingSystem();
        this.customer = parentWindow.getCurrentCustomer();

        setLayout(new BorderLayout(0, 20));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        initializeComponents();
    }

    private void initializeComponents() {
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content with filters and results
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setOpaque(false);

        // Left side - filters
        JPanel filtersPanel = createFiltersPanel();
        contentPanel.add(filtersPanel, BorderLayout.WEST);

        // Right side - results
        JPanel resultsPanel = createResultsPanel();
        contentPanel.add(resultsPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Search Flights");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Find the perfect flight for your journey");
        subtitleLabel.setFont(UIStyles.BODY_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createFiltersPanel() {
        JPanel panel = UIStyles.createCard();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(280, 0));

        JLabel filtersTitle = new JLabel("Filters");
        filtersTitle.setFont(UIStyles.SUBHEADER_FONT);
        filtersTitle.setForeground(UIStyles.TEXT_PRIMARY);
        filtersTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(filtersTitle);
        panel.add(Box.createVerticalStrut(20));

        // Origin filter
        panel.add(createFilterLabel("Origin"));
        originCombo = new JComboBox<>();
        originCombo.addItem("All Origins");
        populateOrigins();
        originCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        originCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(originCombo);
        panel.add(Box.createVerticalStrut(15));

        // Destination filter
        panel.add(createFilterLabel("Destination"));
        destinationCombo = new JComboBox<>();
        destinationCombo.addItem("All Destinations");
        populateDestinations();
        destinationCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        destinationCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(destinationCombo);
        panel.add(Box.createVerticalStrut(15));

        // Departure date filter
        panel.add(createFilterLabel("Departure Date (YYYY-MM-DD)"));
        departureDateField = new JTextField();
        departureDateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        departureDateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        departureDateField.setToolTipText("Leave empty to show all dates");
        panel.add(departureDateField);
        panel.add(Box.createVerticalStrut(15));

        // Max price filter
        panel.add(createFilterLabel("Max Price (£)"));
        maxPriceField = new JTextField();
        maxPriceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        maxPriceField.setAlignmentX(Component.LEFT_ALIGNMENT);
        maxPriceField.setToolTipText("Leave empty for no limit");
        panel.add(maxPriceField);
        panel.add(Box.createVerticalStrut(15));

        // Sort by
        panel.add(createFilterLabel("Sort By"));
        sortByCombo = new JComboBox<>(new String[]{
            "Price: Low to High",
            "Price: High to Low",
            "Departure Date",
            "Available Seats"
        });
        sortByCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        sortByCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sortByCombo);
        panel.add(Box.createVerticalStrut(25));

        // Search button
        JButton searchButton = UIStyles.createPrimaryButton("Search");
        searchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchButton.addActionListener(e -> performSearch());
        panel.add(searchButton);
        panel.add(Box.createVerticalStrut(10));

        // Clear button
        JButton clearButton = UIStyles.createSecondaryButton("Clear Filters");
        clearButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        clearButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearButton.addActionListener(e -> clearFilters());
        panel.add(clearButton);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JLabel createFilterLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIStyles.BODY_FONT);
        label.setForeground(UIStyles.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        return label;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Results header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel resultsLabel = new JLabel("Search Results");
        resultsLabel.setFont(UIStyles.SUBHEADER_FONT);
        resultsLabel.setForeground(UIStyles.TEXT_PRIMARY);
        headerPanel.add(resultsLabel, BorderLayout.WEST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Flights container
        flightsContainer = new JPanel();
        flightsContainer.setLayout(new BoxLayout(flightsContainer, BoxLayout.Y_AXIS));
        flightsContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(flightsContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void updateResultsDisplay() {
        flightsContainer.removeAll();
        LocalDate today = fbs.getSystemDate();

        if (currentResults.isEmpty()) {
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setOpaque(false);
            JLabel emptyLabel = new JLabel("No flights found matching your criteria.");
            emptyLabel.setFont(UIStyles.BODY_FONT);
            emptyLabel.setForeground(UIStyles.TEXT_SECONDARY);
            emptyPanel.add(emptyLabel);
            flightsContainer.add(emptyPanel);
        } else {
            for (Flight flight : currentResults) {
                flightsContainer.add(createFlightCard(flight));
                flightsContainer.add(Box.createVerticalStrut(12));
            }
        }

        flightsContainer.revalidate();
        flightsContainer.repaint();
    }

    private JPanel createFlightCard(Flight flight) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(241, 245, 249));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setLayout(new GridBagLayout());
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Plane Icon Circle
        JPanel iconCircle = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 249, 255));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        iconCircle.setPreferredSize(new Dimension(42, 42));
        iconCircle.setOpaque(false);
        
        if (UIStyles.ICON_PLANE != null) {
            Image scaled = UIStyles.ICON_PLANE.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            iconCircle.add(new JLabel(new ImageIcon(scaled)));
        }

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 15);
        card.add(iconCircle, gbc);

        // Flight details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        JLabel routeLabel = new JLabel(flight.getOrigin() + " → " + flight.getDestination());
        routeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        routeLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JLabel flightLabel = new JLabel(flight.getFlightNumber() + " • " + 
            flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        flightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        flightLabel.setForeground(UIStyles.TEXT_SECONDARY);

        detailsPanel.add(routeLabel);
        detailsPanel.add(flightLabel);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        card.add(detailsPanel, gbc);

        // Availability & Price
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setOpaque(false);
        pricePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        int available = flight.getCapacity() - flight.getPassengers().size();
        JLabel availLabel = new JLabel(available + " seats left");
        availLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        availLabel.setForeground(available < 5 ? new Color(220, 38, 38) : new Color(21, 128, 61));
        availLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        double price = DynamicPricing.calculatePrice(flight, fbs.getSystemDate());
        JLabel priceLabel = new JLabel("£" + String.format("%.2f", price));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        priceLabel.setForeground(UIStyles.PRIMARY_COLOR);
        priceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pricePanel.add(availLabel);
        pricePanel.add(priceLabel);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 30, 0, 20);
        card.add(pricePanel, gbc);

        // Book Button
        boolean alreadyBooked = customer.getBookings().stream()
                .anyMatch(b -> !b.isCancelled() && b.getOutboundFlight().getId() == flight.getId());
        
        JButton bookBtn;
        if (alreadyBooked) {
            bookBtn = new JButton("Booked");
            bookBtn.setEnabled(false);
            bookBtn.setBackground(new Color(226, 232, 240));
        } else if (available <= 0) {
            bookBtn = new JButton("Full");
            bookBtn.setEnabled(false);
        } else {
            bookBtn = UIStyles.createPrimaryButton("Book Now");
            bookBtn.addActionListener(e -> bookFlight(flight));
        }
        bookBtn.setPreferredSize(new Dimension(120, 40));

        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(bookBtn, gbc);

        return card;
    }

    private void bookFlight(Flight flight) {
        LocalDate today = fbs.getSystemDate();
        double price = DynamicPricing.calculatePrice(flight, today);

        String message = String.format(
            "Confirm Booking:\n\n" +
            "Flight: %s\n" +
            "Route: %s → %s\n" +
            "Date: %s\n" +
            "Price: £%.2f\n\n" +
            "Proceed with booking?",
            flight.getFlightNumber(),
            flight.getOrigin(),
            flight.getDestination(),
            flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
            price
        );

        int confirm = JOptionPane.showConfirmDialog(this, message, "Confirm Booking",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Booking booking = new Booking(customer, flight, today);
                customer.addBooking(booking);
                flight.addPassenger(customer);

                int points = (int) (price / 10) * 10;
                customer.addRewardPoints(points);

                JOptionPane.showMessageDialog(this,
                    String.format("Booking confirmed!\n\nReference: %s\nYou earned %d reward points!",
                        booking.getReference(), points),
                    "Booking Successful", JOptionPane.INFORMATION_MESSAGE);

                refreshData();
                parentWindow.refreshAllPanels();
                parentWindow.refreshHeader();

            } catch (FlightBookingSystemException e) {
                UIStyles.showErrorDialog(this, e.getMessage(), "Booking Error");
            }
        }
    }

    private void populateOrigins() {
        fbs.getFlights().stream()
            .map(Flight::getOrigin)
            .distinct()
            .sorted()
            .forEach(origin -> originCombo.addItem(origin));
    }

    private void populateDestinations() {
        fbs.getFlights().stream()
            .map(Flight::getDestination)
            .distinct()
            .sorted()
            .forEach(dest -> destinationCombo.addItem(dest));
    }

    private void performSearch() {
        LocalDate today = fbs.getSystemDate();

        // Start with all future flights
        currentResults = fbs.getFlights().stream()
            .filter(f -> !f.getDepartureDate().isBefore(today))
            .collect(Collectors.toList());

        // Apply origin filter
        String origin = (String) originCombo.getSelectedItem();
        if (origin != null && !origin.equals("All Origins")) {
            currentResults = currentResults.stream()
                .filter(f -> f.getOrigin().equals(origin))
                .collect(Collectors.toList());
        }

        // Apply destination filter
        String dest = (String) destinationCombo.getSelectedItem();
        if (dest != null && !dest.equals("All Destinations")) {
            currentResults = currentResults.stream()
                .filter(f -> f.getDestination().equals(dest))
                .collect(Collectors.toList());
        }

        // Apply date filter
        String dateStr = departureDateField.getText().trim();
        if (!dateStr.isEmpty()) {
            try {
                LocalDate filterDate = LocalDate.parse(dateStr);
                currentResults = currentResults.stream()
                    .filter(f -> f.getDepartureDate().equals(filterDate))
                    .collect(Collectors.toList());
            } catch (Exception e) {
                UIStyles.showErrorDialog(this, "Invalid date format. Use YYYY-MM-DD", "Filter Error");
                return;
            }
        }

        // Apply price filter
        String priceStr = maxPriceField.getText().trim();
        if (!priceStr.isEmpty()) {
            try {
                double maxPrice = Double.parseDouble(priceStr);
                currentResults = currentResults.stream()
                    .filter(f -> DynamicPricing.calculatePrice(f, today) <= maxPrice)
                    .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                UIStyles.showErrorDialog(this, "Invalid price format", "Filter Error");
                return;
            }
        }

        // Apply sorting
        String sortOption = (String) sortByCombo.getSelectedItem();
        if (sortOption != null) {
            switch (sortOption) {
                case "Price: Low to High":
                    currentResults.sort((f1, f2) -> Double.compare(
                        DynamicPricing.calculatePrice(f1, today),
                        DynamicPricing.calculatePrice(f2, today)));
                    break;
                case "Price: High to Low":
                    currentResults.sort((f1, f2) -> Double.compare(
                        DynamicPricing.calculatePrice(f2, today),
                        DynamicPricing.calculatePrice(f1, today)));
                    break;
                case "Departure Date":
                    currentResults.sort((f1, f2) -> f1.getDepartureDate().compareTo(f2.getDepartureDate()));
                    break;
                case "Available Seats":
                    currentResults.sort((f1, f2) -> Integer.compare(
                        f2.getCapacity() - f2.getPassengers().size(),
                        f1.getCapacity() - f1.getPassengers().size()));
                    break;
            }
        }

        // Update display
        updateResultsDisplay();
    }

    private void clearFilters() {
        originCombo.setSelectedIndex(0);
        destinationCombo.setSelectedIndex(0);
        departureDateField.setText("");
        maxPriceField.setText("");
        sortByCombo.setSelectedIndex(0);
        refreshData();
    }

    /**
     * Refreshes the search results.
     */
    public void refreshData() {
        performSearch();
    }
}
