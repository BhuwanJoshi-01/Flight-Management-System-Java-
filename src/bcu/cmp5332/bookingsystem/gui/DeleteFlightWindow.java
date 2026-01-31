package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.DeleteFlight;
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
 * Window for deleting a flight from the system.
 */
public class DeleteFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField flightIdText = new JTextField();

    private JButton deleteBtn = new JButton("Delete");
    private JButton cancelBtn = new JButton("Cancel");

    public DeleteFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Delete a Flight");

        setSize(300, 120);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));
        topPanel.add(new JLabel("Flight ID : "));
        topPanel.add(flightIdText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(deleteBtn);
        bottomPanel.add(cancelBtn);

        deleteBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == deleteBtn) {
            deleteFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void deleteFlight() {
        try {
            int flightId;
            try {
                flightId = Integer.parseInt(flightIdText.getText());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Flight ID must be a valid number");
            }
            
            boolean confirm = UIStyles.showWarningConfirmDialog(this,
                "Are you sure you want to delete Flight #" + flightId + "? This action cannot be undone.",
                "Confirm Delete");

            if (confirm) {
                Command deleteFlight = new DeleteFlight(flightId);
                deleteFlight.execute(mw.getFlightBookingSystem());
                mw.refreshAllPanels();
                UIStyles.showSuccessDialog(this, "Flight deleted successfully!", "Success");
                this.setVisible(false);
            }
        } catch (FlightBookingSystemException ex) {
            UIStyles.showErrorDialog(this, ex.getMessage(), "Error");
        }
    }
}

