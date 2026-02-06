package bcu.cmp5332.bookingsystem.gui.admin;

import bcu.cmp5332.bookingsystem.gui.MainWindow;
import bcu.cmp5332.bookingsystem.gui.UIStyles;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Admin Flights Management Panel matching the React AdminFlights.tsx design exactly.
 * Features search, export, and a rich table with custom renderers for flight details,
 * occupancy progress bars, dynamic pricing, and action buttons.
 */
public class AdminFlightsPanel extends JPanel {
    
    private final MainWindow mainWindow;
    private final FlightBookingSystem fbs;
    
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable flightsTable;
    
    public AdminFlightsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.fbs = mainWindow.getFlightBookingSystem();
        
        setLayout(new BorderLayout());
        setBackground(AdminTheme.BACKGROUND);
        setBorder(new EmptyBorder(24, 24, 24, 24));
        
        initializeComponents();
        refreshData();
    }
    
    private void initializeComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Header with title and Add button
        JPanel header = createHeader();
        contentPanel.add(header);
        contentPanel.add(Box.createVerticalStrut(24));
        
        // Toolbar (Search and Export)
        JPanel toolbar = createToolbar();
        contentPanel.add(toolbar);
        contentPanel.add(Box.createVerticalStrut(16));
        
        // Table Panel (Inside a card)
        JPanel tableCard = createTablePanel();
        contentPanel.add(tableCard);
        
        // Wrap everything in a scroll pane
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
        
        JLabel title = new JLabel("Flights Management");
        title.setFont(AdminTheme.H1_FONT);
        title.setForeground(AdminTheme.FOREGROUND);
        titlePanel.add(title);
        
        JLabel subtitle = new JLabel("Manage all flights in the system");
        subtitle.setFont(AdminTheme.BODY_FONT);
        subtitle.setForeground(AdminTheme.MUTED_FOREGROUND);
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // Add Flight button (Blue)
        JButton addBtn = AdminTheme.createPrimaryButton("+ Add Flight");
        addBtn.setPreferredSize(new Dimension(130, 40));
        addBtn.addActionListener(e -> mainWindow.showAddFlightDialog());
        header.add(addBtn, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(16, 0));
        toolbar.setOpaque(false);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        // Search Field
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(AdminTheme.CARD);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminTheme.BORDER, 1),
            new EmptyBorder(0, 12, 0, 12)
        ));
        searchPanel.setPreferredSize(new Dimension(400, 44));
        
        JLabel searchIcon = new JLabel();
        if (UIStyles.ICON_SEARCH != null) {
            Image scaled = UIStyles.ICON_SEARCH.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            searchIcon.setIcon(new ImageIcon(scaled));
        }
        searchIcon.setForeground(AdminTheme.MUTED_FOREGROUND);
        searchPanel.add(searchIcon, BorderLayout.WEST);
        
        searchField = new JTextField();
        searchField.setFont(AdminTheme.BODY_FONT);
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        toolbar.add(searchPanel, BorderLayout.WEST);
        
        // Export Button
        JButton exportBtn = AdminTheme.createOutlineButton("Export");
        if (UIStyles.ICON_DOWNLOAD != null) {
            Image scaled = UIStyles.ICON_DOWNLOAD.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            exportBtn.setIcon(new ImageIcon(scaled));
            exportBtn.setIconTextGap(8);
        }
        exportBtn.setPreferredSize(new Dimension(110, 44));
        toolbar.add(exportBtn, BorderLayout.EAST);
        
        return toolbar;
    }
    
    private JPanel createTablePanel() {
        JPanel card = AdminTheme.createCard();
        card.setLayout(new BorderLayout());
        card.setBorder(null); // Table fills the card
        
        // Define columns
        String[] columns = {"Flight", "Route", "Date & Time", "Capacity", "Price", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only actions are interactive
            }
        };
        
        flightsTable = new JTable(tableModel);
        AdminTheme.styleTable(flightsTable);
        
        // Set custom renderers
        flightsTable.getColumnModel().getColumn(0).setCellRenderer(new FlightCellRenderer());
        flightsTable.getColumnModel().getColumn(1).setCellRenderer(new RouteCellRenderer());
        flightsTable.getColumnModel().getColumn(2).setCellRenderer(new DateTimeCellRenderer());
        flightsTable.getColumnModel().getColumn(3).setCellRenderer(new CapacityCellRenderer());
        flightsTable.getColumnModel().getColumn(4).setCellRenderer(new PriceCellRenderer());
        flightsTable.getColumnModel().getColumn(5).setCellRenderer(new StatusBadgeRenderer());
        flightsTable.getColumnModel().getColumn(6).setCellRenderer(new ActionsCellRenderer());
        flightsTable.getColumnModel().getColumn(6).setCellEditor(new ActionsEditor());
        
        // Column spacing
        flightsTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        flightsTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        flightsTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        flightsTable.getColumnModel().getColumn(3).setPreferredWidth(140);
        flightsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        flightsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        flightsTable.getColumnModel().getColumn(6).setPreferredWidth(160);
        
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AdminTheme.CARD);
        
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    public void refreshData() {
        loadFlights(fbs.getFlights());
    }
    
    private void filterTable() {
        String query = searchField.getText().toLowerCase();
        List<Flight> filtered = fbs.getFlights().stream()
            .filter(f -> f.getFlightNumber().toLowerCase().contains(query) ||
                        f.getOrigin().toLowerCase().contains(query) ||
                        f.getDestination().toLowerCase().contains(query))
            .toList();
        loadFlights(filtered);
    }
    
    private void loadFlights(List<Flight> flights) {
        tableModel.setRowCount(0);
        for (Flight f : flights) {
            tableModel.addRow(new Object[]{ f, f, f, f, f, "scheduled", f.getId() });
        }
    }
    
    // ==================== RENDERERS ====================
    
    /** Renders Flight No + Airline + Icon */
    private class FlightCellRenderer extends JPanel implements TableCellRenderer {
        private final JLabel flightNo = new JLabel();
        private final JLabel airline = new JLabel();
        private JPanel iconCircle;
        
        public FlightCellRenderer() {
            setOpaque(true);
            setLayout(new BorderLayout(12, 0));
            setBorder(new EmptyBorder(0, 16, 0, 8));
            
            iconCircle = AdminTheme.createIconCircle(UIStyles.ICON_PLANE, AdminTheme.ACCENT);
            add(iconCircle, BorderLayout.WEST);
            
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);
            
            flightNo.setFont(AdminTheme.LABEL_BOLD);
            airline.setFont(AdminTheme.SMALL_FONT);
            airline.setForeground(AdminTheme.MUTED_FOREGROUND);
            
            textPanel.add(Box.createVerticalGlue());
            textPanel.add(flightNo);
            textPanel.add(airline);
            textPanel.add(Box.createVerticalGlue());
            
            add(textPanel, BorderLayout.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus, int row, int column) {
            Flight f = (Flight) value;
            flightNo.setText(f.getFlightNumber());
            
            // Map prefix to airline
            String prefix = f.getFlightNumber().replaceAll("[0-9]", "");
            String airlineName = switch(prefix) {
                case "BA" -> "British Airways";
                case "EK" -> "Emirates";
                case "LH" -> "Lufthansa";
                case "AF" -> "Air France";
                case "QF" -> "Qantas";
                case "SQ" -> "Singapore Airlines";
                default -> "Commercial Flight";
            };
            airline.setText(airlineName);
            
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
    
    /** Renders ORG -> DST */
    private class RouteCellRenderer extends JPanel implements TableCellRenderer {
        private final JLabel route = new JLabel();
        
        public RouteCellRenderer() {
            setOpaque(true);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(0, 16, 0, 16));
            
            route.setFont(AdminTheme.LABEL_BOLD);
            add(route, BorderLayout.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus, int row, int column) {
            Flight f = (Flight) value;
            route.setText("<html>" + f.getOrigin() + " <font color='#64748b'>→</font> " + f.getDestination() + "</html>");
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
    
    /** Renders Date and mocked Time */
    private class DateTimeCellRenderer extends JPanel implements TableCellRenderer {
        private final JLabel dateLabel = new JLabel();
        private final JLabel timeLabel = new JLabel();
        
        public DateTimeCellRenderer() {
            setOpaque(true);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(0, 16, 0, 16));
            
            dateLabel.setFont(AdminTheme.SMALL_FONT);
            timeLabel.setFont(AdminTheme.CAPTION_FONT);
            timeLabel.setForeground(AdminTheme.MUTED_FOREGROUND);
            
            add(Box.createVerticalGlue());
            add(dateLabel);
            add(timeLabel);
            add(Box.createVerticalGlue());
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus, int row, int column) {
            Flight f = (Flight) value;
            dateLabel.setText(f.getDepartureDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
            timeLabel.setText("10:30 - 14:30"); // Mocked as per screenshot
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
    
    /** Renders Booked/Capacity + Progress Bar */
    private class CapacityCellRenderer extends JPanel implements TableCellRenderer {
        private final JLabel textLabel = new JLabel();
        private final JProgressBar progressBar = new JProgressBar(0, 100);
        
        public CapacityCellRenderer() {
            setOpaque(true);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(0, 16, 0, 24));
            
            textLabel.setFont(AdminTheme.SMALL_FONT);
            progressBar.setPreferredSize(new Dimension(80, 6));
            progressBar.setMaximumSize(new Dimension(80, 6));
            progressBar.setBorderPainted(false);
            progressBar.setOpaque(false);
            
            add(Box.createVerticalGlue());
            add(textLabel);
            add(Box.createVerticalStrut(4));
            add(progressBar);
            add(Box.createVerticalGlue());
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus, int row, int column) {
            Flight f = (Flight) value;
            int booked = f.getPassengers().size();
            int cap = f.getCapacity();
            double percent = cap > 0 ? (double) booked / cap : 0;
            
            Color statusColor = percent >= 0.8 ? AdminTheme.SUCCESS : percent >= 0.5 ? AdminTheme.WARNING : AdminTheme.DESTRUCTIVE;
            
            textLabel.setText("<html><b><font color='" + toHex(statusColor) + "'>" + booked + "</font></b> / " + cap + "</html>");
            progressBar.setValue((int)(percent * 100));
            progressBar.setForeground(statusColor);
            
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
        
        private String toHex(Color c) {
            return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
        }
    }
    
    /** Renders Price + Base Price */
    private class PriceCellRenderer extends JPanel implements TableCellRenderer {
        private final JLabel priceLabel = new JLabel();
        private final JLabel basePriceLabel = new JLabel();
        
        public PriceCellRenderer() {
            setOpaque(true);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(0, 16, 0, 16));
            
            priceLabel.setFont(AdminTheme.LABEL_BOLD);
            basePriceLabel.setFont(AdminTheme.CAPTION_FONT);
            basePriceLabel.setForeground(AdminTheme.MUTED_FOREGROUND);
            
            add(Box.createVerticalGlue());
            add(priceLabel);
            add(basePriceLabel);
            add(Box.createVerticalGlue());
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus, int row, int column) {
            Flight f = (Flight) value;
            double dynamicPrice = f.getPrice();
            double basePrice = f.getPrice() * 0.9; // Mocked
            
            priceLabel.setText(String.format("£%.0f", dynamicPrice));
            basePriceLabel.setText("<html><strike>£" + String.format("%.0f", basePrice) + "</strike></html>");
            
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
    
    /** Renders scheduled badge */
    private class StatusBadgeRenderer extends JPanel implements TableCellRenderer {
        private JLabel badge;
        
        public StatusBadgeRenderer() {
            setOpaque(true);
            setLayout(new FlowLayout(FlowLayout.LEFT, 16, 12));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            badge = AdminTheme.createStatusBadge("confirmed"); 
            badge.setText("scheduled");
            add(badge);
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
    
    /** Renders action icon buttons */
    private class ActionsCellRenderer extends JPanel implements TableCellRenderer {
        public ActionsCellRenderer() {
            setOpaque(true);
            setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 10));
            setBorder(new EmptyBorder(0, 0, 0, 16));
            
            add(createIconButton(UIStyles.ICON_EYE));
            add(createIconButton(UIStyles.ICON_USERS));
            add(createIconButton(UIStyles.ICON_EDIT));
            add(createIconButton(UIStyles.ICON_TRASH, AdminTheme.DESTRUCTIVE));
        }
        
        private JButton createIconButton(ImageIcon icon) { return createIconButton(icon, AdminTheme.MUTED_FOREGROUND); }
        private JButton createIconButton(ImageIcon icon, Color color) {
            JButton btn = new JButton();
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                ImageIcon tinted = UIStyles.tintIcon(new ImageIcon(scaled), color);
                btn.setIcon(tinted);
            }
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(32, 32));
            return btn;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
    
    /** Editor for action buttons */
    private class ActionsEditor extends DefaultCellEditor {
        private int flightId;
        public ActionsEditor() { 
            super(new JTextField()); 
            setClickCountToStart(1); // Single click to activate
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            flightId = (Integer) value;
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 10));
            panel.setBackground(table.getSelectionBackground());
            panel.setBorder(new EmptyBorder(0, 0, 0, 16));
            
            panel.add(createBtn(UIStyles.ICON_EYE, e -> { mainWindow.showFlightDetails(flightId); stopCellEditing(); }));
            panel.add(createBtn(UIStyles.ICON_USERS, e -> { mainWindow.showFlightPassengers(flightId); stopCellEditing(); }));
            panel.add(createBtn(UIStyles.ICON_EDIT, e -> { 
                mainWindow.showEditFlightDialog(flightId); 
                stopCellEditing(); 
            }));
            panel.add(createBtn(UIStyles.ICON_TRASH, AdminTheme.DESTRUCTIVE, e -> {
                if (AdminTheme.showConfirmDialog(table, "Delete this flight?", "Confirm")) {
                    mainWindow.deleteFlight(flightId);
                    refreshData();
                }
                stopCellEditing();
            }));
            return panel;
        }
        
        private JButton createBtn(ImageIcon icon, java.awt.event.ActionListener l) { return createBtn(icon, AdminTheme.MUTED_FOREGROUND, l); }
        private JButton createBtn(ImageIcon icon, Color c, java.awt.event.ActionListener l) {
            JButton btn = new JButton();
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                ImageIcon tinted = UIStyles.tintIcon(new ImageIcon(scaled), c);
                btn.setIcon(tinted);
            }
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.addActionListener(l);
            btn.setPreferredSize(new Dimension(32, 32));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }
        @Override public Object getCellEditorValue() { return flightId; }
    }
}
