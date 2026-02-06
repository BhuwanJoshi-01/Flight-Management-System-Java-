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
 * Admin Customers Management Panel matching the React AdminCustomers.tsx design.
 * Features search, customer table with tier badges, and action buttons.
 */
public class AdminCustomersPanel extends JPanel {
    
    private final MainWindow mainWindow;
    private final FlightBookingSystem fbs;
    
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable customersTable;
    
    public AdminCustomersPanel(MainWindow mainWindow) {
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
        
        // Header
        JPanel header = createHeader();
        contentPanel.add(header);
        contentPanel.add(Box.createVerticalStrut(24));
        
        // Search and filters
        JPanel toolbar = createToolbar();
        contentPanel.add(toolbar);
        contentPanel.add(Box.createVerticalStrut(16));
        
        // Table
        JPanel tableCard = createTablePanel();
        contentPanel.add(tableCard);
        
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
        
        JLabel title = new JLabel("Customers Management");
        title.setFont(AdminTheme.H1_FONT);
        title.setForeground(AdminTheme.FOREGROUND);
        titlePanel.add(title);
        
        JLabel subtitle = new JLabel("Manage all registered customers");
        subtitle.setFont(AdminTheme.BODY_FONT);
        subtitle.setForeground(AdminTheme.MUTED_FOREGROUND);
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // Add Customer button
        JButton addBtn = AdminTheme.createPrimaryButton("+ Add Customer");
        addBtn.setPreferredSize(new Dimension(150, 40));
        addBtn.addActionListener(e -> mainWindow.showAddCustomerDialog());
        header.add(addBtn, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(16, 0));
        toolbar.setOpaque(false);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        // Search field
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setPreferredSize(new Dimension(400, 44));
        searchPanel.setBackground(AdminTheme.CARD);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminTheme.BORDER, 1),
            new EmptyBorder(0, 12, 0, 12)
        ));
        
        JLabel searchIcon = new JLabel("🔍 ");
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
        
        // Export button
        JButton exportBtn = AdminTheme.createOutlineButton("⬇ Export");
        exportBtn.setPreferredSize(new Dimension(100, 44));
        toolbar.add(exportBtn, BorderLayout.EAST);
        
        return toolbar;
    }
    
    private JPanel createTablePanel() {
        JPanel card = AdminTheme.createCard();
        card.setLayout(new BorderLayout());
        
        // Table columns
        String[] columns = {"Customer", "Contact", "Tier", "Points", "Bookings", "Member Since", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        customersTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(AdminTheme.CARD);
                }
                return c;
            }
        };
        AdminTheme.styleTable(customersTable);
        
        // Custom cell renderers
        customersTable.getColumnModel().getColumn(0).setCellRenderer(new CustomerCellRenderer());
        customersTable.getColumnModel().getColumn(2).setCellRenderer(new TierCellRenderer());
        customersTable.getColumnModel().getColumn(3).setCellRenderer(new PointsCellRenderer());
        customersTable.getColumnModel().getColumn(6).setCellRenderer(new ActionsCellRenderer());
        customersTable.getColumnModel().getColumn(6).setCellEditor(new ActionsEditor());
        
        // Column widths
        customersTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        customersTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        customersTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        customersTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        customersTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        customersTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        customersTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(customersTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AdminTheme.CARD);
        
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Refreshes the customers table data.
     */
    public void refreshData() {
        tableModel.setRowCount(0);
        
        List<Customer> customers = fbs.getCustomers();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        
        for (Customer customer : customers) {
            int bookingsCount = customer.getBookings().size();
            String contact = customer.getEmail() + "\n" + customer.getPhone();
            
            tableModel.addRow(new Object[]{
                customer.getName(),
                contact,
                customer.getTier().name(),
                customer.getRewardPoints(),
                bookingsCount,
                "Jan 1, 2024", // Would need registration date field
                customer.getId()
            });
        }
    }
    
    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        
        List<Customer> customers = fbs.getCustomers();
        
        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(searchText) ||
                customer.getEmail().toLowerCase().contains(searchText) ||
                customer.getPhone().contains(searchText)) {
                
                int bookingsCount = customer.getBookings().size();
                String contact = customer.getEmail() + "\n" + customer.getPhone();
                
                tableModel.addRow(new Object[]{
                    customer.getName(),
                    contact,
                    customer.getTier().name(),
                    customer.getRewardPoints(),
                    bookingsCount,
                    "Jan 1, 2024",
                    customer.getId()
                });
            }
        }
    }
    
    // Customer cell renderer (with icon)
    private class CustomerCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            panel.setOpaque(false);
            
            // Icon circle
            JPanel iconCircle = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(AdminTheme.ACCENT.getRed(), AdminTheme.ACCENT.getGreen(), 
                                         AdminTheme.ACCENT.getBlue(), 25));
                    g2.fillOval(0, 0, 32, 32);
                    g2.dispose();
                }
            };
            iconCircle.setOpaque(false);
            iconCircle.setPreferredSize(new Dimension(32, 32));
            iconCircle.setLayout(new GridBagLayout());
            
            JLabel icon = new JLabel("👤");
            icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            icon.setForeground(AdminTheme.ACCENT);
            iconCircle.add(icon);
            panel.add(iconCircle);
            
            JLabel nameLabel = new JLabel(value.toString());
            nameLabel.setFont(AdminTheme.LABEL_BOLD);
            nameLabel.setForeground(AdminTheme.FOREGROUND);
            panel.add(nameLabel);
            
            return panel;
        }
    }
    
    // Tier badge renderer
    private class TierCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            String tier = value.toString();
            Color bgColor, textColor;
            
            switch (tier.toUpperCase()) {
                case "PLATINUM":
                    bgColor = AdminTheme.TIER_PLATINUM_BG;
                    textColor = AdminTheme.TIER_PLATINUM_TEXT;
                    break;
                case "GOLD":
                    bgColor = AdminTheme.TIER_GOLD_BG;
                    textColor = AdminTheme.TIER_GOLD_TEXT;
                    break;
                case "SILVER":
                    bgColor = AdminTheme.TIER_SILVER_BG;
                    textColor = AdminTheme.TIER_SILVER_TEXT;
                    break;
                default:
                    bgColor = AdminTheme.TIER_BRONZE_BG;
                    textColor = AdminTheme.TIER_BRONZE_TEXT;
            }
            
            final Color bg = bgColor;
            JLabel label = new JLabel(tier.substring(0, 1) + tier.substring(1).toLowerCase()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            label.setFont(AdminTheme.SMALL_FONT);
            label.setForeground(textColor);
            label.setOpaque(false);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(new EmptyBorder(4, 8, 4, 8));
            
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            wrapper.setOpaque(false);
            wrapper.add(label);
            return wrapper;
        }
    }
    
    // Points renderer (with award icon)
    private class PointsCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            panel.setOpaque(false);
            
            JLabel icon = new JLabel("🏆");
            icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
            icon.setForeground(AdminTheme.WARNING);
            panel.add(icon);
            
            JLabel pointsLabel = new JLabel(String.format("%,d", (Integer) value));
            pointsLabel.setFont(AdminTheme.LABEL_BOLD);
            pointsLabel.setForeground(AdminTheme.FOREGROUND);
            panel.add(pointsLabel);
            
            return panel;
        }
    }
    
    // Actions renderer
    private class ActionsCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
            panel.setOpaque(false);
            
            panel.add(createIconButton("👁"));
            panel.add(createIconButton("✉"));
            panel.add(createIconButton("🗑", AdminTheme.DESTRUCTIVE));
            
            return panel;
        }
        
        private JButton createIconButton(String icon) {
            return createIconButton(icon, AdminTheme.MUTED_FOREGROUND);
        }
        
        private JButton createIconButton(String icon, Color color) {
            JButton btn = new JButton(icon);
            btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            btn.setForeground(color);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setPreferredSize(new Dimension(32, 32));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }
    }
    
    // Actions editor
    private class ActionsEditor extends DefaultCellEditor {
        private int customerId;
        
        public ActionsEditor() {
            super(new JTextField());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            customerId = (Integer) value;
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
            panel.setOpaque(false);
            
            JButton viewBtn = createActionButton("👁", e -> {
                mainWindow.showCustomerDetails(customerId);
                stopCellEditing();
            });
            
            JButton emailBtn = createActionButton("✉", e -> {
                // Send email functionality
                stopCellEditing();
            });
            
            JButton deleteBtn = createActionButton("🗑", AdminTheme.DESTRUCTIVE, e -> {
                if (AdminTheme.showConfirmDialog(AdminCustomersPanel.this, 
                    "Are you sure you want to delete this customer?", "Confirm Delete")) {
                    mainWindow.deleteCustomer(customerId);
                    refreshData();
                }
                stopCellEditing();
            });
            
            panel.add(viewBtn);
            panel.add(emailBtn);
            panel.add(deleteBtn);
            
            return panel;
        }
        
        private JButton createActionButton(String icon, java.awt.event.ActionListener listener) {
            return createActionButton(icon, AdminTheme.MUTED_FOREGROUND, listener);
        }
        
        private JButton createActionButton(String icon, Color color, java.awt.event.ActionListener listener) {
            JButton btn = new JButton(icon);
            btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            btn.setForeground(color);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setPreferredSize(new Dimension(32, 32));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(listener);
            return btn;
        }
    }
}
