package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.AuthService;
import bcu.cmp5332.bookingsystem.auth.UserSession;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Login form panel with Customer/Admin toggle, email/password fields, and action buttons.
 */
public class LoginFormPanel extends JPanel {

    private final FlightBookingSystem flightBookingSystem;
    private final AuthService authService;
    private final Runnable onSwitchToRegister;
    private final Runnable onForgotPassword;
    private final Runnable onLoginSuccess;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheckbox;
    private boolean isAdminMode = false;

    public LoginFormPanel(FlightBookingSystem flightBookingSystem, 
                          Runnable onSwitchToRegister, 
                          Runnable onForgotPassword,
                          Runnable onLoginSuccess) {
        this.flightBookingSystem = flightBookingSystem;
        this.authService = new AuthService(flightBookingSystem);
        this.onSwitchToRegister = onSwitchToRegister;
        this.onForgotPassword = onForgotPassword;
        this.onLoginSuccess = onLoginSuccess;

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
        contentWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo/Icon
        JLabel logoLabel = createLogoLabel();
        JPanel logoWrapper = createCenteredWrapper(logoLabel);
        contentWrapper.add(logoWrapper);
        contentWrapper.add(Box.createVerticalStrut(20));

        // Title
        JLabel titleLabel = new JLabel("Welcome to SkyBook");
        titleLabel.setFont(UIStyles.LOGIN_TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        JPanel titleWrapper = createCenteredWrapper(titleLabel);
        contentWrapper.add(titleWrapper);
        contentWrapper.add(Box.createVerticalStrut(5));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Flight Booking System");
        subtitleLabel.setFont(UIStyles.LOGIN_SUBTITLE_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        JPanel subtitleWrapper = createCenteredWrapper(subtitleLabel);
        contentWrapper.add(subtitleWrapper);
        contentWrapper.add(Box.createVerticalStrut(25));

        // Customer/Admin Toggle
        JPanel togglePanel = UIStyles.createTogglePanel(
            new String[]{"Customer", "Admin"},
            0,
            index -> isAdminMode = (index == 1)
        );
        JPanel toggleWrapper = createCenteredWrapper(togglePanel);
        contentWrapper.add(toggleWrapper);
        contentWrapper.add(Box.createVerticalStrut(25));

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(UIStyles.LOGIN_LABEL_FONT);
        emailLabel.setForeground(UIStyles.TEXT_PRIMARY);
        JPanel emailLabelWrapper = createFullWidthWrapper(emailLabel);
        contentWrapper.add(emailLabelWrapper);
        contentWrapper.add(Box.createVerticalStrut(8));

        emailField = UIStyles.createLoginTextField("Enter your email");
        JPanel emailFieldWrapper = createFullWidthWrapper(emailField);
        emailFieldWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        contentWrapper.add(emailFieldWrapper);
        contentWrapper.add(Box.createVerticalStrut(15));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIStyles.LOGIN_LABEL_FONT);
        passwordLabel.setForeground(UIStyles.TEXT_PRIMARY);
        JPanel passwordLabelWrapper = createFullWidthWrapper(passwordLabel);
        contentWrapper.add(passwordLabelWrapper);
        contentWrapper.add(Box.createVerticalStrut(8));

        JPanel passwordPanel = createPasswordPanel();
        JPanel passwordPanelWrapper = createFullWidthWrapper(passwordPanel);
        passwordPanelWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        contentWrapper.add(passwordPanelWrapper);
        contentWrapper.add(Box.createVerticalStrut(15));

        // Remember me & Forgot password row
        JPanel optionsPanel = createOptionsPanel();
        JPanel optionsWrapper = createFullWidthWrapper(optionsPanel);
        contentWrapper.add(optionsWrapper);
        contentWrapper.add(Box.createVerticalStrut(20));

        // Sign In button
        JButton signInButton = UIStyles.createLoginButton("Sign In");
        signInButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        signInButton.addActionListener(e -> handleLogin());
        JPanel buttonWrapper = createFullWidthWrapper(signInButton);
        buttonWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        contentWrapper.add(buttonWrapper);
        contentWrapper.add(Box.createVerticalStrut(20));

        // Sign up link
        JPanel signUpPanel = createSignUpPanel();
        JPanel signUpWrapper = createCenteredWrapper(signUpPanel);
        contentWrapper.add(signUpWrapper);
        contentWrapper.add(Box.createVerticalStrut(25));

        // Demo credentials box
        JPanel demoPanel = createDemoCredentialsPanel();
        JPanel demoWrapper = createFullWidthWrapper(demoPanel);
        contentWrapper.add(demoWrapper);

        // Add the content wrapper to main panel
        add(contentWrapper);
    }

    /**
     * Creates a wrapper panel that centers its content horizontally.
     */
    private JPanel createCenteredWrapper(JComponent component) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setBackground(UIStyles.CARD_BACKGROUND);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, component.getPreferredSize().height + 10));
        wrapper.add(component);
        return wrapper;
    }

    /**
     * Creates a wrapper panel that stretches content to full width.
     */
    private JPanel createFullWidthWrapper(JComponent component) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UIStyles.CARD_BACKGROUND);
        wrapper.add(component, BorderLayout.CENTER);
        return wrapper;
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

                // Draw airplane symbol
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
                FontMetrics fm = g2.getFontMetrics();
                String airplane = "✈";
                int x = (56 - fm.stringWidth(airplane)) / 2;
                int y = (56 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(airplane, x, y);

                g2.dispose();
            }
        };
        label.setPreferredSize(new Dimension(56, 56));
        label.setMinimumSize(new Dimension(56, 56));
        label.setMaximumSize(new Dimension(56, 56));
        return label;
    }

    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyles.CARD_BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        passwordField = UIStyles.createLoginPasswordField("Enter your password");
        panel.add(passwordField, BorderLayout.CENTER);

        // Eye icon button for show/hide password
        JButton eyeButton = new JButton("👁") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getParent().getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        eyeButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        eyeButton.setForeground(UIStyles.TEXT_SECONDARY);
        eyeButton.setBorderPainted(false);
        eyeButton.setContentAreaFilled(false);
        eyeButton.setFocusPainted(false);
        eyeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeButton.setPreferredSize(new Dimension(40, 48));
        
        eyeButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == 0) {
                passwordField.setEchoChar('•');
            } else {
                passwordField.setEchoChar((char) 0);
            }
        });

        // Overlay the eye button on the password field
        passwordField.setLayout(new BorderLayout());
        passwordField.add(eyeButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyles.CARD_BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Remember me checkbox
        rememberMeCheckbox = new JCheckBox("Remember me");
        rememberMeCheckbox.setFont(UIStyles.LOGIN_LINK_FONT);
        rememberMeCheckbox.setForeground(UIStyles.TEXT_SECONDARY);
        rememberMeCheckbox.setBackground(UIStyles.CARD_BACKGROUND);
        rememberMeCheckbox.setFocusPainted(false);
        panel.add(rememberMeCheckbox, BorderLayout.WEST);

        // Forgot password link
        JButton forgotButton = UIStyles.createLinkButton("Forgot password?");
        forgotButton.addActionListener(e -> onForgotPassword.run());
        panel.add(forgotButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(UIStyles.CARD_BACKGROUND);

        JLabel label = new JLabel("Don't have an account?");
        label.setFont(UIStyles.LOGIN_LINK_FONT);
        label.setForeground(UIStyles.TEXT_SECONDARY);
        panel.add(label);

        JButton signUpButton = UIStyles.createLinkButton("Sign up");
        signUpButton.addActionListener(e -> onSwitchToRegister.run());
        panel.add(signUpButton);

        return panel;
    }

    private JPanel createDemoCredentialsPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2.setColor(UIStyles.DEMO_BOX_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Draw rounded border
                g2.setColor(UIStyles.TOGGLE_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 18, 15, 18));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        JLabel titleLabel = new JLabel("Demo Credentials:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));

        JLabel customerLabel = new JLabel("Customer: john@example.com / password123");
        customerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        customerLabel.setForeground(new Color(0x1A73E8));
        panel.add(customerLabel);
        panel.add(Box.createVerticalStrut(4));

        JLabel adminLabel = new JLabel("Admin: admin@flights.com / admin123");
        adminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        adminLabel.setForeground(new Color(0x1A73E8));
        panel.add(adminLabel);

        return panel;
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            UIStyles.showErrorDialog(this, "Please enter both email and password.", "Login Error");
            return;
        }

        try {
            if (isAdminMode) {
                // Admin login
                authService.authenticateAdmin(email, password);
                UserSession.getInstance().loginAsAdmin();
            } else {
                // Customer login
                Customer customer = authService.authenticateCustomer(email, password);
                UserSession.getInstance().loginAsCustomer(customer);
            }

            // Login successful
            onLoginSuccess.run();

        } catch (FlightBookingSystemException ex) {
            UIStyles.showErrorDialog(this, ex.getMessage(), "Login Failed");
        }
    }

    /**
     * Pre-fills the form with demo credentials for testing.
     */
    public void fillDemoCredentials(boolean admin) {
        if (admin) {
            emailField.setText("admin@flights.com");
            passwordField.setText("admin123");
        } else {
            emailField.setText("john@example.com");
            passwordField.setText("password123");
        }
    }
}
