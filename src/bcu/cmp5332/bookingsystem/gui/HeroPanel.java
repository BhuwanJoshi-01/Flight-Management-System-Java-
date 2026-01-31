package bcu.cmp5332.bookingsystem.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Right-side hero panel for the login page with gradient background and statistics.
 * Displays promotional content and key metrics.
 */
public class HeroPanel extends JPanel {

    public HeroPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 600));
        initializeComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, UIStyles.GRADIENT_NAVY_START,
            getWidth(), getHeight(), UIStyles.GRADIENT_NAVY_END
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw decorative circles (blur effect simulation)
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        
        // Top-right circle
        g2d.setColor(UIStyles.PRIMARY_COLOR);
        int circleSize1 = 300;
        g2d.fillOval(getWidth() - circleSize1 / 2, -50, circleSize1, circleSize1);
        
        // Bottom-left circle
        g2d.setColor(UIStyles.ACCENT_CYAN);
        int circleSize2 = 250;
        g2d.fillOval(-50, getHeight() - circleSize2, circleSize2, circleSize2);
        
        g2d.dispose();
    }

    private void initializeComponents() {
        // Main content panel (centered)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(60, 50, 60, 50));

        // Airplane icon
        JLabel iconLabel = createAirplaneIcon();
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Title
        JLabel titleLabel = new JLabel("Your Journey Starts Here");
        titleLabel.setFont(UIStyles.HERO_TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Subtitle
        JLabel subtitleLabel = new JLabel("<html><div style='text-align: center; width: 350px;'>" +
            "Book flights to destinations around the world with our modern, easy-to-use booking system." +
            "</div></html>");
        subtitleLabel.setFont(UIStyles.HERO_SUBTITLE_FONT);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(50));

        // Statistics panel
        JPanel statsPanel = createStatsPanel();
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(statsPanel);

        // Center the content vertically
        add(contentPanel, BorderLayout.CENTER);
    }

    private JLabel createAirplaneIcon() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circle background
                g2.setColor(UIStyles.PRIMARY_COLOR);
                g2.fillOval(0, 0, 70, 70);

                // Draw airplane symbol
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 32));
                FontMetrics fm = g2.getFontMetrics();
                String airplane = "✈";
                int x = (70 - fm.stringWidth(airplane)) / 2;
                int y = (70 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(airplane, x, y);

                g2.dispose();
            }
        };
        label.setPreferredSize(new Dimension(70, 70));
        label.setMinimumSize(new Dimension(70, 70));
        label.setMaximumSize(new Dimension(70, 70));
        return label;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 30, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 80));

        // Stat 1: Destinations
        panel.add(createStatItem("500+", "Destinations"));
        
        // Stat 2: Happy Travelers
        panel.add(createStatItem("50K+", "Happy Travelers"));
        
        // Stat 3: Support
        panel.add(createStatItem("24/7", "Support"));

        return panel;
    }

    private JPanel createStatItem(String value, String label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UIStyles.STAT_VALUE_FONT);
        valueLabel.setForeground(UIStyles.ACCENT_CYAN);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(UIStyles.STAT_LABEL_FONT);
        textLabel.setForeground(new Color(255, 255, 255, 150));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(valueLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(textLabel);

        return panel;
    }
}
