package bcu.cmp5332.bookingsystem.gui.customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Centralized theme utilities for the customer portal.
 * Provides reusable card panels, buttons, and styled inputs - 
 * following React component architecture principles.
 */
public class UITheme {

    // Colors matching React Tailwind theme
    public static final Color BG = new Color(247, 249, 252);           // bg-slate-50
    public static final Color CARD = Color.WHITE;                       // bg-white
    public static final Color BORDER = new Color(229, 231, 235);       // border-gray-200
    public static final Color BORDER_LIGHT = new Color(241, 245, 249); // border-slate-100
    public static final Color PRIMARY = new Color(8, 145, 178);        // sky-600
    public static final Color PRIMARY_HOVER = new Color(6, 125, 157);  // sky-700
    public static final Color SECONDARY = new Color(30, 58, 95);       // slate-800
    public static final Color ACCENT = new Color(14, 165, 233);        // sky-500
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);    // slate-900
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139); // slate-500
    public static final Color TEXT_MUTED = new Color(148, 163, 184);   // slate-400
    public static final Color SUCCESS = new Color(21, 128, 61);        // green-700
    public static final Color DANGER = new Color(220, 38, 38);         // red-600
    public static final Color MUTED_BG = new Color(241, 245, 249);     // slate-100
    public static final Color ICON_BG = new Color(240, 249, 255);      // sky-50

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font PRICE_FONT = new Font("Segoe UI", Font.BOLD, 20);

    // Dimensions
    public static final int CORNER_RADIUS = 12;
    public static final int CARD_RADIUS = 16;
    public static final int BUTTON_RADIUS = 10;

    /**
     * Creates a styled card panel with rounded corners and subtle shadow.
     * Equivalent to React's bg-card rounded-xl shadow-soft border
     */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Soft shadow
                g2.setColor(new Color(15, 23, 42, 20));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, CARD_RADIUS, CARD_RADIUS);
                
                // White background
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CARD_RADIUS, CARD_RADIUS);
                
                // Border
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_RADIUS, CARD_RADIUS);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 24, 20, 24));
        return card;
    }

    /**
     * Creates a flight result card with hover effect.
     */
    public static JPanel createFlightCard() {
        JPanel card = new JPanel() {
            private boolean hovered = false;
            
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow (slightly larger on hover)
                int shadowOffset = hovered ? 8 : 6;
                int shadowAlpha = hovered ? 28 : 20;
                g2.setColor(new Color(15, 23, 42, shadowAlpha));
                g2.fillRoundRect(4, shadowOffset, getWidth() - 8, getHeight() - 10, CARD_RADIUS, CARD_RADIUS);
                
                // White background
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CARD_RADIUS, CARD_RADIUS);
                
                // Border
                g2.setColor(hovered ? PRIMARY : BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_RADIUS, CARD_RADIUS);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        return card;
    }

    /**
     * Creates a primary button (sky blue) - like React's Button variant="sky"
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;
            
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? PRIMARY_HOVER : PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_RADIUS, BUTTON_RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a secondary button (dark slate) - like React's Button variant="outline"
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;
            
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? new Color(51, 65, 85) : SECONDARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_RADIUS, BUTTON_RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a styled text input with rounded border.
     */
    public static JTextField createInput(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setFont(BODY_FONT);
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(12, 14, 12, 14));
        
        // Placeholder text behavior
        field.setText(placeholder);
        field.setForeground(TEXT_MUTED);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_MUTED);
                }
            }
        });
        return field;
    }

    /**
     * Creates a styled combo box.
     */
    public static JComboBox<String> createComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(BODY_FONT);
        combo.setBackground(CARD);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return combo;
    }

    /**
     * Creates a toggle button panel (One Way / Round Trip) like React's trip type toggle.
     */
    public static JPanel createTogglePanel(String[] options, int selectedIndex, java.util.function.Consumer<Integer> onSelect) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        panel.setOpaque(false);
        
        JButton[] buttons = new JButton[options.length];
        
        for (int i = 0; i < options.length; i++) {
            final int index = i;
            buttons[i] = new JButton(options[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    boolean selected = Boolean.TRUE.equals(getClientProperty("selected"));
                    g2.setColor(selected ? ACCENT : MUTED_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            buttons[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            buttons[i].setForeground(i == selectedIndex ? Color.WHITE : TEXT_SECONDARY);
            buttons[i].putClientProperty("selected", i == selectedIndex);
            buttons[i].setFocusPainted(false);
            buttons[i].setBorderPainted(false);
            buttons[i].setContentAreaFilled(false);
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttons[i].setPreferredSize(new Dimension(100, 36));
            
            buttons[i].addActionListener(e -> {
                for (int j = 0; j < buttons.length; j++) {
                    buttons[j].putClientProperty("selected", j == index);
                    buttons[j].setForeground(j == index ? Color.WHITE : TEXT_SECONDARY);
                    buttons[j].repaint();
                }
                onSelect.accept(index);
            });
            
            panel.add(buttons[i]);
        }
        
        return panel;
    }

    /**
     * Creates a labeled input group - like React's Label + Input pattern.
     */
    public static JPanel createInputGroup(String label, JComponent input) {
        JPanel wrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setLayout(new BorderLayout(0, 4));
        wrapper.setBorder(new EmptyBorder(10, 14, 10, 14));
        wrapper.setPreferredSize(new Dimension(200, 70));

        JLabel lbl = new JLabel(label);
        lbl.setFont(SMALL_FONT);
        lbl.setForeground(TEXT_SECONDARY);

        if (input instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) input;
            combo.setFont(BODY_FONT);
            combo.setBackground(CARD);
            combo.setBorder(null);
        } else if (input instanceof JTextField) {
            JTextField field = (JTextField) input;
            field.setBorder(null);
            field.setOpaque(false);
        }

        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(input, BorderLayout.CENTER);
        return wrapper;
    }

    /**
     * Creates an icon circle (for flight cards) - like React's plane icon container.
     */
    public static JPanel createIconCircle(ImageIcon icon, int size) {
        JPanel circle = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ICON_BG);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        circle.setPreferredSize(new Dimension(size, size));
        circle.setOpaque(false);
        
        if (icon != null) {
            int iconSize = size - 16;
            Image scaled = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            circle.add(new JLabel(new ImageIcon(scaled)));
        }
        
        return circle;
    }

    /**
     * Creates availability label with dynamic coloring.
     */
    public static JLabel createAvailabilityLabel(int available) {
        JLabel label = new JLabel(available + " seats left");
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(available < 5 ? DANGER : SUCCESS);
        return label;
    }

    /**
     * Creates a price label.
     */
    public static JLabel createPriceLabel(double price) {
        JLabel label = new JLabel("£" + String.format("%.2f", price));
        label.setFont(PRICE_FONT);
        label.setForeground(PRIMARY);
        return label;
    }

    /**
     * Shows an error dialog with consistent styling.
     */
    public static void showError(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a success dialog with consistent styling.
     */
    public static void showSuccess(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
