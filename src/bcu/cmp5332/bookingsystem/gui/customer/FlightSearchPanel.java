package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Flight Search Panel - Main container component.
 * Equivalent to React's CustomerSearchFlights page.
 * 
 * Follows React component architecture:
 * - Uses SearchFormPanel for search inputs
 * - Uses FilterPanel for sidebar filters
 * - Uses FlightCardPanel for result cards
 * - State management via instance fields (like useState)
 */
public class FlightSearchPanel extends JPanel {

    private final CustomerMainWindow parentWindow;
    private final FlightBookingSystem fbs;
    private final Customer customer;

    // Child components (like React component composition)
    private SearchFormPanel searchForm;
    private FilterPanel filterPanel;
    
    // Results state
    private JPanel resultsContainer;
    private JLabel resultsCountLabel;
    private JComboBox<String> sortByCombo;
    private List<Flight> currentResults;

    public FlightSearchPanel(CustomerMainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.fbs = parentWindow.getFlightBookingSystem();
        this.customer = parentWindow.getCurrentCustomer();

        setLayout(new BorderLayout(0, 24));
        setBackground(UITheme.BG);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        initializeComponents();
        performSearch();
    }

    private void initializeComponents() {
        // Top section: Header + Search Form
        JPanel topSection = new JPanel();
        topSection.setOpaque(false);
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.add(createHeaderPanel());
        topSection.add(Box.createVerticalStrut(24));
        
        // Search Form Component
        searchForm = new SearchFormPanel(fbs, this::performSearch);
        topSection.add(searchForm);
        
        add(topSection, BorderLayout.NORTH);

        // Content section: Filters + Results (like React's flex gap-6)
        JPanel contentSection = new JPanel(new BorderLayout(24, 0));
        contentSection.setOpaque(false);

        // Filter Panel Component (left sidebar)
        filterPanel = new FilterPanel(this::performSearch, this::clearFilters);
        contentSection.add(filterPanel, BorderLayout.WEST);

        // Results Panel (main content)
        contentSection.add(createResultsPanel(), BorderLayout.CENTER);

        add(contentSection, BorderLayout.CENTER);
    }

    /**
     * Header panel - equivalent to React's header div
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        // h1 - text-2xl font-bold text-foreground
        JLabel titleLabel = new JLabel("Search Flights");
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        // p - text-muted-foreground
        JLabel subtitleLabel = new JLabel("Find the perfect flight for your journey");
        subtitleLabel.setFont(UITheme.BODY_FONT);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);
        return panel;
    }

    /**
     * Results panel - equivalent to React's results div
     */
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Results header - flex items-center justify-between mb-4
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 8, 16, 8));

        // Left: results count - text-muted-foreground
        resultsCountLabel = new JLabel("0 flights found");
        resultsCountLabel.setFont(UITheme.BODY_FONT);
        resultsCountLabel.setForeground(UITheme.TEXT_SECONDARY);
        headerPanel.add(resultsCountLabel, BorderLayout.WEST);

        // Right: sort dropdown
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        sortPanel.setOpaque(false);

        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setFont(UITheme.SMALL_FONT);
        sortLabel.setForeground(UITheme.TEXT_MUTED);
        sortPanel.add(sortLabel);

        sortByCombo = new JComboBox<>(new String[]{
            "Price: Low to High",
            "Price: High to Low",
            "Departure Time",
            "Available Seats"
        });
        sortByCombo.setFont(UITheme.BODY_FONT);
        sortByCombo.setPreferredSize(new Dimension(180, 36));
        sortByCombo.addActionListener(e -> performSearch());
        sortPanel.add(sortByCombo);

        headerPanel.add(sortPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Results container - space-y-4
        resultsContainer = new JPanel();
        resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
        resultsContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(resultsContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Updates the results display - like React's re-render on state change
     */
    private void updateResultsDisplay() {
        resultsContainer.removeAll();
        LocalDate today = fbs.getSystemDate();

        // Update count label
        if (resultsCountLabel != null) {
            resultsCountLabel.setText(currentResults.size() + " flights found");
        }

        if (currentResults.isEmpty()) {
            // Empty state - like React's conditional rendering
            resultsContainer.add(createEmptyState());
        } else {
            // Map over results and create FlightCardPanel for each
            for (Flight flight : currentResults) {
                boolean alreadyBooked = customer.getBookings().stream()
                    .anyMatch(b -> !b.isCancelled() && b.getOutboundFlight().getId() == flight.getId());
                
                FlightCardPanel card = new FlightCardPanel(
                    flight, 
                    today, 
                    alreadyBooked,
                    () -> bookFlight(flight)
                );
                resultsContainer.add(card);
                resultsContainer.add(Box.createVerticalStrut(16));
            }
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }

    /**
     * Empty state component - like React's conditional "No flights found" render
     */
    private JPanel createEmptyState() {
        JPanel panel = UITheme.createCard();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(48, 24, 48, 24));

        // Plane icon
        if (UIStyles.ICON_PLANE != null) {
            ImageIcon scaledIcon = UIStyles.scaleIcon(UIStyles.ICON_PLANE, 48, 48);
            ImageIcon tintedIcon = UIStyles.tintIcon(scaledIcon, UITheme.TEXT_MUTED);
            JLabel iconLabel = new JLabel(tintedIcon);
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(iconLabel);
            panel.add(Box.createVerticalStrut(16));
        }

        // h3 - text-lg font-semibold mb-2
        JLabel titleLabel = new JLabel("No flights found");
        titleLabel.setFont(UITheme.HEADER_FONT);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));

        // p - text-muted-foreground
        JLabel subtitleLabel = new JLabel("Try adjusting your search criteria or filters");
        subtitleLabel.setFont(UITheme.BODY_FONT);
        subtitleLabel.setForeground(UITheme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitleLabel);

        return panel;
    }

    /**
     * Book flight action - like React's event handler
     */
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

                // Calculate reward points
                int points = (int) (price / 10) * 10;
                customer.addRewardPoints(points);

                JOptionPane.showMessageDialog(this,
                    String.format("Booking confirmed!\n\nReference: %s\nYou earned %d reward points!",
                        booking.getReference(), points),
                    "Booking Successful", JOptionPane.INFORMATION_MESSAGE);

                // Refresh UI (like React's state update triggering re-render)
                refreshData();
                parentWindow.refreshAllPanels();
                parentWindow.refreshHeader();

            } catch (FlightBookingSystemException e) {
                UITheme.showError(this, e.getMessage(), "Booking Error");
            }
        }
    }

    /**
     * Perform search - like React's handleSearch function
     */
    private void performSearch() {
        LocalDate today = fbs.getSystemDate();

        // Start with all future flights
        currentResults = fbs.getFlights().stream()
            .filter(f -> !f.getDepartureDate().isBefore(today))
            .collect(Collectors.toList());

        // Apply origin filter from SearchFormPanel
        String origin = searchForm.getSelectedOrigin();
        if (origin != null) {
            currentResults = currentResults.stream()
                .filter(f -> f.getOrigin().equals(origin))
                .collect(Collectors.toList());
        }

        // Apply destination filter
        String destination = searchForm.getSelectedDestination();
        if (destination != null) {
            currentResults = currentResults.stream()
                .filter(f -> f.getDestination().equals(destination))
                .collect(Collectors.toList());
        }

        // Apply date filter
        String dateStr = searchForm.getDepartureDate();
        if (dateStr != null) {
            try {
                LocalDate filterDate = LocalDate.parse(dateStr);
                currentResults = currentResults.stream()
                    .filter(f -> f.getDepartureDate().equals(filterDate))
                    .collect(Collectors.toList());
            } catch (Exception e) {
                UITheme.showError(this, "Invalid date format. Use yyyy-MM-dd", "Filter Error");
                return;
            }
        }

        // Apply price filter from FilterPanel
        double maxPrice = filterPanel.getMaxPrice();
        if (maxPrice < 1500) {
            currentResults = currentResults.stream()
                .filter(f -> DynamicPricing.calculatePrice(f, today) <= maxPrice)
                .collect(Collectors.toList());
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
                case "Departure Time":
                    currentResults.sort((f1, f2) -> f1.getDepartureDate().compareTo(f2.getDepartureDate()));
                    break;
                case "Available Seats":
                    currentResults.sort((f1, f2) -> Integer.compare(
                        f2.getCapacity() - f2.getPassengers().size(),
                        f1.getCapacity() - f1.getPassengers().size()));
                    break;
            }
        }

        updateResultsDisplay();
    }

    /**
     * Clear all filters - like React's clear button handler
     */
    private void clearFilters() {
        searchForm.clearForm();
        filterPanel.clearFilters();
        sortByCombo.setSelectedIndex(0);
        performSearch();
    }

    /**
     * Refresh data - public method for external refresh triggers
     */
    public void refreshData() {
        performSearch();
    }
}
