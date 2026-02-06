package bcu.cmp5332.bookingsystem.gui.admin;

import bcu.cmp5332.bookingsystem.gui.MainWindow;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin Bookings Management Panel matching the React AdminBookings.tsx design.
 * Features status filters, booking table, and cancel functionality.
 */
public class AdminBookingsPanel extends JPanel {
    
    private final MainWindow mainWindow;
    private final FlightBookingSystem fbs;
    
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private DefaultTableModel tableModel;
    private JTable bookingsTable;
    
    // Status colors matching TSX
    private static final Color STATUS_CONFIRMED_BG = new Color(34, 197, 94, 25);
    private static final Color STATUS_CONFIRMED_TEXT = AdminTheme.SUCCESS;
    private static final Color STATUS_CANCELLED_BG = new Color(239, 68, 68, 25);
    private static final Color STATUS_CANCELLED_TEXT = AdminTheme.DESTRUCTIVE;
    private static final Color STATUS_PENDING_BG = new Color(245, 158, 11, 25);
    private static final Color STATUS_PENDING_TEXT = AdminTheme.WARNING;
    
    public AdminBookingsPanel(MainWindow mainWindow) {
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
        
        // Toolbar with search and filters
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
        
        JLabel title = new JLabel("Bookings Management");
        title.setFont(AdminTheme.H1_FONT);
        title.setForeground(AdminTheme.FOREGROUND);
        titlePanel.add(title);
        
        JLabel subtitle = new JLabel("View and manage all flight bookings");
        subtitle.setFont(AdminTheme.BODY_FONT);
        subtitle.setForeground(AdminTheme.MUTED_FOREGROUND);
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // Manual booking button
        JButton newBookingBtn = AdminTheme.createPrimaryButton("+ New Booking");
        newBookingBtn.setPreferredSize(new Dimension(140, 40));
        newBookingBtn.addActionListener(e -> mainWindow.showBookingDialog());
        header.add(newBookingBtn, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(16, 0));
        toolbar.setOpaque(false);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        // Left side: search and status filter
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);
        
        // Search field
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setPreferredSize(new Dimension(350, 44));
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
        leftPanel.add(searchPanel);
        
        // Status filter dropdown
        String[] statuses = {"All Status", "Confirmed", "Cancelled", "Completed"};
        statusFilter = new JComboBox<>(statuses);
        statusFilter.setFont(AdminTheme.BODY_FONT);
        statusFilter.setPreferredSize(new Dimension(150, 44));
        statusFilter.addActionListener(e -> filterTable());
        leftPanel.add(statusFilter);
        
        toolbar.add(leftPanel, BorderLayout.WEST);
        
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
        String[] columns = {"Booking ID", "Customer", "Flight", "Route", "Travel Date", "Status", "Total", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        
        bookingsTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(AdminTheme.CARD);
                }
                return c;
            }
        };
        AdminTheme.styleTable(bookingsTable);
        
        // Custom cell renderers
        bookingsTable.getColumnModel().getColumn(0).setCellRenderer(new BookingIdCellRenderer());
        bookingsTable.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());
        bookingsTable.getColumnModel().getColumn(6).setCellRenderer(new TotalCellRenderer());
        bookingsTable.getColumnModel().getColumn(7).setCellRenderer(new ActionsCellRenderer());
        bookingsTable.getColumnModel().getColumn(7).setCellEditor(new ActionsEditor());
        
        // Column widths
        bookingsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        bookingsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        bookingsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        bookingsTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        bookingsTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        bookingsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        bookingsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        bookingsTable.getColumnModel().getColumn(7).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AdminTheme.CARD);
        
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Refreshes the bookings table data.
     */
    public void refreshData() {
        tableModel.setRowCount(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        LocalDate today = fbs.getSystemDate();
        
        List<Booking> allBookings = getAllBookings();
        
        for (Booking booking : allBookings) {
            Customer customer = booking.getCustomer();
            Flight outboundFlight = booking.getOutboundFlight();
            
            String status;
            if (booking.isCancelled()) {
                status = "Cancelled";
            } else if (outboundFlight.getDepartureDate().isBefore(today)) {
                status = "Completed";
            } else {
                status = "Confirmed";
            }
            
            String route = outboundFlight.getOrigin() + " → " + outboundFlight.getDestination();
            if (booking.getReturnFlight() != null) {
                route += " (Return)";
            }
            
            double total = booking.getTotalPrice(today);
            
            tableModel.addRow(new Object[]{
                "BK" + String.format("%05d", booking.getOutboundFlight().getId() * 100 + customer.getId()),
                customer.getName(),
                outboundFlight.getFlightNumber(),
                route,
                outboundFlight.getDepartureDate().format(dateFormatter),
                status,
                total,
                new int[]{customer.getId(), outboundFlight.getId(), booking.isCancelled() ? 1 : 0}
            });
        }
    }
    
    private List<Booking> getAllBookings() {
        List<Booking> allBookings = new ArrayList<>();
        for (Customer customer : fbs.getCustomers()) {
            allBookings.addAll(customer.getBookings());
        }
        return allBookings;
    }
    
    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        String statusText = (String) statusFilter.getSelectedItem();
        
        tableModel.setRowCount(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        LocalDate today = fbs.getSystemDate();
        
        List<Booking> allBookings = getAllBookings();
        
        for (Booking booking : allBookings) {
            Customer customer = booking.getCustomer();
            Flight outboundFlight = booking.getOutboundFlight();
            
            String status;
            if (booking.isCancelled()) {
                status = "Cancelled";
            } else if (outboundFlight.getDepartureDate().isBefore(today)) {
                status = "Completed";
            } else {
                status = "Confirmed";
            }
            
            // Status filter
            if (!"All Status".equals(statusText) && !status.equals(statusText)) {
                continue;
            }
            
            // Search filter
            String route = outboundFlight.getOrigin() + " → " + outboundFlight.getDestination();
            if (!customer.getName().toLowerCase().contains(searchText) &&
                !outboundFlight.getFlightNumber().toLowerCase().contains(searchText) &&
                !route.toLowerCase().contains(searchText)) {
                continue;
            }
            
            if (booking.getReturnFlight() != null) {
                route += " (Return)";
            }
            
            double total = booking.getTotalPrice(today);
            
            tableModel.addRow(new Object[]{
                "BK" + String.format("%05d", booking.getOutboundFlight().getId() * 100 + customer.getId()),
                customer.getName(),
                outboundFlight.getFlightNumber(),
                route,
                outboundFlight.getDepartureDate().format(dateFormatter),
                status,
                total,
                new int[]{customer.getId(), outboundFlight.getId(), booking.isCancelled() ? 1 : 0}
            });
        }
    }
    
    // Booking ID renderer with icon
    private class BookingIdCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            panel.setOpaque(false);
            
            JLabel icon = new JLabel("📋");
            icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
            panel.add(icon);
            
            JLabel label = new JLabel(value.toString());
            label.setFont(AdminTheme.LABEL_BOLD);
            label.setForeground(AdminTheme.FOREGROUND);
            panel.add(label);
            
            return panel;
        }
    }
    
    // Status badge renderer
    private class StatusCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            String status = value.toString();
            Color bgColor, textColor;
            
            switch (status) {
                case "Confirmed":
                    bgColor = STATUS_CONFIRMED_BG;
                    textColor = STATUS_CONFIRMED_TEXT;
                    break;
                case "Cancelled":
                    bgColor = STATUS_CANCELLED_BG;
                    textColor = STATUS_CANCELLED_TEXT;
                    break;
                case "Completed":
                    bgColor = new Color(100, 116, 139, 25);
                    textColor = new Color(100, 116, 139);
                    break;
                default:
                    bgColor = STATUS_PENDING_BG;
                    textColor = STATUS_PENDING_TEXT;
            }
            
            final Color bg = bgColor;
            JLabel label = new JLabel(status) {
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
    
    // Total price renderer
    private class TotalCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            double total = (Double) value;
            JLabel label = new JLabel(String.format("£%.2f", total));
            label.setFont(AdminTheme.LABEL_BOLD);
            label.setForeground(AdminTheme.SUCCESS);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.setBorder(new EmptyBorder(0, 0, 0, 8));
            label.setOpaque(false);
            return label;
        }
    }
    
    // Actions renderer
    private class ActionsCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
            panel.setOpaque(false);
            
            int[] data = (int[]) value;
            boolean isCancelled = data[2] == 1;
            
            panel.add(createIconButton("👁"));
            
            if (!isCancelled) {
                panel.add(createIconButton("❌", AdminTheme.DESTRUCTIVE));
            }
            
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
        private int[] bookingData;
        
        public ActionsEditor() {
            super(new JTextField());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            bookingData = (int[]) value;
            boolean isCancelled = bookingData[2] == 1;
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
            panel.setOpaque(false);
            
            JButton viewBtn = createActionButton("👁", e -> {
                // Show booking details
                stopCellEditing();
            });
            panel.add(viewBtn);
            
            if (!isCancelled) {
                JButton cancelBtn = createActionButton("❌", AdminTheme.DESTRUCTIVE, e -> {
                    if (AdminTheme.showConfirmDialog(AdminBookingsPanel.this,
                        "Are you sure you want to cancel this booking? A cancellation fee will be applied.",
                        "Confirm Cancellation")) {
                        mainWindow.cancelBooking(bookingData[0], bookingData[1]);
                        refreshData();
                    }
                    stopCellEditing();
                });
                panel.add(cancelBtn);
            }
            
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
