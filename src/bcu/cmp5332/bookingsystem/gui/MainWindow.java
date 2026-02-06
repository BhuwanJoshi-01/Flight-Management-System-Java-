package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.gui.admin.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Main application window with modern dashboard-driven layout.
 * Features sidebar navigation, KPI cards, and tabbed content panels.
 */
public class MainWindow extends JFrame implements ActionListener {

    private FlightBookingSystem fbs;

    // Sidebar navigation buttons
    private JButton dashboardBtn;
    private JButton flightsBtn;
    private JButton customersBtn;
    private JButton bookingsBtn;
    private JButton revenueBtn;
    private JButton settingsBtn;
    private JButton exitBtn;

    // Content panels - using new admin panels
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private AdminDashboardPanel dashboardPanel;
    private AdminFlightsPanel flightsPanel;
    private AdminCustomersPanel customersPanel;
    private AdminBookingsPanel bookingsPanel;
    private AdminRevenuePanel revenuePanel;
    private AdminSettingsPanel settingsPanel;

    // Status bar
    private JLabel statusLabel;
    private JLabel dateLabel;



    public MainWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
        showPanel("dashboard");
    }

    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Use default look and feel
        }

        setTitle("Flight Booking Management System");
        setLayout(new BorderLayout());

        // Create main components
        JPanel sidebar = createSidebar();
        JPanel header = createHeader();
        JPanel statusBar = createStatusBar();
        contentPanel = createContentPanel();

        // Layout - Sidebar on left, Header on top of content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(header, BorderLayout.NORTH);
        mainContent.add(contentPanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        setVisible(true);
        setAutoRequestFocus(true);
        toFront();

        // Handle window closing - save data
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
            new EmptyBorder(12, 24, 12, 24)
        ));

        // Page title
        JLabel titleLabel = new JLabel("Admin Control Center");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(UIStyles.SIDEBAR_COLOR);

        // Date and Info
        dateLabel = new JLabel("System Date: " +
            fbs.getSystemDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        if (UIStyles.ICON_CALENDAR != null) {
            Image scaled = UIStyles.ICON_CALENDAR.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            dateLabel.setIcon(new ImageIcon(scaled));
            dateLabel.setIconTextGap(8);
        }
        dateLabel.setFont(UIStyles.BODY_FONT);
        dateLabel.setForeground(UIStyles.TEXT_SECONDARY);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(dateLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIStyles.SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(UIStyles.SIDEBAR_WIDTH, 0));
        sidebar.setBorder(new EmptyBorder(0, 0, 16, 0));

        // Logo header
        JPanel header = UIStyles.createSidebarLogoHeader("SkyBook", "Flight System", UIStyles.ICON_FLIGHTS);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(header);

        // Role badge
        JPanel roleBadge = UIStyles.createSidebarBadge("Admin Portal", true);
        roleBadge.setBorder(new EmptyBorder(12, 16, 12, 16));
        roleBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(roleBadge);
        sidebar.add(Box.createVerticalStrut(6));

        // Navigation buttons
        dashboardBtn = UIStyles.createSidebarButton("Dashboard", UIStyles.ICON_DASHBOARD, true);
        flightsBtn   = UIStyles.createSidebarButton("Flights", UIStyles.ICON_FLIGHTS, false);
        customersBtn = UIStyles.createSidebarButton("Customers", UIStyles.ICON_CUSTOMERS, false);
        bookingsBtn  = UIStyles.createSidebarButton("Bookings", UIStyles.ICON_BOOKINGS, false);
        revenueBtn   = UIStyles.createSidebarButton("Revenue", UIStyles.ICON_REVENUE, false);
        settingsBtn  = UIStyles.createSidebarButton("Settings", UIStyles.ICON_SETTINGS, false);

        dashboardBtn.addActionListener(e -> showPanel("dashboard"));
        flightsBtn.addActionListener(e -> showPanel("flights"));
        customersBtn.addActionListener(e -> showPanel("customers"));
        bookingsBtn.addActionListener(e -> showPanel("bookings"));
        revenueBtn.addActionListener(e -> showPanel("revenue"));
        settingsBtn.addActionListener(e -> showPanel("settings"));

        JButton[] buttons = {dashboardBtn, flightsBtn, customersBtn, bookingsBtn, revenueBtn, settingsBtn};
        for (JButton btn : buttons) {
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(btn);
        }
        
        sidebar.add(Box.createVerticalGlue());

        // Exit button at bottom
        JPanel exitDivider = new JPanel();
        exitDivider.setBackground(UIStyles.SIDEBAR_BORDER);
        exitDivider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        exitDivider.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(exitDivider);
        sidebar.add(Box.createVerticalStrut(8));

        exitBtn = UIStyles.createSidebarButton("Exit", UIStyles.ICON_EXIT, false);
        exitBtn.addActionListener(e -> handleExit());
        exitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(exitBtn);

        return sidebar;
    }

    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        panel.setBackground(UIStyles.BACKGROUND_COLOR);

        // Create content panels - using new admin panels
        dashboardPanel = new AdminDashboardPanel(this);
        flightsPanel = new AdminFlightsPanel(this);
        customersPanel = new AdminCustomersPanel(this);
        bookingsPanel = new AdminBookingsPanel(this);
        revenuePanel = new AdminRevenuePanel(this);
        settingsPanel = new AdminSettingsPanel(this);

        panel.add(wrapInScrollPane(dashboardPanel), "dashboard");
        panel.add(wrapInScrollPane(flightsPanel), "flights");
        panel.add(wrapInScrollPane(customersPanel), "customers");
        panel.add(wrapInScrollPane(bookingsPanel), "bookings");
        panel.add(wrapInScrollPane(revenuePanel), "revenue");
        panel.add(wrapInScrollPane(settingsPanel), "settings");

        return panel;
    }

    private JScrollPane wrapInScrollPane(JComponent content) {
        JScrollPane scrollPane = new JScrollPane(content,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UIStyles.BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(240, 240, 240));
        statusBar.setBorder(new EmptyBorder(8, 15, 8, 15));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(UIStyles.SMALL_FONT);
        statusLabel.setForeground(UIStyles.TEXT_SECONDARY);

        JLabel versionLabel = new JLabel("v1.0 | CMP5332 Coursework");
        versionLabel.setFont(UIStyles.SMALL_FONT);
        versionLabel.setForeground(UIStyles.TEXT_SECONDARY);

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(versionLabel, BorderLayout.EAST);

        return statusBar;
    }

    /**
     * Shows the specified panel and updates sidebar button states.
     */
    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);

        // Update sidebar button states
        UIStyles.setSidebarButtonSelected(dashboardBtn, panelName.equals("dashboard"));
        UIStyles.setSidebarButtonSelected(flightsBtn, panelName.equals("flights"));
        UIStyles.setSidebarButtonSelected(customersBtn, panelName.equals("customers"));
        UIStyles.setSidebarButtonSelected(bookingsBtn, panelName.equals("bookings"));
        UIStyles.setSidebarButtonSelected(revenueBtn, panelName.equals("revenue"));
        UIStyles.setSidebarButtonSelected(settingsBtn, panelName.equals("settings"));

        // Refresh data when switching panels
        switch (panelName) {
            case "dashboard":
                dashboardPanel.refreshData();
                break;
            case "flights":
                flightsPanel.refreshData();
                break;
            case "customers":
                customersPanel.refreshData();
                break;
            case "bookings":
                bookingsPanel.refreshData();
                break;
            case "revenue":
                revenuePanel.refreshData();
                break;
            case "settings":
                // Settings panel doesn't need refresh in this version
                break;
        }

        statusLabel.setText("Viewing: " + panelName.substring(0, 1).toUpperCase() + panelName.substring(1));
    }

    /**
     * Handles application exit with data save.
     */
    private void handleExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Do you want to save changes before exiting?",
            "Exit Application", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                FlightBookingSystemData.store(fbs);
                statusLabel.setText("Data saved successfully");
                dispose();
                System.exit(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error saving data: " + ex.getMessage() + "\nChanges may be lost.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (confirm == JOptionPane.NO_OPTION) {
            dispose();
            System.exit(0);
        }
        // Cancel - do nothing
    }

    /**
     * Refreshes all panels - called after data changes.
     */
    public void refreshAllPanels() {
        dashboardPanel.refreshData();
        flightsPanel.refreshData();
        customersPanel.refreshData();
        bookingsPanel.refreshData();
        revenuePanel.refreshData();
        // settingsPanel doesn't need refresh in this version
    }

    // ==================== Admin Panel Helper Methods ====================

    /**
     * Shows the Add Flight dialog.
     */
    public void showAddFlightDialog() {
        new AddFlightWindow(this);
        refreshAllPanels();
        statusLabel.setText("Flight operation completed");
    }

    /**
     * Shows the Edit Flight dialog.
     */
    public void showEditFlightDialog(int flightId) {
        new EditFlightWindow(this, flightId);
        refreshAllPanels();
        statusLabel.setText("Flight updated successfully");
    }

    /**
     * Shows the Add Customer dialog.
     */
    public void showAddCustomerDialog() {
        new AddCustomerWindow(this);
        refreshAllPanels();
        statusLabel.setText("Customer operation completed");
    }

    /**
     * Shows the Booking dialog.
     */
    public void showBookingDialog() {
        new IssueBookingWindow(this);
        refreshAllPanels();
        statusLabel.setText("Booking operation completed");
    }

    /**
     * Shows flight details.
     */
    public void showFlightDetails(int flightId) {
        try {
            Flight flight = fbs.getFlightByID(flightId);
            StringBuilder info = new StringBuilder();
            info.append("Flight Number: ").append(flight.getFlightNumber()).append("\n");
            info.append("Origin: ").append(flight.getOrigin()).append("\n");
            info.append("Destination: ").append(flight.getDestination()).append("\n");
            info.append("Date: ").append(flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))).append("\n");
            info.append("Capacity: ").append(flight.getCapacity()).append("\n");
            info.append("Available Seats: ").append(flight.getAvailableSeats()).append("\n");
            info.append("Base Price: £").append(String.format("%.2f", flight.getPrice()));
            
            JOptionPane.showMessageDialog(this, info.toString(), 
                "Flight Details - " + flight.getFlightNumber(), JOptionPane.INFORMATION_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows flight passengers.
     */
    public void showFlightPassengers(int flightId) {
        try {
            Flight flight = fbs.getFlightByID(flightId);
            java.util.List<Customer> passengers = flight.getPassengers();
            
            if (passengers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No passengers booked on this flight.",
                    "Passengers - " + flight.getFlightNumber(), JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            StringBuilder info = new StringBuilder();
            info.append("Passengers on flight ").append(flight.getFlightNumber()).append(":\n\n");
            for (Customer p : passengers) {
                info.append("• ").append(p.getName()).append(" (").append(p.getEmail()).append(")\n");
            }
            
            JOptionPane.showMessageDialog(this, info.toString(),
                "Passengers - " + flight.getFlightNumber(), JOptionPane.INFORMATION_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows customer details.
     */
    public void showCustomerDetails(int customerId) {
        try {
            Customer customer = fbs.getCustomerByID(customerId);
            StringBuilder info = new StringBuilder();
            info.append("Name: ").append(customer.getName()).append("\n");
            info.append("Email: ").append(customer.getEmail()).append("\n");
            info.append("Phone: ").append(customer.getPhone()).append("\n");
            info.append("Tier: ").append(customer.getTier()).append("\n");
            info.append("Reward Points: ").append(customer.getRewardPoints()).append("\n");
            info.append("Active Bookings: ").append(customer.getBookings().size());
            
            JOptionPane.showMessageDialog(this, info.toString(),
                "Customer Details - " + customer.getName(), JOptionPane.INFORMATION_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a flight (soft delete).
     */
    public void deleteFlight(int flightId) {
        try {
            fbs.deleteFlight(flightId);
            FlightBookingSystemData.store(fbs);
            refreshAllPanels();
            statusLabel.setText("Flight deleted successfully");
        } catch (FlightBookingSystemException | IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a customer (soft delete).
     */
    public void deleteCustomer(int customerId) {
        try {
            fbs.deleteCustomer(customerId);
            FlightBookingSystemData.store(fbs);
            refreshAllPanels();
            statusLabel.setText("Customer deleted successfully");
        } catch (FlightBookingSystemException | IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cancels a booking.
     */
    public void cancelBooking(int customerId, int flightId) {
        try {
            Customer customer = fbs.getCustomerByID(customerId);
            Flight flight = fbs.getFlightByID(flightId);
            customer.cancelBookingForFlight(flight);
            FlightBookingSystemData.store(fbs);
            refreshAllPanels();
            statusLabel.setText("Booking cancelled successfully");
        } catch (FlightBookingSystemException | IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Navigates to the specified panel.
     */
    public void navigateToPanel(String panelName) {
        showPanel(panelName);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // Legacy action handler - kept for compatibility
    }
}
