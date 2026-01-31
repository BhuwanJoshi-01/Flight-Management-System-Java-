package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
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
    private JButton exitBtn;

    // Content panels
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private DashboardPanel dashboardPanel;
    private FlightsPanel flightsPanel;
    private CustomersPanel customersPanel;
    private BookingsPanel bookingsPanel;

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

        // Layout
        add(header, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
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
        header.setBackground(UIStyles.PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Flight Booking System");
        titleLabel.setFont(UIStyles.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);

        dateLabel = new JLabel("System Date: " +
            fbs.getSystemDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        dateLabel.setFont(UIStyles.BODY_FONT);
        dateLabel.setForeground(new Color(200, 220, 255));

        header.add(titleLabel, BorderLayout.WEST);
        header.add(dateLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIStyles.SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(UIStyles.SIDEBAR_WIDTH, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Navigation buttons
        dashboardBtn = UIStyles.createSidebarButton("Dashboard", UIStyles.ICON_DASHBOARD, true);
        flightsBtn   = UIStyles.createSidebarButton("Flights", UIStyles.ICON_FLIGHTS, false);
        customersBtn = UIStyles.createSidebarButton("Customers", UIStyles.ICON_CUSTOMERS, false);
        bookingsBtn  = UIStyles.createSidebarButton("Bookings", UIStyles.ICON_BOOKINGS, false);

        dashboardBtn.addActionListener(e -> showPanel("dashboard"));
        flightsBtn.addActionListener(e -> showPanel("flights"));
        customersBtn.addActionListener(e -> showPanel("customers"));
        bookingsBtn.addActionListener(e -> showPanel("bookings"));

        sidebar.add(dashboardBtn);
        sidebar.add(flightsBtn);
        sidebar.add(customersBtn);
        sidebar.add(bookingsBtn);
        sidebar.add(Box.createVerticalGlue());

        // Exit button at bottom
        JButton exitBtn      = UIStyles.createSidebarButton("Exit", UIStyles.ICON_EXIT, false);
        exitBtn.addActionListener(e -> handleExit());
        sidebar.add(exitBtn);

        return sidebar;
    }

    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        panel.setBackground(UIStyles.BACKGROUND_COLOR);

        // Create content panels
        dashboardPanel = new DashboardPanel(this);
        flightsPanel = new FlightsPanel(this);
        customersPanel = new CustomersPanel(this);
        bookingsPanel = new BookingsPanel(this);

        panel.add(dashboardPanel, "dashboard");
        panel.add(flightsPanel, "flights");
        panel.add(customersPanel, "customers");
        panel.add(bookingsPanel, "bookings");

        return panel;
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
        dashboardBtn.setBackground(panelName.equals("dashboard") ? UIStyles.PRIMARY_COLOR : UIStyles.SIDEBAR_COLOR);
        flightsBtn.setBackground(panelName.equals("flights") ? UIStyles.PRIMARY_COLOR : UIStyles.SIDEBAR_COLOR);
        customersBtn.setBackground(panelName.equals("customers") ? UIStyles.PRIMARY_COLOR : UIStyles.SIDEBAR_COLOR);
        bookingsBtn.setBackground(panelName.equals("bookings") ? UIStyles.PRIMARY_COLOR : UIStyles.SIDEBAR_COLOR);

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
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // Legacy action handler - kept for compatibility
    }
}
