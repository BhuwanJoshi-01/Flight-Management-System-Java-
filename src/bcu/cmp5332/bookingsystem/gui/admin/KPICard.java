package bcu.cmp5332.bookingsystem.gui.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A KPI (Key Performance Indicator) card component matching the React design.
 * Features a colored left accent bar, value, label, and optional trend indicator.
 */
public class KPICard extends JPanel {
    
    private final String title;
    private final String value;
    private final Color accentColor;
    private final ImageIcon icon;
    private final Integer trendValue;
    private final Boolean trendPositive;
    
    /**
     * Creates a KPI card without trend.
     */
    public KPICard(String title, String value, Color accentColor, ImageIcon icon) {
        this(title, value, accentColor, icon, null, null);
    }
    
    /**
     * Creates a KPI card with trend indicator.
     */
    public KPICard(String title, String value, Color accentColor, ImageIcon icon, 
                   Integer trendValue, Boolean trendPositive) {
        this.title = title;
        this.value = value;
        this.accentColor = accentColor;
        this.icon = icon;
        this.trendValue = trendValue;
        this.trendPositive = trendPositive;
        
        initializeComponent();
    }
    
    private void initializeComponent() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setPreferredSize(new Dimension(180, 120));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        int radius = AdminTheme.CORNER_RADIUS;
        
        // Shadow
        g2.setColor(new Color(0, 0, 0, 10));
        g2.fillRoundRect(2, 4, w - 4, h - 4, radius, radius);
        
        // Background
        g2.setColor(AdminTheme.CARD);
        g2.fillRoundRect(0, 0, w - 2, h - 2, radius, radius);
        
        // Border
        g2.setColor(new Color(AdminTheme.BORDER.getRed(), AdminTheme.BORDER.getGreen(), 
                             AdminTheme.BORDER.getBlue(), 128));
        g2.drawRoundRect(0, 0, w - 3, h - 3, radius, radius);
        
        // Left accent bar
        g2.setColor(accentColor);
        g2.fillRoundRect(0, 0, 4, h - 2, 4, 4);
        
        g2.dispose();
        
        // Draw content
        drawContent(g);
    }
    
    private void drawContent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int padding = 20;
        int x = padding;
        int y = padding;
        
        // Icon in top right
        if (icon != null) {
            int iconSize = 24;
            int iconX = getWidth() - padding - iconSize - 2;
            int iconY = padding;
            
            // Icon background circle
            g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 25));
            g2.fillOval(iconX - 8, iconY - 4, 36, 36);
            
            // Draw icon
            g2.drawImage(icon.getImage(), iconX, iconY, iconSize, iconSize, null);
        }
        
        // Title (top)
        g2.setFont(AdminTheme.KPI_LABEL_FONT);
        g2.setColor(AdminTheme.MUTED_FOREGROUND);
        g2.drawString(title.toUpperCase(), x, y + 14);
        
        // Value
        g2.setFont(AdminTheme.KPI_VALUE_FONT);
        g2.setColor(AdminTheme.FOREGROUND);
        g2.drawString(value, x, y + 55);
        
        // Trend indicator
        if (trendValue != null && trendPositive != null) {
            int trendY = y + 75;
            
            g2.setFont(AdminTheme.SMALL_FONT);
            String arrow = trendPositive ? "↑" : "↓";
            String trendText = arrow + " " + trendValue + "%";
            
            g2.setColor(trendPositive ? AdminTheme.SUCCESS : AdminTheme.DESTRUCTIVE);
            g2.drawString(trendText, x, trendY);
            
            // "vs last month" text
            g2.setColor(AdminTheme.MUTED_FOREGROUND);
            int trendWidth = g2.getFontMetrics().stringWidth(trendText);
            g2.drawString(" vs last month", x + trendWidth, trendY);
        }
        
        g2.dispose();
    }
    
    /**
     * Factory method for sky-colored KPI card.
     */
    public static KPICard createSkyCard(String title, String value, ImageIcon icon) {
        return new KPICard(title, value, AdminTheme.ACCENT, icon);
    }
    
    /**
     * Factory method for success-colored KPI card.
     */
    public static KPICard createSuccessCard(String title, String value, ImageIcon icon, Integer trend) {
        return new KPICard(title, value, AdminTheme.SUCCESS, icon, trend, trend != null && trend > 0);
    }
    
    /**
     * Factory method for warning-colored KPI card.
     */
    public static KPICard createWarningCard(String title, String value, ImageIcon icon) {
        return new KPICard(title, value, AdminTheme.WARNING, icon);
    }
    
    /**
     * Factory method for navy-colored KPI card.
     */
    public static KPICard createNavyCard(String title, String value, ImageIcon icon) {
        return new KPICard(title, value, AdminTheme.PRIMARY, icon);
    }
}
