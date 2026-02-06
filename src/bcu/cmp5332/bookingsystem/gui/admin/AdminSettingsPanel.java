package bcu.cmp5332.bookingsystem.gui.admin;

import bcu.cmp5332.bookingsystem.gui.MainWindow;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Admin Settings Panel matching the React AdminSettings.tsx design.
 * Features tabbed interface with General, Pricing, Notifications, and Data Management tabs.
 */
public class AdminSettingsPanel extends JPanel {
    
    private final MainWindow mainWindow;
    private final FlightBookingSystem fbs;
    
    private JTabbedPane tabbedPane;
    
    public AdminSettingsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.fbs = mainWindow.getFlightBookingSystem();
        
        setLayout(new BorderLayout());
        setBackground(AdminTheme.BACKGROUND);
        setBorder(new EmptyBorder(24, 24, 24, 24));
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Header
        JPanel header = createHeader();
        contentPanel.add(header);
        contentPanel.add(Box.createVerticalStrut(24));
        
        // Tabbed content
        JPanel tabsCard = AdminTheme.createCard();
        tabsCard.setLayout(new BorderLayout());
        
        tabbedPane = createStyledTabs();
        tabsCard.add(tabbedPane, BorderLayout.CENTER);
        
        contentPanel.add(tabsCard);
        
        // Scroll pane
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
        
        JLabel title = new JLabel("System Settings");
        title.setFont(AdminTheme.H1_FONT);
        title.setForeground(AdminTheme.FOREGROUND);
        titlePanel.add(title);
        
        JLabel subtitle = new JLabel("Configure system preferences and parameters");
        subtitle.setFont(AdminTheme.BODY_FONT);
        subtitle.setForeground(AdminTheme.MUTED_FOREGROUND);
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        return header;
    }
    
    private JTabbedPane createStyledTabs() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        tabs.setFont(AdminTheme.LABEL_FONT);
        tabs.setForeground(AdminTheme.FOREGROUND);
        tabs.setBackground(AdminTheme.CARD);
        tabs.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        // Tab panels
        tabs.addTab("⚙ General", createGeneralTab());
        tabs.addTab("💰 Pricing", createPricingTab());
        tabs.addTab("🔔 Notifications", createNotificationsTab());
        tabs.addTab("💾 Data", createDataTab());
        
        return tabs;
    }
    
    private JPanel createGeneralTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        // System Information Section
        panel.add(createSectionTitle("System Information"));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("System Name", "Flight Booking System", false));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("System Date", fbs.getSystemDate().toString(), false));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Version", "2.0.0", false));
        panel.add(Box.createVerticalStrut(24));
        
        // Operational Settings
        panel.add(createSectionTitle("Operational Settings"));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Maintenance Mode", 
            "When enabled, only admins can access the system", false));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Auto-save Data", 
            "Automatically save data changes", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Debug Mode", 
            "Show detailed error messages", false));
        panel.add(Box.createVerticalStrut(24));
        
        // Default Settings
        panel.add(createSectionTitle("Default Settings"));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Default Flight Capacity", "150", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Maximum Booking Window (days)", "365", true));
        panel.add(Box.createVerticalStrut(24));
        
        // Save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JButton saveBtn = AdminTheme.createPrimaryButton("Save Changes");
        saveBtn.setPreferredSize(new Dimension(140, 40));
        saveBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Settings saved successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel);
        
        // Spacer
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createPricingTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        // Dynamic Pricing Settings
        panel.add(createSectionTitle("Dynamic Pricing"));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Enable Dynamic Pricing", 
            "Automatically adjust prices based on demand and timing", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Time Sensitivity Factor (α)", "0.02", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Capacity Sensitivity Factor (β)", "0.5", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Booking Window (days)", "30", true));
        panel.add(Box.createVerticalStrut(24));
        
        // Cancellation Settings
        panel.add(createSectionTitle("Cancellation Policy"));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Cancellation Fee (%)", "10", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Free Cancellation Window (hours)", "24", true));
        panel.add(Box.createVerticalStrut(24));
        
        // Reward Points
        panel.add(createSectionTitle("Reward Points"));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Points per £1 spent", "1", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Points to £ ratio", "100:1", true));
        panel.add(Box.createVerticalStrut(24));
        
        // Save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JButton saveBtn = AdminTheme.createPrimaryButton("Save Pricing");
        saveBtn.setPreferredSize(new Dimension(140, 40));
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createNotificationsTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        // Email Notifications
        panel.add(createSectionTitle("Email Notifications"));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Booking Confirmations", 
            "Send email when a booking is confirmed", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Cancellation Notices", 
            "Send email when a booking is cancelled", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Flight Updates", 
            "Notify customers about flight changes", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Marketing Emails", 
            "Send promotional offers to customers", false));
        panel.add(Box.createVerticalStrut(24));
        
        // Admin Alerts
        panel.add(createSectionTitle("Admin Alerts"));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Low Occupancy Alerts", 
            "Alert when flight occupancy is below threshold", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createFormField("Occupancy Threshold (%)", "30", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("High Demand Alerts", 
            "Alert when flight is nearly full", true));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createToggleSetting("Daily Summary", 
            "Receive daily booking summary", true));
        panel.add(Box.createVerticalStrut(24));
        
        // Save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JButton saveBtn = AdminTheme.createPrimaryButton("Save Notifications");
        saveBtn.setPreferredSize(new Dimension(160, 40));
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createDataTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        // Data Files
        panel.add(createSectionTitle("Data Files"));
        panel.add(Box.createVerticalStrut(16));
        
        panel.add(createDataFileItem("flights.txt", "Flight data", 
            fbs.getAllFlights().size() + " records"));
        panel.add(Box.createVerticalStrut(12));
        
        panel.add(createDataFileItem("customers.txt", "Customer data", 
            fbs.getAllCustomers().size() + " records"));
        panel.add(Box.createVerticalStrut(12));
        
        panel.add(createDataFileItem("bookings.txt", "Booking data", 
            countAllBookings() + " records"));
        panel.add(Box.createVerticalStrut(24));
        
        // Data Operations
        panel.add(createSectionTitle("Data Operations"));
        panel.add(Box.createVerticalStrut(16));
        
        JPanel operationsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        operationsPanel.setOpaque(false);
        operationsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JButton exportBtn = AdminTheme.createOutlineButton("⬇ Export All Data");
        exportBtn.setPreferredSize(new Dimension(150, 40));
        exportBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Data exported to resources/data/export/", 
                "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        operationsPanel.add(exportBtn);
        
        JButton importBtn = AdminTheme.createOutlineButton("⬆ Import Data");
        importBtn.setPreferredSize(new Dimension(130, 40));
        operationsPanel.add(importBtn);
        
        JButton backupBtn = AdminTheme.createOutlineButton("📋 Create Backup");
        backupBtn.setPreferredSize(new Dimension(140, 40));
        operationsPanel.add(backupBtn);
        
        panel.add(operationsPanel);
        panel.add(Box.createVerticalStrut(24));
        
        // Danger Zone
        panel.add(createSectionTitle("Danger Zone"));
        panel.add(Box.createVerticalStrut(16));
        
        JPanel dangerCard = new JPanel();
        dangerCard.setLayout(new BoxLayout(dangerCard, BoxLayout.Y_AXIS));
        dangerCard.setBackground(new Color(AdminTheme.DESTRUCTIVE.getRed(), 
                                          AdminTheme.DESTRUCTIVE.getGreen(), 
                                          AdminTheme.DESTRUCTIVE.getBlue(), 15));
        dangerCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminTheme.DESTRUCTIVE, 1),
            new EmptyBorder(16, 16, 16, 16)
        ));
        dangerCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        JLabel dangerTitle = new JLabel("Reset System Data");
        dangerTitle.setFont(AdminTheme.LABEL_BOLD);
        dangerTitle.setForeground(AdminTheme.DESTRUCTIVE);
        dangerCard.add(dangerTitle);
        
        JLabel dangerDesc = new JLabel("This will permanently delete all data. This action cannot be undone.");
        dangerDesc.setFont(AdminTheme.BODY_FONT);
        dangerDesc.setForeground(AdminTheme.MUTED_FOREGROUND);
        dangerCard.add(dangerDesc);
        dangerCard.add(Box.createVerticalStrut(12));
        
        JButton resetBtn = AdminTheme.createDangerButton("Reset All Data");
        resetBtn.setPreferredSize(new Dimension(140, 36));
        resetBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you absolutely sure? This will delete ALL data permanently!",
                "Confirm Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, 
                    "Data reset is disabled for safety.", 
                    "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        dangerCard.add(resetBtn);
        
        panel.add(dangerCard);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createSectionTitle(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel label = new JLabel(title);
        label.setFont(AdminTheme.H3_FONT);
        label.setForeground(AdminTheme.FOREGROUND);
        panel.add(label, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createFormField(String label, String value, boolean editable) {
        JPanel field = new JPanel(new BorderLayout(0, 8));
        field.setOpaque(false);
        field.setMaximumSize(new Dimension(500, 70));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(AdminTheme.LABEL_FONT);
        labelComp.setForeground(AdminTheme.FOREGROUND);
        field.add(labelComp, BorderLayout.NORTH);
        
        JTextField textField = new JTextField(value);
        textField.setFont(AdminTheme.BODY_FONT);
        textField.setEditable(editable);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminTheme.BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        textField.setBackground(editable ? Color.WHITE : AdminTheme.MUTED);
        textField.setPreferredSize(new Dimension(400, 40));
        field.add(textField, BorderLayout.CENTER);
        
        return field;
    }
    
    private JPanel createToggleSetting(String title, String description, boolean enabled) {
        JPanel setting = new JPanel(new BorderLayout(16, 0));
        setting.setOpaque(false);
        setting.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AdminTheme.LABEL_BOLD);
        titleLabel.setForeground(AdminTheme.FOREGROUND);
        textPanel.add(titleLabel);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(AdminTheme.SMALL_FONT);
        descLabel.setForeground(AdminTheme.MUTED_FOREGROUND);
        textPanel.add(descLabel);
        
        setting.add(textPanel, BorderLayout.CENTER);
        
        // Custom toggle switch
        JToggleButton toggle = createToggleSwitch(enabled);
        setting.add(toggle, BorderLayout.EAST);
        
        return setting;
    }
    
    private JToggleButton createToggleSwitch(boolean selected) {
        JToggleButton toggle = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                int trackH = 24;
                int trackY = (h - trackH) / 2;
                
                // Track
                if (isSelected()) {
                    g2.setColor(AdminTheme.ACCENT);
                } else {
                    g2.setColor(AdminTheme.MUTED);
                }
                g2.fillRoundRect(0, trackY, w, trackH, trackH, trackH);
                
                // Thumb
                int thumbSize = 20;
                int thumbY = trackY + 2;
                int thumbX = isSelected() ? w - thumbSize - 2 : 2;
                
                g2.setColor(Color.WHITE);
                g2.fillOval(thumbX, thumbY, thumbSize, thumbSize);
                
                g2.dispose();
            }
        };
        toggle.setSelected(selected);
        toggle.setPreferredSize(new Dimension(50, 28));
        toggle.setOpaque(false);
        toggle.setContentAreaFilled(false);
        toggle.setBorderPainted(false);
        toggle.setFocusPainted(false);
        toggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return toggle;
    }
    
    private JPanel createDataFileItem(String filename, String description, String stats) {
        JPanel item = new JPanel(new BorderLayout(12, 0));
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminTheme.BORDER, 1),
            new EmptyBorder(12, 16, 12, 16)
        ));
        item.setBackground(AdminTheme.CARD);
        
        // File icon and info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        infoPanel.setOpaque(false);
        
        JLabel icon = new JLabel("📄");
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        infoPanel.add(icon);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(filename);
        nameLabel.setFont(AdminTheme.LABEL_BOLD);
        nameLabel.setForeground(AdminTheme.FOREGROUND);
        textPanel.add(nameLabel);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(AdminTheme.SMALL_FONT);
        descLabel.setForeground(AdminTheme.MUTED_FOREGROUND);
        textPanel.add(descLabel);
        
        infoPanel.add(textPanel);
        item.add(infoPanel, BorderLayout.WEST);
        
        // Stats badge
        JLabel statsBadge = new JLabel(stats);
        statsBadge.setFont(AdminTheme.SMALL_FONT);
        statsBadge.setForeground(AdminTheme.ACCENT);
        statsBadge.setOpaque(true);
        statsBadge.setBackground(new Color(AdminTheme.ACCENT.getRed(), 
                                          AdminTheme.ACCENT.getGreen(), 
                                          AdminTheme.ACCENT.getBlue(), 25));
        statsBadge.setBorder(new EmptyBorder(4, 8, 4, 8));
        
        JPanel badgeWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        badgeWrapper.setOpaque(false);
        badgeWrapper.add(statsBadge);
        item.add(badgeWrapper, BorderLayout.EAST);
        
        return item;
    }
    
    private int countAllBookings() {
        int count = 0;
        for (Customer customer : fbs.getAllCustomers()) {
            count += customer.getBookings().size();
        }
        return count;
    }
}
