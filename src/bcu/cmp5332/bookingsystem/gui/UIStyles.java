package bcu.cmp5332.bookingsystem.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Centralized styling constants and utility methods for consistent UI appearance.
 */
public class UIStyles {
    
    // Color Palette
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Blue
    public static final Color PRIMARY_DARK = new Color(31, 97, 141);        // Darker blue
    public static final Color SECONDARY_COLOR = new Color(52, 73, 94);      // Dark gray-blue
    public static final Color ACCENT_COLOR = new Color(46, 204, 113);       // Green
    public static final Color WARNING_COLOR = new Color(231, 76, 60);       // Red
    public static final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Light gray
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);         // Dark text
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);    // Gray text
    public static final Color TABLE_ALTERNATE = new Color(245, 248, 250);   // Light blue-gray
    public static final Color SIDEBAR_COLOR = new Color(44, 62, 80);        // Dark sidebar

    // Login Page Colors
    public static final Color GRADIENT_NAVY_START = new Color(15, 23, 42);    // Dark navy
    public static final Color GRADIENT_NAVY_END = new Color(30, 41, 59);      // Lighter navy
    public static final Color ACCENT_CYAN = new Color(34, 211, 238);          // Cyan for stats
    public static final Color INPUT_BORDER = new Color(203, 213, 225);        // Light gray border
    public static final Color INPUT_FOCUS_BORDER = new Color(59, 130, 246);   // Blue focus border
    public static final Color LINK_COLOR = new Color(59, 130, 246);           // Blue link
    public static final Color TOGGLE_ACTIVE_BG = Color.WHITE;                  // Active toggle bg
    public static final Color TOGGLE_INACTIVE_BG = new Color(241, 245, 249);  // Inactive toggle bg
    public static final Color TOGGLE_BORDER = new Color(226, 232, 240);       // Toggle border
    public static final Color DEMO_BOX_BG = new Color(248, 250, 252);         // Demo credentials bg
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font KPI_VALUE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font KPI_LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    // Login Page Fonts
    public static final Font LOGIN_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font LOGIN_SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font LOGIN_LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font LOGIN_INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font LOGIN_LINK_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font HERO_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font HERO_SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font STAT_VALUE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font STAT_LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    // Dimensions
    public static final int SIDEBAR_WIDTH = 200;
    public static final int CARD_PADDING = 20;
    public static final int SECTION_SPACING = 15;
    
    // Borders
    public static final Border CARD_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
        new EmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING)
    );

    // ---------------- Icons ----------------
    public static final ImageIcon ICON_DASHBOARD = new ImageIcon(
            UIStyles.class.getResource("/icons/dashboard.png")
    );
    public static final ImageIcon ICON_FLIGHTS = new ImageIcon(
            UIStyles.class.getResource("/icons/flight.png")
    );
    public static final ImageIcon ICON_CUSTOMERS = new ImageIcon(
            UIStyles.class.getResource("/icons/customers.png")
    );
    public static final ImageIcon ICON_BOOKINGS = new ImageIcon(
            UIStyles.class.getResource("/icons/bookings.png")
    );
    public static final ImageIcon ICON_EXIT = new ImageIcon(
            UIStyles.class.getResource("/icons/exit.png")
    );


    public static final Border SECTION_BORDER = new EmptyBorder(SECTION_SPACING, SECTION_SPACING, SECTION_SPACING, SECTION_SPACING);
    
    /**
     * Creates a styled primary button.
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 36));
        return button;
    }
    
    /**
     * Creates a styled secondary button.
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 36));
        return button;
    }
    
    /**
     * Creates a styled danger button.
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(WARNING_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 36));
        return button;
    }
    
    /**
     * Creates a sidebar navigation button.
     */
    /**
     * Creates a sidebar navigation button with optional icon.
     */
    public static JButton createSidebarButton(String text, ImageIcon icon, boolean selected) {
        JButton button = new JButton(text, icon);   // set icon here
        button.setFont(BODY_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(selected ? PRIMARY_COLOR : SIDEBAR_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);  // space between icon and text
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return button;
    }


    /**
     * Styles a JTable with alternating row colors and modern appearance.
     */
    public static void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setBackground(CARD_BACKGROUND);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(SECONDARY_COLOR);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_BACKGROUND : TABLE_ALTERNATE);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }
    
    /**
     * Creates a styled card panel.
     */
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(CARD_BORDER);
        return card;
    }

    // Emoji icons using Unicode hex (surrogate pairs)


    /**
     * Shows a styled success dialog.
     */
    public static void showSuccessDialog(Component parent, String message, String title) {
        JPanel panel = createDialogPanel(message, ACCENT_COLOR, "SUCCESS");
        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Shows a styled error dialog.
     */
    public static void showErrorDialog(Component parent, String message, String title) {
        JPanel panel = createDialogPanel(message, WARNING_COLOR, "ERROR");
        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Shows a styled info dialog.
     */
    public static void showInfoDialog(Component parent, String message, String title) {
        JPanel panel = createDialogPanel(message, PRIMARY_COLOR, "INFO");
        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Shows a styled confirmation dialog.
     * @return true if user confirmed, false otherwise
     */
    public static boolean showConfirmDialog(Component parent, String message, String title) {
        JPanel panel = createDialogPanel(message, new Color(243, 156, 18), "?");
        int result = JOptionPane.showConfirmDialog(parent, panel, title,
            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Shows a styled warning confirmation dialog.
     * @return true if user confirmed, false otherwise
     */
    public static boolean showWarningConfirmDialog(Component parent, String message, String title) {
        JPanel panel = createDialogPanel(message, WARNING_COLOR, "!");
        int result = JOptionPane.showConfirmDialog(parent, panel, title,
            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    private static JPanel createDialogPanel(String message, Color accentColor, String iconText) {
        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Icon circle
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillOval(0, 0, 50, 50);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                FontMetrics fm = g2.getFontMetrics();
                int x = (50 - fm.stringWidth(iconText)) / 2;
                int y = (50 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(iconText, x, y);
            }
        };
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setBackground(CARD_BACKGROUND);

        // Message
        JLabel messageLabel = new JLabel("<html><body style='width: 250px'>" + message + "</body></html>");
        messageLabel.setFont(BODY_FONT);
        messageLabel.setForeground(TEXT_PRIMARY);

        panel.add(iconPanel, BorderLayout.WEST);
        panel.add(messageLabel, BorderLayout.CENTER);

        return panel;
    }

    // ==================== Login Page Utility Methods ====================

    /**
     * Creates a styled text field for login forms.
     */
    public static JTextField createLoginTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(TEXT_SECONDARY);
                    g2.setFont(LOGIN_INPUT_FONT);
                    g2.drawString(placeholder, getInsets().left + 5, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        field.setFont(LOGIN_INPUT_FONT);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(CARD_BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INPUT_BORDER, 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        field.setPreferredSize(new Dimension(320, 48));
        return field;
    }

    /**
     * Creates a styled password field for login forms.
     */
    public static JPasswordField createLoginPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            private boolean showPlaceholder = true;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !hasFocus() && showPlaceholder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(TEXT_SECONDARY);
                    g2.setFont(LOGIN_INPUT_FONT);
                    g2.drawString(placeholder, getInsets().left + 5, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        field.setFont(LOGIN_INPUT_FONT);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(CARD_BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INPUT_BORDER, 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        field.setPreferredSize(new Dimension(320, 48));
        return field;
    }

    /**
     * Creates a styled primary button for login forms (cyan/blue).
     */
    public static JButton createLoginButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(PRIMARY_DARK);
                } else if (getModel().isRollover()) {
                    g2.setColor(PRIMARY_COLOR.brighter());
                } else {
                    g2.setColor(PRIMARY_COLOR);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(320, 48));
        return button;
    }

    /**
     * Creates a styled link button.
     */
    public static JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setFont(LOGIN_LINK_FONT);
        button.setForeground(LINK_COLOR);
        button.setBackground(null);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setText("<html><u>" + text + "</u></html>");
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setText(text);
            }
        });
        return button;
    }

    /**
     * Creates a toggle button panel (Customer/Admin toggle).
     */
    public static JPanel createTogglePanel(String[] options, int selectedIndex, java.util.function.Consumer<Integer> onSelect) {
        JPanel panel = new JPanel(new GridLayout(1, options.length, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TOGGLE_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        panel.setBackground(TOGGLE_INACTIVE_BG);
        panel.setBorder(new EmptyBorder(4, 4, 4, 4));
        panel.setPreferredSize(new Dimension(320, 48));

        JButton[] buttons = new JButton[options.length];
        final int[] selected = {selectedIndex};

        for (int i = 0; i < options.length; i++) {
            final int index = i;
            buttons[i] = new JButton(options[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (selected[0] == index) {
                        g2.setColor(TOGGLE_ACTIVE_BG);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    }
                    
                    g2.setColor(selected[0] == index ? TEXT_PRIMARY : TEXT_SECONDARY);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(getText(), x, y);
                    
                    g2.dispose();
                }
            };
            buttons[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            buttons[i].setFocusPainted(false);
            buttons[i].setBorderPainted(false);
            buttons[i].setContentAreaFilled(false);
            buttons[i].setOpaque(false);
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            buttons[i].addActionListener(e -> {
                selected[0] = index;
                for (JButton btn : buttons) {
                    btn.repaint();
                }
                onSelect.accept(index);
            });
            
            panel.add(buttons[i]);
        }

        return panel;
    }
}
