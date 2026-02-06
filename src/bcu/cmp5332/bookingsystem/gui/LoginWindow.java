package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.UserSession;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.gui.customer.CustomerMainWindow;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Main login window with split-panel layout.
 * Left side: Login/Register/Forgot password forms
 * Right side: Hero panel with promotional content
 * 
 * Responsive behavior:
 * - >= 800px width: Shows both form and hero panels (55%/45% split)
 * - < 800px width: Shows only form panel (hero hidden)
 */
public class LoginWindow extends JFrame {

    private static final int WINDOW_WIDTH = 1100;
    private static final int WINDOW_HEIGHT = 700;
    private static final int BREAKPOINT_WIDTH = 700;

    private final FlightBookingSystem flightBookingSystem;
    
    private JPanel mainPanel;
    private JPanel leftPanel;
    private CardLayout cardLayout;
    
    private LoginFormPanel loginFormPanel;
    private RegisterFormPanel registerFormPanel;
    private ForgotPasswordPanel forgotPasswordPanel;
    private HeroPanel heroPanel;
    
    private boolean heroVisible = true;

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
        setMinimumSize(new Dimension(350, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Handle window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });

        // Create components first
        createLeftPanel();
        createRightPanel();
        
        // Main panel using GridBagLayout for proportional sizing
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIStyles.CARD_BACKGROUND);
        
        layoutPanels();
        
        setContentPane(mainPanel);
        
        // Add resize listener for responsive behavior
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                handleResize();
            }
        });

        setVisible(true);
    }
    
    private void layoutPanels() {
        mainPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        
        if (heroVisible) {
            // Left panel (form) - 60% width for better visibility
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.60;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            mainPanel.add(leftPanel, gbc);
            
            // Right panel (hero) - 40% width
            gbc.gridx = 1;
            gbc.weightx = 0.40;
            mainPanel.add(heroPanel, gbc);
        } else {
            // Only form panel - centered, full width
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            mainPanel.add(leftPanel, gbc);
        }
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void handleResize() {
        int width = getWidth();
        boolean shouldShowHero = width >= BREAKPOINT_WIDTH;
        
        if (shouldShowHero != heroVisible) {
            heroVisible = shouldShowHero;
            layoutPanels();
        }
    }

    private void createLeftPanel() {
        cardLayout = new CardLayout();
        leftPanel = new JPanel(cardLayout);
        leftPanel.setBackground(UIStyles.CARD_BACKGROUND);

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

        // Wrap panels in scroll panes for small-window support
        JScrollPane loginWrapper = createScrollableWrapper(loginFormPanel);
        JScrollPane registerWrapper = createScrollableWrapper(registerFormPanel);
        JScrollPane forgotWrapper = createScrollableWrapper(forgotPasswordPanel);

        // Add cards
        leftPanel.add(loginWrapper, LOGIN_CARD);
        leftPanel.add(registerWrapper, REGISTER_CARD);
        leftPanel.add(forgotWrapper, FORGOT_CARD);

        // Show login by default
        showCard(LOGIN_CARD);
    }

    private JScrollPane createScrollableWrapper(JPanel panel) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIStyles.CARD_BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 15, 15, 15);

        wrapper.add(panel, gbc);

        JScrollPane scrollPane = new JScrollPane(wrapper,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UIStyles.CARD_BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
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
        
        // Check user role and open appropriate window
        SwingUtilities.invokeLater(() -> {
            UserSession session = UserSession.getInstance();
            
            if (session.isAdmin()) {
                // Admin user - open Admin Portal (MainWindow)
                MainWindow mainWindow = new MainWindow(flightBookingSystem);
                mainWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        handleWindowClose();
                    }
                });
            } else {
                // Customer user - open Customer Portal
                CustomerMainWindow customerWindow = new CustomerMainWindow(flightBookingSystem);
                customerWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        handleWindowClose();
                    }
                });
            }
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
