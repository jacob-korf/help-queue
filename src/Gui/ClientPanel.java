package Gui;


import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class ClientPanel extends JFrame {


    private JButton cancelHelpRequest = new JButton("Cancel Help Request");
    private JButton HelpRequest = new JButton("Submit Help Request");
    private JTextField textField = new JTextField(20 );


    public ClientPanel() {
        super("Client Help Request");

        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());
        cancelHelpRequest.setBackground(Color.RED);
        HelpRequest.setBackground(Color.GREEN);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 50, 10, 50);

        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        newPanel.add(HelpRequest, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        newPanel.add(cancelHelpRequest, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        textField.setBounds(20, 20, 20, 20);
        newPanel.add(textField, constraints);

        HelpRequest.addActionListener(this::helpAction);
        cancelHelpRequest.addActionListener(this::cancelAction);
        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Help Request"));

        // add the panel to this frame
        add(newPanel);

        pack();
        setLocationRelativeTo(null);
    }

    public void helpAction(ActionEvent e){
        textField.setText("Help Requested");
    }

    public void cancelAction(ActionEvent e){
        textField.setText("Help Request canceled");
    }


}
