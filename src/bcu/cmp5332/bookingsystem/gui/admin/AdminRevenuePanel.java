package bcu.cmp5332.bookingsystem.gui.admin;

import bcu.cmp5332.bookingsystem.gui.MainWindow;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

/**
 * Admin Revenue Analytics Panel matching the React AdminRevenue.tsx design.
 * Features revenue KPIs, chart placeholders, and top routes analysis.
 */
public class AdminRevenuePanel extends JPanel {
    
    private final MainWindow mainWindow;
    private final FlightBookingSystem fbs;
    
    // Revenue data
    private double totalRevenue;
    private double monthlyRevenue;
    private double avgBookingValue;
    private int totalBookingsCount;
    private Map<String, Double> routeRevenue;
    
    public AdminRevenuePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.fbs = mainWindow.getFlightBookingSystem();
        
        setLayout(new BorderLayout());
        setBackground(AdminTheme.BACKGROUND);
        setBorder(new EmptyBorder(24, 24, 24, 24));
        
        calculateRevenueData();
        initializeComponents();
    }
    
    private void calculateRevenueData() {
        totalRevenue = 0;
        monthlyRevenue = 0;
        totalBookingsCount = 0;
        routeRevenue = new HashMap<>();
        
        LocalDate today = fbs.getSystemDate();
        LocalDate monthStart = today.withDayOfMonth(1);
        
        for (Customer customer : fbs.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                if (!booking.isCancelled()) {
                    double price = booking.getTotalPrice(today);
                    totalRevenue += price;
                    totalBookingsCount++;;
                    
                    // Monthly revenue
                    if (!booking.getBookingDate().isBefore(monthStart)) {
                        monthlyRevenue += price;
                    }
                    
                    // Route revenue
                    Flight flight = booking.getOutboundFlight();
                    String route = flight.getOrigin() + " → " + flight.getDestination();
                    routeRevenue.merge(route, price, Double::sum);
                }
            }
        }
        
        avgBookingValue = totalBookingsCount > 0 ? totalRevenue / totalBookingsCount : 0;
    }
    
    private void initializeComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Header
        JPanel header = createHeader();
        contentPanel.add(header);
        contentPanel.add(Box.createVerticalStrut(24));
        
        // KPI Cards Row
        JPanel kpiRow = createKPIRow();
        contentPanel.add(kpiRow);
        contentPanel.add(Box.createVerticalStrut(24));
        
        // Charts Section
        JPanel chartsSection = createChartsSection();
        contentPanel.add(chartsSection);
        contentPanel.add(Box.createVerticalStrut(24));
        
        // Bottom section: Top Routes + Revenue Breakdown
        JPanel bottomSection = createBottomSection();
        contentPanel.add(bottomSection);
        
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
        
        JLabel title = new JLabel("Revenue Analytics");
        title.setFont(AdminTheme.H1_FONT);
        title.setForeground(AdminTheme.FOREGROUND);
        titlePanel.add(title);
        
        JLabel subtitle = new JLabel("Track financial performance and revenue metrics");
        subtitle.setFont(AdminTheme.BODY_FONT);
        subtitle.setForeground(AdminTheme.MUTED_FOREGROUND);
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // Date range selector
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        datePanel.setOpaque(false);
        
        String[] periods = {"Last 7 Days", "Last 30 Days", "Last 90 Days", "This Year"};
        JComboBox<String> periodSelector = new JComboBox<>(periods);
        periodSelector.setSelectedIndex(1);
        periodSelector.setFont(AdminTheme.BODY_FONT);
        periodSelector.setPreferredSize(new Dimension(150, 40));
        datePanel.add(periodSelector);
        
        JButton exportBtn = AdminTheme.createOutlineButton("⬇ Export Report");
        exportBtn.setPreferredSize(new Dimension(130, 40));
        datePanel.add(exportBtn);
        
        header.add(datePanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createKPIRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        
        DecimalFormat currencyFormat = new DecimalFormat("£#,##0.00");
        DecimalFormat numberFormat = new DecimalFormat("#,##0");
        
        // Total Revenue
        row.add(KPICard.createSuccessCard(
            "Total Revenue",
            currencyFormat.format(totalRevenue),
            null,
            12
        ));
        
        // Monthly Revenue
        row.add(KPICard.createSkyCard(
            "Monthly Revenue",
            currencyFormat.format(monthlyRevenue),
            null
        ));
        
        // Average Booking Value
        row.add(KPICard.createWarningCard(
            "Avg. Booking Value",
            currencyFormat.format(avgBookingValue),
            null
        ));
        
        // Total Bookings
        row.add(KPICard.createNavyCard(
            "Total Bookings",
            numberFormat.format(totalBookingsCount),
            null
        ));
        
        return row;
    }
    
    private JPanel createChartsSection() {
        JPanel section = new JPanel(new GridLayout(1, 2, 16, 0));
        section.setOpaque(false);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        
        // Revenue Trend Chart
        section.add(createChartCard("Revenue Trend", "📈", createTrendChartPlaceholder()));
        
        // Monthly Comparison Chart
        section.add(createChartCard("Monthly Comparison", "📊", createBarChartPlaceholder()));
        
        return section;
    }
    
    private JPanel createChartCard(String title, String icon, JPanel chartContent) {
        JPanel card = AdminTheme.createCard();
        card.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 16, 12, 16));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        header.add(iconLabel);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AdminTheme.H4_FONT);
        titleLabel.setForeground(AdminTheme.FOREGROUND);
        header.add(titleLabel);
        
        card.add(header, BorderLayout.NORTH);
        card.add(chartContent, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createTrendChartPlaceholder() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth() - 40;
                int h = getHeight() - 60;
                int x = 20;
                int y = 30;
                
                // Background gradient
                GradientPaint gradient = new GradientPaint(
                    x, y, new Color(AdminTheme.SUCCESS.getRed(), AdminTheme.SUCCESS.getGreen(), 
                                   AdminTheme.SUCCESS.getBlue(), 50),
                    x, y + h, new Color(AdminTheme.SUCCESS.getRed(), AdminTheme.SUCCESS.getGreen(), 
                                       AdminTheme.SUCCESS.getBlue(), 10)
                );
                
                // Draw simulated line chart
                g2.setColor(AdminTheme.BORDER);
                g2.drawRect(x, y, w, h);
                
                // Grid lines
                g2.setColor(new Color(AdminTheme.BORDER.getRed(), AdminTheme.BORDER.getGreen(), 
                                     AdminTheme.BORDER.getBlue(), 100));
                for (int i = 1; i < 5; i++) {
                    int lineY = y + (h * i / 5);
                    g2.drawLine(x, lineY, x + w, lineY);
                }
                
                // Line chart simulation
                g2.setStroke(new BasicStroke(2));
                g2.setColor(AdminTheme.SUCCESS);
                
                int[] points = {70, 45, 65, 55, 80, 70, 90, 75, 85, 95, 80, 100};
                int pointW = w / (points.length - 1);
                
                int[] xPoints = new int[points.length];
                int[] yPoints = new int[points.length];
                
                for (int i = 0; i < points.length; i++) {
                    xPoints[i] = x + i * pointW;
                    yPoints[i] = y + h - (h * points[i] / 100);
                }
                
                // Fill area
                int[] fillXPoints = new int[points.length + 2];
                int[] fillYPoints = new int[points.length + 2];
                System.arraycopy(xPoints, 0, fillXPoints, 0, points.length);
                System.arraycopy(yPoints, 0, fillYPoints, 0, points.length);
                fillXPoints[points.length] = x + w;
                fillYPoints[points.length] = y + h;
                fillXPoints[points.length + 1] = x;
                fillYPoints[points.length + 1] = y + h;
                
                g2.setPaint(gradient);
                g2.fillPolygon(fillXPoints, fillYPoints, fillXPoints.length);
                
                g2.setColor(AdminTheme.SUCCESS);
                g2.drawPolyline(xPoints, yPoints, points.length);
                
                // Data points
                for (int i = 0; i < points.length; i++) {
                    g2.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
                }
                
                g2.dispose();
            }
        };
    }
    
    private JPanel createBarChartPlaceholder() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth() - 40;
                int h = getHeight() - 60;
                int x = 20;
                int y = 30;
                
                // Bar chart
                String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
                int[] values = {65, 80, 55, 90, 75, 85};
                int barW = (w - (months.length - 1) * 8) / months.length;
                
                for (int i = 0; i < months.length; i++) {
                    int barH = h * values[i] / 100;
                    int barX = x + i * (barW + 8);
                    int barY = y + h - barH;
                    
                    // Bar gradient
                    GradientPaint gradient = new GradientPaint(
                        barX, barY, AdminTheme.ACCENT,
                        barX, barY + barH, new Color(AdminTheme.ACCENT.getRed(), 
                                                     AdminTheme.ACCENT.getGreen(), 
                                                     AdminTheme.ACCENT.getBlue(), 150)
                    );
                    g2.setPaint(gradient);
                    g2.fillRoundRect(barX, barY, barW, barH, 6, 6);
                    
                    // Month label
                    g2.setColor(AdminTheme.MUTED_FOREGROUND);
                    g2.setFont(AdminTheme.CAPTION_FONT);
                    FontMetrics fm = g2.getFontMetrics();
                    int labelX = barX + (barW - fm.stringWidth(months[i])) / 2;
                    g2.drawString(months[i], labelX, y + h + 15);
                }
                
                g2.dispose();
            }
        };
    }
    
    private JPanel createBottomSection() {
        JPanel section = new JPanel(new GridLayout(1, 2, 16, 0));
        section.setOpaque(false);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        
        // Top Routes
        section.add(createTopRoutesCard());
        
        // Revenue Breakdown
        section.add(createRevenueBreakdownCard());
        
        return section;
    }
    
    private JPanel createTopRoutesCard() {
        JPanel card = AdminTheme.createCard();
        card.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 16, 12, 16));
        
        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        headerLeft.setOpaque(false);
        
        JLabel icon = new JLabel("🛫");
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        headerLeft.add(icon);
        
        JLabel titleLabel = new JLabel("Top Routes by Revenue");
        titleLabel.setFont(AdminTheme.H4_FONT);
        titleLabel.setForeground(AdminTheme.FOREGROUND);
        headerLeft.add(titleLabel);
        
        header.add(headerLeft, BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);
        
        // Routes list
        JPanel routesList = new JPanel();
        routesList.setLayout(new BoxLayout(routesList, BoxLayout.Y_AXIS));
        routesList.setOpaque(false);
        routesList.setBorder(new EmptyBorder(0, 16, 16, 16));
        
        // Sort routes by revenue
        List<Map.Entry<String, Double>> sortedRoutes = new ArrayList<>(routeRevenue.entrySet());
        sortedRoutes.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        double maxRevenue = sortedRoutes.isEmpty() ? 1 : sortedRoutes.get(0).getValue();
        
        int count = 0;
        for (Map.Entry<String, Double> entry : sortedRoutes) {
            if (count++ >= 5) break;
            routesList.add(createRouteItem(entry.getKey(), entry.getValue(), maxRevenue));
            routesList.add(Box.createVerticalStrut(12));
        }
        
        // If no routes, show placeholder
        if (sortedRoutes.isEmpty()) {
            JLabel noData = new JLabel("No route data available");
            noData.setFont(AdminTheme.BODY_FONT);
            noData.setForeground(AdminTheme.MUTED_FOREGROUND);
            noData.setAlignmentX(CENTER_ALIGNMENT);
            routesList.add(noData);
        }
        
        card.add(routesList, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRouteItem(String route, double revenue, double maxRevenue) {
        JPanel item = new JPanel(new BorderLayout(0, 4));
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        // Top row: route and revenue
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        
        JLabel routeLabel = new JLabel(route);
        routeLabel.setFont(AdminTheme.LABEL_FONT);
        routeLabel.setForeground(AdminTheme.FOREGROUND);
        topRow.add(routeLabel, BorderLayout.WEST);
        
        JLabel revenueLabel = new JLabel(String.format("£%,.2f", revenue));
        revenueLabel.setFont(AdminTheme.LABEL_BOLD);
        revenueLabel.setForeground(AdminTheme.SUCCESS);
        topRow.add(revenueLabel, BorderLayout.EAST);
        
        item.add(topRow, BorderLayout.NORTH);
        
        // Progress bar
        double progress = revenue / maxRevenue;
        JPanel progressBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(new Color(AdminTheme.ACCENT.getRed(), AdminTheme.ACCENT.getGreen(), 
                                     AdminTheme.ACCENT.getBlue(), 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                
                // Progress
                int progressWidth = (int) (getWidth() * progress);
                g2.setColor(AdminTheme.ACCENT);
                g2.fillRoundRect(0, 0, progressWidth, getHeight(), 4, 4);
                
                g2.dispose();
            }
        };
        progressBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 8));
        progressBar.setOpaque(false);
        
        item.add(progressBar, BorderLayout.SOUTH);
        
        return item;
    }
    
    private JPanel createRevenueBreakdownCard() {
        JPanel card = AdminTheme.createCard();
        card.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 16, 12, 16));
        
        JLabel icon = new JLabel("🎯");
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        header.add(icon);
        
        JLabel titleLabel = new JLabel("Revenue Breakdown");
        titleLabel.setFont(AdminTheme.H4_FONT);
        titleLabel.setForeground(AdminTheme.FOREGROUND);
        header.add(titleLabel);
        
        card.add(header, BorderLayout.NORTH);
        
        // Breakdown items
        JPanel breakdownList = new JPanel();
        breakdownList.setLayout(new BoxLayout(breakdownList, BoxLayout.Y_AXIS));
        breakdownList.setOpaque(false);
        breakdownList.setBorder(new EmptyBorder(0, 16, 16, 16));
        
        // Calculate breakdown
        double flightRevenue = totalRevenue * 0.85;
        double addonsRevenue = totalRevenue * 0.10;
        double feesRevenue = totalRevenue * 0.05;
        
        breakdownList.add(createBreakdownItem("Flight Tickets", flightRevenue, 85, AdminTheme.ACCENT));
        breakdownList.add(Box.createVerticalStrut(16));
        breakdownList.add(createBreakdownItem("Add-ons & Services", addonsRevenue, 10, AdminTheme.SUCCESS));
        breakdownList.add(Box.createVerticalStrut(16));
        breakdownList.add(createBreakdownItem("Fees & Charges", feesRevenue, 5, AdminTheme.WARNING));
        
        card.add(breakdownList, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createBreakdownItem(String label, double amount, int percentage, Color color) {
        JPanel item = new JPanel(new BorderLayout(0, 8));
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        // Label row
        JPanel labelRow = new JPanel(new BorderLayout());
        labelRow.setOpaque(false);
        
        JPanel leftLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftLabel.setOpaque(false);
        
        // Color dot
        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 4, 10, 10);
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(10, 18));
        leftLabel.add(dot);
        
        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(AdminTheme.LABEL_FONT);
        nameLabel.setForeground(AdminTheme.FOREGROUND);
        leftLabel.add(nameLabel);
        
        labelRow.add(leftLabel, BorderLayout.WEST);
        
        JLabel amountLabel = new JLabel(String.format("£%,.2f (%d%%)", amount, percentage));
        amountLabel.setFont(AdminTheme.LABEL_BOLD);
        amountLabel.setForeground(AdminTheme.FOREGROUND);
        labelRow.add(amountLabel, BorderLayout.EAST);
        
        item.add(labelRow, BorderLayout.NORTH);
        
        // Progress bar
        final Color barColor = color;
        JPanel progressBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(new Color(barColor.getRed(), barColor.getGreen(), 
                                     barColor.getBlue(), 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                
                // Progress
                int progressWidth = getWidth() * percentage / 100;
                g2.setColor(barColor);
                g2.fillRoundRect(0, 0, progressWidth, getHeight(), 6, 6);
                
                g2.dispose();
            }
        };
        progressBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 12));
        progressBar.setOpaque(false);
        
        item.add(progressBar, BorderLayout.SOUTH);
        
        return item;
    }
    
    /**
     * Refreshes the revenue data.
     */
    public void refreshData() {
        calculateRevenueData();
        removeAll();
        initializeComponents();
        revalidate();
        repaint();
    }
}
