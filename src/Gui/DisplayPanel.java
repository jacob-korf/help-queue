package Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import javax.swing.*;

public class DisplayPanel extends JFrame {

    private JTextArea currentClients = new JTextArea("Current Clients: ");
    private JTextArea workstation = new JTextArea("WorkStation");
    private JTextArea RequestTime = new JTextArea("RequestTime");
    private JTextArea WaitTime = new JTextArea("WaitTime");
    private JTextArea positionNumber = new JTextArea("PositionNumber");
    private JTextField display0 = new JTextField();
    private JTextField display1 = new JTextField();
    private JTextField display2 = new JTextField();
    private JTextField display3 = new JTextField();
    private JTextField display4 = new JTextField();
    long epoch = System.currentTimeMillis() / 1000;
    String date = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(epoch * 1000));


    public DisplayPanel() {
        super("Help Queue Display");

        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        newPanel.add(currentClients, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        newPanel.add(positionNumber, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        newPanel.add(workstation, constraints);

        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        newPanel.add(RequestTime, constraints);

        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        newPanel.add(WaitTime, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 7;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        display0.setColumns(60);
        newPanel.add(display0, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 7;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        display1.setColumns(60);
        newPanel.add(display1, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 7;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        display2.setColumns(60);
        newPanel.add(display2, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 7;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        display3.setColumns(60);
        newPanel.add(display3, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 7;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.WEST;
        display4.setColumns(60);
        newPanel.add(display4, constraints);

        setDisplay();

        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Philips 115 Lab " + date));

        int delay = 1000;
        ActionListener update = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                date = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));
            }
        };
        new Timer(delay, update).start();
        // add the panel to this frame
        add(newPanel);

        pack();
        setLocationRelativeTo(null);
    }

    public void setDisplay() {
        display0.setText(" things  ");
    }
}
