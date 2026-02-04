package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.auth.UserSession;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.gui.LoginWindow;
import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Main window for the Customer Portal.
 * Features sidebar navigation, header with user info, and panel switching.
 */
public class CustomerMainWindow extends JFrame {

    private final FlightBookingSystem flightBookingSystem;
    private final Customer currentCustomer;

    // Sidebar navigation buttons
    private JButton dashboardBtn;
    private JButton searchFlightsBtn;
    private JButton myBookingsBtn;
    private JButton profileBtn;
    private JButton notificationsBtn;
    private JButton logoutBtn;

    // Content panels
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private CustomerDashboardPanel dashboardPanel;
    private FlightSearchPanel searchPanel;
    private MyBookingsPanel bookingsPanel;
    private CustomerProfilePanel profilePanel;
    private CustomerNotificationsPanel notificationsPanel;

    // Status bar
    private JLabel statusLabel;
    private JLabel userLabel;

    public CustomerMainWindow(FlightBookingSystem flightBookingSystem) {
        this.flightBookingSystem = flightBookingSystem;
        this.currentCustomer = UserSession.getInstance().getCurrentCustomer();
        
        if (currentCustomer == null) {
            throw new IllegalStateException("No customer logged in");
        }
        
        initialize();
        showPanel("dashboard");
    }

    public FlightBookingSystem getFlightBookingSystem() {
        return flightBookingSystem;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Use default look and feel
        }

        setTitle("SkyBook - Customer Portal");
        setLayout(new BorderLayout());

        // Create main components
        JPanel sidebar = createSidebar();
        JPanel header = createHeader();
        JPanel statusBar = createStatusBar();
        contentPanel = createContentPanel();

        // Layout - Header on top of content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(header, BorderLayout.NORTH);
        mainContent.add(contentPanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
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
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
            new EmptyBorder(12, 24, 12, 24)
        ));

        // Left side - Page context
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        JLabel portalLabel = new JLabel("Customer Dashboard");
        portalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        portalLabel.setForeground(UIStyles.SIDEBAR_COLOR);
        leftPanel.add(portalLabel);

        // Right side - user info
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        // Points indicator
        JPanel pointsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 249, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(186, 230, 253));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        pointsPanel.setOpaque(false);
        
        JLabel ptsIcon = new JLabel();
        if (UIStyles.ICON_AWARD != null) {
            Image scaled = UIStyles.ICON_AWARD.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            ptsIcon.setIcon(new ImageIcon(scaled));
        } else {
            ptsIcon.setText("★");
            ptsIcon.setForeground(new Color(14, 165, 233));
        }
        
        JLabel ptsLabel = new JLabel(currentCustomer.getRewardPoints() + " pts");
        ptsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ptsLabel.setForeground(new Color(3, 105, 161));
        
        pointsPanel.add(ptsIcon);
        pointsPanel.add(ptsLabel);
        rightPanel.add(pointsPanel);

        // Tier badge
        JLabel tierBadge = createTierBadge(currentCustomer.getTier());
        rightPanel.add(tierBadge);

        // User profile summary
        JPanel userBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userBox.setOpaque(false);
        
        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        avatarLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        userLabel = new JLabel(currentCustomer.getName());
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        userBox.add(avatarLabel);
        userBox.add(userLabel);
        rightPanel.add(userBox);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JLabel createTierBadge(Customer.CustomerTier tier) {
        JLabel badge = new JLabel(tier.name());
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));

        switch (tier) {
            case PLATINUM:
                badge.setBackground(new Color(138, 43, 226)); // Purple
                break;
            case GOLD:
                badge.setBackground(new Color(218, 165, 32)); // Gold
                break;
            case SILVER:
                badge.setBackground(new Color(192, 192, 192)); // Silver
                break;
            default:
                badge.setBackground(new Color(205, 127, 50)); // Bronze
                break;
        }

        return badge;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIStyles.SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(240, 0));  // Fixed width
        sidebar.setBorder(new EmptyBorder(0, 0, 16, 0));

        // Logo header with airplane icon
        JPanel logoHeader = createSidebarLogoPanel();
        logoHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoHeader);
        sidebar.add(Box.createVerticalStrut(8));

        // Role badge - Customer Portal
        JPanel roleBadge = createRoleBadgePanel("Customer Portal");
        roleBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(roleBadge);
        sidebar.add(Box.createVerticalStrut(12));

        // Navigation buttons with Icons from assets
        dashboardBtn = createNavButton("Home", UIStyles.ICON_DASHBOARD, true);
        searchFlightsBtn = createNavButton("Search Flights", UIStyles.ICON_SEARCH, false);
        myBookingsBtn = createNavButton("My Bookings", UIStyles.ICON_BOOKINGS, false);
        profileBtn = createNavButton("Profile", UIStyles.ICON_USER, false);
        notificationsBtn = createNavButton("Notifications", UIStyles.ICON_BELL, false);

        dashboardBtn.addActionListener(e -> showPanel("dashboard"));
        searchFlightsBtn.addActionListener(e -> showPanel("search"));
        myBookingsBtn.addActionListener(e -> showPanel("bookings"));
        profileBtn.addActionListener(e -> showPanel("profile"));
        notificationsBtn.addActionListener(e -> showPanel("notifications"));

        JButton[] navButtons = {dashboardBtn, searchFlightsBtn, myBookingsBtn, profileBtn, notificationsBtn};
        for (JButton btn : navButtons) {
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(btn);
        }

        sidebar.add(Box.createVerticalGlue());

        // Logout button at bottom
        JPanel exitDivider = new JPanel();
        exitDivider.setBackground(UIStyles.SIDEBAR_BORDER);
        exitDivider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        exitDivider.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(exitDivider);
        sidebar.add(Box.createVerticalStrut(8));

        logoutBtn = createNavButton("Logout", UIStyles.ICON_LOGOUT, false);
        logoutBtn.addActionListener(e -> handleLogout());
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoutBtn);

        return sidebar;
    }

    /**
     * Creates the sidebar logo panel with airplane icon.
     */
    private JPanel createSidebarLogoPanel() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        header.setBackground(UIStyles.SIDEBAR_COLOR);
        header.setBorder(new EmptyBorder(25, 20, 16, 12));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        // Logo box with airplane icon
        JPanel logoBox = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIStyles.SIDEBAR_ACTIVE_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIStyles.CORNER_RADIUS, UIStyles.CORNER_RADIUS);
                g2.dispose();
            }
        };
        logoBox.setPreferredSize(new Dimension(42, 42));
        logoBox.setOpaque(false);
        
        // Use flight icon from assets
        if (UIStyles.ICON_SEARCH != null) {
            ImageIcon whiteIcon = UIStyles.tintIcon(UIStyles.ICON_SEARCH, Color.WHITE);
            ImageIcon scaledIcon = UIStyles.scaleIcon(whiteIcon, 22, 22);
            logoBox.add(new JLabel(scaledIcon));
        }

        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setBackground(UIStyles.SIDEBAR_COLOR);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("SkyBook");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titleLabel.setForeground(UIStyles.SIDEBAR_TEXT);
        
        JLabel subtitleLabel = new JLabel("Flight System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitleLabel.setForeground(UIStyles.SIDEBAR_TEXT_MUTED);
        
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        header.add(logoBox);
        header.add(textPanel);

        return header;
    }

    /**
     * Creates the role badge panel (Customer Portal).
     */
    private JPanel createRoleBadgePanel(String text) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setBackground(UIStyles.SIDEBAR_COLOR);
        wrapper.setBorder(new EmptyBorder(4, 20, 4, 12));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIStyles.SIDEBAR_ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIStyles.CORNER_RADIUS, UIStyles.CORNER_RADIUS);
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        
        // indicator dot
        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(14, 165, 233));
                g2.fillOval(0, 0, 8, 8);
                g2.dispose();
            }
        };
        dot.setPreferredSize(new Dimension(8, 8));
        dot.setOpaque(false);
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(UIStyles.SIDEBAR_TEXT);
        
        badge.add(dot);
        badge.add(label);
        wrapper.add(badge);
        
        return wrapper;
    }

    private JButton createNavButton(String text, ImageIcon icon, boolean selected) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                boolean isSelected = Boolean.TRUE.equals(getClientProperty("sidebar.selected"));
                boolean isHover = getModel().isRollover();

                if (isSelected) {
                    g2.setColor(UIStyles.SIDEBAR_ACTIVE_BG);
                    g2.fillRoundRect(12, 2, getWidth() - 24, getHeight() - 4, UIStyles.CORNER_RADIUS, UIStyles.CORNER_RADIUS);
                } else if (isHover) {
                    g2.setColor(UIStyles.SIDEBAR_ACCENT);
                    g2.fillRoundRect(12, 2, getWidth() - 24, getHeight() - 4, UIStyles.CORNER_RADIUS, UIStyles.CORNER_RADIUS);
                }

                // Draw icon from assets
                if (icon != null) {
                    ImageIcon tintedIcon = UIStyles.tintIcon(icon, isSelected ? Color.WHITE : UIStyles.SIDEBAR_TEXT_MUTED);
                    int iconSize = 20;
                    int x = 28;
                    int y = (getHeight() - iconSize) / 2;
                    g2.drawImage(tintedIcon.getImage(), x, y, iconSize, iconSize, null);
                }

                // Draw text - right after icon
                g2.setColor(isSelected ? Color.WHITE : UIStyles.SIDEBAR_TEXT);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(text, 74, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

                g2.dispose();
            }
        };
        button.putClientProperty("sidebar.selected", selected);
        button.setBackground(UIStyles.SIDEBAR_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setPreferredSize(new Dimension(240, 44));
        button.setRolloverEnabled(true);
        return button;
    }

    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        panel.setBackground(UIStyles.BACKGROUND_COLOR);

        // Create content panels
        dashboardPanel = new CustomerDashboardPanel(this);
        searchPanel = new FlightSearchPanel(this);
        bookingsPanel = new MyBookingsPanel(this);
        profilePanel = new CustomerProfilePanel(this);
        notificationsPanel = new CustomerNotificationsPanel(this);

        panel.add(wrapInScrollPane(dashboardPanel), "dashboard");
        panel.add(wrapInScrollPane(searchPanel), "search");
        panel.add(wrapInScrollPane(bookingsPanel), "bookings");
        panel.add(wrapInScrollPane(profilePanel), "profile");
        panel.add(wrapInScrollPane(notificationsPanel), "notifications");

        return panel;
    }

    private JScrollPane wrapInScrollPane(JComponent content) {
        JScrollPane scrollPane = new JScrollPane(content,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UIStyles.BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(240, 240, 240));
        statusBar.setBorder(new EmptyBorder(8, 15, 8, 15));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(UIStyles.SMALL_FONT);
        statusLabel.setForeground(UIStyles.TEXT_SECONDARY);

        JLabel dateLabel = new JLabel("System Date: " +
            flightBookingSystem.getSystemDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        dateLabel.setFont(UIStyles.SMALL_FONT);
        dateLabel.setForeground(UIStyles.TEXT_SECONDARY);

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(dateLabel, BorderLayout.EAST);

        return statusBar;
    }

    /**
     * Shows the specified panel and updates sidebar button states.
     */
    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);

        // Update sidebar button states
        setNavButtonSelected(dashboardBtn, panelName.equals("dashboard"));
        setNavButtonSelected(searchFlightsBtn, panelName.equals("search"));
        setNavButtonSelected(myBookingsBtn, panelName.equals("bookings"));
        setNavButtonSelected(profileBtn, panelName.equals("profile"));
        setNavButtonSelected(notificationsBtn, panelName.equals("notifications"));

        // Refresh data when switching panels
        refreshCurrentPanel(panelName);

        String displayName = panelName.substring(0, 1).toUpperCase() + panelName.substring(1);
        statusLabel.setText("Viewing: " + displayName);
    }

    /**
     * Sets the selected state on a navigation button.
     */
    private void setNavButtonSelected(JButton button, boolean selected) {
        button.putClientProperty("sidebar.selected", selected);
        button.repaint();
    }

    private void refreshCurrentPanel(String panelName) {
        switch (panelName) {
            case "dashboard":
                dashboardPanel.refreshData();
                break;
            case "search":
                searchPanel.refreshData();
                break;
            case "bookings":
                bookingsPanel.refreshData();
                break;
            case "profile":
                profilePanel.refreshData();
                break;
            case "notifications":
                notificationsPanel.refreshData();
                break;
        }
    }

    /**
     * Refreshes all panels - called after data changes.
     */
    public void refreshAllPanels() {
        dashboardPanel.refreshData();
        searchPanel.refreshData();
        bookingsPanel.refreshData();
        profilePanel.refreshData();
        notificationsPanel.refreshData();
    }

    /**
     * Updates the header to reflect changes in customer data (e.g., points, tier).
     */
    public void refreshHeader() {
        // Re-create header with updated data
        // For simplicity, just update the user label
        userLabel.setText("Welcome, " + currentCustomer.getName().split(" ")[0]);
    }

    /**
     * Handles logout action.
     */
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                FlightBookingSystemData.store(flightBookingSystem);
            } catch (IOException ex) {
                System.err.println("Warning: Could not save data: " + ex.getMessage());
            }

            UserSession.getInstance().logout();
            dispose();

            // Return to login window
            SwingUtilities.invokeLater(() -> new LoginWindow(flightBookingSystem));
        }
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
                FlightBookingSystemData.store(flightBookingSystem);
                statusLabel.setText("Data saved successfully");
                UserSession.getInstance().logout();
                dispose();
                System.exit(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error saving data: " + ex.getMessage() + "\nChanges may be lost.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (confirm == JOptionPane.NO_OPTION) {
            UserSession.getInstance().logout();
            dispose();
            System.exit(0);
        }
        // Cancel - do nothing
    }

    /**
     * Shows a status message in the status bar.
     */
    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}
