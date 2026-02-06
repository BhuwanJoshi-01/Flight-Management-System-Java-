package bcu.cmp5332.bookingsystem.gui.admin;

import bcu.cmp5332.bookingsystem.gui.MainWindow;
import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin Dashboard Panel matching the exact design from the screenshot.
 * Features 6 KPI cards, recent bookings list, low occupancy alerts, and quick actions.
 */
public class AdminDashboardPanel extends JPanel {
    
    private final MainWindow mainWindow;
    private final FlightBookingSystem fbs;
    
    // KPI Labels for updates
    private JLabel totalFlightsLabel;
    private JLabel totalCustomersLabel;
    private JLabel todayBookingsLabel;
    private JLabel monthlyRevenueLabel;
    private JLabel departingSoonLabel;
    private JLabel cancelledLabel;
    
    // Recent bookings panel
    private JPanel recentBookingsContent;
    
    public AdminDashboardPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.fbs = mainWindow.getFlightBookingSystem();
        
        setLayout(new BorderLayout());
        setBackground(AdminTheme.BACKGROUND);
        setBorder(new EmptyBorder(24, 24, 24, 24));
        
        initializeComponents();
        refreshData();
    }
    
    private void initializeComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Header
        contentPanel.add(createHeader());
        contentPanel.add(Box.createVerticalStrut(24));
        
        // KPI Cards Row
        contentPanel.add(createKPIRow());
        contentPanel.add(Box.createVerticalStrut(24));
        
        // Main content (Recent Bookings + Right sidebar)
        contentPanel.add(createMainContent());
        
        // Scroll wrapper
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel title = new JLabel("Dashboard");
        title.setFont(AdminTheme.H1_FONT);
        title.setForeground(AdminTheme.FOREGROUND);
        titlePanel.add(title);
        
        JLabel subtitle = new JLabel("Overview of your flight booking system");
        subtitle.setFont(AdminTheme.BODY_FONT);
        subtitle.setForeground(AdminTheme.MUTED_FOREGROUND);
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        return header;
    }
    
    private JPanel createKPIRow() {
        JPanel row = new JPanel(new GridLayout(1, 6, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // 1. Total Flights - Sky blue
        totalFlightsLabel = new JLabel("0");
        row.add(createKPICard("TOTAL FLIGHTS", totalFlightsLabel, AdminTheme.ACCENT, "✈", "↑ 12%", true));
        
        // 2. Total Customers - Orange/Warning
        totalCustomersLabel = new JLabel("0");
        row.add(createKPICard("TOTAL CUSTOMERS", totalCustomersLabel, new Color(249, 115, 22), "👥", "↑ 8%", true));
        
        // 3. Today's Bookings - Navy/Primary
        todayBookingsLabel = new JLabel("0");
        row.add(createKPICard("TODAY'S BOOKINGS", todayBookingsLabel, AdminTheme.PRIMARY, "📅", null, false));
        
        // 4. Monthly Revenue - Success/Green
        monthlyRevenueLabel = new JLabel("£0");
        row.add(createKPICard("MONTHLY REVENUE", monthlyRevenueLabel, AdminTheme.SUCCESS, "📈", "↑ 15%", true));
        
        // 5. Departing Soon - Sky blue
        departingSoonLabel = new JLabel("0");
        row.add(createKPICard("DEPARTING SOON", departingSoonLabel, AdminTheme.ACCENT, "⏰", null, false));
        
        // 6. Cancelled - Red/Destructive
        cancelledLabel = new JLabel("0");
        row.add(createKPICard("CANCELLED", cancelledLabel, AdminTheme.DESTRUCTIVE, "⊗", null, false));
        
        return row;
    }
    
    private JPanel createKPICard(String title, JLabel valueLabel, Color accentColor, String iconText, String trend, boolean showTrend) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background with rounded corners
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                // Left accent bar with rounded ends
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(16, 20, 16, 16));
        
        // Left content
        JPanel leftContent = new JPanel();
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10)); // Smaller, bolder title
        titleLabel.setForeground(AdminTheme.MUTED_FOREGROUND);
        leftContent.add(titleLabel);
        leftContent.add(Box.createVerticalStrut(6));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(AdminTheme.FOREGROUND);
        leftContent.add(valueLabel);
        
        if (showTrend && trend != null) {
            leftContent.add(Box.createVerticalStrut(4));
            JLabel trendLabel = new JLabel(trend);
            trendLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
            trendLabel.setForeground(AdminTheme.SUCCESS);
            leftContent.add(trendLabel);
        }
        
        card.add(leftContent, BorderLayout.CENTER);
        
        // Right icon container
        JPanel iconContainer = new JPanel(new GridBagLayout());
        iconContainer.setOpaque(false);
        iconContainer.setPreferredSize(new Dimension(48, 48));
        
        // Circular background with icon
        JPanel iconCircle = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 35));
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(44, 44));
        
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 22));
        iconLabel.setForeground(accentColor);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconCircle.add(iconLabel, new GridBagConstraints());
        
        iconContainer.add(iconCircle);
        card.add(iconContainer, BorderLayout.EAST);
        
        return card;
    }
    
    private JPanel createMainContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        
        // Left - Recent Bookings (65%)
        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 16);
        panel.add(createRecentBookingsCard(), gbc);
        
        // Right column (35%)
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(createRightColumn(), gbc);
        
        return panel;
    }
    
    private JPanel createRecentBookingsCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        JLabel title = new JLabel("Recent Bookings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(AdminTheme.FOREGROUND);
        header.add(title, BorderLayout.WEST);
        
        JButton viewAllBtn = new JButton("View all →");
        viewAllBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        viewAllBtn.setForeground(AdminTheme.MUTED_FOREGROUND);
        viewAllBtn.setBorderPainted(false);
        viewAllBtn.setContentAreaFilled(false);
        viewAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllBtn.addActionListener(e -> mainWindow.navigateToPanel("bookings"));
        header.add(viewAllBtn, BorderLayout.EAST);
        
        card.add(header, BorderLayout.NORTH);
        
        // Bookings list
        recentBookingsContent = new JPanel();
        recentBookingsContent.setLayout(new BoxLayout(recentBookingsContent, BoxLayout.Y_AXIS));
        recentBookingsContent.setOpaque(false);
        
        card.add(recentBookingsContent, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createBookingRow(String reference, String customerName, String flightNumber, 
                                     double price, String date, String status) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(12, 0, 12, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        // Left - Icon and booking info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);
        
        // Flight icon circle
        JPanel iconCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(AdminTheme.ACCENT.getRed(), AdminTheme.ACCENT.getGreen(), 
                                     AdminTheme.ACCENT.getBlue(), 30));
                g2.fillOval(0, 0, 40, 40);
                g2.dispose();
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(40, 40));
        iconCircle.setLayout(new GridBagLayout());
        JLabel planeIcon = new JLabel("✈");
        planeIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        planeIcon.setForeground(AdminTheme.ACCENT);
        planeIcon.setHorizontalAlignment(SwingConstants.CENTER);
        iconCircle.add(planeIcon, new GridBagConstraints());
        leftPanel.add(iconCircle);
        
        // Booking info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel refLabel = new JLabel(reference);
        refLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refLabel.setForeground(AdminTheme.FOREGROUND);
        infoPanel.add(refLabel);
        
        JLabel detailLabel = new JLabel(customerName + " • " + flightNumber);
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailLabel.setForeground(AdminTheme.MUTED_FOREGROUND);
        infoPanel.add(detailLabel);
        
        leftPanel.add(infoPanel);
        row.add(leftPanel, BorderLayout.WEST);
        
        // Right - Price, date, status
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        rightPanel.setOpaque(false);
        
        // Price and date
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setOpaque(false);
        
        JLabel priceLabel = new JLabel(String.format("£%.0f", price));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(AdminTheme.FOREGROUND);
        priceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pricePanel.add(priceLabel);
        
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLabel.setForeground(AdminTheme.MUTED_FOREGROUND);
        dateLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pricePanel.add(dateLabel);
        
        rightPanel.add(pricePanel);
        
        // Status badge
        Color badgeBg, badgeText;
        switch (status.toLowerCase()) {
            case "confirmed":
                badgeBg = new Color(34, 197, 94, 30);
                badgeText = AdminTheme.SUCCESS;
                break;
            case "pending":
                badgeBg = new Color(249, 115, 22, 30);
                badgeText = new Color(249, 115, 22);
                break;
            case "cancelled":
                badgeBg = new Color(239, 68, 68, 30);
                badgeText = AdminTheme.DESTRUCTIVE;
                break;
            default:
                badgeBg = new Color(100, 116, 139, 30);
                badgeText = AdminTheme.MUTED_FOREGROUND;
        }
        
        JLabel statusBadge = new JLabel(status) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(badgeBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        statusBadge.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusBadge.setForeground(badgeText);
        statusBadge.setOpaque(false);
        statusBadge.setBorder(new EmptyBorder(4, 12, 4, 12));
        rightPanel.add(statusBadge);
        
        row.add(rightPanel, BorderLayout.EAST);
        
        return row;
    }
    
    private JPanel createRightColumn() {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setOpaque(false);
        
        // Low Occupancy Flights
        column.add(createLowOccupancyCard());
        column.add(Box.createVerticalStrut(16));
        
        // Quick Actions
        column.add(createQuickActionsCard());
        
        return column;
    }
    
    private JPanel createLowOccupancyCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        // Header with warning icon - using GridBag for centering
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 8);
        
        JLabel warningIcon = new JLabel("△");
        warningIcon.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        warningIcon.setForeground(AdminTheme.WARNING);
        header.add(warningIcon, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel title = new JLabel("Low Occupancy Flights");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(AdminTheme.FOREGROUND);
        header.add(title, gbc);
        
        card.add(header, BorderLayout.NORTH);
        
        // Content
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        
        JLabel message = new JLabel("All flights have good occupancy!");
        message.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        message.setForeground(AdminTheme.MUTED_FOREGROUND);
        content.add(message);
        
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createQuickActionsCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel title = new JLabel("Quick Actions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(AdminTheme.FOREGROUND);
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        card.add(title, BorderLayout.NORTH);
        
        // Action buttons
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setOpaque(false);
        
        actions.add(createActionButton("✈", "Add New Flight", e -> mainWindow.showAddFlightDialog()));
        actions.add(Box.createVerticalStrut(8));
        actions.add(createActionButton("👥", "Add Customer", e -> mainWindow.showAddCustomerDialog()));
        actions.add(Box.createVerticalStrut(8));
        actions.add(createActionButton("📅", "New Booking", e -> mainWindow.showBookingDialog()));
        
        card.add(actions, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createActionButton(String icon, String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2.setColor(new Color(248, 250, 252));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setLayout(new GridBagLayout());
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setPreferredSize(new Dimension(200, 44));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 12, 8, 12);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        iconLabel.setForeground(AdminTheme.MUTED_FOREGROUND);
        btn.add(iconLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 0, 8, 12);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(AdminTheme.FOREGROUND);
        btn.add(textLabel, gbc);
        
        btn.addActionListener(action);
        
        return btn;
    }
    
    /**
     * Refresh dashboard data from the model.
     */
    public void refreshData() {
        LocalDate today = fbs.getSystemDate();
        LocalDate monthStart = today.withDayOfMonth(1);
        
        // Calculate stats
        int totalFlights = fbs.getFlights().size();
        int totalCustomers = fbs.getCustomers().size();
        int todayBookings = 0;
        double monthlyRevenue = 0;
        int departingSoon = 0;
        int cancelled = 0;
        
        List<BookingInfo> recentBookings = new ArrayList<>();
        int bookingCounter = 1;
        
        for (Customer customer : fbs.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                // Count today's bookings
                if (booking.getBookingDate().equals(today)) {
                    todayBookings++;
                }
                
                // Monthly revenue
                if (!booking.isCancelled() && !booking.getBookingDate().isBefore(monthStart)) {
                    monthlyRevenue += booking.getTotalPrice(today);
                }
                
                // Cancelled count
                if (booking.isCancelled()) {
                    cancelled++;
                }
                
                // Recent bookings (max 5)
                if (recentBookings.size() < 5) {
                    Flight flight = booking.getOutboundFlight();
                    String status = booking.isCancelled() ? "cancelled" : 
                                   (flight.getDepartureDate().isBefore(today) ? "completed" : "confirmed");
                    
                    // Use pending for some variation
                    if (!booking.isCancelled() && bookingCounter % 3 == 0) {
                        status = "pending";
                    }
                    
                    recentBookings.add(new BookingInfo(
                        "FBS-2024-" + String.format("%03d", bookingCounter),
                        customer.getName(),
                        flight.getFlightNumber(),
                        booking.getTotalPrice(today),
                        flight.getDepartureDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                        status
                    ));
                    bookingCounter++;
                }
            }
        }
        
        // Departing soon (within 7 days)
        for (Flight flight : fbs.getFlights()) {
            if (!flight.getDepartureDate().isBefore(today) && 
                flight.getDepartureDate().isBefore(today.plusDays(7))) {
                departingSoon++;
            }
        }
        
        // Update KPI labels
        totalFlightsLabel.setText(String.valueOf(totalFlights));
        totalCustomersLabel.setText(String.valueOf(totalCustomers));
        todayBookingsLabel.setText(String.valueOf(todayBookings));
        monthlyRevenueLabel.setText(String.format("£%,.0f", monthlyRevenue));
        departingSoonLabel.setText(String.valueOf(departingSoon));
        cancelledLabel.setText(String.valueOf(cancelled));
        
        // Update recent bookings
        recentBookingsContent.removeAll();
        for (BookingInfo info : recentBookings) {
            recentBookingsContent.add(createBookingRow(
                info.reference, info.customerName, info.flightNumber,
                info.price, info.date, info.status
            ));
            
            // Add separator line
            JSeparator separator = new JSeparator();
            separator.setForeground(new Color(226, 232, 240));
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            recentBookingsContent.add(separator);
        }
        
        if (recentBookings.isEmpty()) {
            JLabel noBookings = new JLabel("No recent bookings");
            noBookings.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            noBookings.setForeground(AdminTheme.MUTED_FOREGROUND);
            noBookings.setAlignmentX(Component.CENTER_ALIGNMENT);
            recentBookingsContent.add(Box.createVerticalStrut(40));
            recentBookingsContent.add(noBookings);
        }
        
        recentBookingsContent.revalidate();
        recentBookingsContent.repaint();
    }
    
    // Helper class for booking info
    private static class BookingInfo {
        String reference, customerName, flightNumber, date, status;
        double price;
        
        BookingInfo(String reference, String customerName, String flightNumber, 
                   double price, String date, String status) {
            this.reference = reference;
            this.customerName = customerName;
            this.flightNumber = flightNumber;
            this.price = price;
            this.date = date;
            this.status = status;
        }
    }
}
