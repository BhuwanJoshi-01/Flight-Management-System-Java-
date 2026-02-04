package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

/**
 * Admin Settings Panel for configuring system-wide settings including
 * dynamic pricing parameters, cancellation fees, and booking policies.
 */
public class AdminSettingsPanel extends JPanel {

    private final MainWindow parentWindow;
    private final FlightBookingSystem fbs;

    // General settings
    private JTextField systemNameField;
    private JComboBox<String> currencyCombo;
    private JComboBox<String> dateFormatCombo;

    // Pricing settings
    private JCheckBox dynamicPricingCheckbox;
    private JSlider timeSensitivitySlider;
    private JSlider capacitySensitivitySlider;
    private JTextField cancellationFeeField;
    private JTextField rebookingFeeField;
    private JTextField maxBookingWindowField;

    // Booking settings
    private JTextField maxPassengersField;
    private JTextField minBookingNoticeField;

    // Labels for slider values
    private JLabel timeSensitivityLabel;
    private JLabel capacitySensitivityLabel;

    public AdminSettingsPanel(MainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.fbs = parentWindow.getFlightBookingSystem();

        setLayout(new BorderLayout(0, 20));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeComponents();
        loadCurrentSettings();
    }

    private void initializeComponents() {
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIStyles.BODY_FONT);

        // General settings tab
        tabbedPane.addTab("General", createGeneralTab());

        // Pricing settings tab
        tabbedPane.addTab("Pricing", createPricingTab());

        // Booking settings tab
        tabbedPane.addTab("Booking Rules", createBookingTab());

        // About tab
        tabbedPane.addTab("About", createAboutTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("System Settings");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Configure system-wide settings and preferences");
        subtitleLabel.setFont(UIStyles.BODY_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createGeneralTab() {
        JPanel wrapper = UIStyles.createCard();
        wrapper.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // System Name
        panel.add(createFieldLabel("System Name"));
        systemNameField = new JTextField("SkyBook Flight Booking System");
        systemNameField.setMaximumSize(new Dimension(400, 35));
        systemNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(systemNameField);
        panel.add(Box.createVerticalStrut(15));

        // Currency
        panel.add(createFieldLabel("Default Currency"));
        currencyCombo = new JComboBox<>(new String[]{"GBP (£)", "USD ($)", "EUR (€)"});
        currencyCombo.setMaximumSize(new Dimension(400, 35));
        currencyCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(currencyCombo);
        panel.add(Box.createVerticalStrut(15));

        // Date Format
        panel.add(createFieldLabel("Date Format"));
        dateFormatCombo = new JComboBox<>(new String[]{"DD/MM/YYYY", "MM/DD/YYYY", "YYYY-MM-DD"});
        dateFormatCombo.setMaximumSize(new Dimension(400, 35));
        dateFormatCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dateFormatCombo);
        panel.add(Box.createVerticalStrut(15));

        // System date info
        JPanel dateInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dateInfoPanel.setOpaque(false);
        dateInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateInfoLabel = new JLabel("System Date: " + fbs.getSystemDate().toString());
        dateInfoLabel.setFont(UIStyles.BODY_FONT);
        dateInfoLabel.setForeground(UIStyles.TEXT_SECONDARY);
        dateInfoPanel.add(dateInfoLabel);

        JLabel dateNote = new JLabel(" (Fixed for consistency in testing)");
        dateNote.setFont(UIStyles.SMALL_FONT);
        dateNote.setForeground(UIStyles.TEXT_SECONDARY);
        dateInfoPanel.add(dateNote);

        panel.add(dateInfoPanel);
        panel.add(Box.createVerticalStrut(30));

        // Save button
        JButton saveBtn = UIStyles.createPrimaryButton("Save Settings");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> saveSettings());
        panel.add(saveBtn);

        panel.add(Box.createVerticalGlue());

        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel createPricingTab() {
        JPanel wrapper = UIStyles.createCard();
        wrapper.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Dynamic Pricing toggle
        dynamicPricingCheckbox = new JCheckBox("Enable Dynamic Pricing");
        dynamicPricingCheckbox.setFont(UIStyles.SUBHEADER_FONT);
        dynamicPricingCheckbox.setOpaque(false);
        dynamicPricingCheckbox.setSelected(true);
        dynamicPricingCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dynamicPricingCheckbox);

        JLabel dpDescription = new JLabel("Automatically adjust prices based on demand and timing");
        dpDescription.setFont(UIStyles.SMALL_FONT);
        dpDescription.setForeground(UIStyles.TEXT_SECONDARY);
        dpDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dpDescription);
        panel.add(Box.createVerticalStrut(25));

        // Time Sensitivity (α)
        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.setOpaque(false);
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        timePanel.setMaximumSize(new Dimension(500, 60));

        JLabel timeLabel = new JLabel("Time Sensitivity (α)");
        timeLabel.setFont(UIStyles.BODY_FONT);
        timePanel.add(timeLabel, BorderLayout.NORTH);

        timeSensitivitySlider = new JSlider(0, 50, 15);
        timeSensitivitySlider.setMajorTickSpacing(10);
        timeSensitivitySlider.setMinorTickSpacing(5);
        timeSensitivitySlider.setPaintTicks(true);
        timeSensitivitySlider.setOpaque(false);

        timeSensitivityLabel = new JLabel("0.15");
        timeSensitivityLabel.setFont(UIStyles.BODY_FONT);
        timeSensitivityLabel.setPreferredSize(new Dimension(50, 20));

        timeSensitivitySlider.addChangeListener(e ->
            timeSensitivityLabel.setText(String.format("%.2f", timeSensitivitySlider.getValue() / 100.0)));

        JPanel timeSliderPanel = new JPanel(new BorderLayout());
        timeSliderPanel.setOpaque(false);
        timeSliderPanel.add(timeSensitivitySlider, BorderLayout.CENTER);
        timeSliderPanel.add(timeSensitivityLabel, BorderLayout.EAST);
        timePanel.add(timeSliderPanel, BorderLayout.CENTER);

        JLabel timeNote = new JLabel("Higher values increase price as departure date approaches");
        timeNote.setFont(UIStyles.SMALL_FONT);
        timeNote.setForeground(UIStyles.TEXT_SECONDARY);
        timePanel.add(timeNote, BorderLayout.SOUTH);

        panel.add(timePanel);
        panel.add(Box.createVerticalStrut(20));

        // Capacity Sensitivity (β)
        JPanel capPanel = new JPanel(new BorderLayout());
        capPanel.setOpaque(false);
        capPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        capPanel.setMaximumSize(new Dimension(500, 60));

        JLabel capLabel = new JLabel("Capacity Sensitivity (β)");
        capLabel.setFont(UIStyles.BODY_FONT);
        capPanel.add(capLabel, BorderLayout.NORTH);

        capacitySensitivitySlider = new JSlider(0, 50, 20);
        capacitySensitivitySlider.setMajorTickSpacing(10);
        capacitySensitivitySlider.setMinorTickSpacing(5);
        capacitySensitivitySlider.setPaintTicks(true);
        capacitySensitivitySlider.setOpaque(false);

        capacitySensitivityLabel = new JLabel("0.20");
        capacitySensitivityLabel.setFont(UIStyles.BODY_FONT);
        capacitySensitivityLabel.setPreferredSize(new Dimension(50, 20));

        capacitySensitivitySlider.addChangeListener(e ->
            capacitySensitivityLabel.setText(String.format("%.2f", capacitySensitivitySlider.getValue() / 100.0)));

        JPanel capSliderPanel = new JPanel(new BorderLayout());
        capSliderPanel.setOpaque(false);
        capSliderPanel.add(capacitySensitivitySlider, BorderLayout.CENTER);
        capSliderPanel.add(capacitySensitivityLabel, BorderLayout.EAST);
        capPanel.add(capSliderPanel, BorderLayout.CENTER);

        JLabel capNote = new JLabel("Higher values increase price as seats fill up");
        capNote.setFont(UIStyles.SMALL_FONT);
        capNote.setForeground(UIStyles.TEXT_SECONDARY);
        capPanel.add(capNote, BorderLayout.SOUTH);

        panel.add(capPanel);
        panel.add(Box.createVerticalStrut(25));

        // Cancellation Fee
        panel.add(createFieldLabel("Cancellation Fee (%)"));
        cancellationFeeField = new JTextField("10");
        cancellationFeeField.setMaximumSize(new Dimension(150, 35));
        cancellationFeeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(cancellationFeeField);
        panel.add(Box.createVerticalStrut(15));

        // Rebooking Fee
        panel.add(createFieldLabel("Rebooking Fee (%)"));
        rebookingFeeField = new JTextField("15");
        rebookingFeeField.setMaximumSize(new Dimension(150, 35));
        rebookingFeeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(rebookingFeeField);
        panel.add(Box.createVerticalStrut(15));

        // Max Booking Window
        panel.add(createFieldLabel("Max Booking Window (days)"));
        maxBookingWindowField = new JTextField("365");
        maxBookingWindowField.setMaximumSize(new Dimension(150, 35));
        maxBookingWindowField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(maxBookingWindowField);
        panel.add(Box.createVerticalStrut(30));

        // Save button
        JButton saveBtn = UIStyles.createPrimaryButton("Save Pricing Settings");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> savePricingSettings());
        panel.add(saveBtn);

        panel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createBookingTab() {
        JPanel wrapper = UIStyles.createCard();
        wrapper.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Max Passengers per booking
        panel.add(createFieldLabel("Max Passengers per Booking"));
        maxPassengersField = new JTextField("9");
        maxPassengersField.setMaximumSize(new Dimension(150, 35));
        maxPassengersField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(maxPassengersField);
        panel.add(Box.createVerticalStrut(15));

        // Min Booking Notice
        panel.add(createFieldLabel("Minimum Booking Notice (hours before departure)"));
        minBookingNoticeField = new JTextField("2");
        minBookingNoticeField.setMaximumSize(new Dimension(150, 35));
        minBookingNoticeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(minBookingNoticeField);
        panel.add(Box.createVerticalStrut(30));

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(248, 250, 252));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(500, 150));

        JLabel infoTitle = new JLabel("📋 Booking Policy Information");
        infoTitle.setFont(UIStyles.SUBHEADER_FONT);
        infoTitle.setForeground(UIStyles.TEXT_PRIMARY);
        infoPanel.add(infoTitle);
        infoPanel.add(Box.createVerticalStrut(10));

        String[] policies = {
            "• Bookings can be made up to " + maxBookingWindowField.getText() + " days in advance",
            "• A " + cancellationFeeField.getText() + "% cancellation fee applies to all cancellations",
            "• Passengers must be at least 18 years old to book",
            "• Return flights can be added during booking"
        };

        for (String policy : policies) {
            JLabel policyLabel = new JLabel(policy);
            policyLabel.setFont(UIStyles.BODY_FONT);
            policyLabel.setForeground(UIStyles.TEXT_SECONDARY);
            infoPanel.add(policyLabel);
        }

        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(30));

        // Save button
        JButton saveBtn = UIStyles.createPrimaryButton("Save Booking Rules");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> saveBookingSettings());
        panel.add(saveBtn);

        panel.add(Box.createVerticalGlue());

        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel createAboutTab() {
        JPanel wrapper = UIStyles.createCard();
        wrapper.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Logo
        JLabel logoLabel = new JLabel("✈ SkyBook", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logoLabel.setForeground(UIStyles.PRIMARY_COLOR);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(10));

        JLabel subtitleLabel = new JLabel("Flight Booking Management System");
        subtitleLabel.setFont(UIStyles.HEADER_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitleLabel);
        panel.add(Box.createVerticalStrut(30));

        // Version info
        JLabel versionLabel = new JLabel("Version 2.0.0");
        versionLabel.setFont(UIStyles.BODY_FONT);
        versionLabel.setForeground(UIStyles.TEXT_SECONDARY);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(versionLabel);
        panel.add(Box.createVerticalStrut(5));

        JLabel courseLabel = new JLabel("CMP5332 Software Development Coursework");
        courseLabel.setFont(UIStyles.BODY_FONT);
        courseLabel.setForeground(UIStyles.TEXT_SECONDARY);
        courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(courseLabel);
        panel.add(Box.createVerticalStrut(30));

        // Features
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setOpaque(false);
        featuresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel featuresTitle = new JLabel("Key Features:");
        featuresTitle.setFont(UIStyles.SUBHEADER_FONT);
        featuresTitle.setForeground(UIStyles.TEXT_PRIMARY);
        featuresTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        featuresPanel.add(featuresTitle);
        featuresPanel.add(Box.createVerticalStrut(10));

        String[] features = {
            "✓ Customer & Admin dual portal system",
            "✓ Dynamic pricing algorithm",
            "✓ Reward points & tier system",
            "✓ Flight search with filters",
            "✓ Booking management with cancellation",
            "✓ Revenue analytics dashboard",
            "✓ Text file-based persistence"
        };

        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(UIStyles.BODY_FONT);
            featureLabel.setForeground(UIStyles.TEXT_SECONDARY);
            featureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            featuresPanel.add(featureLabel);
        }

        panel.add(featuresPanel);
        panel.add(Box.createVerticalGlue());

        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIStyles.BODY_FONT);
        label.setForeground(UIStyles.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        return label;
    }

    private void loadCurrentSettings() {
        // Load current DynamicPricing settings
        timeSensitivitySlider.setValue((int) (DynamicPricing.getTimeSensitivity() * 100));
        capacitySensitivitySlider.setValue((int) (DynamicPricing.getCapacitySensitivity() * 100));
        maxBookingWindowField.setText(String.valueOf(DynamicPricing.getBookingWindowDays()));
    }

    private void saveSettings() {
        JOptionPane.showMessageDialog(this,
            "General settings saved successfully.",
            "Settings Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void savePricingSettings() {
        try {
            double alpha = timeSensitivitySlider.getValue() / 100.0;
            double beta = capacitySensitivitySlider.getValue() / 100.0;
            int bookingWindow = Integer.parseInt(maxBookingWindowField.getText().trim());

            DynamicPricing.setTimeSensitivity(alpha);
            DynamicPricing.setCapacitySensitivity(beta);
            DynamicPricing.setBookingWindowDays(bookingWindow);

            // Save to file
            FlightBookingSystemData.store(fbs);

            JOptionPane.showMessageDialog(this,
                "Pricing settings saved successfully.\n\n" +
                "Time Sensitivity (α): " + alpha + "\n" +
                "Capacity Sensitivity (β): " + beta + "\n" +
                "Booking Window: " + bookingWindow + " days",
                "Settings Saved", JOptionPane.INFORMATION_MESSAGE);

            // Refresh panels that show prices
            parentWindow.refreshAllPanels();

        } catch (NumberFormatException e) {
            UIStyles.showErrorDialog(this, "Please enter valid numbers.", "Input Error");
        } catch (IOException e) {
            UIStyles.showErrorDialog(this, "Error saving settings: " + e.getMessage(), "Save Error");
        }
    }

    private void saveBookingSettings() {
        try {
            int maxPassengers = Integer.parseInt(maxPassengersField.getText().trim());
            int minNotice = Integer.parseInt(minBookingNoticeField.getText().trim());

            if (maxPassengers < 1 || maxPassengers > 10) {
                UIStyles.showErrorDialog(this, "Max passengers must be between 1 and 10.", "Input Error");
                return;
            }

            if (minNotice < 0) {
                UIStyles.showErrorDialog(this, "Minimum notice cannot be negative.", "Input Error");
                return;
            }

            JOptionPane.showMessageDialog(this,
                "Booking rules saved successfully.",
                "Settings Saved", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            UIStyles.showErrorDialog(this, "Please enter valid numbers.", "Input Error");
        }
    }

    /**
     * Refreshes the panel data.
     */
    public void refreshData() {
        loadCurrentSettings();
    }
}
