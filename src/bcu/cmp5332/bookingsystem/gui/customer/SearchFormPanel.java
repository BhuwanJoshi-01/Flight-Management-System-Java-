package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.stream.Collectors;

/**
 * Search Form Component - equivalent to React's search form section.
 * Contains trip type toggle, origin/destination selectors, date picker, and search button.
 */
public class SearchFormPanel extends JPanel {

    private final FlightBookingSystem fbs;
    
    // Form state (like React's useState)
    private int tripType = 0; // 0 = One Way, 1 = Round Trip
    private JComboBox<String> originCombo;
    private JComboBox<String> destinationCombo;
    private JTextField departureDateField;
    private JComboBox<String> passengersCombo;
    
    // Callback for search action
    private final Runnable onSearch;

    public SearchFormPanel(FlightBookingSystem fbs, Runnable onSearch) {
        this.fbs = fbs;
        this.onSearch = onSearch;
        
        initializeUI();
        populateData();
    }

    private void initializeUI() {
        // Card styling - bg-card rounded-2xl p-6 shadow-soft border
        setLayout(new BorderLayout(0, 20));
        setOpaque(false);
        
        // Create card wrapper with shadow
        JPanel cardContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Soft shadow
                g2.setColor(new Color(15, 23, 42, 20));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, 20, 20);
                
                // White card
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Border
                g2.setColor(UITheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                g2.dispose();
            }
        };
        cardContent.setOpaque(false);
        cardContent.setLayout(new BorderLayout(0, 20));
        cardContent.setBorder(new EmptyBorder(24, 28, 24, 28));

        // Trip Type Toggle - flex gap-2 mb-6
        JPanel toggleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toggleContainer.setOpaque(false);
        JPanel togglePanel = UITheme.createTogglePanel(
            new String[]{"One Way", "Round Trip"}, 
            tripType, 
            idx -> tripType = idx
        );
        toggleContainer.add(togglePanel);
        cardContent.add(toggleContainer, BorderLayout.NORTH);

        // Search Fields Grid - grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4
        JPanel fieldsRow = new JPanel(new GridBagLayout());
        fieldsRow.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 6, 0, 6);
        gbc.gridy = 0;

        // From selector
        originCombo = new JComboBox<>();
        originCombo.setFont(UITheme.BODY_FONT);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        fieldsRow.add(UITheme.createInputGroup("From", originCombo), gbc);

        // Swap button with icon
        JButton swapBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.MUTED_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(UITheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        // Add icon if available
        if (UIStyles.ICON_PLANE != null) {
            ImageIcon scaledIcon = new ImageIcon(UIStyles.ICON_PLANE.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));
            swapBtn.setIcon(scaledIcon);
        } else {
            swapBtn.setText("⇄");
            swapBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }
        swapBtn.setForeground(UITheme.TEXT_SECONDARY);
        swapBtn.setFocusPainted(false);
        swapBtn.setBorderPainted(false);
        swapBtn.setContentAreaFilled(false);
        swapBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        swapBtn.setPreferredSize(new Dimension(48, 48));
        swapBtn.addActionListener(e -> swapLocations());
        
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(12, 4, 0, 4);
        fieldsRow.add(swapBtn, gbc);

        // To selector
        destinationCombo = new JComboBox<>();
        destinationCombo.setFont(UITheme.BODY_FONT);
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 6, 0, 6);
        fieldsRow.add(UITheme.createInputGroup("To", destinationCombo), gbc);

        // Departure date
        departureDateField = new JTextField();
        departureDateField.setFont(UITheme.BODY_FONT);
        departureDateField.setText("yyyy-MM-dd");
        departureDateField.setForeground(UITheme.TEXT_MUTED);
        departureDateField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (departureDateField.getText().equals("yyyy-MM-dd")) {
                    departureDateField.setText("");
                    departureDateField.setForeground(UITheme.TEXT_PRIMARY);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (departureDateField.getText().isEmpty()) {
                    departureDateField.setText("yyyy-MM-dd");
                    departureDateField.setForeground(UITheme.TEXT_MUTED);
                }
            }
        });
        gbc.gridx = 3;
        fieldsRow.add(UITheme.createInputGroup("Departure", departureDateField), gbc);

        // Passengers selector
        passengersCombo = new JComboBox<>();
        passengersCombo.setFont(UITheme.BODY_FONT);
        for (int i = 1; i <= 9; i++) {
            passengersCombo.addItem(i + (i == 1 ? " Passenger" : " Passengers"));
        }
        gbc.gridx = 4;
        fieldsRow.add(UITheme.createInputGroup("Passengers", passengersCombo), gbc);

        // Search button with icon
        JButton searchBtn = UITheme.createPrimaryButton("Search");
        searchBtn.setPreferredSize(new Dimension(140, 50));
        searchBtn.addActionListener(e -> onSearch.run());
        
        // Add search icon to button
        if (UIStyles.ICON_SEARCH != null) {
            ImageIcon searchIcon = new ImageIcon(UIStyles.ICON_SEARCH.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            searchBtn.setIcon(searchIcon);
            searchBtn.setIconTextGap(8);
        }
        
        gbc.gridx = 5;
        gbc.weightx = 0;
        gbc.insets = new Insets(12, 12, 0, 0);
        fieldsRow.add(searchBtn, gbc);

        cardContent.add(fieldsRow, BorderLayout.CENTER);
        add(cardContent, BorderLayout.CENTER);
    }

    private void populateData() {
        // Populate origins
        originCombo.addItem("Select origin");
        fbs.getFlights().stream()
            .map(f -> f.getOrigin())
            .distinct()
            .sorted()
            .forEach(origin -> originCombo.addItem(origin));

        // Populate destinations
        destinationCombo.addItem("Select destination");
        fbs.getFlights().stream()
            .map(f -> f.getDestination())
            .distinct()
            .sorted()
            .forEach(dest -> destinationCombo.addItem(dest));
    }

    private void swapLocations() {
        int originIndex = originCombo.getSelectedIndex();
        int destIndex = destinationCombo.getSelectedIndex();
        
        String originValue = (String) originCombo.getSelectedItem();
        String destValue = (String) destinationCombo.getSelectedItem();
        
        // Find and select swapped values
        if (originValue != null && !originValue.equals("Select origin")) {
            for (int i = 0; i < destinationCombo.getItemCount(); i++) {
                if (destinationCombo.getItemAt(i).equals(originValue)) {
                    destinationCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        if (destValue != null && !destValue.equals("Select destination")) {
            for (int i = 0; i < originCombo.getItemCount(); i++) {
                if (originCombo.getItemAt(i).equals(destValue)) {
                    originCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    // Getters for filter values (like React's state access)
    public String getSelectedOrigin() {
        String value = (String) originCombo.getSelectedItem();
        return (value != null && !value.equals("Select origin")) ? value : null;
    }

    public String getSelectedDestination() {
        String value = (String) destinationCombo.getSelectedItem();
        return (value != null && !value.equals("Select destination")) ? value : null;
    }

    public String getDepartureDate() {
        String value = departureDateField.getText().trim();
        return (!value.isEmpty() && !value.equals("yyyy-MM-dd")) ? value : null;
    }

    public int getPassengerCount() {
        return passengersCombo.getSelectedIndex() + 1;
    }

    public int getTripType() {
        return tripType;
    }

    // Clear form (reset state)
    public void clearForm() {
        originCombo.setSelectedIndex(0);
        destinationCombo.setSelectedIndex(0);
        departureDateField.setText("yyyy-MM-dd");
        departureDateField.setForeground(UITheme.TEXT_MUTED);
        passengersCombo.setSelectedIndex(0);
    }
}
