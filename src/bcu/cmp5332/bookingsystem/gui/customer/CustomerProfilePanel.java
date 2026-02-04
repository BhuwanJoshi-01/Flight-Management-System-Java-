package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.auth.AuthService;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Customer Profile Panel - Redesigned to match React CustomerProfile.tsx
 * Features modern tabs, gradient rewards card, and 2-column form layout.
 */
public class CustomerProfilePanel extends JPanel {

    private final CustomerMainWindow parentWindow;
    private final FlightBookingSystem fbs;
    private final Customer customer;
    private final AuthService authService;

    // Personal info fields
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField dobField;
    private JComboBox<Customer.Gender> genderCombo;

    // Preferences fields
    private JComboBox<Customer.SeatPreference> seatCombo;
    private JComboBox<Customer.MealPreference> mealCombo;
    private JCheckBox emailNotifCheckbox;
    private JCheckBox smsNotifCheckbox;

    // Password fields
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    // Tab system
    private JPanel tabButtonsPanel;
    private JPanel tabContentPanel;
    private CardLayout tabCardLayout;
    private String activeTab = "personal";
    private JButton[] tabButtons;

    // Colors matching React design
    private static final Color BG_COLOR = new Color(248, 250, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color PRIMARY_COLOR = new Color(8, 145, 178);
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);
    private static final Color INPUT_BG = new Color(248, 250, 252);
    private static final Color TAB_BG = new Color(241, 245, 249);
    private static final Color TAB_ACTIVE_BG = Color.WHITE;
    private static final Color TAB_ACTIVE_BORDER = new Color(8, 145, 178);

    // Gold gradient colors
    private static final Color GOLD_START = new Color(250, 204, 21);
    private static final Color GOLD_END = new Color(202, 138, 4);

    public CustomerProfilePanel(CustomerMainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.fbs = parentWindow.getFlightBookingSystem();
        this.customer = parentWindow.getCurrentCustomer();
        this.authService = new AuthService(fbs);

        setLayout(new BorderLayout(24, 24));
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        initializeComponents();
        loadCustomerData();
    }

    private void initializeComponents() {
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content - 2 columns (1:2 ratio)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 24);
        gbc.gridy = 0;

        // Left column - Profile card (1 part)
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        gbc.weighty = 1.0;
        JPanel leftColumn = createLeftColumn();
        contentPanel.add(leftColumn, gbc);

        // Right column - Settings tabs (2 parts)
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel rightColumn = createSettingsPanel();
        contentPanel.add(rightColumn, gbc);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Manage your account settings and preferences");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createLeftColumn() {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setOpaque(false);

        // Profile card with avatar, name, email
        JPanel profileCard = createProfileCard();
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        column.add(profileCard);

        return column;
    }

    private JPanel createProfileCard() {
        // Rounded card panel
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, 16, 16);
                
                // Card background
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                
                // Border
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(32, 24, 24, 24));

        // Avatar circle with user icon
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 96;
                int x = (getWidth() - size) / 2;
                int y = 0;

                // Light blue circle background
                g2.setColor(new Color(224, 242, 254));
                g2.fillOval(x, y, size, size);

                // User icon
                if (UIStyles.ICON_USER != null) {
                    Image img = UIStyles.ICON_USER.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                    int iconX = x + (size - 48) / 2;
                    int iconY = y + (size - 48) / 2;
                    g2.drawImage(img, iconX, iconY, null);
                }
                g2.dispose();
            }
        };
        avatarPanel.setPreferredSize(new Dimension(0, 100));
        avatarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        avatarPanel.setOpaque(false);
        card.add(avatarPanel);

        card.add(Box.createVerticalStrut(16));

        // Name
        JLabel nameLabel = new JLabel(customer.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);

        card.add(Box.createVerticalStrut(4));

        // Email
        JLabel emailLabel = new JLabel(customer.getEmail());
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(TEXT_SECONDARY);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(emailLabel);

        card.add(Box.createVerticalStrut(24));

        // Rewards card with gradient
        JPanel rewardsCard = createGradientRewardsCard();
        rewardsCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(rewardsCard);

        card.add(Box.createVerticalStrut(16));

        // Progress to next tier
        JPanel progressPanel = createProgressPanel();
        progressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(progressPanel);

        return card;
    }

    private JPanel createGradientRewardsCard() {
        Customer.CustomerTier tier = customer.getTier();
        
        JPanel gradientCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background based on tier
                Color startColor, endColor;
                switch (tier) {
                    case PLATINUM:
                        startColor = new Color(168, 85, 247);
                        endColor = new Color(126, 34, 206);
                        break;
                    case GOLD:
                        startColor = new Color(250, 204, 21);
                        endColor = new Color(202, 138, 4);
                        break;
                    case SILVER:
                        startColor = new Color(156, 163, 175);
                        endColor = new Color(107, 114, 128);
                        break;
                    default: // BRONZE
                        startColor = new Color(217, 119, 6);
                        endColor = new Color(180, 83, 9);
                }
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, startColor,
                    getWidth(), getHeight(), endColor
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        gradientCard.setOpaque(false);
        gradientCard.setLayout(new BorderLayout(0, 8));
        gradientCard.setBorder(new EmptyBorder(16, 16, 16, 16));
        gradientCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        gradientCard.setPreferredSize(new Dimension(0, 120));

        // Top row - icon and tier label
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        // Trophy/Award icon
        JLabel iconLabel = new JLabel();
        if (UIStyles.ICON_AWARD != null) {
            iconLabel.setIcon(new ImageIcon(UIStyles.ICON_AWARD.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        } else {
            iconLabel.setText("🏆");
            iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        }
        iconLabel.setForeground(Color.WHITE);
        topRow.add(iconLabel, BorderLayout.WEST);

        JLabel tierLabel = new JLabel(tier.name() + " MEMBER");
        tierLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tierLabel.setForeground(new Color(255, 255, 255, 200));
        topRow.add(tierLabel, BorderLayout.EAST);

        gradientCard.add(topRow, BorderLayout.NORTH);

        // Bottom - Points
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        JLabel pointsLabel = new JLabel(String.format("%,d", customer.getRewardPoints()));
        pointsLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.add(pointsLabel);

        JLabel pointsDescLabel = new JLabel("Reward Points");
        pointsDescLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pointsDescLabel.setForeground(new Color(255, 255, 255, 200));
        pointsDescLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.add(pointsDescLabel);

        gradientCard.add(bottomPanel, BorderLayout.SOUTH);

        return gradientCard;
    }

    private JPanel createProgressPanel() {
        Customer.CustomerTier tier = customer.getTier();
        int currentPoints = customer.getRewardPoints();
        int nextTierPoints = getNextTierPoints(tier);
        int progress = nextTierPoints > 0 ? (int) ((double) currentPoints / nextTierPoints * 100) : 100;
        progress = Math.min(progress, 100);
        int pointsToGo = Math.max(0, nextTierPoints - currentPoints);

        final int progressFinal = progress;
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Light background
                g2.setColor(new Color(241, 245, 249));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        panel.setPreferredSize(new Dimension(0, 90));

        // Next tier row
        JPanel tierRow = new JPanel(new BorderLayout());
        tierRow.setOpaque(false);
        tierRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        tierRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        JLabel nextLabel = new JLabel("Next tier");
        nextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nextLabel.setForeground(TEXT_SECONDARY);
        tierRow.add(nextLabel, BorderLayout.WEST);

        JLabel tierNameLabel = new JLabel(getNextTierName(tier));
        tierNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tierNameLabel.setForeground(TEXT_PRIMARY);
        tierRow.add(tierNameLabel, BorderLayout.EAST);

        panel.add(tierRow);
        panel.add(Box.createVerticalStrut(12));

        // Progress bar
        JPanel progressBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(new Color(226, 232, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Fill
                int fillWidth = (int) (getWidth() * progressFinal / 100.0);
                g2.setColor(PRIMARY_COLOR);
                g2.fillRoundRect(0, 0, fillWidth, getHeight(), 8, 8);
                
                g2.dispose();
            }
        };
        progressBar.setOpaque(false);
        progressBar.setPreferredSize(new Dimension(0, 8));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(progressBar);

        panel.add(Box.createVerticalStrut(8));

        // Points to go
        JLabel pointsToGoLabel = new JLabel(pointsToGo > 0 ? pointsToGo + " points to go" : "Max tier reached!");
        pointsToGoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pointsToGoLabel.setForeground(TEXT_MUTED);
        pointsToGoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(pointsToGoLabel);

        return panel;
    }

    private int getNextTierPoints(Customer.CustomerTier tier) {
        switch (tier) {
            case BRONZE: return 1000;
            case SILVER: return 5000;
            case GOLD: return 10000;
            default: return 10000;
        }
    }

    private String getNextTierName(Customer.CustomerTier tier) {
        switch (tier) {
            case BRONZE: return "Silver";
            case SILVER: return "Gold";
            case GOLD: return "Platinum";
            default: return "Platinum";
        }
    }

    private JPanel createSettingsPanel() {
        // Rounded card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, 16, 16);
                
                // Background
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                
                // Border
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(0, 0));
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Tab buttons panel (like React TabsList)
        tabButtonsPanel = createTabButtonsPanel();
        card.add(tabButtonsPanel, BorderLayout.NORTH);

        // Tab content with CardLayout
        tabCardLayout = new CardLayout();
        tabContentPanel = new JPanel(tabCardLayout);
        tabContentPanel.setOpaque(false);
        tabContentPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Add tab contents
        tabContentPanel.add(createPersonalTab(), "personal");
        tabContentPanel.add(createSecurityTab(), "security");
        tabContentPanel.add(createPreferencesTab(), "preferences");
        tabContentPanel.add(createPaymentTab(), "payment");

        card.add(tabContentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTabButtonsPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(20, 20, 0, 20));

        // Tab buttons with rounded background
        JPanel tabsWrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TAB_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        tabsWrapper.setOpaque(false);
        tabsWrapper.setLayout(new GridLayout(1, 4, 4, 0));
        tabsWrapper.setBorder(new EmptyBorder(4, 4, 4, 4));

        // Create tab buttons
        String[] tabIds = {"personal", "security", "preferences", "payment"};
        String[] tabLabels = {"Personal", "Security", "Preferences", "Payment"};
        ImageIcon[] tabIcons = {UIStyles.ICON_USER, UIStyles.ICON_LOCK, UIStyles.ICON_SETTINGS, UIStyles.ICON_REVENUE};

        tabButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            final String tabId = tabIds[i];
            tabButtons[i] = createStyledTabButton(tabLabels[i], tabIcons[i], tabId.equals(activeTab));
            tabButtons[i].addActionListener(e -> switchTab(tabId));
            tabsWrapper.add(tabButtons[i]);
        }

        container.add(tabsWrapper, BorderLayout.CENTER);
        return container;
    }

    private JButton createStyledTabButton(String text, ImageIcon icon, boolean active) {
        JButton btn = new JButton(text) {
            private boolean isActive = active;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isActive) {
                    // Active state - white background with border
                    g2.setColor(TAB_ACTIVE_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(TAB_ACTIVE_BORDER);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }

                g2.dispose();
                super.paintComponent(g);
            }

            public void setActive(boolean active) {
                this.isActive = active;
                setForeground(active ? PRIMARY_COLOR : TEXT_SECONDARY);
                repaint();
            }
        };

        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(active ? PRIMARY_COLOR : TEXT_SECONDARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 40));

        // Add icon
        if (icon != null) {
            ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            btn.setIcon(scaledIcon);
            btn.setIconTextGap(8);
        }

        return btn;
    }

    private void switchTab(String tabId) {
        activeTab = tabId;
        tabCardLayout.show(tabContentPanel, tabId);

        // Update button states
        String[] tabIds = {"personal", "security", "preferences", "payment"};
        for (int i = 0; i < tabButtons.length; i++) {
            boolean isActive = tabIds[i].equals(tabId);
            tabButtons[i].setForeground(isActive ? PRIMARY_COLOR : TEXT_SECONDARY);
            tabButtons[i].repaint();
        }

        // Rebuild tabs panel to update active state visually
        tabButtonsPanel.removeAll();
        tabButtonsPanel.setLayout(new BorderLayout());
        tabButtonsPanel.setBorder(new EmptyBorder(20, 20, 0, 20));

        JPanel tabsWrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TAB_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        tabsWrapper.setOpaque(false);
        tabsWrapper.setLayout(new GridLayout(1, 4, 4, 0));
        tabsWrapper.setBorder(new EmptyBorder(4, 4, 4, 4));

        String[] tabLabels = {"Personal", "Security", "Preferences", "Payment"};
        ImageIcon[] tabIcons = {UIStyles.ICON_USER, UIStyles.ICON_LOCK, UIStyles.ICON_SETTINGS, UIStyles.ICON_REVENUE};

        for (int i = 0; i < 4; i++) {
            final String tid = tabIds[i];
            tabButtons[i] = createStyledTabButton(tabLabels[i], tabIcons[i], tid.equals(activeTab));
            tabButtons[i].addActionListener(e -> switchTab(tid));
            tabsWrapper.add(tabButtons[i]);
        }

        tabButtonsPanel.add(tabsWrapper, BorderLayout.CENTER);
        tabButtonsPanel.revalidate();
        tabButtonsPanel.repaint();
    }

    private JPanel createPersonalTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // 2-column grid for form fields
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 16, 16);

        // Row 1: Full Name | Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        nameField = createStyledTextField();
        formGrid.add(createLabeledField("Full Name", nameField), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 16, 0);
        emailField = createStyledTextField();
        emailField.setEditable(false);
        emailField.setBackground(INPUT_BG);
        formGrid.add(createLabeledField("Email (read-only)", emailField), gbc);

        // Row 2: Phone | Date of Birth
        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 16, 16);
        phoneField = createStyledTextField();
        formGrid.add(createLabeledField("Phone Number", phoneField), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 16, 0);
        dobField = createStyledTextField();
        formGrid.add(createLabeledField("Date of Birth", dobField), gbc);

        // Row 3: Gender (half width)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 24, 16);
        genderCombo = new JComboBox<>(Customer.Gender.values());
        styleComboBox(genderCombo);
        formGrid.add(createLabeledField("Gender", genderCombo), gbc);

        panel.add(formGrid);

        // Save button
        JButton saveBtn = createPrimaryButton("Save Changes", UIStyles.ICON_DOWNLOAD);
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> savePersonalInfo());
        panel.add(saveBtn);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createSecurityTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        // Current password
        currentPasswordField = createStyledPasswordField();
        formPanel.add(createLabeledField("Current Password", currentPasswordField));
        formPanel.add(Box.createVerticalStrut(16));

        // New password
        newPasswordField = createStyledPasswordField();
        formPanel.add(createLabeledField("New Password", newPasswordField));
        formPanel.add(Box.createVerticalStrut(16));

        // Confirm password
        confirmPasswordField = createStyledPasswordField();
        formPanel.add(createLabeledField("Confirm New Password", confirmPasswordField));
        formPanel.add(Box.createVerticalStrut(24));

        panel.add(formPanel);

        // Update button
        JButton updateBtn = createPrimaryButton("Update Password", null);
        updateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateBtn.addActionListener(e -> updatePassword());
        panel.add(updateBtn);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createPreferencesTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // 2-column grid
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 16, 16);

        // Row 1: Preferred Seat | Meal Preference
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        seatCombo = new JComboBox<>(Customer.SeatPreference.values());
        styleComboBox(seatCombo);
        formGrid.add(createLabeledField("Preferred Seat", seatCombo), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 16, 0);
        mealCombo = new JComboBox<>(Customer.MealPreference.values());
        styleComboBox(mealCombo);
        formGrid.add(createLabeledField("Meal Preference", mealCombo), gbc);

        panel.add(formGrid);
        panel.add(Box.createVerticalStrut(16));

        // Notification settings
        JLabel notifHeader = new JLabel("Notification Settings");
        notifHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        notifHeader.setForeground(TEXT_PRIMARY);
        notifHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(notifHeader);
        panel.add(Box.createVerticalStrut(16));

        // Email notifications toggle
        panel.add(createNotificationToggle("Email Notifications", 
            "Receive booking confirmations and updates via email", true));
        panel.add(Box.createVerticalStrut(12));

        // SMS notifications toggle
        panel.add(createNotificationToggle("SMS Notifications", 
            "Get flight updates and alerts via SMS", false));
        panel.add(Box.createVerticalStrut(24));

        // Save button
        JButton saveBtn = createPrimaryButton("Save Preferences", null);
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> savePreferences());
        panel.add(saveBtn);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createPaymentTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(40, 0, 40, 0));

        // Empty state
        JPanel emptyState = new JPanel();
        emptyState.setLayout(new BoxLayout(emptyState, BoxLayout.Y_AXIS));
        emptyState.setOpaque(false);
        emptyState.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Credit card icon
        JLabel iconLabel = new JLabel();
        if (UIStyles.ICON_REVENUE != null) {
            iconLabel.setIcon(new ImageIcon(UIStyles.ICON_REVENUE.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
        }
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyState.add(iconLabel);

        emptyState.add(Box.createVerticalStrut(16));

        JLabel titleLabel = new JLabel("No payment methods saved");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyState.add(titleLabel);

        emptyState.add(Box.createVerticalStrut(8));

        JLabel descLabel = new JLabel("Add a payment method for faster checkout");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyState.add(descLabel);

        emptyState.add(Box.createVerticalStrut(20));

        // Add button (outline style)
        JButton addBtn = new JButton("Add Payment Method") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        addBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addBtn.setForeground(TEXT_PRIMARY);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setPreferredSize(new Dimension(180, 44));
        addBtn.setMaximumSize(new Dimension(180, 44));
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyState.add(addBtn);

        panel.add(Box.createVerticalGlue());
        panel.add(emptyState);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createNotificationToggle(String title, String description, boolean isEmail) {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_PRIMARY);
        textPanel.add(titleLabel);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_SECONDARY);
        textPanel.add(descLabel);

        row.add(textPanel, BorderLayout.CENTER);

        // Toggle switch (using checkbox styled as toggle)
        JCheckBox toggle = isEmail ? (emailNotifCheckbox = new JCheckBox()) : (smsNotifCheckbox = new JCheckBox());
        toggle.setOpaque(false);
        row.add(toggle, BorderLayout.EAST);

        return row;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(CARD_BG);
        field.setBorder(new EmptyBorder(10, 12, 10, 12));
        field.setOpaque(false);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(CARD_BG);
        field.setBorder(new EmptyBorder(10, 12, 10, 12));
        field.setOpaque(false);
        return field;
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(CARD_BG);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));
        panel.add(label);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            field.setPreferredSize(new Dimension(0, 44));
        } else if (field instanceof JComboBox) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            field.setPreferredSize(new Dimension(0, 44));
        }
        panel.add(field);

        return panel;
    }

    private JButton createPrimaryButton(String text, ImageIcon icon) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PRIMARY_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 44));
        btn.setMaximumSize(new Dimension(160, 44));

        if (icon != null) {
            btn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
            btn.setIconTextGap(8);
        }

        return btn;
    }

    private void loadCustomerData() {
        // Personal info
        nameField.setText(customer.getName());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhone());
        dobField.setText(customer.getDateOfBirth() != null
            ? customer.getDateOfBirth().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) : "");
        genderCombo.setSelectedItem(customer.getGender());

        // Preferences
        seatCombo.setSelectedItem(customer.getPreferredSeat());
        mealCombo.setSelectedItem(customer.getMealPreference());
        emailNotifCheckbox.setSelected(customer.isEmailNotifications());
        smsNotifCheckbox.setSelected(customer.isSmsNotifications());
    }

    private void savePersonalInfo() {
        String name = nameField.getText().trim();
        if (!ValidationUtils.isValidName(name)) {
            UIStyles.showErrorDialog(this, "Name must be at least 2 characters.", "Validation Error");
            return;
        }

        String phone = phoneField.getText().trim();
        if (!ValidationUtils.isValidPhone(phone)) {
            UIStyles.showErrorDialog(this, "Please enter a valid phone number.", "Validation Error");
            return;
        }

        LocalDate dob = null;
        String dobStr = dobField.getText().trim();
        if (!dobStr.isEmpty()) {
            try {
                // Try parsing different formats
                if (dobStr.contains("/")) {
                    String[] parts = dobStr.split("/");
                    dob = LocalDate.of(Integer.parseInt(parts[2]), Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                } else {
                    dob = LocalDate.parse(dobStr);
                }
                if (!ValidationUtils.isValidDateOfBirth(dob)) {
                    UIStyles.showErrorDialog(this, "Please enter a valid date of birth.", "Validation Error");
                    return;
                }
            } catch (Exception e) {
                UIStyles.showErrorDialog(this, "Invalid date format. Use MM/DD/YYYY or YYYY-MM-DD.", "Validation Error");
                return;
            }
        }

        customer.setName(name);
        customer.setPhone(phone);
        customer.setDateOfBirth(dob);
        customer.setGender((Customer.Gender) genderCombo.getSelectedItem());

        saveData();
        JOptionPane.showMessageDialog(this, "Personal information updated successfully.",
            "Success", JOptionPane.INFORMATION_MESSAGE);
        parentWindow.refreshHeader();
    }

    private void updatePassword() {
        String currentPwd = new String(currentPasswordField.getPassword());
        String newPwd = new String(newPasswordField.getPassword());
        String confirmPwd = new String(confirmPasswordField.getPassword());

        if (currentPwd.isEmpty()) {
            UIStyles.showErrorDialog(this, "Please enter your current password.", "Validation Error");
            return;
        }

        String hashedCurrent = AuthService.hashPassword(currentPwd);
        if (!hashedCurrent.equals(customer.getPassword())) {
            UIStyles.showErrorDialog(this, "Current password is incorrect.", "Validation Error");
            return;
        }

        String passwordError = ValidationUtils.getPasswordError(newPwd);
        if (passwordError != null) {
            UIStyles.showErrorDialog(this, passwordError, "Validation Error");
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            UIStyles.showErrorDialog(this, "New passwords do not match.", "Validation Error");
            return;
        }

        try {
            authService.updatePassword(customer, newPwd);
            saveData();
            JOptionPane.showMessageDialog(this, "Password updated successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        } catch (FlightBookingSystemException e) {
            UIStyles.showErrorDialog(this, e.getMessage(), "Error");
        }
    }

    private void savePreferences() {
        customer.setPreferredSeat((Customer.SeatPreference) seatCombo.getSelectedItem());
        customer.setMealPreference((Customer.MealPreference) mealCombo.getSelectedItem());
        customer.setEmailNotifications(emailNotifCheckbox.isSelected());
        customer.setSmsNotifications(smsNotifCheckbox.isSelected());

        saveData();
        JOptionPane.showMessageDialog(this, "Preferences saved successfully.",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveData() {
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException e) {
            UIStyles.showErrorDialog(this, "Error saving data: " + e.getMessage(), "Save Error");
        }
    }

    public void refreshData() {
        loadCustomerData();
    }
}
