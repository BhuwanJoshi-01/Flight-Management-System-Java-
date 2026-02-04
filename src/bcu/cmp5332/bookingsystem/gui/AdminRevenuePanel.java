package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin Revenue Analytics Panel showing revenue statistics, charts, and reports.
 */
public class AdminRevenuePanel extends JPanel {

    private final MainWindow parentWindow;
    private final FlightBookingSystem fbs;

    // KPI components
    private JLabel totalRevenueValue;
    private JLabel avgBookingValue;
    private JLabel totalBookingsValue;
    private JLabel activeCustomersValue;

    // Tables
    private JTable routesTable;
    private DefaultTableModel routesModel;
    private JTable customerTierTable;
    private DefaultTableModel tierModel;

    // Chart panel
    private JPanel chartPanel;

    public AdminRevenuePanel(MainWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.fbs = parentWindow.getFlightBookingSystem();

        setLayout(new BorderLayout(0, 20));
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeComponents();
    }

    private void initializeComponents() {
        // Header with export button
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);

        // KPI Cards
        JPanel kpiPanel = createKPIPanel();
        kpiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(kpiPanel);
        mainContent.add(Box.createVerticalStrut(20));

        // Charts and tables section
        JPanel chartsSection = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsSection.setOpaque(false);
        chartsSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        chartsSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        // Revenue chart
        JPanel chartCard = createChartCard();
        chartsSection.add(chartCard);

        // Top routes
        JPanel routesCard = createRoutesCard();
        chartsSection.add(routesCard);

        mainContent.add(chartsSection);
        mainContent.add(Box.createVerticalStrut(20));

        // Revenue by tier section
        JPanel tierSection = createTierRevenueSection();
        tierSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(tierSection);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
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

        JLabel titleLabel = new JLabel("Revenue & Analytics");
        titleLabel.setFont(UIStyles.TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Financial overview and insights");
        subtitleLabel.setFont(UIStyles.BODY_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        titlePanel.add(subtitleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setOpaque(false);

        // Period selector
        JComboBox<String> periodCombo = new JComboBox<>(new String[]{
            "This Month", "This Quarter", "This Year", "All Time"
        });
        periodCombo.setFont(UIStyles.BODY_FONT);
        periodCombo.addActionListener(e -> refreshData());
        actionsPanel.add(periodCombo);

        // Export button
        JButton exportBtn = UIStyles.createSecondaryButton("Export Report");
        exportBtn.addActionListener(e -> exportReport());
        actionsPanel.add(exportBtn);

        panel.add(actionsPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createKPIPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Total Revenue KPI
        JPanel revenueKPI = createKPICard("Total Revenue", "£0", UIStyles.ACCENT_COLOR, "+15%");
        totalRevenueValue = (JLabel) ((JPanel) ((JPanel) revenueKPI.getComponent(1)).getComponent(0)).getComponent(0);
        panel.add(revenueKPI);

        // Average Booking Value KPI
        JPanel avgKPI = createKPICard("Avg. Booking Value", "£0", UIStyles.PRIMARY_COLOR, "+5%");
        avgBookingValue = (JLabel) ((JPanel) ((JPanel) avgKPI.getComponent(1)).getComponent(0)).getComponent(0);
        panel.add(avgKPI);

        // Total Bookings KPI
        JPanel bookingsKPI = createKPICard("Total Bookings", "0", UIStyles.SECONDARY_COLOR, null);
        totalBookingsValue = (JLabel) ((JPanel) ((JPanel) bookingsKPI.getComponent(1)).getComponent(0)).getComponent(0);
        panel.add(bookingsKPI);

        // Active Customers KPI
        JPanel customersKPI = createKPICard("Active Customers", "0", new Color(243, 156, 18), "+8%");
        activeCustomersValue = (JLabel) ((JPanel) ((JPanel) customersKPI.getComponent(1)).getComponent(0)).getComponent(0);
        panel.add(customersKPI);

        return panel;
    }

    private JPanel createKPICard(String title, String value, Color accentColor, String trend) {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BorderLayout());

        // Left accent bar
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accentColor);
        accentBar.setPreferredSize(new Dimension(5, 0));
        card.add(accentBar, BorderLayout.WEST);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Value and trend row
        JPanel valueRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        valueRow.setOpaque(false);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UIStyles.KPI_VALUE_FONT);
        valueLabel.setForeground(UIStyles.TEXT_PRIMARY);
        valueRow.add(valueLabel);

        if (trend != null) {
            JLabel trendLabel = new JLabel(trend);
            trendLabel.setFont(UIStyles.SMALL_FONT);
            trendLabel.setForeground(trend.startsWith("+") ? UIStyles.ACCENT_COLOR : UIStyles.WARNING_COLOR);
            valueRow.add(trendLabel);
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIStyles.KPI_LABEL_FONT);
        titleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(valueRow);
        content.add(Box.createVerticalStrut(5));
        content.add(titleLabel);

        card.add(content, BorderLayout.CENTER);

        return card;
    }

    private JPanel createChartCard() {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("📊 Revenue Trend");
        titleLabel.setFont(UIStyles.SUBHEADER_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        header.add(titleLabel, BorderLayout.WEST);

        card.add(header, BorderLayout.NORTH);

        // Chart area (placeholder)
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarChart(g);
            }
        };
        chartPanel.setOpaque(false);
        chartPanel.setPreferredSize(new Dimension(0, 250));
        card.add(chartPanel, BorderLayout.CENTER);

        return card;
    }

    private void drawBarChart(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        int padding = 40;
        int barWidth = (width - 2 * padding) / 7;
        int maxBarHeight = height - 2 * padding;

        // Calculate revenue by day of week (simplified)
        Map<String, Double> revenueByDay = calculateRevenueByDay();
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        double maxRevenue = revenueByDay.values().stream().mapToDouble(d -> d).max().orElse(100);

        // Draw bars
        int x = padding;
        for (String day : days) {
            double revenue = revenueByDay.getOrDefault(day, 0.0);
            int barHeight = (int) ((revenue / Math.max(maxRevenue, 1)) * maxBarHeight);

            // Bar
            g2.setColor(UIStyles.PRIMARY_COLOR);
            g2.fillRoundRect(x + 5, height - padding - barHeight, barWidth - 10, barHeight, 5, 5);

            // Label
            g2.setColor(UIStyles.TEXT_SECONDARY);
            g2.setFont(UIStyles.SMALL_FONT);
            FontMetrics fm = g2.getFontMetrics();
            int labelX = x + (barWidth - fm.stringWidth(day)) / 2;
            g2.drawString(day, labelX, height - padding + 15);

            // Value on top
            String valueStr = "£" + (int) revenue;
            int valueX = x + (barWidth - fm.stringWidth(valueStr)) / 2;
            g2.drawString(valueStr, valueX, height - padding - barHeight - 5);

            x += barWidth;
        }

        g2.dispose();
    }

    private Map<String, Double> calculateRevenueByDay() {
        Map<String, Double> revenueByDay = new LinkedHashMap<>();
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : days) {
            revenueByDay.put(day, 0.0);
        }

        LocalDate today = fbs.getSystemDate();

        for (Customer customer : fbs.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                if (!booking.isCancelled() && booking.getBookingDate() != null) {
                    String dayOfWeek = booking.getBookingDate().getDayOfWeek().toString().substring(0, 3);
                    dayOfWeek = dayOfWeek.substring(0, 1) + dayOfWeek.substring(1).toLowerCase();
                    double price = DynamicPricing.calculatePrice(booking.getOutboundFlight(), today);
                    revenueByDay.merge(dayOfWeek, price, Double::sum);
                }
            }
        }

        return revenueByDay;
    }

    private JPanel createRoutesCard() {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("📈 Top Routes by Revenue");
        titleLabel.setFont(UIStyles.SUBHEADER_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        header.add(titleLabel, BorderLayout.WEST);

        card.add(header, BorderLayout.NORTH);

        // Routes table
        String[] columns = {"#", "Route", "Revenue"};
        routesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        routesTable = new JTable(routesModel);
        UIStyles.styleTable(routesTable);
        routesTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        routesTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        routesTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(routesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTierRevenueSection() {
        JPanel card = UIStyles.createCard();
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("👥 Revenue by Customer Tier");
        titleLabel.setFont(UIStyles.SUBHEADER_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        header.add(titleLabel, BorderLayout.WEST);

        card.add(header, BorderLayout.NORTH);

        // Tier table
        String[] columns = {"Tier", "Customers", "Bookings", "Revenue", "Avg. Spend"};
        tierModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTierTable = new JTable(tierModel);
        UIStyles.styleTable(customerTierTable);

        JScrollPane scrollPane = new JScrollPane(customerTierTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    /**
     * Refreshes all revenue data.
     */
    public void refreshData() {
        LocalDate today = fbs.getSystemDate();
        List<Customer> customers = fbs.getCustomers();

        // Calculate totals
        double totalRevenue = 0;
        int totalBookings = 0;
        Set<Integer> activeCustomerIds = new HashSet<>();

        Map<String, Double> revenueByRoute = new HashMap<>();
        Map<Customer.CustomerTier, TierStats> tierStats = new EnumMap<>(Customer.CustomerTier.class);

        for (Customer.CustomerTier tier : Customer.CustomerTier.values()) {
            tierStats.put(tier, new TierStats());
        }

        for (Customer customer : customers) {
            TierStats stats = tierStats.get(customer.getTier());
            stats.customerCount++;

            for (Booking booking : customer.getBookings()) {
                if (!booking.isCancelled()) {
                    Flight flight = booking.getOutboundFlight();
                    double price = DynamicPricing.calculatePrice(flight, today);

                    totalRevenue += price;
                    totalBookings++;
                    activeCustomerIds.add(customer.getId());

                    // Route revenue
                    String route = flight.getOrigin() + " → " + flight.getDestination();
                    revenueByRoute.merge(route, price, Double::sum);

                    // Tier stats
                    stats.bookingCount++;
                    stats.totalRevenue += price;
                }
            }
        }

        // Update KPIs
        totalRevenueValue.setText("£" + String.format("%,.0f", totalRevenue));
        avgBookingValue.setText("£" + String.format("%.0f", totalBookings > 0 ? totalRevenue / totalBookings : 0));
        totalBookingsValue.setText(String.valueOf(totalBookings));
        activeCustomersValue.setText(String.valueOf(activeCustomerIds.size()));

        // Update routes table
        routesModel.setRowCount(0);
        List<Map.Entry<String, Double>> sortedRoutes = revenueByRoute.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<String, Double> entry : sortedRoutes) {
            routesModel.addRow(new Object[]{
                "#" + rank++,
                entry.getKey(),
                "£" + String.format("%,.0f", entry.getValue())
            });
        }

        // Update tier table
        tierModel.setRowCount(0);
        for (Customer.CustomerTier tier : Customer.CustomerTier.values()) {
            TierStats stats = tierStats.get(tier);
            double avgSpend = stats.bookingCount > 0 ? stats.totalRevenue / stats.bookingCount : 0;

            tierModel.addRow(new Object[]{
                tier.name(),
                stats.customerCount,
                stats.bookingCount,
                "£" + String.format("%,.0f", stats.totalRevenue),
                "£" + String.format("%.0f", avgSpend)
            });
        }

        // Repaint chart
        chartPanel.repaint();
    }

    private void exportReport() {
        JOptionPane.showMessageDialog(this,
            "Revenue report would be exported to a file.\n" +
            "This feature simulates export functionality.",
            "Export Report", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Helper class for tier statistics.
     */
    private static class TierStats {
        int customerCount = 0;
        int bookingCount = 0;
        double totalRevenue = 0;
    }
}
