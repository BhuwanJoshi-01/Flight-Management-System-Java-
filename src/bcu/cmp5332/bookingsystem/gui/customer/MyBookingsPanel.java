package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * My Bookings Panel showing all customer bookings with cancel/download actions.
 */
public class MyBookingsPanel extends JPanel {

    private final CustomerMainWindow parentWindow;
    private final FlightBookingSystem fbs;
    private final Customer customer;

    // Tab components
    private JTabbedPane tabbedPane;
    private JPanel upcomingContainer;
    private JPanel completedContainer;
    private JPanel cancelledContainer;

    // Current data
    private List<Booking> upcomingBookings;
    private List<Booking> completedBookings;
    private List<Booking> cancelledBookings;

    public MyBookingsPanel(CustomerMainWindow parentWindow) {
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

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setFocusable(false);

        // Upcoming tab
        upcomingContainer = createContainerPanel();
        tabbedPane.addTab("Upcoming", createScrollPane(upcomingContainer));

        // Completed tab
        completedContainer = createContainerPanel();
        tabbedPane.addTab("Completed", createScrollPane(completedContainer));

        // Cancelled tab
        cancelledContainer = createContainerPanel();
        tabbedPane.addTab("Cancelled", createScrollPane(cancelledContainer));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createContainerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));
        return panel;
    }

    private JScrollPane createScrollPane(JPanel content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("View and manage your flight bookings");
        subtitleLabel.setFont(UIStyles.BODY_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        return panel;
    }

    /**
     * Refreshes all booking data.
     */
    public void refreshData() {
        LocalDate today = fbs.getSystemDate();

        // Categorize bookings
        upcomingBookings = customer.getBookings().stream()
            .filter(b -> !b.isCancelled())
            .filter(b -> !b.getOutboundFlight().getDepartureDate().isBefore(today))
            .collect(Collectors.toList());

        completedBookings = customer.getBookings().stream()
            .filter(b -> !b.isCancelled())
            .filter(b -> b.getOutboundFlight().getDepartureDate().isBefore(today))
            .collect(Collectors.toList());

        cancelledBookings = customer.getBookings().stream()
            .filter(Booking::isCancelled)
            .collect(Collectors.toList());

        // Update containers
        updateContainer(upcomingContainer, upcomingBookings, "upcoming");
        updateContainer(completedContainer, completedBookings, "completed");
        updateContainer(cancelledContainer, cancelledBookings, "cancelled");

        // Update tab titles with counts
        tabbedPane.setTitleAt(0, "Upcoming (" + upcomingBookings.size() + ")");
        tabbedPane.setTitleAt(1, "Completed (" + completedBookings.size() + ")");
        tabbedPane.setTitleAt(2, "Cancelled (" + cancelledBookings.size() + ")");
    }

    private void updateContainer(JPanel container, List<Booking> bookings, String type) {
        container.removeAll();

        if (bookings.isEmpty()) {
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setOpaque(false);
            JLabel emptyLabel = new JLabel("No " + type + " bookings found.");
            emptyLabel.setFont(UIStyles.BODY_FONT);
            emptyLabel.setForeground(UIStyles.TEXT_SECONDARY);
            emptyPanel.add(emptyLabel);
            container.add(emptyPanel);
        } else {
            for (Booking booking : bookings) {
                container.add(createBookingCard(booking, type));
                container.add(Box.createVerticalStrut(15));
            }
        }

        container.revalidate();
        container.repaint();
    }

    private JPanel createBookingCard(Booking booking, String type) {
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
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        Flight flight = booking.getOutboundFlight();

        // Top Row: Booking ID and Status
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel idLabel = new JLabel(booking.getReference());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(UIStyles.TEXT_PRIMARY);

        JPanel statusWrapper = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bg = new Color(220, 252, 231); // Success green
                if (type.equals("cancelled")) bg = new Color(254, 226, 226); // Red
                if (type.equals("completed")) bg = new Color(241, 245, 249); // Slate
                
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        statusWrapper.setOpaque(false);
        
        String statusText = "Confirmed";
        Color textColor = new Color(21, 128, 61);
        if (type.equals("cancelled")) {
            statusText = "Cancelled";
            textColor = new Color(220, 38, 38);
        } else if (type.equals("completed")) {
            statusText = "Completed";
            textColor = UIStyles.TEXT_SECONDARY;
        }

        JLabel statusLabel = new JLabel(statusText);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusLabel.setForeground(textColor);
        statusLabel.setBorder(new EmptyBorder(4, 12, 4, 12));
        statusWrapper.add(statusLabel);

        topRow.add(idLabel, BorderLayout.WEST);
        topRow.add(statusWrapper, BorderLayout.EAST);

        // Middle Row: Flight Info
        JPanel middleRow = new JPanel(new GridBagLayout());
        middleRow.setOpaque(false);
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
        middleRow.add(iconCircle, gbc);

        // Flight details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        JLabel flightNoLabel = new JLabel(flight.getFlightNumber());
        flightNoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        flightNoLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JLabel routeLabel = new JLabel(flight.getOrigin() + " → " + flight.getDestination());
        routeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        routeLabel.setForeground(UIStyles.TEXT_SECONDARY);

        detailsPanel.add(flightNoLabel);
        detailsPanel.add(routeLabel);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        middleRow.add(detailsPanel, gbc);

        // Date/Time
        JLabel dateLabel = new JLabel(" " + flight.getDepartureDate().format(DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy")));
        if (UIStyles.ICON_CALENDAR != null) {
            Image scaled = UIStyles.ICON_CALENDAR.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            dateLabel.setIcon(new ImageIcon(scaled));
        }
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        gbc.gridx = 2;
        gbc.weightx = 0.4;
        middleRow.add(dateLabel, gbc);

        // Action Buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setOpaque(false);

        if (type.equals("upcoming")) {
            JButton cancelBtn = UIStyles.createSecondaryButton("Cancel");
            cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            cancelBtn.setPreferredSize(new Dimension(90, 32));
            cancelBtn.addActionListener(e -> cancelBooking(booking));
            actionsPanel.add(cancelBtn);
        }

        JButton downloadBtn = UIStyles.createPrimaryButton("Download");
        downloadBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        downloadBtn.setPreferredSize(new Dimension(100, 32));
        downloadBtn.addActionListener(e -> downloadETicket(booking));
        actionsPanel.add(downloadBtn);

        gbc.gridx = 3;
        gbc.weightx = 0;
        middleRow.add(actionsPanel, gbc);

        card.add(topRow, BorderLayout.NORTH);
        card.add(middleRow, BorderLayout.CENTER);

        return card;
    }

    private void cancelBooking(Booking booking) {
        Flight flight = booking.getOutboundFlight();
        LocalDate today = fbs.getSystemDate();

        double price = DynamicPricing.calculatePrice(flight, today);
        double cancellationFee = price * Booking.CANCELLATION_FEE_PERCENTAGE;

        String message = String.format(
            "Cancel Booking: %s\n\n" +
            "Flight: %s (%s → %s)\n" +
            "Date: %s\n\n" +
            "Cancellation Fee: £%.2f\n\n" +
            "Are you sure you want to cancel?",
            booking.getReference(),
            flight.getFlightNumber(),
            flight.getOrigin(),
            flight.getDestination(),
            flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
            cancellationFee
        );

        int confirm = JOptionPane.showConfirmDialog(this, message, "Cancel Booking",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            booking.setCancelled(true);
            booking.setCancellationFee(cancellationFee);
            booking.setStatus(Booking.BookingStatus.CANCELLED);

            try {
                flight.removePassenger(customer);
                FlightBookingSystemData.store(fbs);
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
                refreshData();
                parentWindow.refreshAllPanels();
            } catch (Exception e) {
                UIStyles.showErrorDialog(this, e.getMessage(), "Error");
            }
        }
    }

    /**
     * Downloads/displays e-ticket (simulated).
     */
    private void downloadETicket(Booking booking) {
        Flight flight = booking.getOutboundFlight();

        String ticket = String.format(
            "═══════════════════════════════════════════════════════\n" +
            "                   ✈ SKYBOOK E-TICKET ✈               \n" +
            "═══════════════════════════════════════════════════════\n\n" +
            "  BOOKING REFERENCE: %s\n\n" +
            "  PASSENGER: %s\n" +
            "  EMAIL: %s\n\n" +
            "  ─────────────────────────────────────────────────────\n" +
            "  FLIGHT DETAILS\n" +
            "  ─────────────────────────────────────────────────────\n\n" +
            "  Flight Number:    %s\n" +
            "  From:             %s\n" +
            "  To:               %s\n" +
            "  Date:             %s\n" +
            "  Status:           %s\n\n" +
            "  ─────────────────────────────────────────────────────\n" +
            "  PASSENGER PREFERENCES\n" +
            "  ─────────────────────────────────────────────────────\n\n" +
            "  Seat Preference:  %s\n" +
            "  Meal Preference:  %s\n\n" +
            "═══════════════════════════════════════════════════════\n" +
            "         Please arrive at the airport 2 hours before\n" +
            "                    your scheduled departure.\n" +
            "═══════════════════════════════════════════════════════\n",
            booking.getReference(),
            customer.getName(),
            customer.getEmail(),
            flight.getFlightNumber(),
            flight.getOrigin(),
            flight.getDestination(),
            flight.getDepartureDate().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")),
            booking.getStatusDisplay(),
            customer.getPreferredSeat().name().replace("_", " "),
            customer.getMealPreference().name()
        );

        // Show in a dialog with monospace font
        JTextArea textArea = new JTextArea(ticket);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(new Color(250, 250, 250));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 450));

        JOptionPane.showMessageDialog(this, scrollPane, "E-Ticket - " + booking.getReference(),
            JOptionPane.PLAIN_MESSAGE);
    }
}
