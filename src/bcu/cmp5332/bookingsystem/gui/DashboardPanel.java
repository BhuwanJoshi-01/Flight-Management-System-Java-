package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Dashboard panel displaying KPI cards, quick actions, and recent activity.
 */
public class DashboardPanel extends JPanel {
    
    private final MainWindow mainWindow;
    private JLabel totalFlightsValue;
    private JLabel totalCustomersValue;
    private JLabel activeBookingsValue;
    private JLabel upcomingFlightsValue;
    private JTable recentActivityTable;
    private DefaultTableModel activityTableModel;


    public DashboardPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        initializeComponents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // KPI Cards
        JPanel kpiPanel = createKPIPanel();
        contentPanel.add(kpiPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Quick Actions
        JPanel actionsPanel = createQuickActionsPanel();
        contentPanel.add(actionsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Recent Activity
        JPanel activityPanel = createRecentActivityPanel();
        contentPanel.add(activityPanel);
        
        add(new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyles.BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        FlightBookingSystem fbs = mainWindow.getFlightBookingSystem();
        JLabel dateLabel = new JLabel("System Date: " + 
            fbs.getSystemDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        dateLabel.setFont(UIStyles.BODY_FONT);
        dateLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(dateLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createKPIPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(UIStyles.BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Total Flights Card
        JPanel flightsCard = createKPICard("Total Flights", "0", UIStyles.PRIMARY_COLOR);
        totalFlightsValue = (JLabel) ((JPanel) flightsCard.getComponent(0)).getComponent(0);
        panel.add(flightsCard);
        
        // Total Customers Card
        JPanel customersCard = createKPICard("Total Customers", "0", UIStyles.ACCENT_COLOR);
        totalCustomersValue = (JLabel) ((JPanel) customersCard.getComponent(0)).getComponent(0);
        panel.add(customersCard);
        
        // Active Bookings Card
        JPanel bookingsCard = createKPICard("Active Bookings", "0", UIStyles.SECONDARY_COLOR);
        activeBookingsValue = (JLabel) ((JPanel) bookingsCard.getComponent(0)).getComponent(0);
        panel.add(bookingsCard);
        
        // Upcoming Flights Card
        JPanel upcomingCard = createKPICard("Departing Soon", "0", new Color(155, 89, 182));
        upcomingFlightsValue = (JLabel) ((JPanel) upcomingCard.getComponent(0)).getComponent(0);
        panel.add(upcomingCard);
        
        return panel;
    }
    
    private JPanel createKPICard(String label, String value, Color accentColor) {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BorderLayout());
        
        // Value panel with colored left border
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        valuePanel.setBackground(UIStyles.CARD_BACKGROUND);
        valuePanel.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UIStyles.KPI_VALUE_FONT);
        valueLabel.setForeground(UIStyles.TEXT_PRIMARY);
        valueLabel.setBorder(new EmptyBorder(0, 15, 0, 0));
        valuePanel.add(valueLabel);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(UIStyles.KPI_LABEL_FONT);
        labelText.setForeground(UIStyles.TEXT_SECONDARY);
        
        card.add(valuePanel, BorderLayout.CENTER);
        card.add(labelText, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JLabel title = new JLabel("Quick Actions");
        title.setFont(UIStyles.HEADER_FONT);
        title.setForeground(UIStyles.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsPanel.setBackground(UIStyles.CARD_BACKGROUND);
        
        JButton addFlightBtn = UIStyles.createPrimaryButton("Add Flight");
        JButton addCustomerBtn = UIStyles.createPrimaryButton("Add Customer");
        JButton addBookingBtn = UIStyles.createSecondaryButton("Issue Booking");
        JButton cancelBookingBtn = UIStyles.createDangerButton("Cancel Booking");
        
        addFlightBtn.addActionListener(e -> new AddFlightWindow(mainWindow));
        addCustomerBtn.addActionListener(e -> new AddCustomerWindow(mainWindow));
        addBookingBtn.addActionListener(e -> new IssueBookingWindow(mainWindow));
        cancelBookingBtn.addActionListener(e -> new CancelBookingWindow(mainWindow));
        
        buttonsPanel.add(addFlightBtn);
        buttonsPanel.add(addCustomerBtn);
        buttonsPanel.add(addBookingBtn);
        buttonsPanel.add(cancelBookingBtn);
        
        card.add(title, BorderLayout.NORTH);
        card.add(buttonsPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createRecentActivityPanel() {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BorderLayout());

        JLabel title = new JLabel("Recent Bookings");
        title.setFont(UIStyles.HEADER_FONT);
        title.setForeground(UIStyles.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] columns = {"Customer", "Flight", "Route", "Departure", "Price Paid"};
        activityTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recentActivityTable = new JTable(activityTableModel);
        UIStyles.styleTable(recentActivityTable);

        JScrollPane scrollPane = new JScrollPane(recentActivityTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        card.add(title, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    /**
     * Refreshes all dashboard data from the FlightBookingSystem.
     */
    public void refreshData() {
        FlightBookingSystem fbs = mainWindow.getFlightBookingSystem();
        LocalDate systemDate = fbs.getSystemDate();

        // Update KPI values
        List<Flight> flights = fbs.getFlights();
        List<Customer> customers = fbs.getCustomers();

        int totalFlights = flights.size();
        int totalCustomers = customers.size();
        int activeBookings = 0;
        int upcomingFlights = 0;

        // Count active bookings
        for (Customer customer : customers) {
            for (Booking booking : customer.getBookings()) {
                if (!booking.isCancelled()) {
                    activeBookings++;
                }
            }
        }

        // Count flights departing in next 7 days
        for (Flight flight : flights) {
            long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(systemDate, flight.getDepartureDate());
            if (daysUntil >= 0 && daysUntil <= 7) {
                upcomingFlights++;
            }
        }

        totalFlightsValue.setText(String.valueOf(totalFlights));
        totalCustomersValue.setText(String.valueOf(totalCustomers));
        activeBookingsValue.setText(String.valueOf(activeBookings));
        upcomingFlightsValue.setText(String.valueOf(upcomingFlights));

        // Update recent activity table
        activityTableModel.setRowCount(0);
        int count = 0;
        for (Customer customer : customers) {
            for (Booking booking : customer.getBookings()) {
                if (!booking.isCancelled() && count < 10) {
                    Flight flight = booking.getOutboundFlight();
                    double price = DynamicPricing.calculatePrice(flight, systemDate);
                    activityTableModel.addRow(new Object[]{
                        customer.getName(),
                        flight.getFlightNumber(),
                        flight.getOrigin() + " → " + flight.getDestination(),
                        flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                        String.format("£%.2f", price)
                    });
                    count++;
                }
            }
        }
    }
}

