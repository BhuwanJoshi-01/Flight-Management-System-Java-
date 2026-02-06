package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Customer Notifications Panel - Redesigned to match React design
 * Features modern cards with colored left borders, icon circles, and action buttons
 */
public class CustomerNotificationsPanel extends JPanel {

    private final CustomerMainWindow parentWindow;
    private final FlightBookingSystem fbs;
    private final Customer customer;

    // Notification list
    private JPanel notificationsList;
    private List<Notification> notifications;
    private JLabel subtitleLabel;
    private JPanel tabsPanel;

    // Filter state
    private String currentFilter = "all";

    // Colors matching React design
    private static final Color BG_COLOR = new Color(248, 250, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(241, 245, 249);
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);
    
    // Type-specific colors
    private static final Color BOOKING_GREEN = new Color(34, 197, 94);
    private static final Color BOOKING_GREEN_BG = new Color(220, 252, 231);
    private static final Color UPDATE_ORANGE = new Color(251, 146, 60);
    private static final Color UPDATE_ORANGE_BG = new Color(255, 237, 213);
    private static final Color PROMO_BLUE = new Color(14, 165, 233);
    private static final Color PROMO_BLUE_BG = new Color(224, 242, 254);
    private static final Color DELETE_RED = new Color(239, 68, 68);
    
    // Tab colors
    private static final Color TAB_BG = new Color(241, 245, 249);
    private static final Color TAB_ACTIVE_BG = Color.WHITE;

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
        String time;

        Notification(String id, Type type, String title, String message, LocalDate createdAt, String time) {
            this.id = id;
            this.type = type;
            this.title = title;
            this.message = message;
            this.isRead = false;
            this.createdAt = createdAt;
            this.time = time;
        }
    }

    public CustomerNotificationsPanel(CustomerMainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.fbs = parentWindow.getFlightBookingSystem();
        this.customer = parentWindow.getCurrentCustomer();
        this.notifications = new ArrayList<>();

        setLayout(new BorderLayout(0, 24));
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        generateNotifications();
        initializeComponents();
    }

    private void initializeComponents() {
        // Header section
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setOpaque(false);

        // Tabs panel
        tabsPanel = createTabsPanel();
        mainPanel.add(tabsPanel, BorderLayout.NORTH);

        // Notifications list with scroll
        notificationsList = new JPanel();
        notificationsList.setLayout(new BoxLayout(notificationsList, BoxLayout.Y_AXIS));
        notificationsList.setOpaque(false);
        notificationsList.setBorder(new EmptyBorder(20, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(notificationsList);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        refreshNotificationsList();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 8, 0));

        // Left side - Title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));

        long unreadCount = notifications.stream().filter(n -> !n.isRead).count();
        subtitleLabel = new JLabel("You have " + unreadCount + " unread notification" + (unreadCount != 1 ? "s" : ""));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Mark all as read button
        JButton markAllBtn = createMarkAllButton();
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(markAllBtn);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JButton createMarkAllButton() {
        JButton btn = new JButton("Mark all as read") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_PRIMARY);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.setIcon(createCheckIcon());
        btn.setIconTextGap(8);
        btn.addActionListener(e -> markAllAsRead());
        return btn;
    }

    private Icon createCheckIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TEXT_SECONDARY);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 2, y + 7, x + 6, y + 11);
                g2.drawLine(x + 6, y + 11, x + 14, y + 3);
                g2.dispose();
            }
            @Override public int getIconWidth() { return 16; }
            @Override public int getIconHeight() { return 16; }
        };
    }

    private JPanel createTabsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        // Create rounded tab container
        JPanel tabContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TAB_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        tabContainer.setOpaque(false);
        tabContainer.setBorder(new EmptyBorder(4, 4, 4, 4));

        long unreadCount = notifications.stream().filter(n -> !n.isRead).count();

        JButton allTab = createTabButton("All", "all");
        JButton unreadTab = createTabButton("Unread (" + unreadCount + ")", "unread");

        tabContainer.add(allTab);
        tabContainer.add(unreadTab);

        panel.add(tabContainer);
        return panel;
    }

    private JButton createTabButton(String text, String filter) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (currentFilter.equals(filter)) {
                    g2.setColor(new Color(0, 0, 0, 15));
                    g2.fillRoundRect(1, 2, getWidth() - 2, getHeight() - 2, 8, 8);
                    g2.setColor(TAB_ACTIVE_BG);
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 2, 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(currentFilter.equals(filter) ? TEXT_PRIMARY : TEXT_SECONDARY);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));

        btn.addActionListener(e -> {
            currentFilter = filter;
            refreshAll();
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
            Notification bookingNotif = new Notification(
                String.valueOf(id++),
                Notification.Type.BOOKING,
                "Booking Confirmed",
                "Your booking " + booking.getReference() + " has been confirmed.",
                booking.getBookingDate(),
                "10:30 AM"
            );
            notifications.add(bookingNotif);

            // Flight reminder (if departure is within 3 days)
            long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(today, flight.getDepartureDate());
            if (daysUntil > 0 && daysUntil <= 3 && !booking.isCancelled()) {
                Notification reminderNotif = new Notification(
                    String.valueOf(id++),
                    Notification.Type.FLIGHT_UPDATE,
                    "Flight Update",
                    "Flight " + flight.getFlightNumber() + " gate has changed to A15.",
                    today,
                    "8:00 AM"
                );
                notifications.add(reminderNotif);
            }

            // Cancellation notification
            if (booking.isCancelled()) {
                Notification cancelNotif = new Notification(
                    String.valueOf(id++),
                    Notification.Type.SYSTEM,
                    "Booking Cancelled",
                    String.format("Your booking %s has been cancelled. Refund: £%.2f",
                        booking.getReference(), booking.getCancellationFee()),
                    today,
                    "3:00 PM"
                );
                cancelNotif.isRead = true;
                notifications.add(cancelNotif);
            }
        }

        // Promotional notification
        Notification promoNotif = new Notification(
            String.valueOf(id++),
            Notification.Type.PROMO,
            "Special Offer!",
            "Get 20% off on flights to Dubai this weekend!",
            today.minusDays(3),
            "2:00 PM"
        );
        promoNotif.isRead = true;
        notifications.add(promoNotif);

        // Tier upgrade notification if applicable
        if (customer.getTier() != Customer.CustomerTier.BRONZE) {
            Notification tierNotif = new Notification(
                String.valueOf(id++),
                Notification.Type.SYSTEM,
                "Congratulations on your " + customer.getTier().name() + " Status! 🎉",
                "You've earned " + customer.getTier().name() + " tier status! Enjoy exclusive benefits.",
                today.minusDays(7),
                "9:00 AM"
            );
            tierNotif.isRead = true;
            notifications.add(tierNotif);
        }

        // Sort by date (newest first)
        notifications.sort((a, b) -> b.createdAt.compareTo(a.createdAt));
    }

    private void refreshNotificationsList() {
        notificationsList.removeAll();

        List<Notification> filtered = notifications.stream()
            .filter(n -> currentFilter.equals("all") || !n.isRead)
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            notificationsList.add(createEmptyState());
        } else {
            for (Notification notification : filtered) {
                notificationsList.add(createNotificationCard(notification));
                notificationsList.add(Box.createVerticalStrut(12));
            }
        }

        notificationsList.revalidate();
        notificationsList.repaint();
    }

    private JPanel createEmptyState() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(60, 0, 60, 0));

        JLabel iconLabel = new JLabel("🔔");
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 48));
        iconLabel.setForeground(TEXT_MUTED);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(16));

        JLabel msgLabel = new JLabel(currentFilter.equals("unread") 
            ? "No unread notifications" 
            : "No notifications yet");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        msgLabel.setForeground(TEXT_SECONDARY);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(msgLabel);

        return panel;
    }

    private JPanel createNotificationCard(Notification notification) {
        // Get colors based on notification type
        Color borderColor = getBorderColor(notification.type);
        Color iconBg = getIconBgColor(notification.type);
        Color iconFg = getIconFgColor(notification.type);

        // Main card panel with left colored border
        JPanel card = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // White background with rounded corners
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Subtle border
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                // Left colored border (4px wide)
                g2.setColor(borderColor);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Content wrapper
        JPanel contentWrapper = new JPanel(new BorderLayout(16, 0));
        contentWrapper.setOpaque(false);

        // Left - Icon circle
        JPanel iconCircle = createIconCircle(notification.type, iconBg, iconFg);
        contentWrapper.add(iconCircle, BorderLayout.WEST);

        // Center - Content
        JPanel centerPanel = new JPanel(new BorderLayout(0, 4));
        centerPanel.setOpaque(false);

        // Top row - Title
        JLabel titleLabel = new JLabel(notification.title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_PRIMARY);
        centerPanel.add(titleLabel, BorderLayout.NORTH);

        // Middle - Message
        JLabel messageLabel = new JLabel(notification.message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setForeground(TEXT_SECONDARY);
        centerPanel.add(messageLabel, BorderLayout.CENTER);

        // Bottom - Date and actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Date/time
        String dateStr = notification.createdAt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + ", " + notification.time;
        JLabel dateLabel = new JLabel(dateStr);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(TEXT_MUTED);
        bottomPanel.add(dateLabel);

        bottomPanel.add(Box.createHorizontalStrut(20));

        // Mark as read button (only if unread)
        if (!notification.isRead) {
            JButton markReadBtn = createActionLink("Mark as read", TEXT_PRIMARY, false);
            markReadBtn.addActionListener(e -> {
                notification.isRead = true;
                refreshAll();
            });
            bottomPanel.add(markReadBtn);
            bottomPanel.add(Box.createHorizontalStrut(16));
        }

        // Delete button
        JButton deleteBtn = createActionLink("Delete", DELETE_RED, true);
        deleteBtn.addActionListener(e -> {
            notifications.remove(notification);
            refreshAll();
        });
        bottomPanel.add(deleteBtn);

        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
        contentWrapper.add(centerPanel, BorderLayout.CENTER);

        // Right - New badge (only if unread)
        if (!notification.isRead) {
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            rightPanel.setOpaque(false);
            
            JLabel newBadge = new JLabel("New") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(241, 245, 249));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            newBadge.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            newBadge.setForeground(TEXT_SECONDARY);
            newBadge.setBorder(new EmptyBorder(4, 10, 4, 10));
            newBadge.setOpaque(false);
            rightPanel.add(newBadge);
            
            contentWrapper.add(rightPanel, BorderLayout.EAST);
        }

        card.add(contentWrapper, BorderLayout.CENTER);

        return card;
    }

    private JPanel createIconCircle(Notification.Type type, Color bg, Color fg) {
        JPanel circle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        circle.setPreferredSize(new Dimension(44, 44));
        circle.setMinimumSize(new Dimension(44, 44));
        circle.setMaximumSize(new Dimension(44, 44));
        circle.setOpaque(false);
        circle.setLayout(new GridBagLayout());

        // Add icon
        JLabel iconLabel = new JLabel();
        iconLabel.setForeground(fg);
        
        ImageIcon icon = getIconForType(type);
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaled));
        } else {
            iconLabel.setText(getEmojiForType(type));
            iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        }
        
        circle.add(iconLabel);
        return circle;
    }

    private ImageIcon getIconForType(Notification.Type type) {
        switch (type) {
            case BOOKING:
                return UIStyles.ICON_PLANE;
            case FLIGHT_UPDATE:
                return UIStyles.ICON_BELL;
            case PROMO:
                return null; // Use emoji for promo
            default:
                return UIStyles.ICON_SETTINGS;
        }
    }

    private String getEmojiForType(Notification.Type type) {
        switch (type) {
            case BOOKING: return "✈";
            case FLIGHT_UPDATE: return "🔔";
            case PROMO: return "🏷";
            default: return "⚙";
        }
    }

    private Color getBorderColor(Notification.Type type) {
        switch (type) {
            case BOOKING: return BOOKING_GREEN;
            case FLIGHT_UPDATE: return UPDATE_ORANGE;
            case PROMO: return PROMO_BLUE;
            default: return TEXT_MUTED;
        }
    }

    private Color getIconBgColor(Notification.Type type) {
        switch (type) {
            case BOOKING: return BOOKING_GREEN_BG;
            case FLIGHT_UPDATE: return UPDATE_ORANGE_BG;
            case PROMO: return PROMO_BLUE_BG;
            default: return new Color(241, 245, 249);
        }
    }

    private Color getIconFgColor(Notification.Type type) {
        switch (type) {
            case BOOKING: return BOOKING_GREEN;
            case FLIGHT_UPDATE: return UPDATE_ORANGE;
            case PROMO: return PROMO_BLUE;
            default: return TEXT_SECONDARY;
        }
    }

    private JButton createActionLink(String text, Color color, boolean hasIcon) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(color);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 0, 0, 0));

        if (hasIcon) {
            btn.setIcon(createTrashIcon(color));
            btn.setIconTextGap(4);
        }

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(color.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(color);
            }
        });

        return btn;
    }

    private Icon createTrashIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(x + 2, y + 4, 10, 10, 2, 2);
                g2.drawLine(x + 1, y + 4, x + 13, y + 4);
                g2.drawRoundRect(x + 5, y + 1, 4, 3, 1, 1);
                g2.drawLine(x + 5, y + 7, x + 5, y + 11);
                g2.drawLine(x + 7, y + 7, x + 7, y + 11);
                g2.drawLine(x + 9, y + 7, x + 9, y + 11);
                g2.dispose();
            }
            @Override public int getIconWidth() { return 14; }
            @Override public int getIconHeight() { return 14; }
        };
    }

    private void markAllAsRead() {
        for (Notification notification : notifications) {
            notification.isRead = true;
        }
        refreshAll();
    }

    private void refreshAll() {
        // Update subtitle
        long unreadCount = notifications.stream().filter(n -> !n.isRead).count();
        subtitleLabel.setText("You have " + unreadCount + " unread notification" + (unreadCount != 1 ? "s" : ""));
        
        // Rebuild tabs panel
        Component center = ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (center != null) {
            remove(center);
        }
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setOpaque(false);

        tabsPanel = createTabsPanel();
        mainPanel.add(tabsPanel, BorderLayout.NORTH);

        notificationsList = new JPanel();
        notificationsList.setLayout(new BoxLayout(notificationsList, BoxLayout.Y_AXIS));
        notificationsList.setOpaque(false);
        notificationsList.setBorder(new EmptyBorder(20, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(notificationsList);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        refreshNotificationsList();
        revalidate();
        repaint();
    }

    /**
     * Refreshes the panel data.
     */
    public void refreshData() {
        generateNotifications();
        refreshAll();
    }
}
