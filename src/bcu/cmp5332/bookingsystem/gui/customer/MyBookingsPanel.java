package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * My Bookings Panel - Redesigned to match React CustomerBookings.tsx
 * Features tabbed view with Upcoming/Completed/Cancelled bookings
 */
public class MyBookingsPanel extends JPanel {

    private final CustomerMainWindow parentWindow;
    private final FlightBookingSystem fbs;
    private final Customer customer;

    // Custom tab buttons (like React TabsList)
    private JPanel tabsPanel;
    private JButton upcomingTab;
    private JButton completedTab;
    private JButton cancelledTab;
    private String activeTab = "upcoming";

    // Content panels for each tab
    private JPanel contentPanel;
    private JPanel upcomingContainer;
    private JPanel completedContainer;
    private JPanel cancelledContainer;

    // Booking data
    private List<Booking> upcomingBookings;
    private List<Booking> completedBookings;
    private List<Booking> cancelledBookings;

    // Colors matching React design
    private static final Color TAB_BG = new Color(241, 245, 249);
    private static final Color TAB_ACTIVE_BG = Color.WHITE;
    private static final Color TAB_TEXT = new Color(100, 116, 139);
    private static final Color TAB_ACTIVE_TEXT = new Color(15, 23, 42);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color CARD_BORDER = new Color(241, 245, 249);
    private static final Color STATUS_CONFIRMED_BG = new Color(220, 252, 231);
    private static final Color STATUS_CONFIRMED_TEXT = new Color(21, 128, 61);
    private static final Color STATUS_PENDING_BG = new Color(254, 243, 199);
    private static final Color STATUS_PENDING_TEXT = new Color(180, 83, 9);
    private static final Color STATUS_CANCELLED_BG = new Color(254, 226, 226);
    private static final Color STATUS_CANCELLED_TEXT = new Color(220, 38, 38);
    private static final Color ICON_CIRCLE_BG = new Color(240, 249, 255);
    private static final Color PRICE_COLOR = new Color(15, 23, 42);
    private static final Color ACTION_TEXT = new Color(100, 116, 139);
    private static final Color CANCEL_TEXT = new Color(239, 68, 68);

    public MyBookingsPanel(CustomerMainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.fbs = parentWindow.getFlightBookingSystem();
        this.customer = parentWindow.getCurrentCustomer();

        setLayout(new BorderLayout(0, 24));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        initializeComponents();
    }

    private void initializeComponents() {
        // Header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content with tabs
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setOpaque(false);

        // Tabs panel (like React TabsList)
        tabsPanel = createTabsPanel();
        mainContent.add(tabsPanel, BorderLayout.NORTH);

        // Content panel (card list area)
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setOpaque(false);

        upcomingContainer = createContainerPanel();
        completedContainer = createContainerPanel();
        cancelledContainer = createContainerPanel();

        contentPanel.add(createScrollPane(upcomingContainer), "upcoming");
        contentPanel.add(createScrollPane(completedContainer), "completed");
        contentPanel.add(createScrollPane(cancelledContainer), "cancelled");

        mainContent.add(contentPanel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createContainerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 20, 0));
        return panel;
    }

    private JScrollPane createScrollPane(JPanel content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    /**
     * Creates the custom tabs panel matching React TabsList design
     */
    private JPanel createTabsPanel() {
        // Outer wrapper with rounded background
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false);

        // Inner tabs container with rounded gray background
        JPanel tabsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TAB_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        tabsContainer.setOpaque(false);
        tabsContainer.setBorder(new EmptyBorder(4, 4, 4, 4));

        // Create tab buttons
        upcomingTab = createTabButton("Upcoming", "upcoming", true);
        completedTab = createTabButton("Completed", "completed", false);
        cancelledTab = createTabButton("Cancelled", "cancelled", false);

        tabsContainer.add(upcomingTab);
        tabsContainer.add(completedTab);
        tabsContainer.add(cancelledTab);

        wrapper.add(tabsContainer);
        return wrapper;
    }

    private JButton createTabButton(String text, String tabId, boolean active) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean isActive = activeTab.equals(tabId);
                g2.setColor(isActive ? TAB_ACTIVE_BG : new Color(0, 0, 0, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                if (isActive) {
                    g2.setColor(new Color(0, 0, 0, 15));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        // Add clock/check/x icon based on tab
        ImageIcon icon = null;
        if (tabId.equals("upcoming") && UIStyles.ICON_CALENDAR != null) {
            icon = new ImageIcon(UIStyles.ICON_CALENDAR.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH));
        } else if (tabId.equals("completed") && UIStyles.ICON_AWARD != null) {
            icon = new ImageIcon(UIStyles.ICON_AWARD.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH));
        } else if (tabId.equals("cancelled") && UIStyles.ICON_EXIT != null) {
            icon = new ImageIcon(UIStyles.ICON_EXIT.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH));
        }
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(6);
        }

        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(active ? TAB_ACTIVE_TEXT : TAB_TEXT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));

        btn.addActionListener(e -> switchTab(tabId));

        return btn;
    }

    private void switchTab(String tabId) {
        activeTab = tabId;
        
        // Update tab button appearances
        upcomingTab.setForeground(tabId.equals("upcoming") ? TAB_ACTIVE_TEXT : TAB_TEXT);
        completedTab.setForeground(tabId.equals("completed") ? TAB_ACTIVE_TEXT : TAB_TEXT);
        cancelledTab.setForeground(tabId.equals("cancelled") ? TAB_ACTIVE_TEXT : TAB_TEXT);
        
        upcomingTab.repaint();
        completedTab.repaint();
        cancelledTab.repaint();

        // Switch content panel
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, tabId);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("View and manage your flight bookings");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        return panel;
    }

    /**
     * Refreshes all booking data and updates tab counts.
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

        // Update tab button text with counts
        upcomingTab.setText("Upcoming (" + upcomingBookings.size() + ")");
        completedTab.setText("Completed (" + completedBookings.size() + ")");
        cancelledTab.setText("Cancelled (" + cancelledBookings.size() + ")");
    }

    private void updateContainer(JPanel container, List<Booking> bookings, String type) {
        container.removeAll();

        if (bookings.isEmpty()) {
            container.add(createEmptyState(type));
        } else {
            for (Booking booking : bookings) {
                container.add(createBookingCard(booking, type));
                container.add(Box.createVerticalStrut(12));
            }
        }

        container.revalidate();
        container.repaint();
    }

    private JPanel createEmptyState(String type) {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 200));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Icon
        if (UIStyles.ICON_CALENDAR != null) {
            JLabel iconLabel = new JLabel(new ImageIcon(
                UIStyles.ICON_CALENDAR.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(iconLabel);
            content.add(Box.createVerticalStrut(16));
        }

        String message = "No " + type + " bookings found.";
        if (type.equals("upcoming")) message = "You have no upcoming flights. Start exploring!";
        else if (type.equals("completed")) message = "No completed trips yet.";
        else if (type.equals("cancelled")) message = "No cancelled bookings.";

        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msgLabel.setForeground(UIStyles.TEXT_SECONDARY);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(msgLabel);

        panel.add(content);
        return panel;
    }

    /**
     * Creates a booking card matching React BookingCard design
     */
    private JPanel createBookingCard(Booking booking, String type) {
        Flight flight = booking.getOutboundFlight();
        LocalDate today = fbs.getSystemDate();
        double price = DynamicPricing.calculatePrice(flight, today);

        // Main card with rounded corners
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout(0, 0));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setPreferredSize(new Dimension(0, 180));

        // Content wrapper with padding
        JPanel contentWrapper = new JPanel(new BorderLayout(0, 12));
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 20, 16, 20));

        // === TOP ROW: Reference + Status Badge ===
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel refLabel = new JLabel(booking.getReference());
        refLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refLabel.setForeground(TAB_ACTIVE_TEXT);
        topRow.add(refLabel, BorderLayout.WEST);

        // Status badge
        JPanel statusBadge = createStatusBadge(type, booking);
        topRow.add(statusBadge, BorderLayout.EAST);

        contentWrapper.add(topRow, BorderLayout.NORTH);

        // === MIDDLE ROW: Flight info, date, passengers, price ===
        JPanel middleRow = new JPanel(new GridBagLayout());
        middleRow.setOpaque(false);
        middleRow.setBorder(new EmptyBorder(8, 0, 8, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Plane icon circle
        JPanel iconCircle = createIconCircle();
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 16);
        middleRow.add(iconCircle, gbc);

        // Flight number + route
        JPanel flightInfo = new JPanel();
        flightInfo.setLayout(new BoxLayout(flightInfo, BoxLayout.Y_AXIS));
        flightInfo.setOpaque(false);

        JLabel flightNoLabel = new JLabel(flight.getFlightNumber());
        flightNoLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        flightNoLabel.setForeground(TAB_ACTIVE_TEXT);

        JLabel routeLabel = new JLabel(getAirportCode(flight.getOrigin()) + " → " + getAirportCode(flight.getDestination()));
        routeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        routeLabel.setForeground(TAB_TEXT);

        flightInfo.add(flightNoLabel);
        flightInfo.add(Box.createVerticalStrut(2));
        flightInfo.add(routeLabel);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(0, 0, 0, 24);
        middleRow.add(flightInfo, gbc);

        // Date with calendar icon
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        datePanel.setOpaque(false);

        if (UIStyles.ICON_CALENDAR != null) {
            JLabel calIcon = new JLabel(new ImageIcon(
                UIStyles.ICON_CALENDAR.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH)));
            datePanel.add(calIcon);
        }
        JLabel dateLabel = new JLabel(flight.getDepartureDate().format(
            DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy")) + " • 10:30");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setForeground(TAB_TEXT);
        datePanel.add(dateLabel);

        gbc.gridx = 2;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(0, 0, 0, 16);
        middleRow.add(datePanel, gbc);

        // Passengers with user icon
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        passPanel.setOpaque(false);

        if (UIStyles.ICON_USER != null) {
            JLabel userIcon = new JLabel(new ImageIcon(
                UIStyles.ICON_USER.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH)));
            passPanel.add(userIcon);
        }
        JLabel passLabel = new JLabel("1 passenger");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passLabel.setForeground(TAB_TEXT);
        passPanel.add(passLabel);

        gbc.gridx = 3;
        gbc.weightx = 0.2;
        gbc.insets = new Insets(0, 0, 0, 24);
        middleRow.add(passPanel, gbc);

        // Price
        JLabel priceLabel = new JLabel(String.format("£%.2f", price));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        priceLabel.setForeground(PRICE_COLOR);

        gbc.gridx = 4;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 0);
        middleRow.add(priceLabel, gbc);

        contentWrapper.add(middleRow, BorderLayout.CENTER);

        // === BOTTOM ROW: Action buttons ===
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomRow.setOpaque(false);
        bottomRow.setBorder(new EmptyBorder(8, 0, 0, 0));

        // View Details button
        JButton viewBtn = createActionButton("View Details", UIStyles.ICON_EYE, ACTION_TEXT, false);
        viewBtn.addActionListener(e -> showBookingDetails(booking));
        bottomRow.add(viewBtn);

        bottomRow.add(Box.createHorizontalStrut(16));

        // E-Ticket button
        JButton ticketBtn = createActionButton("E-Ticket", UIStyles.ICON_DOWNLOAD, ACTION_TEXT, false);
        ticketBtn.addActionListener(e -> downloadETicket(booking));
        bottomRow.add(ticketBtn);

        // Cancel button (only for upcoming)
        if (type.equals("upcoming")) {
            bottomRow.add(Box.createHorizontalStrut(16));
            JButton cancelBtn = createActionButton("Cancel", UIStyles.ICON_EXIT, CANCEL_TEXT, true);
            cancelBtn.addActionListener(e -> cancelBooking(booking));
            bottomRow.add(cancelBtn);
        }

        contentWrapper.add(bottomRow, BorderLayout.SOUTH);

        card.add(contentWrapper, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatusBadge(String type, Booking booking) {
        Color bgColor, textColor;
        String statusText;

        if (type.equals("cancelled")) {
            bgColor = STATUS_CANCELLED_BG;
            textColor = STATUS_CANCELLED_TEXT;
            statusText = "Cancelled";
        } else if (type.equals("completed")) {
            bgColor = TAB_BG;
            textColor = TAB_TEXT;
            statusText = "Completed";
        } else {
            // Check if pending or confirmed based on some condition
            boolean isPending = booking.getStatus() == Booking.BookingStatus.PENDING;
            if (isPending) {
                bgColor = STATUS_PENDING_BG;
                textColor = STATUS_PENDING_TEXT;
                statusText = "Pending";
            } else {
                bgColor = STATUS_CONFIRMED_BG;
                textColor = STATUS_CONFIRMED_TEXT;
                statusText = "Confirmed";
            }
        }

        final Color finalBg = bgColor;
        JPanel badge = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(finalBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        badge.setOpaque(false);

        JLabel label = new JLabel(statusText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(textColor);
        label.setBorder(new EmptyBorder(4, 12, 4, 12));
        badge.add(label);

        return badge;
    }

    private JPanel createIconCircle() {
        JPanel circle = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ICON_CIRCLE_BG);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        circle.setPreferredSize(new Dimension(44, 44));
        circle.setOpaque(false);

        if (UIStyles.ICON_PLANE != null) {
            Image scaled = UIStyles.ICON_PLANE.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            circle.add(new JLabel(new ImageIcon(scaled)));
        }

        return circle;
    }

    private JButton createActionButton(String text, ImageIcon icon, Color textColor, boolean isCancel) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(textColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(4, 0, 4, 0));

        if (icon != null) {
            // Tint icon to match text color if cancel
            Image img = icon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
            btn.setIconTextGap(6);
        }

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(isCancel ? new Color(185, 28, 28) : new Color(15, 23, 42));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(textColor);
            }
        });

        return btn;
    }

    private String getAirportCode(String location) {
        // Get first 3 letters as airport code
        if (location.length() >= 3) {
            return location.substring(0, 3).toUpperCase();
        }
        return location.toUpperCase();
    }

    private void showBookingDetails(Booking booking) {
        Flight flight = booking.getOutboundFlight();
        LocalDate today = fbs.getSystemDate();
        double price = DynamicPricing.calculatePrice(flight, today);

        // Create modal dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Booking Details", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBorder(new EmptyBorder(24, 24, 24, 24));
        content.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titleLabel = new JLabel("Booking Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel refLabel = new JLabel("Reference: " + booking.getReference());
        refLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        refLabel.setForeground(TAB_TEXT);
        header.add(titleLabel, BorderLayout.NORTH);
        header.add(refLabel, BorderLayout.SOUTH);
        content.add(header, BorderLayout.NORTH);

        // Details grid
        JPanel grid = new JPanel(new GridLayout(6, 2, 16, 12));
        grid.setOpaque(false);

        addDetailRow(grid, "Flight", flight.getFlightNumber());
        addDetailRow(grid, "Airline", "Airline");
        addDetailRow(grid, "Route", getAirportCode(flight.getOrigin()) + " → " + getAirportCode(flight.getDestination()));
        addDetailRow(grid, "Date", flight.getDepartureDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        addDetailRow(grid, "Time", "10:30 - 14:30");
        addDetailRow(grid, "Passengers", "1");

        content.add(grid, BorderLayout.CENTER);

        // Price footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(16, 0, 0, 0));

        JLabel priceTitle = new JLabel("Total Price");
        priceTitle.setForeground(TAB_TEXT);
        JLabel priceValue = new JLabel(String.format("£%.2f", price));
        priceValue.setFont(new Font("Segoe UI", Font.BOLD, 20));

        footer.add(priceTitle, BorderLayout.WEST);
        footer.add(priceValue, BorderLayout.EAST);
        content.add(footer, BorderLayout.SOUTH);

        dialog.add(content);
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel grid, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelComp.setForeground(TAB_TEXT);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueComp.setForeground(TAB_ACTIVE_TEXT);

        grid.add(labelComp);
        grid.add(valueComp);
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
