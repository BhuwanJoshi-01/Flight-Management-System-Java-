package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.AuthService;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Forgot password panel for password reset functionality.
 */
public class ForgotPasswordPanel extends JPanel {

    private final FlightBookingSystem flightBookingSystem;
    private final AuthService authService;
    private final Runnable onBackToLogin;

    private JTextField emailField;

    public ForgotPasswordPanel(FlightBookingSystem flightBookingSystem, Runnable onBackToLogin) {
        this.flightBookingSystem = flightBookingSystem;
        this.authService = new AuthService(flightBookingSystem);
        this.onBackToLogin = onBackToLogin;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UIStyles.CARD_BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Allow the panel to shrink/expand with the window
        setMinimumSize(new Dimension(280, 0));
        setMaximumSize(new Dimension(450, Integer.MAX_VALUE));

        initializeComponents();
    }

    private void initializeComponents() {
        // Create a wrapper panel to properly center all content
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(UIStyles.CARD_BACKGROUND);

        // Logo/Icon
        JLabel logoLabel = createLogoLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(logoLabel);
        contentWrapper.add(Box.createVerticalStrut(20));

        // Title
        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(UIStyles.LOGIN_TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(titleLabel);
        contentWrapper.add(Box.createVerticalStrut(10));

        // Subtitle - using HTML for proper centering and wrapping
        JLabel subtitleLabel = new JLabel("<html><div style='text-align: center; width: 280px;'>Enter your email address and we'll send you a link to reset your password.</div></html>");
        subtitleLabel.setFont(UIStyles.LOGIN_SUBTITLE_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentWrapper.add(subtitleLabel);
        contentWrapper.add(Box.createVerticalStrut(30));

        // Email Label
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(UIStyles.LOGIN_LABEL_FONT);
        emailLabel.setForeground(UIStyles.TEXT_PRIMARY);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Email field
        emailField = UIStyles.createLoginTextField("Enter your email");
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Send Reset Link button
        JButton resetButton = UIStyles.createLoginButton("Send Reset Link");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> handleResetRequest());

        // Create a form panel with fixed width for consistent alignment
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(UIStyles.CARD_BACKGROUND);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set preferred/max width for form elements
        Dimension fieldSize = new Dimension(320, 48);
        Dimension labelSize = new Dimension(320, 25);
        
        emailLabel.setPreferredSize(labelSize);
        emailLabel.setMaximumSize(labelSize);
        emailLabel.setMinimumSize(labelSize);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        emailField.setPreferredSize(fieldSize);
        emailField.setMaximumSize(fieldSize);
        emailField.setMinimumSize(fieldSize);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        resetButton.setPreferredSize(fieldSize);
        resetButton.setMaximumSize(fieldSize);
        resetButton.setMinimumSize(fieldSize);
        resetButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(resetButton);
        
        // Wrap form panel to center it
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        formWrapper.setBackground(UIStyles.CARD_BACKGROUND);
        formWrapper.add(formPanel);
        contentWrapper.add(formWrapper);
        contentWrapper.add(Box.createVerticalStrut(20));

        // Back to login link
        JButton backButton = UIStyles.createLinkButton("Back to login");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> onBackToLogin.run());
        contentWrapper.add(backButton);

        // Add the content wrapper to main panel
        add(contentWrapper);
    }

    private JLabel createLogoLabel() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded rectangle background
                g2.setColor(UIStyles.PRIMARY_COLOR);
                g2.fillRoundRect(0, 0, 56, 56, 12, 12);

                // Draw lock symbol
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
                FontMetrics fm = g2.getFontMetrics();
                String lock = "🔒";
                int x = (56 - fm.stringWidth(lock)) / 2;
                int y = (56 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(lock, x, y);

                g2.dispose();
            }
        };
        label.setPreferredSize(new Dimension(56, 56));
        label.setMinimumSize(new Dimension(56, 56));
        label.setMaximumSize(new Dimension(56, 56));
        return label;
    }

    private void handleResetRequest() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            UIStyles.showErrorDialog(this, "Please enter your email address.", "Error");
            return;
        }

        try {
            authService.initiatePasswordReset(email);
            
            // In a real application, an email would be sent here
            UIStyles.showSuccessDialog(this, 
                "If an account exists with this email, you will receive a password reset link shortly.\n\n" +
                "(Note: This is a demo. In a real application, an email would be sent.)", 
                "Reset Link Sent");
            
            // Go back to login
            onBackToLogin.run();

        } catch (FlightBookingSystemException ex) {
            // For security, we show the same message regardless of whether the email exists
            UIStyles.showSuccessDialog(this, 
                "If an account exists with this email, you will receive a password reset link shortly.\n\n" +
                "(Note: This is a demo. In a real application, an email would be sent.)", 
                "Reset Link Sent");
            onBackToLogin.run();
        }
    }

    /**
     * Clears the email field.
     */
    public void clearFields() {
        emailField.setText("");
    }
}
