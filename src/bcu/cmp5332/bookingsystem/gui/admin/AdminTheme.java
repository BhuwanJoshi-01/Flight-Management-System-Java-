package bcu.cmp5332.bookingsystem.gui.admin;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Centralized theme class for Admin Portal.
 * Matches the React TSX admin panel design exactly.
 */
public class AdminTheme {
    
    // ==================== COLOR PALETTE ====================
    
    // Primary Colors (Deep Navy)
    public static final Color PRIMARY = new Color(30, 41, 59);           // hsl(222, 47%, 20%)
    public static final Color PRIMARY_FOREGROUND = new Color(248, 250, 252); // hsl(210, 40%, 98%)
    
    // Background Colors
    public static final Color BACKGROUND = new Color(248, 250, 252);     // hsl(210, 20%, 98%)
    public static final Color FOREGROUND = new Color(15, 23, 42);        // hsl(222, 47%, 11%)
    
    // Card Colors
    public static final Color CARD = Color.WHITE;                         // hsl(0, 0%, 100%)
    public static final Color CARD_FOREGROUND = new Color(15, 23, 42);
    
    // Secondary Colors (Light blue gray)
    public static final Color SECONDARY = new Color(226, 232, 240);      // hsl(214, 32%, 91%)
    public static final Color SECONDARY_FOREGROUND = new Color(15, 23, 42);
    
    // Muted Colors
    public static final Color MUTED = new Color(226, 232, 240);          // hsl(214, 32%, 91%)
    public static final Color MUTED_FOREGROUND = new Color(100, 116, 139); // hsl(215, 16%, 47%)
    
    // Accent Color (Sky Blue)
    public static final Color ACCENT = new Color(14, 165, 233);          // hsl(199, 89%, 48%)
    public static final Color ACCENT_FOREGROUND = Color.WHITE;
    public static final Color ACCENT_LIGHT = new Color(224, 242, 254);   // hsl(199, 89%, 95%)
    
    // Destructive (Red)
    public static final Color DESTRUCTIVE = new Color(239, 68, 68);      // hsl(0, 84%, 60%)
    public static final Color DESTRUCTIVE_FOREGROUND = new Color(248, 250, 252);
    public static final Color DESTRUCTIVE_LIGHT = new Color(254, 226, 226);
    
    // Success (Green)
    public static final Color SUCCESS = new Color(34, 197, 94);          // hsl(142, 76%, 36%)
    public static final Color SUCCESS_FOREGROUND = Color.WHITE;
    public static final Color SUCCESS_LIGHT = new Color(220, 252, 231);
    
    // Warning (Orange/Yellow)
    public static final Color WARNING = new Color(245, 158, 11);         // hsl(38, 92%, 50%)
    public static final Color WARNING_FOREGROUND = Color.WHITE;
    public static final Color WARNING_LIGHT = new Color(254, 243, 199);
    
    // Border Colors
    public static final Color BORDER = new Color(226, 232, 240);         // hsl(214, 32%, 91%)
    public static final Color BORDER_LIGHT = new Color(241, 245, 249);
    
    // Sidebar Colors
    public static final Color SIDEBAR_BG = new Color(15, 23, 42);        // hsl(222, 47%, 15%)
    public static final Color SIDEBAR_FOREGROUND = new Color(226, 232, 240);
    public static final Color SIDEBAR_ACCENT = new Color(30, 41, 59);
    public static final Color SIDEBAR_ACTIVE = new Color(14, 165, 233);
    public static final Color SIDEBAR_BORDER = new Color(51, 65, 85);
    
    // Table Colors
    public static final Color TABLE_HEADER_BG = new Color(241, 245, 249); // muted/30
    public static final Color TABLE_ROW_HOVER = new Color(248, 250, 252); // muted/20
    public static final Color TABLE_ALTERNATE = new Color(249, 250, 251);
    
    // Status Badge Colors
    public static final Color STATUS_CONFIRMED_BG = new Color(220, 252, 231);
    public static final Color STATUS_CONFIRMED_TEXT = new Color(22, 101, 52);
    public static final Color STATUS_CONFIRMED_BORDER = new Color(187, 247, 208);
    
    public static final Color STATUS_PENDING_BG = new Color(254, 243, 199);
    public static final Color STATUS_PENDING_TEXT = new Color(161, 98, 7);
    public static final Color STATUS_PENDING_BORDER = new Color(253, 224, 71);
    
    public static final Color STATUS_CANCELLED_BG = new Color(254, 226, 226);
    public static final Color STATUS_CANCELLED_TEXT = new Color(185, 28, 28);
    public static final Color STATUS_CANCELLED_BORDER = new Color(254, 202, 202);
    
    // Tier Badge Colors
    public static final Color TIER_BRONZE_BG = new Color(254, 243, 199);
    public static final Color TIER_BRONZE_TEXT = new Color(161, 98, 7);
    public static final Color TIER_SILVER_BG = new Color(241, 245, 249);
    public static final Color TIER_SILVER_TEXT = new Color(71, 85, 105);
    public static final Color TIER_GOLD_BG = new Color(254, 249, 195);
    public static final Color TIER_GOLD_TEXT = new Color(161, 98, 7);
    public static final Color TIER_PLATINUM_BG = new Color(243, 232, 255);
    public static final Color TIER_PLATINUM_TEXT = new Color(107, 33, 168);
    
    // ==================== TYPOGRAPHY ====================
    
    public static final String FONT_FAMILY = "Segoe UI";
    
    // Heading Fonts
    public static final Font H1_FONT = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font H2_FONT = new Font(FONT_FAMILY, Font.BOLD, 20);
    public static final Font H3_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font H4_FONT = new Font(FONT_FAMILY, Font.BOLD, 16);
    
    // Body Fonts
    public static final Font BODY_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font BODY_MEDIUM = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font BODY_SMALL = new Font(FONT_FAMILY, Font.PLAIN, 13);
    
    // Label Fonts
    public static final Font LABEL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font LABEL_BOLD = new Font(FONT_FAMILY, Font.BOLD, 13);
    
    // Small/Caption Fonts
    public static final Font SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font CAPTION_FONT = new Font(FONT_FAMILY, Font.PLAIN, 11);
    
    // KPI Fonts
    public static final Font KPI_VALUE_FONT = new Font(FONT_FAMILY, Font.BOLD, 28);
    public static final Font KPI_LABEL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);
    
    // Button Fonts
    public static final Font BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font BUTTON_SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);
    
    // Table Fonts
    public static final Font TABLE_HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 12);
    public static final Font TABLE_CELL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);
    
    // ==================== DIMENSIONS ====================
    
    public static final int CORNER_RADIUS = 12;          // 0.75rem
    public static final int CARD_PADDING = 20;
    public static final int SECTION_GAP = 24;
    public static final int COMPONENT_GAP = 16;
    public static final int SMALL_GAP = 8;
    public static final int SIDEBAR_WIDTH = 240;
    public static final int BUTTON_HEIGHT = 40;
    public static final int INPUT_HEIGHT = 44;
    public static final int TABLE_ROW_HEIGHT = 52;
    
    // ==================== COMPONENT FACTORIES ====================
    
    /**
     * Creates a rounded card panel with shadow effect.
     */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, CORNER_RADIUS, CORNER_RADIUS);
                
                // Background
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, CORNER_RADIUS, CORNER_RADIUS);
                
                // Border
                g2.setColor(new Color(BORDER.getRed(), BORDER.getGreen(), BORDER.getBlue(), 128));
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, CORNER_RADIUS, CORNER_RADIUS);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING));
        return card;
    }
    
    /**
     * Creates a primary button (sky blue).
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = getModel().isPressed() ? ACCENT.darker() : 
                               getModel().isRollover() ? ACCENT.brighter() : ACCENT;
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, BUTTON_HEIGHT));
        return button;
    }
    
    /**
     * Creates an outline button.
     */
    public static JButton createOutlineButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2.setColor(TABLE_ROW_HOVER);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        button.setFont(BUTTON_FONT);
        button.setForeground(FOREGROUND);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, BUTTON_HEIGHT));
        return button;
    }
    
    /**
     * Creates a ghost button (transparent with hover effect).
     */
    public static JButton createGhostButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(TABLE_ROW_HOVER);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        button.setFont(BUTTON_SMALL_FONT);
        button.setForeground(MUTED_FOREGROUND);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Creates a danger button (red).
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = getModel().isPressed() ? DESTRUCTIVE.darker() : 
                               getModel().isRollover() ? DESTRUCTIVE.brighter() : DESTRUCTIVE;
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, BUTTON_HEIGHT));
        return button;
    }
    
    /**
     * Creates an icon button.
     */
    public static JButton createIconButton(ImageIcon icon, Color normalColor, Color hoverColor) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(hoverColor != null ? new Color(hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue(), 30) : TABLE_ROW_HOVER);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        if (icon != null) {
            button.setIcon(icon);
        }
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(36, 36));
        return button;
    }
    
    /**
     * Creates a styled text field with placeholder.
     */
    public static JTextField createTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(MUTED_FOREGROUND);
                    g2.setFont(getFont());
                    g2.drawString(placeholder, getInsets().left, g2.getFontMetrics().getMaxAscent() + getInsets().top);
                    g2.dispose();
                }
            }
        };
        field.setFont(BODY_FONT);
        field.setForeground(FOREGROUND);
        field.setBackground(CARD);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        field.setPreferredSize(new Dimension(200, INPUT_HEIGHT));
        return field;
    }
    
    /**
     * Creates a search field with icon.
     */
    public static JPanel createSearchField(String placeholder) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(0, 12, 0, 12)
        ));
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setForeground(MUTED_FOREGROUND);
        searchIcon.setBorder(new EmptyBorder(0, 0, 0, 8));
        
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(MUTED_FOREGROUND);
                    g2.setFont(getFont());
                    g2.drawString(placeholder, 0, g2.getFontMetrics().getMaxAscent() + 2);
                    g2.dispose();
                }
            }
        };
        field.setFont(BODY_FONT);
        field.setForeground(FOREGROUND);
        field.setBorder(null);
        field.setOpaque(false);
        
        panel.add(searchIcon, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(300, INPUT_HEIGHT));
        
        return panel;
    }
    
    /**
     * Creates a status badge.
     */
    public static JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel(status.substring(0, 1).toUpperCase() + status.substring(1)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor, borderColor;
                switch (status.toLowerCase()) {
                    case "confirmed":
                        bgColor = STATUS_CONFIRMED_BG;
                        borderColor = STATUS_CONFIRMED_BORDER;
                        break;
                    case "pending":
                        bgColor = STATUS_PENDING_BG;
                        borderColor = STATUS_PENDING_BORDER;
                        break;
                    case "cancelled":
                        bgColor = STATUS_CANCELLED_BG;
                        borderColor = STATUS_CANCELLED_BORDER;
                        break;
                    default:
                        bgColor = MUTED;
                        borderColor = BORDER;
                }
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        Color textColor;
        switch (status.toLowerCase()) {
            case "confirmed":
                textColor = STATUS_CONFIRMED_TEXT;
                break;
            case "pending":
                textColor = STATUS_PENDING_TEXT;
                break;
            case "cancelled":
                textColor = STATUS_CANCELLED_TEXT;
                break;
            default:
                textColor = MUTED_FOREGROUND;
        }
        
        badge.setFont(SMALL_FONT);
        badge.setForeground(textColor);
        badge.setOpaque(false);
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));
        return badge;
    }
    
    /**
     * Creates a tier badge.
     */
    public static JLabel createTierBadge(String tier) {
        JLabel badge = new JLabel(tier.substring(0, 1).toUpperCase() + tier.substring(1).toLowerCase()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor;
                switch (tier.toUpperCase()) {
                    case "PLATINUM": bgColor = TIER_PLATINUM_BG; break;
                    case "GOLD": bgColor = TIER_GOLD_BG; break;
                    case "SILVER": bgColor = TIER_SILVER_BG; break;
                    default: bgColor = TIER_BRONZE_BG;
                }
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        Color textColor;
        switch (tier.toUpperCase()) {
            case "PLATINUM": textColor = TIER_PLATINUM_TEXT; break;
            case "GOLD": textColor = TIER_GOLD_TEXT; break;
            case "SILVER": textColor = TIER_SILVER_TEXT; break;
            default: textColor = TIER_BRONZE_TEXT;
        }
        
        badge.setFont(SMALL_FONT);
        badge.setForeground(textColor);
        badge.setOpaque(false);
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));
        return badge;
    }
    
    /**
     * Styles a JTable to match the admin design.
     */
    public static void styleTable(JTable table) {
        table.setFont(TABLE_CELL_FONT);
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 30));
        table.setSelectionForeground(FOREGROUND);
        table.setBackground(CARD);
        table.setGridColor(BORDER_LIGHT);
        table.setFillsViewportHeight(true);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(TABLE_HEADER_FONT);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(MUTED_FOREGROUND);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT));
        header.setPreferredSize(new Dimension(header.getWidth(), 48));
        
        // Row hover effect
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    c.setBackground(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 30));
                } else {
                    c.setBackground(CARD);
                }
                
                setForeground(FOREGROUND);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT),
                    new EmptyBorder(0, 16, 0, 16)
                ));
                return c;
            }
        });
    }
    
    /**
     * Creates an icon circle (like the plane icon in tables).
     */
    public static JPanel createIconCircle(ImageIcon icon, Color bgColor) {
        JPanel circle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 25));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        circle.setOpaque(false);
        circle.setLayout(new GridBagLayout());
        circle.setPreferredSize(new Dimension(36, 36));
        
        JLabel label = new JLabel();
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
        }
        label.setForeground(bgColor);
        circle.add(label);
        
        return circle;
    }

    public static JPanel createIconCircle(String emoji, Color bgColor) {
        JPanel circle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 25));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        circle.setOpaque(false);
        circle.setLayout(new GridBagLayout());
        circle.setPreferredSize(new Dimension(36, 36));
        
        JLabel icon = new JLabel(emoji);
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        icon.setForeground(bgColor);
        circle.add(icon);
        
        return circle;
    }
    
    /**
     * Creates a section header with title and optional action button.
     */
    public static JPanel createSectionHeader(String title, String actionText, Runnable action) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, COMPONENT_GAP, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(H4_FONT);
        titleLabel.setForeground(FOREGROUND);
        header.add(titleLabel, BorderLayout.WEST);
        
        if (actionText != null && action != null) {
            JButton actionBtn = createGhostButton(actionText + " →");
            actionBtn.addActionListener(e -> action.run());
            header.add(actionBtn, BorderLayout.EAST);
        }
        
        return header;
    }
    
    /**
     * Shows a success dialog.
     */
    public static void showSuccessDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows an error dialog.
     */
    public static void showErrorDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows a confirmation dialog.
     */
    public static boolean showConfirmDialog(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(parent, message, title, 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}
