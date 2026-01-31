package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.CancelBooking;
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

public class CancelBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField customerIdText = new JTextField();
    private JTextField flightIdText = new JTextField();

    private JButton cancelBookingBtn = new JButton("Cancel Booking");
    private JButton closeBtn = new JButton("Close");

    public CancelBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Cancel a Booking");

        setSize(350, 160);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2));
        topPanel.add(new JLabel("Customer ID : "));
        topPanel.add(customerIdText);
        topPanel.add(new JLabel("Flight ID : "));
        topPanel.add(flightIdText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(cancelBookingBtn);
        bottomPanel.add(closeBtn);

        cancelBookingBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == cancelBookingBtn) {
            cancelBooking();
        } else if (ae.getSource() == closeBtn) {
            this.setVisible(false);
        }
    }

    private void cancelBooking() {
        try {
            int customerId;
            int flightId;
            
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
            
            Command cancelBooking = new CancelBooking(customerId, flightId);
            cancelBooking.execute(mw.getFlightBookingSystem());
            mw.refreshAllPanels();
            UIStyles.showSuccessDialog(this, "Booking cancelled successfully! A 10% cancellation fee has been applied.", "Success");
            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            UIStyles.showErrorDialog(this, ex.getMessage(), "Error");
        }
    }
}

