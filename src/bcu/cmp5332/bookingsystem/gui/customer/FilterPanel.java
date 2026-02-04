package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.gui.UIStyles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Filter Panel Component - equivalent to React's FilterPanel.
 * Contains price range slider, departure time filters, and apply/clear buttons.
 */
public class FilterPanel extends JPanel {

    // Filter state (like React's useState)
    private JSlider priceSlider;
    private JLabel priceMinLabel;
    private JLabel priceMaxLabel;
    private JTextField maxPriceField;
    private JCheckBox[] timeFilters;
    private JCheckBox[] airlineFilters;
    
    // Callbacks
    private final Runnable onApply;
    private final Runnable onClear;

    public FilterPanel(Runnable onApply, Runnable onClear) {
        this.onApply = onApply;
        this.onClear = onClear;
        
        initializeUI();
    }

    private void initializeUI() {
        // Card styling - bg-card rounded-xl p-5 shadow-soft border sticky top-4
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(280, 0));
        
        // Card wrapper
        JPanel cardContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Soft shadow
                g2.setColor(new Color(15, 23, 42, 18));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, 16, 16);
                
                // White card
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                
                // Border
                g2.setColor(UITheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                
                g2.dispose();
            }
        };
        cardContent.setOpaque(false);
        cardContent.setLayout(new BoxLayout(cardContent, BoxLayout.Y_AXIS));
        cardContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header - font-semibold mb-4 flex items-center gap-2
        JPanel headerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        headerRow.setOpaque(false);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add filter icon
        if (UIStyles.ICON_SEARCH != null) {
            ImageIcon filterIcon = new ImageIcon(UIStyles.ICON_SEARCH.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(filterIcon);
            headerRow.add(iconLabel);
        }
        
        JLabel headerLabel = new JLabel("Filters");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(UITheme.TEXT_PRIMARY);
        
        headerRow.add(headerLabel);
        cardContent.add(headerRow);
        cardContent.add(Box.createVerticalStrut(20));

        // Price Range Section
        cardContent.add(createSectionLabel("Price Range"));
        cardContent.add(Box.createVerticalStrut(12));
        
        // Price slider
        priceSlider = new JSlider(0, 1500, 1500);
        priceSlider.setOpaque(false);
        priceSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        priceSlider.addChangeListener(e -> updatePriceLabels());
        cardContent.add(priceSlider);
        cardContent.add(Box.createVerticalStrut(4));
        
        // Price range labels
        JPanel priceLabels = new JPanel(new BorderLayout());
        priceLabels.setOpaque(false);
        priceLabels.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceLabels.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        
        priceMinLabel = new JLabel("£0");
        priceMinLabel.setFont(UITheme.SMALL_FONT);
        priceMinLabel.setForeground(UITheme.TEXT_MUTED);
        
        priceMaxLabel = new JLabel("£1500");
        priceMaxLabel.setFont(UITheme.SMALL_FONT);
        priceMaxLabel.setForeground(UITheme.TEXT_MUTED);
        
        priceLabels.add(priceMinLabel, BorderLayout.WEST);
        priceLabels.add(priceMaxLabel, BorderLayout.EAST);
        cardContent.add(priceLabels);
        cardContent.add(Box.createVerticalStrut(24));

        // Departure Time Section
        cardContent.add(createSectionLabel("Departure Time"));
        cardContent.add(Box.createVerticalStrut(8));
        
        String[] times = {"Morning (6AM - 12PM)", "Afternoon (12PM - 6PM)", "Evening (6PM - 12AM)"};
        timeFilters = new JCheckBox[times.length];
        
        for (int i = 0; i < times.length; i++) {
            timeFilters[i] = createCheckbox(times[i]);
            cardContent.add(timeFilters[i]);
            cardContent.add(Box.createVerticalStrut(4));
        }
        cardContent.add(Box.createVerticalStrut(16));

        // Airlines Section
        cardContent.add(createSectionLabel("Airlines"));
        cardContent.add(Box.createVerticalStrut(8));
        
        String[] airlines = {"British Airways", "Emirates", "Lufthansa", "Air France", "Singapore Airlines"};
        airlineFilters = new JCheckBox[airlines.length];
        
        for (int i = 0; i < airlines.length; i++) {
            airlineFilters[i] = createCheckbox(airlines[i]);
            cardContent.add(airlineFilters[i]);
            cardContent.add(Box.createVerticalStrut(4));
        }
        cardContent.add(Box.createVerticalStrut(20));

        // Apply Filters button
        JButton applyBtn = UITheme.createPrimaryButton("Apply Filters");
        applyBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        applyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyBtn.addActionListener(e -> onApply.run());
        cardContent.add(applyBtn);
        cardContent.add(Box.createVerticalStrut(10));

        // Clear Filters button
        JButton clearBtn = UITheme.createSecondaryButton("Clear Filters");
        clearBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        clearBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearBtn.addActionListener(e -> {
            clearFilters();
            onClear.run();
        });
        cardContent.add(clearBtn);

        cardContent.add(Box.createVerticalGlue());
        add(cardContent);
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(UITheme.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JCheckBox createCheckbox(String text) {
        JCheckBox checkbox = new JCheckBox(text);
        checkbox.setFont(UITheme.SMALL_FONT);
        checkbox.setForeground(UITheme.TEXT_SECONDARY);
        checkbox.setOpaque(false);
        checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return checkbox;
    }

    private void updatePriceLabels() {
        priceMaxLabel.setText("£" + priceSlider.getValue());
    }

    // Getters for filter values
    public double getMaxPrice() {
        return priceSlider.getValue();
    }

    public boolean[] getTimeFilters() {
        boolean[] result = new boolean[timeFilters.length];
        for (int i = 0; i < timeFilters.length; i++) {
            result[i] = timeFilters[i].isSelected();
        }
        return result;
    }

    public boolean[] getAirlineFilters() {
        boolean[] result = new boolean[airlineFilters.length];
        for (int i = 0; i < airlineFilters.length; i++) {
            result[i] = airlineFilters[i].isSelected();
        }
        return result;
    }

    // Clear all filters (reset state)
    public void clearFilters() {
        priceSlider.setValue(1500);
        for (JCheckBox cb : timeFilters) {
            cb.setSelected(false);
        }
        for (JCheckBox cb : airlineFilters) {
            cb.setSelected(false);
        }
    }
}
