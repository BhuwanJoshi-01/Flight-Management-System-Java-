package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel for managing bookings with table view and action buttons.
 */
public class BookingsPanel extends JPanel {
    
    private final MainWindow mainWindow;
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private JButton cancelBookingBtn;
    private JButton updateBookingBtn;
    private JCheckBox showCancelledCheckbox;
    
    public BookingsPanel(MainWindow mainWindow) {
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
        
        JLabel titleLabel = new JLabel("Bookings Management");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setBackground(UIStyles.BACKGROUND_COLOR);
        
        showCancelledCheckbox = new JCheckBox("Show Cancelled");
        showCancelledCheckbox.setBackground(UIStyles.BACKGROUND_COLOR);
        showCancelledCheckbox.addActionListener(e -> refreshData());
        
        JButton issueBookingBtn = UIStyles.createPrimaryButton("+ Issue Booking");
        issueBookingBtn.addActionListener(e -> {
            new IssueBookingWindow(mainWindow);
            refreshData();
        });
        
        headerRight.add(showCancelledCheckbox);
        headerRight.add(issueBookingBtn);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(headerRight, BorderLayout.EAST);
        
        // Table panel
        JPanel tableCard = UIStyles.createCard();
        tableCard.setLayout(new BorderLayout());
        
        String[] columns = {"Customer", "Flight", "Route", "Departure", "Booking Date", "Price", "Fee", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookingsTable = new JTable(tableModel);
        UIStyles.styleTable(bookingsTable);
        bookingsTable.getSelectionModel().addListSelectionListener(e -> updateButtonStates());
        
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionsPanel.setBackground(UIStyles.BACKGROUND_COLOR);

        updateBookingBtn = UIStyles.createSecondaryButton("Update Booking");
        cancelBookingBtn = UIStyles.createDangerButton("Cancel Booking");
        
        updateBookingBtn.setEnabled(false);
        cancelBookingBtn.setEnabled(false);
        
        updateBookingBtn.addActionListener(e -> {
            new EditBookingWindow(mainWindow);
            refreshData();
        });
        cancelBookingBtn.addActionListener(e -> {
            new CancelBookingWindow(mainWindow);
            refreshData();
        });
        
        actionsPanel.add(updateBookingBtn);
        actionsPanel.add(cancelBookingBtn);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        JLabel infoLabel = new JLabel("Note: Cancellation fee is 10% of booking price. Rebooking fee is 10% of new flight price.");
        infoLabel.setFont(UIStyles.SMALL_FONT);
        infoLabel.setForeground(UIStyles.TEXT_SECONDARY);
        infoPanel.add(infoLabel);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        bottomPanel.add(actionsPanel, BorderLayout.WEST);
        bottomPanel.add(infoPanel, BorderLayout.EAST);
        
        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(tableCard, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void updateButtonStates() {
        int selectedRow = bookingsTable.getSelectedRow();
        boolean hasSelection = selectedRow >= 0;
        boolean isActive = hasSelection && !"Cancelled".equals(tableModel.getValueAt(selectedRow, 7));
        
        updateBookingBtn.setEnabled(isActive);
        cancelBookingBtn.setEnabled(isActive);
    }
    
    /**
     * Refreshes the bookings table data.
     */
    public void refreshData() {
        tableModel.setRowCount(0);
        FlightBookingSystem fbs = mainWindow.getFlightBookingSystem();
        List<Customer> customers = fbs.getCustomers();
        boolean showCancelled = showCancelledCheckbox.isSelected();
        
        for (Customer customer : customers) {
            for (Booking booking : customer.getBookings()) {
                if (!showCancelled && booking.isCancelled()) continue;
                
                Flight flight = booking.getOutboundFlight();
                double price = DynamicPricing.calculatePrice(flight, fbs.getSystemDate());
                String status = booking.isCancelled() ? "Cancelled" : "Active";
                String fee = booking.getCancellationFee() > 0 ? 
                    String.format("£%.2f", booking.getCancellationFee()) : "-";
                
                tableModel.addRow(new Object[]{
                    customer.getName(),
                    flight.getFlightNumber(),
                    flight.getOrigin() + " → " + flight.getDestination(),
                    flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    booking.getBookingDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    String.format("£%.2f", price),
                    fee,
                    status
                });
            }
        }
        
        updateButtonStates();
    }
}
