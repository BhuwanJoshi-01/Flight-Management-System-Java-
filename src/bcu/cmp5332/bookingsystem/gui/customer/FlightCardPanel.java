package bcu.cmp5332.bookingsystem.gui.customer;

import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.model.DynamicPricing;
import bcu.cmp5332.bookingsystem.model.Flight;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Flight Card Component - equivalent to React's FlightCard.
 * Displays flight information in a card layout with booking action.
 */
public class FlightCardPanel extends JPanel {

    private final Flight flight;
    private final LocalDate systemDate;
    private final boolean alreadyBooked;
    private final Runnable onSelect;
    
    private boolean hovered = false;

    public FlightCardPanel(Flight flight, LocalDate systemDate, boolean alreadyBooked, Runnable onSelect) {
        this.flight = flight;
        this.systemDate = systemDate;
        this.alreadyBooked = alreadyBooked;
        this.onSelect = onSelect;
        
        initializeUI();
    }

    private void initializeUI() {
        // Hover effect listeners
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                repaint();
            }
        });

        setLayout(new GridBagLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(18, 24, 18, 24));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Column 1: Plane Icon Circle
        JPanel iconCircle = UITheme.createIconCircle(UIStyles.ICON_PLANE, 48);
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 20);
        add(iconCircle, gbc);

        // Column 2: Flight Number & Airline
        JPanel flightInfoPanel = new JPanel();
        flightInfoPanel.setLayout(new BoxLayout(flightInfoPanel, BoxLayout.Y_AXIS));
        flightInfoPanel.setOpaque(false);
        flightInfoPanel.setPreferredSize(new Dimension(120, 50));

        JLabel flightNumberLabel = new JLabel(flight.getFlightNumber());
        flightNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        flightNumberLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel airlineLabel = new JLabel("Airline");  // Placeholder - would need airline info
        airlineLabel.setFont(UITheme.SMALL_FONT);
        airlineLabel.setForeground(UITheme.TEXT_MUTED);

        flightInfoPanel.add(flightNumberLabel);
        flightInfoPanel.add(airlineLabel);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 24);
        add(flightInfoPanel, gbc);

        // Column 3: Origin Info
        JPanel originPanel = createLocationPanel(
            flight.getOrigin().substring(0, Math.min(3, flight.getOrigin().length())).toUpperCase(),
            flight.getOrigin(),
            "10:30" // Placeholder time
        );
        gbc.gridx = 2;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(0, 0, 0, 16);
        add(originPanel, gbc);

        // Column 4: Route Line with Duration
        JPanel routePanel = createRoutePanel();
        gbc.gridx = 3;
        gbc.weightx = 0.4;
        gbc.insets = new Insets(0, 0, 0, 16);
        add(routePanel, gbc);

        // Column 5: Destination Info
        JPanel destPanel = createLocationPanel(
            flight.getDestination().substring(0, Math.min(3, flight.getDestination().length())).toUpperCase(),
            flight.getDestination(),
            "14:30" // Placeholder time
        );
        gbc.gridx = 4;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(0, 0, 0, 24);
        add(destPanel, gbc);

        // Column 6: Date
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.setOpaque(false);
        datePanel.setPreferredSize(new Dimension(100, 50));

        JLabel dateLabel = new JLabel(flight.getDepartureDate().format(DateTimeFormatter.ofPattern("EEE, MMM dd")));
        dateLabel.setFont(UITheme.SMALL_FONT);
        dateLabel.setForeground(UITheme.TEXT_SECONDARY);

        JLabel yearLabel = new JLabel(String.valueOf(flight.getDepartureDate().getYear()));
        yearLabel.setFont(UITheme.SMALL_FONT);
        yearLabel.setForeground(UITheme.TEXT_MUTED);

        datePanel.add(dateLabel);
        datePanel.add(yearLabel);

        gbc.gridx = 5;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 24);
        add(datePanel, gbc);

        // Column 7: Availability Badge
        int available = flight.getCapacity() - flight.getPassengers().size();
        JPanel availPanel = createAvailabilityBadge(available);
        gbc.gridx = 6;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 16);
        add(availPanel, gbc);

        // Column 8: Price
        double price = DynamicPricing.calculatePrice(flight, systemDate);
        JPanel pricePanel = createPricePanel(price, flight.getPrice());
        gbc.gridx = 7;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 20);
        add(pricePanel, gbc);

        // Column 9: Select Button
        JButton selectBtn = createSelectButton(available);
        gbc.gridx = 8;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(selectBtn, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Shadow (larger on hover)
        int shadowOffset = hovered ? 10 : 6;
        int shadowAlpha = hovered ? 30 : 20;
        g2.setColor(new Color(15, 23, 42, shadowAlpha));
        g2.fillRoundRect(4, shadowOffset, getWidth() - 8, getHeight() - 12, 18, 18);
        
        // White background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
        
        // Border (primary color on hover)
        g2.setColor(hovered ? UITheme.PRIMARY : UITheme.BORDER);
        g2.setStroke(new BasicStroke(hovered ? 2f : 1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
        
        g2.dispose();
    }

    private JPanel createLocationPanel(String code, String city, String time) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel codeLabel = new JLabel(code);
        codeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        codeLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel cityLabel = new JLabel(city);
        cityLabel.setFont(UITheme.SMALL_FONT);
        cityLabel.setForeground(UITheme.TEXT_MUTED);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeLabel.setForeground(UITheme.TEXT_SECONDARY);

        panel.add(codeLabel);
        panel.add(cityLabel);
        panel.add(timeLabel);

        return panel;
    }

    private JPanel createRoutePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int y = getHeight() / 2 + 8;
                int padding = 10;
                
                // Dashed line
                g2.setColor(UITheme.BORDER);
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 
                    0, new float[]{5, 5}, 0));
                g2.drawLine(padding, y, getWidth() - padding, y);
                
                // Plane icon in center
                g2.setColor(UITheme.PRIMARY);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                String plane = "✈";
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(plane)) / 2;
                g2.drawString(plane, textX, y + 5);
                
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 50));

        // Duration label
        JLabel durationLabel = new JLabel("8h 00m", SwingConstants.CENTER);
        durationLabel.setFont(UITheme.SMALL_FONT);
        durationLabel.setForeground(UITheme.TEXT_MUTED);
        
        panel.setLayout(new BorderLayout());
        panel.add(durationLabel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createAvailabilityBadge(int available) {
        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background color based on availability
                Color bgColor = available < 5 ? 
                    new Color(254, 226, 226) :  // red-100
                    new Color(220, 252, 231);   // green-100
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 6));
        badge.setPreferredSize(new Dimension(100, 32));

        JLabel icon = new JLabel("👤");
        icon.setFont(UITheme.SMALL_FONT);

        JLabel text = new JLabel(available + " seats left");
        text.setFont(new Font("Segoe UI", Font.BOLD, 11));
        text.setForeground(available < 5 ? UITheme.DANGER : UITheme.SUCCESS);

        badge.add(icon);
        badge.add(text);

        // Progress bar indicator
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.add(badge);
        
        // Capacity bar
        JPanel barPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int barHeight = 4;
                int y = (getHeight() - barHeight) / 2;
                
                // Background
                g2.setColor(new Color(229, 231, 235));
                g2.fillRoundRect(0, y, getWidth(), barHeight, 2, 2);
                
                // Fill based on occupancy
                int capacity = flight.getCapacity();
                int booked = capacity - available;
                int fillWidth = (int) ((double) booked / capacity * getWidth());
                
                Color fillColor = available < 5 ? UITheme.DANGER : UITheme.SUCCESS;
                g2.setColor(fillColor);
                g2.fillRoundRect(0, y, fillWidth, barHeight, 2, 2);
                
                g2.dispose();
            }
        };
        barPanel.setOpaque(false);
        barPanel.setPreferredSize(new Dimension(90, 10));
        barPanel.setMaximumSize(new Dimension(90, 10));
        barPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(barPanel);

        return wrapper;
    }

    private JPanel createPricePanel(double currentPrice, double basePrice) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Original price (strikethrough if different)
        if (currentPrice != basePrice) {
            JLabel originalLabel = new JLabel("£" + String.format("%.0f", basePrice));
            originalLabel.setFont(UITheme.SMALL_FONT);
            originalLabel.setForeground(UITheme.TEXT_MUTED);
            // Strikethrough effect
            originalLabel.setText("<html><strike>£" + String.format("%.0f", basePrice) + "</strike></html>");
            originalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            panel.add(originalLabel);
        }

        // Current price
        JLabel priceLabel = new JLabel("£" + String.format("%.0f", currentPrice));
        priceLabel.setFont(UITheme.PRICE_FONT);
        priceLabel.setForeground(UITheme.PRIMARY);
        priceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.add(priceLabel);

        return panel;
    }

    private JButton createSelectButton(int available) {
        JButton button;
        
        if (alreadyBooked) {
            button = new JButton("Booked") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(UITheme.MUTED_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            button.setForeground(UITheme.TEXT_MUTED);
            button.setEnabled(false);
        } else if (available <= 0) {
            button = new JButton("Full") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(UITheme.MUTED_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            button.setForeground(UITheme.TEXT_MUTED);
            button.setEnabled(false);
        } else {
            button = UITheme.createPrimaryButton("Select");
            button.addActionListener(e -> onSelect.run());
        }
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(90, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    // Getter for flight reference
    public Flight getFlight() {
        return flight;
    }
}
