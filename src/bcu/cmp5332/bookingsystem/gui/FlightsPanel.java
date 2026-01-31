package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel for managing flights with table view and action buttons.
 */
public class FlightsPanel extends JPanel {
    
    private final MainWindow mainWindow;
    private JTable flightsTable;
    private DefaultTableModel tableModel;
    private JButton viewPassengersBtn;
    private JButton issueBookingBtn;
    private JButton deleteFlightBtn;
    
    public FlightsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        initializeComponents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Flights Management");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JButton addFlightBtn = UIStyles.createPrimaryButton("+ Add Flight");
        addFlightBtn.addActionListener(e -> {
            new AddFlightWindow(mainWindow);
            refreshData();
        });
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addFlightBtn, BorderLayout.EAST);
        
        // Table panel
        JPanel tableCard = UIStyles.createCard();
        tableCard.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Flight No", "Origin", "Destination", "Departure", "Capacity", "Available", "Base Price", "Dynamic Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        flightsTable = new JTable(tableModel);
        UIStyles.styleTable(flightsTable);
        flightsTable.getSelectionModel().addListSelectionListener(e -> updateButtonStates());
        
        // Double-click to view passengers
        flightsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showPassengerList();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionsPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        viewPassengersBtn = UIStyles.createSecondaryButton("View Passengers");
        issueBookingBtn = UIStyles.createPrimaryButton("Issue Booking");
        deleteFlightBtn = UIStyles.createDangerButton("Delete Flight");
        
        viewPassengersBtn.setEnabled(false);
        issueBookingBtn.setEnabled(false);
        deleteFlightBtn.setEnabled(false);
        
        viewPassengersBtn.addActionListener(e -> showPassengerList());
        issueBookingBtn.addActionListener(e -> issueBookingForSelectedFlight());
        deleteFlightBtn.addActionListener(e -> deleteSelectedFlight());
        
        actionsPanel.add(viewPassengersBtn);
        actionsPanel.add(issueBookingBtn);
        actionsPanel.add(deleteFlightBtn);
        
        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(tableCard, BorderLayout.CENTER);
        add(actionsPanel, BorderLayout.SOUTH);
    }
    
    private void updateButtonStates() {
        boolean hasSelection = flightsTable.getSelectedRow() >= 0;
        viewPassengersBtn.setEnabled(hasSelection);
        issueBookingBtn.setEnabled(hasSelection);
        deleteFlightBtn.setEnabled(hasSelection);
    }
    
    private void showPassengerList() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        int flightId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            Flight flight = mainWindow.getFlightBookingSystem().getFlightByID(flightId);
            List<Customer> passengers = flight.getPassengers();
            
            StringBuilder sb = new StringBuilder();
            sb.append("Passengers on ").append(flight.getFlightNumber()).append(":\n\n");
            
            if (passengers.isEmpty()) {
                sb.append("No passengers booked on this flight.");
            } else {
                for (Customer p : passengers) {
                    sb.append("• ").append(p.getName()).append(" (").append(p.getEmail()).append(")\n");
                }
            }
            
            UIStyles.showInfoDialog(this, sb.toString(), "Passenger List - " + flight.getFlightNumber());
        } catch (FlightBookingSystemException ex) {
            UIStyles.showErrorDialog(this, ex.getMessage(), "Error");
        }
    }

    private void issueBookingForSelectedFlight() {
        new IssueBookingWindow(mainWindow);
    }

    private void deleteSelectedFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow < 0) return;

        int flightId = (int) tableModel.getValueAt(selectedRow, 0);
        String flightNo = (String) tableModel.getValueAt(selectedRow, 1);

        boolean confirm = UIStyles.showWarningConfirmDialog(this,
            "Are you sure you want to delete flight " + flightNo + "? This action cannot be undone.",
            "Confirm Delete");

        if (confirm) {
            try {
                mainWindow.getFlightBookingSystem().deleteFlight(flightId);
                refreshData();
                UIStyles.showSuccessDialog(this, "Flight deleted successfully.", "Success");
            } catch (FlightBookingSystemException ex) {
                UIStyles.showErrorDialog(this, ex.getMessage(), "Error");
            }
        }
    }

    /**
     * Refreshes the flights table data.
     */
    public void refreshData() {
        tableModel.setRowCount(0);
        FlightBookingSystem fbs = mainWindow.getFlightBookingSystem();
        List<Flight> flights = fbs.getFlights();

        for (Flight flight : flights) {
            double dynamicPrice = DynamicPricing.calculatePrice(flight, fbs.getSystemDate());
            tableModel.addRow(new Object[]{
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                flight.getCapacity(),
                flight.getAvailableSeats(),
                String.format("£%.2f", flight.getPrice()),
                String.format("£%.2f", dynamicPrice)
            });
        }

        updateButtonStates();
    }
}

