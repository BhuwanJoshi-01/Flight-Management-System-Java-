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
 * Registration form panel for new customer sign-up.
 */
public class RegisterFormPanel extends JPanel {

    private final FlightBookingSystem flightBookingSystem;
    private final AuthService authService;
    private final Runnable onSwitchToLogin;
    private final Runnable onRegisterSuccess;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterFormPanel(FlightBookingSystem flightBookingSystem, 
                             Runnable onSwitchToLogin,
                             Runnable onRegisterSuccess) {
        this.flightBookingSystem = flightBookingSystem;
        this.authService = new AuthService(flightBookingSystem);
        this.onSwitchToLogin = onSwitchToLogin;
        this.onRegisterSuccess = onRegisterSuccess;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UIStyles.CARD_BACKGROUND);
        setBorder(new EmptyBorder(40, 50, 40, 50));
        
        // Allow the panel to shrink/expand with the window
        setMinimumSize(new Dimension(320, 0));

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
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(UIStyles.LOGIN_TITLE_FONT);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        JPanel titleWrapper = createCenteredWrapper(titleLabel);
        contentWrapper.add(titleWrapper);
        contentWrapper.add(Box.createVerticalStrut(5));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Join SkyBook today");
        subtitleLabel.setFont(UIStyles.LOGIN_SUBTITLE_FONT);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        JPanel subtitleWrapper = createCenteredWrapper(subtitleLabel);
        contentWrapper.add(subtitleWrapper);
        contentWrapper.add(Box.createVerticalStrut(25));

        // Name field
        contentWrapper.add(createFullWidthWrapper(createFieldLabel("Full Name")));
        contentWrapper.add(Box.createVerticalStrut(8));
        nameField = UIStyles.createLoginTextField("Enter your full name");
        JPanel nameWrapper = createFullWidthWrapper(nameField);
        nameWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        contentWrapper.add(nameWrapper);
        contentWrapper.add(Box.createVerticalStrut(12));

        // Email field
        contentWrapper.add(createFullWidthWrapper(createFieldLabel("Email")));
        contentWrapper.add(Box.createVerticalStrut(8));
        emailField = UIStyles.createLoginTextField("Enter your email");
        JPanel emailWrapper = createFullWidthWrapper(emailField);
        emailWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        contentWrapper.add(emailWrapper);
        contentWrapper.add(Box.createVerticalStrut(12));

        // Phone field
        contentWrapper.add(createFullWidthWrapper(createFieldLabel("Phone Number")));
        contentWrapper.add(Box.createVerticalStrut(8));
        phoneField = UIStyles.createLoginTextField("Enter your phone number");
        JPanel phoneWrapper = createFullWidthWrapper(phoneField);
        phoneWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        contentWrapper.add(phoneWrapper);
        contentWrapper.add(Box.createVerticalStrut(12));

        // Password field
        contentWrapper.add(createFullWidthWrapper(createFieldLabel("Password")));
        contentWrapper.add(Box.createVerticalStrut(8));
        passwordField = UIStyles.createLoginPasswordField("Create a password (min 6 characters)");
        JPanel passwordWrapper = createFullWidthWrapper(passwordField);
        passwordWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        contentWrapper.add(passwordWrapper);
        contentWrapper.add(Box.createVerticalStrut(12));

        // Confirm Password field
        contentWrapper.add(createFullWidthWrapper(createFieldLabel("Confirm Password")));
        contentWrapper.add(Box.createVerticalStrut(8));
        confirmPasswordField = UIStyles.createLoginPasswordField("Confirm your password");
        JPanel confirmWrapper = createFullWidthWrapper(confirmPasswordField);
        confirmWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        contentWrapper.add(confirmWrapper);
        contentWrapper.add(Box.createVerticalStrut(20));

        // Sign Up button
        JButton signUpButton = UIStyles.createLoginButton("Sign Up");
        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        signUpButton.addActionListener(e -> handleRegister());
        JPanel buttonWrapper = createFullWidthWrapper(signUpButton);
        buttonWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        contentWrapper.add(buttonWrapper);
        contentWrapper.add(Box.createVerticalStrut(20));

        // Sign in link
        JPanel signInPanel = createSignInPanel();
        JPanel signInWrapper = createCenteredWrapper(signInPanel);
        contentWrapper.add(signInWrapper);

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

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIStyles.LOGIN_LABEL_FONT);
        label.setForeground(UIStyles.TEXT_PRIMARY);
        return label;
    }

    private JPanel createSignInPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(UIStyles.CARD_BACKGROUND);

        JLabel label = new JLabel("Already have an account?");
        label.setFont(UIStyles.LOGIN_LINK_FONT);
        label.setForeground(UIStyles.TEXT_SECONDARY);
        panel.add(label);

        JButton signInButton = UIStyles.createLinkButton("Sign in");
        signInButton.addActionListener(e -> onSwitchToLogin.run());
        panel.add(signInButton);

        return panel;
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validate password match
        if (!password.equals(confirmPassword)) {
            UIStyles.showErrorDialog(this, "Passwords do not match.", "Registration Error");
            return;
        }

        try {
            // Register the customer
            Customer customer = authService.registerCustomer(name, email, phone, password);
            
            // Auto-login after successful registration
            UserSession.getInstance().loginAsCustomer(customer);

            UIStyles.showSuccessDialog(this, 
                "Account created successfully! Welcome, " + customer.getName() + "!", 
                "Registration Successful");

            // Trigger success callback
            onRegisterSuccess.run();

        } catch (FlightBookingSystemException ex) {
            UIStyles.showErrorDialog(this, ex.getMessage(), "Registration Failed");
        }
    }

    /**
     * Clears all form fields.
     */
    public void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
