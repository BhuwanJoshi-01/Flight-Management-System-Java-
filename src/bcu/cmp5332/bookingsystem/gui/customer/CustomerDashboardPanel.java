package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Customer Dashboard Panel showing KPIs, upcoming flights, and quick actions.
 */
public class CustomerDashboardPanel extends JPanel {

    private final CustomerMainWindow parentWindow;
    private final Customer customer;
    private final FlightBookingSystem fbs;

    // KPI components
    private JLabel upcomingFlightsValue;
    private JLabel totalBookingsValue;
    private JLabel rewardPointsValue;
    private JLabel tierValue;

    // Lists
    private JPanel upcomingFlightsPanel;
    private JPanel featuredFlightsPanel;

    public CustomerDashboardPanel(CustomerMainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.customer = parentWindow.getCurrentCustomer();
        this.fbs = parentWindow.getFlightBookingSystem();

        setLayout(new BorderLayout(0, 20));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeComponents();
        refreshData();
    }

    private void initializeComponents() {
        // Main content panel with scroll
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(0, 0, 40, 0));

        // Welcome header
        JPanel welcomePanel = createWelcomePanel();
        welcomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(welcomePanel);
        mainContent.add(Box.createVerticalStrut(25));

        // KPI Cards
        JPanel kpiPanel = createKPIPanel();
        kpiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(kpiPanel);
        mainContent.add(Box.createVerticalStrut(30));

        // Quick Search panel
        JPanel quickSearchPanel = createQuickSearchPanel();
        quickSearchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(quickSearchPanel);
        mainContent.add(Box.createVerticalStrut(35));

        // Upcoming Trips
        JPanel upcomingPanel = createUpcomingTripsPanel();
        upcomingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(upcomingPanel);
        mainContent.add(Box.createVerticalStrut(35));

        // Featured Flights
        JPanel featuredPanel = createFeaturedFlightsPanel();
        featuredPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(featuredPanel);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1400, 65));

        // Left side - welcome message
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        String firstName = customer.getName().split(" ")[0];
        JLabel welcomeLabel = new JLabel("Welcome back, " + firstName + "! ✈");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(UIStyles.TEXT_PRIMARY);
        leftPanel.add(welcomeLabel);

        JLabel subtitleLabel = new JLabel("Ready for your next adventure?");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        leftPanel.add(subtitleLabel);

        // Right side - search button
        JButton searchButton = new JButton("⌕  Search Flights") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIStyles.PRIMARY_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setPreferredSize(new Dimension(180, 48));
        searchButton.addActionListener(e -> parentWindow.showPanel("search"));

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(searchButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createKPIPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 18, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1400, 130));

        // Upcoming Flights KPI
        upcomingFlightsValue = new JLabel("0");
        JPanel upcomingKPI = createKPICard("UPCOMING FLIGHTS", upcomingFlightsValue, UIStyles.ICON_PLANE, new Color(14, 165, 233));
        panel.add(upcomingKPI);

        // Total Bookings KPI
        totalBookingsValue = new JLabel("0");
        JPanel bookingsKPI = createKPICard("TOTAL BOOKINGS", totalBookingsValue, UIStyles.ICON_CALENDAR, new Color(30, 41, 59));
        panel.add(bookingsKPI);

        // Reward Points KPI
        rewardPointsValue = new JLabel("0");
        JPanel pointsKPI = createKPICard("REWARD POINTS", rewardPointsValue, UIStyles.ICON_AWARD, new Color(245, 158, 11));
        panel.add(pointsKPI);

        // Saved Flights KPI
        tierValue = new JLabel("3");
        JPanel tierKPI = createKPICard("SAVED FLIGHTS", tierValue, UIStyles.ICON_HEART, new Color(34, 197, 94));
        panel.add(tierKPI);

        return panel;
    }

    private JPanel createKPICard(String title, JLabel valueLabel, ImageIcon icon, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card background and shadow
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                
                // Left accent bar
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, 4, getHeight(), 0, 0);
                
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top Row - Label and Icon
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        topRow.add(titleLabel, BorderLayout.WEST);

        // Icon Box
        JPanel iconBox = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(36, 36));
        
        if (icon != null) {
            ImageIcon tintedIcon = UIStyles.tintIcon(icon, accentColor);
            ImageIcon scaledIcon = UIStyles.scaleIcon(tintedIcon, 20, 20);
            iconBox.add(new JLabel(scaledIcon));
        }
        
        topRow.add(iconBox, BorderLayout.EAST);

        // Bottom - Value
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(UIStyles.TEXT_PRIMARY);

        card.add(topRow, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.WEST);

        return card;
    }

    private JPanel createQuickSearchPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background: gray-blue to dark-blue
                GradientPaint gp = new GradientPaint(0, 0, new Color(52, 73, 94), getWidth(), 0, new Color(30, 41, 59));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        panel.setMaximumSize(new Dimension(1400, 130));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 0, 8);

        // From field
        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(createSearchFieldPanel("From", "Origin city"), gbc);

        // To field
        gbc.gridx = 1;
        panel.add(createSearchFieldPanel("To", "Destination city"), gbc);

        // Date field
        gbc.gridx = 2;
        panel.add(createSearchFieldPanel("Date", "mm/dd/yyyy"), gbc);

        // Search button
        gbc.gridx = 3;
        gbc.weightx = 0;
        JButton searchBtn = new JButton("Search Flights  →") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(14, 165, 233), getWidth(), 0, new Color(56, 189, 248));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setContentAreaFilled(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(180, 50));
        searchBtn.addActionListener(e -> parentWindow.showPanel("search"));
        panel.add(searchBtn, gbc);

        return panel;
    }

    private JPanel createSearchFieldPanel(String label, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fieldLabel.setForeground(new Color(200, 220, 255));
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(fieldLabel);
        panel.add(Box.createVerticalStrut(6));

        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setFont(UIStyles.BODY_FONT);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setOpaque(false);
        field.setPreferredSize(new Dimension(150, 48));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        field.setBorder(new EmptyBorder(0, 15, 0, 15));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(field);

        return panel;
    }

    private JPanel createUpcomingTripsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1400, 500));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 5, 20, 5));

        JLabel titleLabel = new JLabel("Upcoming Trips");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);

        JButton viewAllBtn = new JButton("View all  →");
        viewAllBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        viewAllBtn.setForeground(UIStyles.TEXT_SECONDARY);
        viewAllBtn.setBorderPainted(false);
        viewAllBtn.setContentAreaFilled(false);
        viewAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllBtn.addActionListener(e -> parentWindow.showPanel("bookings"));

        header.add(titleLabel, BorderLayout.WEST);
        header.add(viewAllBtn, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // Content
        upcomingFlightsPanel = new JPanel();
        upcomingFlightsPanel.setLayout(new BoxLayout(upcomingFlightsPanel, BoxLayout.Y_AXIS));
        upcomingFlightsPanel.setOpaque(false);
        panel.add(upcomingFlightsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFeaturedFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1400, 500));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 5, 20, 5));

        JLabel titleLabel = new JLabel("Featured Flights");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);

        JButton seeMoreBtn = new JButton("See more  →");
        seeMoreBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        seeMoreBtn.setForeground(UIStyles.TEXT_SECONDARY);
        seeMoreBtn.setBorderPainted(false);
        seeMoreBtn.setContentAreaFilled(false);
        seeMoreBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        seeMoreBtn.addActionListener(e -> parentWindow.showPanel("search"));

        header.add(titleLabel, BorderLayout.WEST);
        header.add(seeMoreBtn, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // Content
        featuredFlightsPanel = new JPanel();
        featuredFlightsPanel.setLayout(new BoxLayout(featuredFlightsPanel, BoxLayout.Y_AXIS));
        featuredFlightsPanel.setOpaque(false);
        panel.add(featuredFlightsPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Refreshes all dashboard data.
     */
    public void refreshData() {
        LocalDate today = fbs.getSystemDate();

        // Calculate KPIs
        List<Booking> upcomingBookings = customer.getBookings().stream()
            .filter(b -> !b.isCancelled() && b.getOutboundFlight().getDepartureDate().isAfter(today))
            .collect(Collectors.toList());

        int totalBookings = customer.getTotalBookings();
        int points = customer.getRewardPoints();
        String tier = customer.getTier().name();

        // Update KPI values
        upcomingFlightsValue.setText(String.valueOf(upcomingBookings.size()));
        totalBookingsValue.setText(String.valueOf(totalBookings));
        rewardPointsValue.setText(String.format("%,d", points));
        tierValue.setText(tier);

        // Update upcoming trips list
        updateUpcomingTrips(upcomingBookings);

        // Update featured flights
        updateFeaturedFlights();
    }

    private void updateUpcomingTrips(List<Booking> upcomingBookings) {
        upcomingFlightsPanel.removeAll();

        if (upcomingBookings.isEmpty()) {
            JLabel emptyLabel = new JLabel("No upcoming trips. Start exploring!");
            emptyLabel.setFont(UIStyles.BODY_FONT);
            emptyLabel.setForeground(UIStyles.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            upcomingFlightsPanel.add(Box.createVerticalStrut(30));
            upcomingFlightsPanel.add(emptyLabel);
        } else {
            // Show up to 3 upcoming trips
            int count = 0;
            for (Booking booking : upcomingBookings) {
                if (count >= 3) break;
                upcomingFlightsPanel.add(createBookingCard(booking));
                upcomingFlightsPanel.add(Box.createVerticalStrut(10));
                count++;
            }
        }

        upcomingFlightsPanel.revalidate();
        upcomingFlightsPanel.repaint();
    }

    private void updateFeaturedFlights() {
        featuredFlightsPanel.removeAll();

        LocalDate today = fbs.getSystemDate();

        // Get upcoming flights that have available seats
        List<Flight> availableFlights = fbs.getFlights().stream()
            .filter(f -> f.getDepartureDate().isAfter(today))
            .filter(f -> f.getPassengers().size() < f.getCapacity())
            .sorted((f1, f2) -> f1.getDepartureDate().compareTo(f2.getDepartureDate()))
            .limit(3)
            .collect(Collectors.toList());

        if (availableFlights.isEmpty()) {
            JLabel emptyLabel = new JLabel("No flights available at the moment.");
            emptyLabel.setFont(UIStyles.BODY_FONT);
            emptyLabel.setForeground(UIStyles.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            featuredFlightsPanel.add(Box.createVerticalStrut(30));
            featuredFlightsPanel.add(emptyLabel);
        } else {
            for (Flight flight : availableFlights) {
                featuredFlightsPanel.add(createFlightCard(flight));
                featuredFlightsPanel.add(Box.createVerticalStrut(10));
            }
        }

        featuredFlightsPanel.revalidate();
        featuredFlightsPanel.repaint();
    }

    private JPanel createBookingCard(Booking booking) {
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
        card.setMaximumSize(new Dimension(1400, 160));

        Flight flight = booking.getOutboundFlight();

        // Top Row: Booking ID and Status
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel idLabel = new JLabel("FBS-" + booking.getOutboundFlight().getDepartureDate().getYear() + "-" + String.format("%03d", booking.getOutboundFlight().getId()));
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(UIStyles.TEXT_PRIMARY);

        JPanel statusWrapper = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 252, 231));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        statusWrapper.setOpaque(false);
        JLabel statusLabel = new JLabel("Confirmed");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusLabel.setForeground(new Color(21, 128, 61));
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
            JLabel planeIcon = new JLabel(new ImageIcon(scaled));
            iconCircle.add(planeIcon);
        }

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 15);
        middleRow.add(iconCircle, gbc);

        // Flight details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        JLabel flightNo = new JLabel(flight.getFlightNumber());
        flightNo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        flightNo.setForeground(UIStyles.TEXT_PRIMARY);
        
        JLabel route = new JLabel(flight.getOrigin() + " → " + flight.getDestination());
        route.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        route.setForeground(UIStyles.TEXT_SECONDARY);

        detailsPanel.add(flightNo);
        detailsPanel.add(route);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        middleRow.add(detailsPanel, gbc);

        // Date/Time
        JLabel dateLabel = new JLabel(" " + flight.getDepartureDate().format(DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy • HH:mm")));
        if (UIStyles.ICON_CALENDAR != null) {
            Image scaled = UIStyles.ICON_CALENDAR.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            dateLabel.setIcon(new ImageIcon(scaled));
        }
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        gbc.gridx = 2;
        gbc.weightx = 0.4;
        middleRow.add(dateLabel, gbc);

        // Passengers
        JLabel passLabel = new JLabel(" 1 passenger");
        if (UIStyles.ICON_USER != null) {
            Image scaled = UIStyles.ICON_USER.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            passLabel.setIcon(new ImageIcon(scaled));
        }
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passLabel.setForeground(UIStyles.TEXT_SECONDARY);

        gbc.gridx = 3;
        gbc.weightx = 0.1;
        middleRow.add(passLabel, gbc);

        // Price
        JLabel priceLabel = new JLabel("£" + String.format("%.2f", flight.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(UIStyles.TEXT_PRIMARY);
        gbc.gridx = 4;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        middleRow.add(priceLabel, gbc);

        // Bottom Row: Action Links
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        bottomRow.setOpaque(false);
        bottomRow.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton viewDetails = createActionLink("View Details", UIStyles.TEXT_PRIMARY);
        if (UIStyles.ICON_EYE != null) {
            Image scaled = UIStyles.ICON_EYE.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            viewDetails.setIcon(new ImageIcon(scaled));
        }

        JButton eTicket = createActionLink("E-Ticket", UIStyles.TEXT_PRIMARY);
        if (UIStyles.ICON_DOWNLOAD != null) {
            Image scaled = UIStyles.ICON_DOWNLOAD.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            eTicket.setIcon(new ImageIcon(scaled));
        }

        JButton cancel = createActionLink("Cancel", new Color(220, 38, 38));
        if (UIStyles.ICON_EXIT != null) {
            Image scaled = UIStyles.ICON_EXIT.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            cancel.setIcon(new ImageIcon(scaled));
        }
        
        cancel.addActionListener(e -> {
            int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this booking?", "Cancel Booking", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                // Actual cancellation logic would go here
                try {
                    // booking.cancel(); 
                    // parentWindow.getFlightBookingSystem().store();
                    refreshData();
                } catch (Exception ex) {}
            }
        });

        bottomRow.add(viewDetails);
        bottomRow.add(eTicket);
        bottomRow.add(cancel);

        card.add(topRow, BorderLayout.NORTH);
        card.add(middleRow, BorderLayout.CENTER);
        card.add(bottomRow, BorderLayout.SOUTH);

        return card;
    }

    private JButton createActionLink(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(color);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0, 0, 0, 0));
        return btn;
    }

    private JPanel createFlightCard(Flight flight) {
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
        card.setLayout(new GridBagLayout());
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setMaximumSize(new Dimension(1400, 100));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
            JLabel planeIcon = new JLabel(new ImageIcon(scaled));
            iconCircle.add(planeIcon);
        }

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 15);
        card.add(iconCircle, gbc);

        // Left info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel routeLabel = new JLabel(flight.getOrigin() + " → " + flight.getDestination());
        routeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        routeLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JLabel detailsLabel = new JLabel(flight.getFlightNumber() + " • " +
            flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsLabel.setForeground(UIStyles.TEXT_SECONDARY);

        infoPanel.add(routeLabel);
        infoPanel.add(detailsLabel);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        card.add(infoPanel, gbc);

        // Right details
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        double price = DynamicPricing.calculatePrice(flight, fbs.getSystemDate());
        JLabel priceLabel = new JLabel("£" + String.format("%.2f", price));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(UIStyles.PRIMARY_COLOR);
        rightPanel.add(priceLabel);

        JButton bookBtn = new JButton("Book Now") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 249, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bookBtn.setForeground(UIStyles.PRIMARY_COLOR);
        bookBtn.setFocusPainted(false);
        bookBtn.setBorderPainted(false);
        bookBtn.setContentAreaFilled(false);
        bookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookBtn.setPreferredSize(new Dimension(100, 32));
        
        rightPanel.add(bookBtn);

        gbc.gridx = 2;
        gbc.weightx = 0;
        card.add(rightPanel, gbc);

        // Click interaction
        java.awt.event.MouseAdapter ma = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                parentWindow.showPanel("search");
            }
        };
        card.addMouseListener(ma);

        return card;
    }
}
