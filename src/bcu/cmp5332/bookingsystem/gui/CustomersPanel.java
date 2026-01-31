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
 * Panel for managing customers with table view and action buttons.
 */
public class CustomersPanel extends JPanel {
    
    private final MainWindow mainWindow;
    private JTable customersTable;
    private DefaultTableModel tableModel;
    private JButton viewDetailsBtn;
    private JButton addBookingBtn;
    private JButton deleteCustomerBtn;
    
    public CustomersPanel(MainWindow mainWindow) {
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
        
        JLabel titleLabel = new JLabel("Customers Management");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JButton addCustomerBtn = UIStyles.createPrimaryButton("+ Add Customer");
        addCustomerBtn.addActionListener(e -> {
            new AddCustomerWindow(mainWindow);
            refreshData();
        });
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addCustomerBtn, BorderLayout.EAST);
        
        // Table panel
        JPanel tableCard = UIStyles.createCard();
        tableCard.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Name", "Email", "Phone", "Bookings"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customersTable = new JTable(tableModel);
        UIStyles.styleTable(customersTable);
        customersTable.getSelectionModel().addListSelectionListener(e -> updateButtonStates());
        
        // Double-click to view details
        customersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showCustomerDetails();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(customersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionsPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        viewDetailsBtn = UIStyles.createSecondaryButton("View Details");
        addBookingBtn = UIStyles.createPrimaryButton("Add Booking");
        deleteCustomerBtn = UIStyles.createDangerButton("Delete Customer");
        
        viewDetailsBtn.setEnabled(false);
        addBookingBtn.setEnabled(false);
        deleteCustomerBtn.setEnabled(false);
        
        viewDetailsBtn.addActionListener(e -> showCustomerDetails());
        addBookingBtn.addActionListener(e -> new IssueBookingWindow(mainWindow));
        deleteCustomerBtn.addActionListener(e -> deleteSelectedCustomer());
        
        actionsPanel.add(viewDetailsBtn);
        actionsPanel.add(addBookingBtn);
        actionsPanel.add(deleteCustomerBtn);
        
        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(tableCard, BorderLayout.CENTER);
        add(actionsPanel, BorderLayout.SOUTH);
    }
    
    private void updateButtonStates() {
        boolean hasSelection = customersTable.getSelectedRow() >= 0;
        viewDetailsBtn.setEnabled(hasSelection);
        addBookingBtn.setEnabled(hasSelection);
        deleteCustomerBtn.setEnabled(hasSelection);
    }
    
    private void showCustomerDetails() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            Customer customer = mainWindow.getFlightBookingSystem().getCustomerByID(customerId);
            showCustomerDetailsDialog(customer);
        } catch (FlightBookingSystemException ex) {
            UIStyles.showErrorDialog(this, ex.getMessage(), "Error");
        }
    }
    
    private void showCustomerDetailsDialog(Customer customer) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Customer Details", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(UIStyles.CARD_BACKGROUND);
        
        // Customer info
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        infoPanel.setBackground(UIStyles.CARD_BACKGROUND);
        infoPanel.add(new JLabel("ID:"));
        infoPanel.add(new JLabel(String.valueOf(customer.getId())));
        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(new JLabel(customer.getName()));
        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(new JLabel(customer.getEmail()));
        infoPanel.add(new JLabel("Phone:"));
        infoPanel.add(new JLabel(customer.getPhone()));
        
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Bookings section
        JLabel bookingsLabel = new JLabel("Bookings:");
        bookingsLabel.setFont(UIStyles.SUBHEADER_FONT);
        contentPanel.add(bookingsLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        String[] bookingColumns = {"Flight", "Route", "Departure", "Price", "Status"};
        DefaultTableModel bookingModel = new DefaultTableModel(bookingColumns, 0);

        FlightBookingSystem fbs = mainWindow.getFlightBookingSystem();
        for (Booking booking : customer.getBookings()) {
            Flight flight = booking.getOutboundFlight();
            double price = DynamicPricing.calculatePrice(flight, fbs.getSystemDate());
            String status = booking.isCancelled() ? "Cancelled" : "Active";
            bookingModel.addRow(new Object[]{
                flight.getFlightNumber(),
                flight.getOrigin() + " → " + flight.getDestination(),
                flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                String.format("£%.2f", price),
                status
            });
        }

        JTable bookingsTable = new JTable(bookingModel);
        UIStyles.styleTable(bookingsTable);
        JScrollPane bookingsScroll = new JScrollPane(bookingsTable);
        bookingsScroll.setPreferredSize(new Dimension(450, 150));
        contentPanel.add(bookingsScroll);

        dialog.add(contentPanel, BorderLayout.CENTER);

        JButton closeBtn = UIStyles.createSecondaryButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void deleteSelectedCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow < 0) return;

        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        String customerName = (String) tableModel.getValueAt(selectedRow, 1);

        boolean confirm = UIStyles.showWarningConfirmDialog(this,
            "Are you sure you want to delete customer " + customerName + "? This action cannot be undone.",
            "Confirm Delete");

        if (confirm) {
            try {
                mainWindow.getFlightBookingSystem().deleteCustomer(customerId);
                refreshData();
                UIStyles.showSuccessDialog(this, "Customer deleted successfully.", "Success");
            } catch (FlightBookingSystemException ex) {
                UIStyles.showErrorDialog(this, ex.getMessage(), "Error");
            }
        }
    }

    /**
     * Refreshes the customers table data.
     */
    public void refreshData() {
        tableModel.setRowCount(0);
        List<Customer> customers = mainWindow.getFlightBookingSystem().getCustomers();

        for (Customer customer : customers) {
            int activeBookings = 0;
            for (Booking b : customer.getBookings()) {
                if (!b.isCancelled()) activeBookings++;
            }
            tableModel.addRow(new Object[]{
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                activeBookings
            });
        }

        updateButtonStates();
    }
}

