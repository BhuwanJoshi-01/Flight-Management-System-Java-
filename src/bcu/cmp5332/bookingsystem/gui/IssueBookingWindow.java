package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class IssueBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField customerIdText = new JTextField();
    private JTextField flightIdText = new JTextField();
    private JTextField returnFlightIdText = new JTextField();

    private JButton issueBtn = new JButton("Issue Booking");
    private JButton cancelBtn = new JButton("Cancel");

    public IssueBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Issue a New Booking");

        setSize(400, 200);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 2));
        topPanel.add(new JLabel("Customer ID : "));
        topPanel.add(customerIdText);
        topPanel.add(new JLabel("Outbound Flight ID : "));
        topPanel.add(flightIdText);
        topPanel.add(new JLabel("Return Flight ID (optional) : "));
        topPanel.add(returnFlightIdText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(issueBtn);
        bottomPanel.add(cancelBtn);

        issueBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == issueBtn) {
            issueBooking();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void issueBooking() {
        try {
            int customerId;
            int flightId;
            int returnFlightId = -1;
            
            try {
                customerId = Integer.parseInt(customerIdText.getText());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Customer ID must be a valid number");
            }
            
            try {
                flightId = Integer.parseInt(flightIdText.getText());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Flight ID must be a valid number");
            }
            
            String returnFlightStr = returnFlightIdText.getText().trim();
            if (!returnFlightStr.isEmpty()) {
                try {
                    returnFlightId = Integer.parseInt(returnFlightStr);
                } catch (NumberFormatException nfe) {
                    throw new FlightBookingSystemException("Return Flight ID must be a valid number");
                }
            }
            
            Command addBooking;
            if (returnFlightId != -1) {
                addBooking = new AddBooking(customerId, flightId, returnFlightId);
            } else {
                addBooking = new AddBooking(customerId, flightId);
            }
            addBooking.execute(mw.getFlightBookingSystem());
            mw.refreshAllPanels();
            UIStyles.showSuccessDialog(this, "Booking issued successfully!", "Success");
            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            UIStyles.showErrorDialog(this, ex.getMessage(), "Error");
        }
    }
}

