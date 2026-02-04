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
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Customer Profile Panel with personal details, preferences, and password change.
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

    public CustomerProfilePanel(CustomerMainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.fbs = parentWindow.getFlightBookingSystem();
        this.customer = parentWindow.getCurrentCustomer();
        this.authService = new AuthService(fbs);

        setLayout(new BorderLayout(20, 20));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeComponents();
        loadCustomerData();
    }

    private void initializeComponents() {
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content with two columns
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);

        // Left column - Profile card and rewards
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));
        leftColumn.setOpaque(false);

        JPanel profileCard = createProfileCard();
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftColumn.add(profileCard);
        leftColumn.add(Box.createVerticalStrut(20));

        JPanel rewardsCard = createRewardsCard();
        rewardsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftColumn.add(rewardsCard);

        contentPanel.add(leftColumn);

        // Right column - Settings tabs
        JPanel rightColumn = createSettingsPanel();
        contentPanel.add(rightColumn);

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

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Manage your account settings and preferences");
        subtitleLabel.setFont(UIStyles.BODY_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createProfileCard() {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Avatar area
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 80;
                int x = (getWidth() - size) / 2;
                int y = 10;

                // Circle background
                g2.setColor(new Color(UIStyles.PRIMARY_COLOR.getRed(),
                    UIStyles.PRIMARY_COLOR.getGreen(),
                    UIStyles.PRIMARY_COLOR.getBlue(), 30));
                g2.fillOval(x, y, size, size);

                // User icon
                if (UIStyles.ICON_USER != null) {
                    Image iconImg = UIStyles.ICON_USER.getImage();
                    int iconSize = 40;
                    int iconX = x + (size - iconSize) / 2;
                    int iconY = y + (size - iconSize) / 2;
                    g2.drawImage(iconImg, iconX, iconY, iconSize, iconSize, null);
                } else {
                    g2.setColor(UIStyles.PRIMARY_COLOR);
                    g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 36));
                    FontMetrics fm = g2.getFontMetrics();
                    String icon = "👤";
                    int textX = x + (size - fm.stringWidth(icon)) / 2;
                    int textY = y + (size + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(icon, textX, textY);
                }

                g2.dispose();
            }
        };
        avatarPanel.setPreferredSize(new Dimension(0, 110));
        avatarPanel.setOpaque(false);
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(avatarPanel);

        // Name
        JLabel nameLabel = new JLabel(customer.getName());
        nameLabel.setFont(UIStyles.HEADER_FONT);
        nameLabel.setForeground(UIStyles.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);

        card.add(Box.createVerticalStrut(5));

        // Email
        JLabel emailLabel = new JLabel(customer.getEmail());
        emailLabel.setFont(UIStyles.BODY_FONT);
        emailLabel.setForeground(UIStyles.TEXT_SECONDARY);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(emailLabel);

        card.add(Box.createVerticalStrut(10));

        // Member since
        String memberSince = customer.getCreatedAt() != null
            ? customer.getCreatedAt().format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            : "N/A";
        JLabel memberLabel = new JLabel("Member since " + memberSince);
        memberLabel.setFont(UIStyles.SMALL_FONT);
        memberLabel.setForeground(UIStyles.TEXT_SECONDARY);
        memberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(memberLabel);

        return card;
    }

    private JPanel createRewardsCard() {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Tier badge
        Customer.CustomerTier tier = customer.getTier();
        Color tierColor = getTierColor(tier);

        JPanel tierPanel = new JPanel();
        tierPanel.setBackground(tierColor);
        tierPanel.setLayout(new BoxLayout(tierPanel, BoxLayout.Y_AXIS));
        tierPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        tierPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tierPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel tierIcon = new JLabel("🏆");
        tierIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
        tierIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        tierPanel.add(tierIcon);

        JLabel tierLabel = new JLabel(tier.name() + " Member");
        tierLabel.setFont(UIStyles.SMALL_FONT);
        tierLabel.setForeground(Color.WHITE);
        tierLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tierPanel.add(tierLabel);

        JLabel pointsLabel = new JLabel(String.format("%,d", customer.getRewardPoints()));
        pointsLabel.setFont(UIStyles.KPI_VALUE_FONT);
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tierPanel.add(pointsLabel);

        JLabel pointsDesc = new JLabel("Reward Points");
        pointsDesc.setFont(UIStyles.SMALL_FONT);
        pointsDesc.setForeground(new Color(255, 255, 255, 200));
        pointsDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        tierPanel.add(pointsDesc);

        card.add(tierPanel);

        // Progress to next tier
        card.add(Box.createVerticalStrut(15));

        int currentPoints = customer.getRewardPoints();
        int nextTierPoints = getNextTierPoints(tier);
        int progress = nextTierPoints > 0 ? (int) ((double) currentPoints / nextTierPoints * 100) : 100;
        progress = Math.min(progress, 100);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);
        progressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel nextTierLabel = new JLabel("Next tier: " + getNextTierName(tier));
        nextTierLabel.setFont(UIStyles.SMALL_FONT);
        nextTierLabel.setForeground(UIStyles.TEXT_SECONDARY);
        progressPanel.add(nextTierLabel, BorderLayout.NORTH);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(progress);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 8));
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setForeground(UIStyles.PRIMARY_COLOR);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        int pointsToGo = nextTierPoints - currentPoints;
        JLabel progressLabel = new JLabel(pointsToGo > 0 ? pointsToGo + " points to go" : "Max tier reached!");
        progressLabel.setFont(UIStyles.SMALL_FONT);
        progressLabel.setForeground(UIStyles.TEXT_SECONDARY);
        progressPanel.add(progressLabel, BorderLayout.SOUTH);

        card.add(progressPanel);

        return card;
    }

    private Color getTierColor(Customer.CustomerTier tier) {
        switch (tier) {
            case PLATINUM: return new Color(138, 43, 226);
            case GOLD: return new Color(218, 165, 32);
            case SILVER: return new Color(128, 128, 128);
            default: return new Color(205, 127, 50);
        }
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
            default: return "Platinum (Max)";
        }
    }

    private JPanel createSettingsPanel() {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIStyles.BODY_FONT);

        // Personal Info Tab
        tabbedPane.addTab("Personal", createPersonalTab());

        // Security Tab
        tabbedPane.addTab("Security", createSecurityTab());

        // Preferences Tab
        tabbedPane.addTab("Preferences", createPreferencesTab());

        card.add(tabbedPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createPersonalTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Name
        panel.add(createFieldLabel("Full Name"));
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(15));

        // Email (read-only)
        panel.add(createFieldLabel("Email (cannot be changed)"));
        emailField = new JTextField();
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(15));

        // Phone
        panel.add(createFieldLabel("Phone Number"));
        phoneField = new JTextField();
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(phoneField);
        panel.add(Box.createVerticalStrut(15));

        // Date of Birth
        panel.add(createFieldLabel("Date of Birth (YYYY-MM-DD)"));
        dobField = new JTextField();
        dobField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        dobField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dobField);
        panel.add(Box.createVerticalStrut(15));

        // Gender
        panel.add(createFieldLabel("Gender"));
        genderCombo = new JComboBox<>(Customer.Gender.values());
        genderCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        genderCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(genderCombo);
        panel.add(Box.createVerticalStrut(25));

        // Save button
        JButton saveBtn = UIStyles.createPrimaryButton("Save Changes");
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
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Current password
        panel.add(createFieldLabel("Current Password"));
        currentPasswordField = new JPasswordField();
        currentPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        currentPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(currentPasswordField);
        panel.add(Box.createVerticalStrut(15));

        // New password
        panel.add(createFieldLabel("New Password"));
        newPasswordField = new JPasswordField();
        newPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        newPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(newPasswordField);
        panel.add(Box.createVerticalStrut(5));

        JLabel passwordHint = new JLabel("Min 8 characters, must include letters and numbers");
        passwordHint.setFont(UIStyles.SMALL_FONT);
        passwordHint.setForeground(UIStyles.TEXT_SECONDARY);
        passwordHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(passwordHint);
        panel.add(Box.createVerticalStrut(15));

        // Confirm password
        panel.add(createFieldLabel("Confirm New Password"));
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        confirmPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(confirmPasswordField);
        panel.add(Box.createVerticalStrut(25));

        // Update button
        JButton updateBtn = UIStyles.createPrimaryButton("Update Password");
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
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Seat preference
        panel.add(createFieldLabel("Preferred Seat"));
        seatCombo = new JComboBox<>(Customer.SeatPreference.values());
        seatCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        seatCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(seatCombo);
        panel.add(Box.createVerticalStrut(15));

        // Meal preference
        panel.add(createFieldLabel("Meal Preference"));
        mealCombo = new JComboBox<>(Customer.MealPreference.values());
        mealCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mealCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(mealCombo);
        panel.add(Box.createVerticalStrut(20));

        // Notification preferences
        panel.add(createFieldLabel("Notification Preferences"));
        panel.add(Box.createVerticalStrut(10));

        emailNotifCheckbox = new JCheckBox("Email notifications");
        emailNotifCheckbox.setFont(UIStyles.BODY_FONT);
        emailNotifCheckbox.setOpaque(false);
        emailNotifCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(emailNotifCheckbox);
        panel.add(Box.createVerticalStrut(5));

        smsNotifCheckbox = new JCheckBox("SMS notifications");
        smsNotifCheckbox.setFont(UIStyles.BODY_FONT);
        smsNotifCheckbox.setOpaque(false);
        smsNotifCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(smsNotifCheckbox);
        panel.add(Box.createVerticalStrut(25));

        // Save button
        JButton saveBtn = UIStyles.createPrimaryButton("Save Preferences");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> savePreferences());
        panel.add(saveBtn);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIStyles.BODY_FONT);
        label.setForeground(UIStyles.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        return label;
    }

    private void loadCustomerData() {
        // Personal info
        nameField.setText(customer.getName());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhone());
        dobField.setText(customer.getDateOfBirth() != null
            ? customer.getDateOfBirth().toString() : "");
        genderCombo.setSelectedItem(customer.getGender());

        // Preferences
        seatCombo.setSelectedItem(customer.getPreferredSeat());
        mealCombo.setSelectedItem(customer.getMealPreference());
        emailNotifCheckbox.setSelected(customer.isEmailNotifications());
        smsNotifCheckbox.setSelected(customer.isSmsNotifications());
    }

    private void savePersonalInfo() {
        // Validate name
        String name = nameField.getText().trim();
        if (!ValidationUtils.isValidName(name)) {
            UIStyles.showErrorDialog(this, "Name must be at least 2 characters.", "Validation Error");
            return;
        }

        // Validate phone
        String phone = phoneField.getText().trim();
        if (!ValidationUtils.isValidPhone(phone)) {
            UIStyles.showErrorDialog(this, "Please enter a valid phone number.", "Validation Error");
            return;
        }

        // Validate and parse date of birth
        LocalDate dob = null;
        String dobStr = dobField.getText().trim();
        if (!dobStr.isEmpty()) {
            try {
                dob = LocalDate.parse(dobStr);
                if (!ValidationUtils.isValidDateOfBirth(dob)) {
                    UIStyles.showErrorDialog(this, "Please enter a valid date of birth.", "Validation Error");
                    return;
                }
            } catch (Exception e) {
                UIStyles.showErrorDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Validation Error");
                return;
            }
        }

        // Update customer
        customer.setName(name);
        customer.setPhone(phone);
        customer.setDateOfBirth(dob);
        customer.setGender((Customer.Gender) genderCombo.getSelectedItem());

        // Save to file
        saveData();

        JOptionPane.showMessageDialog(this, "Personal information updated successfully.",
            "Success", JOptionPane.INFORMATION_MESSAGE);

        parentWindow.refreshHeader();
    }

    private void updatePassword() {
        String currentPwd = new String(currentPasswordField.getPassword());
        String newPwd = new String(newPasswordField.getPassword());
        String confirmPwd = new String(confirmPasswordField.getPassword());

        // Validate current password
        if (currentPwd.isEmpty()) {
            UIStyles.showErrorDialog(this, "Please enter your current password.", "Validation Error");
            return;
        }

        // Verify current password
        String hashedCurrent = AuthService.hashPassword(currentPwd);
        if (!hashedCurrent.equals(customer.getPassword())) {
            UIStyles.showErrorDialog(this, "Current password is incorrect.", "Validation Error");
            return;
        }

        // Validate new password
        String passwordError = ValidationUtils.getPasswordError(newPwd);
        if (passwordError != null) {
            UIStyles.showErrorDialog(this, passwordError, "Validation Error");
            return;
        }

        // Check confirmation
        if (!newPwd.equals(confirmPwd)) {
            UIStyles.showErrorDialog(this, "New passwords do not match.", "Validation Error");
            return;
        }

        // Update password
        try {
            authService.updatePassword(customer, newPwd);
            saveData();

            JOptionPane.showMessageDialog(this, "Password updated successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields
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

    /**
     * Refreshes the panel data.
     */
    public void refreshData() {
        loadCustomerData();
    }
}
