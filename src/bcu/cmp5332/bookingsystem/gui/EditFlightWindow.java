package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.EditFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Window for editing an existing flight's details.
 */
public class EditFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private int flightId;
    
    private JTextField flightNoText = new JTextField();
    private JTextField originText = new JTextField();
    private JTextField destinationText = new JTextField();
    private JTextField depDateText = new JTextField();
    private JTextField capacityText = new JTextField();
    private JTextField priceText = new JTextField();

    private JButton saveBtn = new JButton("Save Transitions");
    private JButton cancelBtn = new JButton("Cancel");

    public EditFlightWindow(MainWindow mw, int flightId) {
        this.mw = mw;
        this.flightId = flightId;
        initialize();
        loadFlightData();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Edit Flight Details");

        setSize(400, 300);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(7, 2, 10, 10));
        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        topPanel.add(new JLabel("Flight No : "));
        topPanel.add(flightNoText);
        topPanel.add(new JLabel("Origin : "));
        topPanel.add(originText);
        topPanel.add(new JLabel("Destination : "));
        topPanel.add(destinationText);
        topPanel.add(new JLabel("Departure Date (YYYY-MM-DD) : "));
        topPanel.add(depDateText);
        topPanel.add(new JLabel("Capacity (seats) : "));
        topPanel.add(capacityText);
        topPanel.add(new JLabel("Price (£) : "));
        topPanel.add(priceText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3, 10, 10));
        bottomPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 20, 20));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);

        saveBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);

        setVisible(true);
    }

    private void loadFlightData() {
        try {
            Flight flight = mw.getFlightBookingSystem().getFlightByID(flightId);
            flightNoText.setText(flight.getFlightNumber());
            originText.setText(flight.getOrigin());
            destinationText.setText(flight.getDestination());
            depDateText.setText(flight.getDepartureDate().toString());
            capacityText.setText(String.valueOf(flight.getCapacity()));
            priceText.setText(String.valueOf(flight.getPrice()));
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == saveBtn) {
            updateFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void updateFlight() {
        try {
            String flightNumber = flightNoText.getText();
            String origin = originText.getText();
            String destination = destinationText.getText();
            LocalDate departureDate = LocalDate.parse(depDateText.getText());
            int capacity = Integer.parseInt(capacityText.getText());
            double price = Double.parseDouble(priceText.getText());

            Command editFlight = new EditFlight(flightId, flightNumber, origin, destination, departureDate, capacity, price);
            editFlight.execute(mw.getFlightBookingSystem());
            
            mw.refreshAllPanels();
            this.setVisible(false);
            JOptionPane.showMessageDialog(mw, "Flight details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Batch date must be in YYYY-MM-DD format", "Invalid Date", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity and Price must be numeric", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
