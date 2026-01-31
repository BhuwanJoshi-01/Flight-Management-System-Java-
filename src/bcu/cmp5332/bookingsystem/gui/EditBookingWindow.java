package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.EditBooking;
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

/**
 * Window for editing/updating a booking.
 */
public class EditBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField bookingIdText = new JTextField();
    private JTextField newFlightIdText = new JTextField();

    private JButton updateBtn = new JButton("Update");
    private JButton cancelBtn = new JButton("Cancel");

    public EditBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Update Booking");

        setSize(350, 150);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2));
        topPanel.add(new JLabel("Current Flight ID : "));
        topPanel.add(bookingIdText);
        topPanel.add(new JLabel("New Flight ID : "));
        topPanel.add(newFlightIdText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(updateBtn);
        bottomPanel.add(cancelBtn);

        updateBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            updateBooking();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void updateBooking() {
        try {
            int bookingId;
            int newFlightId;
            try {
                bookingId = Integer.parseInt(bookingIdText.getText());
                newFlightId = Integer.parseInt(newFlightIdText.getText());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("IDs must be valid numbers");
            }
            
            Command editBooking = new EditBooking(bookingId, newFlightId);
            editBooking.execute(mw.getFlightBookingSystem());
            mw.refreshAllPanels();
            UIStyles.showSuccessDialog(this, "Booking updated successfully! A 10% rebooking fee has been applied.", "Success");
            this.setVisible(false);
        } catch (FlightBookingSystemException ex) {
            UIStyles.showErrorDialog(this, ex.getMessage(), "Error");
        }
    }
}

