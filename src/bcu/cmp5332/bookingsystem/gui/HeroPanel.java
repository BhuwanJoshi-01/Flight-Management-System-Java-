package bcu.cmp5332.bookingsystem.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Right-side hero panel for the login page with gradient background and statistics.
 * Displays promotional content and key metrics.
 * 
 * Responsive behavior:
 * - Decorative circles scale with panel size
 * - Stats panel adapts to available width
 * - Text remains readable at all sizes
 */
public class HeroPanel extends JPanel {
    
    private JPanel statsPanel;
    private boolean verticalStats = false;

    public HeroPanel() {
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(300, 400));
        initializeComponents();
        
        // Add resize listener for responsive stats layout
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateStatsLayout();
            }
        });
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

        // Draw decorative circles (scaled based on panel size)
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        
        // Top-right circle - scales with panel width
        g2d.setColor(UIStyles.PRIMARY_COLOR);
        int circleSize1 = Math.min(300, getWidth() / 2);
        g2d.fillOval(getWidth() - circleSize1 / 2, -50, circleSize1, circleSize1);
        
        // Bottom-left circle - scales with panel size
        g2d.setColor(UIStyles.ACCENT_CYAN);
        int circleSize2 = Math.min(250, getWidth() * 2 / 5);
        g2d.fillOval(-50, getHeight() - circleSize2, circleSize2, circleSize2);
        
        g2d.dispose();
    }

    private void initializeComponents() {
        // Main content panel (centered using GridBagLayout of parent)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(40, 30, 40, 30));

        // Airplane icon
        JLabel iconLabel = createAirplaneIcon();
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(25));

        // Title
        JLabel titleLabel = new JLabel("Your Journey Starts Here");
        titleLabel.setFont(UIStyles.HERO_TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(12));

        // Subtitle - using JTextArea for proper wrapping
        JTextArea subtitleText = new JTextArea("Book flights to destinations around the world with our modern, easy-to-use booking system.");
        subtitleText.setFont(UIStyles.HERO_SUBTITLE_FONT);
        subtitleText.setForeground(new Color(255, 255, 255, 200));
        subtitleText.setOpaque(false);
        subtitleText.setEditable(false);
        subtitleText.setFocusable(false);
        subtitleText.setLineWrap(true);
        subtitleText.setWrapStyleWord(true);
        subtitleText.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleText.setMaximumSize(new Dimension(380, 80));
        subtitleText.setPreferredSize(new Dimension(350, 60));
        
        // Center the text area
        JPanel subtitleWrapper = new JPanel();
        subtitleWrapper.setLayout(new BoxLayout(subtitleWrapper, BoxLayout.X_AXIS));
        subtitleWrapper.setOpaque(false);
        subtitleWrapper.add(Box.createHorizontalGlue());
        subtitleWrapper.add(subtitleText);
        subtitleWrapper.add(Box.createHorizontalGlue());
        contentPanel.add(subtitleWrapper);
        contentPanel.add(Box.createVerticalStrut(40));

        // Statistics panel
        statsPanel = createStatsPanel();
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(statsPanel);

        // Add content panel centered in the hero
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(contentPanel, gbc);
    }
    
    private void updateStatsLayout() {
        boolean shouldBeVertical = getWidth() < 350;
        
        if (shouldBeVertical != verticalStats) {
            verticalStats = shouldBeVertical;
            rebuildStatsPanel();
        }
    }
    
    private void rebuildStatsPanel() {
        statsPanel.removeAll();
        
        if (verticalStats) {
            statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
            statsPanel.add(createStatItem("500+", "Destinations"));
            statsPanel.add(Box.createVerticalStrut(15));
            statsPanel.add(createStatItem("50K+", "Happy Travelers"));
            statsPanel.add(Box.createVerticalStrut(15));
            statsPanel.add(createStatItem("24/7", "Support"));
        } else {
            statsPanel.setLayout(new GridLayout(1, 3, 20, 0));
            statsPanel.add(createStatItem("500+", "Destinations"));
            statsPanel.add(createStatItem("50K+", "Happy Travelers"));
            statsPanel.add(createStatItem("24/7", "Support"));
        }
        
        statsPanel.revalidate();
        statsPanel.repaint();
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
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);

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
