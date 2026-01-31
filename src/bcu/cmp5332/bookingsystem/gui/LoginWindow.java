package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Main login window with split-panel layout.
 * Left side: Login/Register/Forgot password forms
 * Right side: Hero panel with promotional content
 */
public class LoginWindow extends JFrame {

    private static final int WINDOW_WIDTH = 1100;
    private static final int WINDOW_HEIGHT = 700;

    private final FlightBookingSystem flightBookingSystem;
    
    private JPanel leftPanel;
    private CardLayout cardLayout;
    
    private LoginFormPanel loginFormPanel;
    private RegisterFormPanel registerFormPanel;
    private ForgotPasswordPanel forgotPasswordPanel;
    private HeroPanel heroPanel;

    // Card names for navigation
    private static final String LOGIN_CARD = "login";
    private static final String REGISTER_CARD = "register";
    private static final String FORGOT_CARD = "forgot";

    public LoginWindow(FlightBookingSystem flightBookingSystem) {
        this.flightBookingSystem = flightBookingSystem;
        initialize();
    }

    private void initialize() {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Window setup
        setTitle("SkyBook - Flight Booking System");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Handle window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });

        // Main layout
        setLayout(new BorderLayout());

        // Create components
        createLeftPanel();
        createRightPanel();

        // Add to frame
        add(leftPanel, BorderLayout.CENTER);
        add(heroPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private void createLeftPanel() {
        cardLayout = new CardLayout();
        leftPanel = new JPanel(cardLayout);
        leftPanel.setBackground(UIStyles.CARD_BACKGROUND);
        leftPanel.setPreferredSize(new Dimension(500, WINDOW_HEIGHT));

        // Create form panels with callbacks
        loginFormPanel = new LoginFormPanel(
            flightBookingSystem,
            () -> showCard(REGISTER_CARD),    // onSwitchToRegister
            () -> showCard(FORGOT_CARD),       // onForgotPassword
            this::handleLoginSuccess           // onLoginSuccess
        );

        registerFormPanel = new RegisterFormPanel(
            flightBookingSystem,
            () -> showCard(LOGIN_CARD),        // onSwitchToLogin
            this::handleLoginSuccess           // onRegisterSuccess
        );

        forgotPasswordPanel = new ForgotPasswordPanel(
            flightBookingSystem,
            () -> showCard(LOGIN_CARD)         // onBackToLogin
        );

        // Wrap panels in scroll panes for smaller screens
        JScrollPane loginScroll = createScrollPane(loginFormPanel);
        JScrollPane registerScroll = createScrollPane(registerFormPanel);
        JScrollPane forgotScroll = createScrollPane(forgotPasswordPanel);

        // Add cards
        leftPanel.add(loginScroll, LOGIN_CARD);
        leftPanel.add(registerScroll, REGISTER_CARD);
        leftPanel.add(forgotScroll, FORGOT_CARD);

        // Show login by default
        showCard(LOGIN_CARD);
    }

    private JScrollPane createScrollPane(JPanel panel) {
        // Create a wrapper panel to center the form
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIStyles.CARD_BACKGROUND);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Set maximum width for the form panel
        panel.setMaximumSize(new Dimension(400, panel.getPreferredSize().height));
        wrapper.add(panel, gbc);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private void createRightPanel() {
        heroPanel = new HeroPanel();
    }

    private void showCard(String cardName) {
        cardLayout.show(leftPanel, cardName);
        
        // Clear fields when switching cards
        if (cardName.equals(REGISTER_CARD)) {
            registerFormPanel.clearFields();
        } else if (cardName.equals(FORGOT_CARD)) {
            forgotPasswordPanel.clearFields();
        }
    }

    private void handleLoginSuccess() {
        // Save data before transitioning
        try {
            FlightBookingSystemData.store(flightBookingSystem);
        } catch (IOException e) {
            System.err.println("Warning: Could not save data: " + e.getMessage());
        }

        // Hide login window
        setVisible(false);
        
        // Open main window
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow(flightBookingSystem);
            
            // Add listener to handle logout/close
            mainWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    // When main window is closed, check if we should show login again
                    // For now, just exit the application
                    handleWindowClose();
                }
            });
        });

        // Dispose login window
        dispose();
    }

    private void handleWindowClose() {
        // Save data before closing
        try {
            FlightBookingSystemData.store(flightBookingSystem);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
        
        // Exit application
        System.exit(0);
    }

    /**
     * Static method to launch the login window.
     */
    public static void launch(FlightBookingSystem flightBookingSystem) {
        SwingUtilities.invokeLater(() -> new LoginWindow(flightBookingSystem));
    }
}
