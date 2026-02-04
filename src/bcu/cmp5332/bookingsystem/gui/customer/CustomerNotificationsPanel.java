package bcu.cmp5332.bookingsystem.gui.customer;

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
 * Customer Notifications Panel displaying booking confirmations, flight updates, and promotional messages.
 */
public class CustomerNotificationsPanel extends JPanel {

    private final CustomerMainWindow parentWindow;
    private final FlightBookingSystem fbs;
    private final Customer customer;

    // Notification list
    private JPanel notificationsList;
    private List<Notification> notifications;

    // Filter state
    private String currentFilter = "all";

    /**
     * Simple notification data class.
     */
    private static class Notification {
        enum Type { BOOKING, FLIGHT_UPDATE, PROMO, SYSTEM }

        String id;
        Type type;
        String title;
        String message;
        boolean isRead;
        LocalDate createdAt;

        Notification(String id, Type type, String title, String message, LocalDate createdAt) {
            this.id = id;
            this.type = type;
            this.title = title;
            this.message = message;
            this.isRead = false;
            this.createdAt = createdAt;
        }
    }

    public CustomerNotificationsPanel(CustomerMainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.fbs = parentWindow.getFlightBookingSystem();
        this.customer = parentWindow.getCurrentCustomer();
        this.notifications = new ArrayList<>();

        setLayout(new BorderLayout(0, 20));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeComponents();
        generateNotifications();
        refreshNotificationsList();
    }

    private void initializeComponents() {
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content
        JPanel mainPanel = UIStyles.createCard();
        mainPanel.setLayout(new BorderLayout());

        // Filter buttons
        JPanel filterPanel = createFilterPanel();
        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // Notifications list
        notificationsList = new JPanel();
        notificationsList.setLayout(new BoxLayout(notificationsList, BoxLayout.Y_AXIS));
        notificationsList.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(notificationsList);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Notifications");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        long unreadCount = notifications.stream().filter(n -> !n.isRead).count();
        JLabel subtitleLabel = new JLabel(unreadCount > 0
            ? "You have " + unreadCount + " unread notifications"
            : "All caught up!");
        subtitleLabel.setFont(UIStyles.BODY_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Mark all as read button
        if (unreadCount > 0) {
            JButton markAllBtn = UIStyles.createSecondaryButton("Mark all as read");
            markAllBtn.addActionListener(e -> markAllAsRead());
            panel.add(markAllBtn, BorderLayout.EAST);
        }

        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton allBtn = createFilterButton("All", "all");
        JButton unreadBtn = createFilterButton("Unread", "unread");

        panel.add(allBtn);
        panel.add(unreadBtn);

        return panel;
    }

    private JButton createFilterButton(String text, String filter) {
        JButton btn = new JButton(text);
        btn.setFont(UIStyles.BODY_FONT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (currentFilter.equals(filter)) {
            btn.setBackground(UIStyles.PRIMARY_COLOR);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(240, 240, 240));
            btn.setForeground(UIStyles.TEXT_PRIMARY);
        }

        btn.addActionListener(e -> {
            currentFilter = filter;
            refreshNotificationsList();
        });

        return btn;
    }

    /**
     * Generates notifications based on customer's bookings and system events.
     */
    private void generateNotifications() {
        notifications.clear();
        LocalDate today = fbs.getSystemDate();
        int id = 1;

        // Generate booking confirmation notifications
        for (Booking booking : customer.getBookings()) {
            Flight flight = booking.getOutboundFlight();

            // Booking confirmation
            notifications.add(new Notification(
                String.valueOf(id++),
                Notification.Type.BOOKING,
                "Booking Confirmed - " + booking.getReference(),
                String.format("Your flight %s from %s to %s on %s has been confirmed.",
                    flight.getFlightNumber(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getDepartureDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))),
                booking.getBookingDate()
            ));

            // Flight reminder (if departure is within 3 days)
            long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(today, flight.getDepartureDate());
            if (daysUntil > 0 && daysUntil <= 3 && !booking.isCancelled()) {
                notifications.add(new Notification(
                    String.valueOf(id++),
                    Notification.Type.FLIGHT_UPDATE,
                    "Flight Reminder - " + flight.getFlightNumber(),
                    String.format("Your flight to %s departs in %d day(s). Please check in online.",
                        flight.getDestination(), daysUntil),
                    today
                ));
            }

            // Cancellation notification
            if (booking.isCancelled()) {
                notifications.add(new Notification(
                    String.valueOf(id++),
                    Notification.Type.SYSTEM,
                    "Booking Cancelled - " + booking.getReference(),
                    String.format("Your booking for flight %s has been cancelled. Cancellation fee: £%.2f",
                        flight.getFlightNumber(), booking.getCancellationFee()),
                    today
                ));
            }
        }

        // Welcome notification
        notifications.add(new Notification(
            String.valueOf(id++),
            Notification.Type.SYSTEM,
            "Welcome to SkyBook! ✈",
            "Thank you for joining SkyBook. Start exploring flights and book your next adventure!",
            customer.getCreatedAt() != null ? customer.getCreatedAt() : today
        ));

        // Promotional notification
        notifications.add(new Notification(
            String.valueOf(id++),
            Notification.Type.PROMO,
            "Earn Double Reward Points!",
            "Book any flight this week and earn double reward points. Use them for discounts on future bookings!",
            today.minusDays(1)
        ));

        // Tier upgrade notification if applicable
        if (customer.getTier() != Customer.CustomerTier.BRONZE) {
            notifications.add(new Notification(
                String.valueOf(id++),
                Notification.Type.SYSTEM,
                "Congratulations on your " + customer.getTier().name() + " Status! 🎉",
                "You've earned " + customer.getTier().name() + " tier status! Enjoy exclusive benefits on your bookings.",
                today.minusDays(7)
            ));
        }

        // Sort by date (newest first)
        notifications.sort((a, b) -> b.createdAt.compareTo(a.createdAt));
    }

    private void refreshNotificationsList() {
        notificationsList.removeAll();

        List<Notification> filtered = notifications.stream()
            .filter(n -> currentFilter.equals("all") || !n.isRead)
            .collect(java.util.stream.Collectors.toList());

        if (filtered.isEmpty()) {
            JLabel emptyLabel = new JLabel(currentFilter.equals("unread")
                ? "No unread notifications"
                : "No notifications yet");
            emptyLabel.setFont(UIStyles.BODY_FONT);
            emptyLabel.setForeground(UIStyles.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setBorder(new EmptyBorder(50, 0, 50, 0));
            notificationsList.add(emptyLabel);
        } else {
            for (Notification notification : filtered) {
                notificationsList.add(createNotificationCard(notification));
                notificationsList.add(Box.createVerticalStrut(10));
            }
        }

        notificationsList.revalidate();
        notificationsList.repaint();
    }

    private JPanel createNotificationCard(Notification notification) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(UIStyles.CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(notification.isRead
                ? new Color(220, 220, 220)
                : UIStyles.PRIMARY_COLOR, notification.isRead ? 1 : 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Left - icon
        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));

        switch (notification.type) {
            case BOOKING:
                iconLabel.setText("✈");
                iconLabel.setBackground(new Color(46, 204, 113, 30));
                iconLabel.setForeground(UIStyles.ACCENT_COLOR);
                break;
            case FLIGHT_UPDATE:
                iconLabel.setText("🔔");
                iconLabel.setBackground(new Color(243, 156, 18, 30));
                iconLabel.setForeground(new Color(243, 156, 18));
                break;
            case PROMO:
                iconLabel.setText("🏷");
                iconLabel.setBackground(new Color(41, 128, 185, 30));
                iconLabel.setForeground(UIStyles.PRIMARY_COLOR);
                break;
            default:
                iconLabel.setText("⚙");
                iconLabel.setBackground(new Color(128, 128, 128, 30));
                iconLabel.setForeground(UIStyles.TEXT_SECONDARY);
        }

        card.add(iconLabel, BorderLayout.WEST);

        // Center - content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Title with badge
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setOpaque(false);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(notification.title);
        titleLabel.setFont(UIStyles.SUBHEADER_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        if (!notification.isRead) {
            JLabel newBadge = new JLabel("New");
            newBadge.setFont(new Font("Segoe UI", Font.BOLD, 9));
            newBadge.setForeground(Color.WHITE);
            newBadge.setOpaque(true);
            newBadge.setBackground(UIStyles.PRIMARY_COLOR);
            newBadge.setBorder(new EmptyBorder(2, 6, 2, 6));
            titlePanel.add(newBadge);
        }

        contentPanel.add(titlePanel);
        contentPanel.add(Box.createVerticalStrut(5));

        // Message
        JLabel messageLabel = new JLabel("<html><body style='width: 400px'>" + notification.message + "</body></html>");
        messageLabel.setFont(UIStyles.BODY_FONT);
        messageLabel.setForeground(UIStyles.TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(8));

        // Date and actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel(notification.createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        dateLabel.setFont(UIStyles.SMALL_FONT);
        dateLabel.setForeground(UIStyles.TEXT_SECONDARY);
        bottomPanel.add(dateLabel);

        if (!notification.isRead) {
            JButton markReadBtn = UIStyles.createLinkButton("Mark as read");
            markReadBtn.addActionListener(e -> {
                notification.isRead = true;
                refreshNotificationsList();
            });
            bottomPanel.add(markReadBtn);
        }

        JButton deleteBtn = UIStyles.createLinkButton("Delete");
        deleteBtn.setForeground(UIStyles.WARNING_COLOR);
        deleteBtn.addActionListener(e -> {
            notifications.remove(notification);
            refreshNotificationsList();
        });
        bottomPanel.add(deleteBtn);

        contentPanel.add(bottomPanel);

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private void markAllAsRead() {
        for (Notification notification : notifications) {
            notification.isRead = true;
        }
        refreshNotificationsList();
        // Rebuild header to update unread count
        removeAll();
        initializeComponents();
        refreshNotificationsList();
    }

    /**
     * Refreshes the panel data.
     */
    public void refreshData() {
        generateNotifications();
        refreshNotificationsList();
    }
}
