package admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.*;


public class AdminPanel extends JFrame {

	private JButton connectServer = new JButton("Connect to server");
	private JButton initHelp = new JButton("Initialize/Reset help queue");
	private JButton cancelWork = new JButton("Submit");
	private JButton setCalendar = new JButton("Submit");
	private JLabel cancelLabel = new JLabel("Cancel workstation request: ");
	private JLabel calendarLabel = new JLabel("Set new course: ");
	private JTextArea cancelText = new JTextArea();
	private JTextArea calendarText = new JTextArea();
	private JLabel messageLogger = new JLabel("Admin application opened successfuly");

	public AdminPanel() throws UnknownHostException, IOException {
		super("Admin Help Request");

		// create a new panel with GridBagLayout manager
		JPanel newPanel = new JPanel(new GridBagLayout());
		connectServer.setBackground(Color.GREEN);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		// add components to the panel
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(connectServer, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(initHelp, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(cancelLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		cancelText.setColumns(40);
		newPanel.add(cancelText, constraints);

		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(cancelWork, constraints);
		
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		cancelWork.setBounds(20, 20, 20, 20);
		newPanel.add(calendarLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		calendarText.setColumns(40);
		newPanel.add(calendarText, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		setCalendar.setBounds(20, 20, 20, 20);
		newPanel.add(setCalendar, constraints);
		

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(messageLogger, constraints);
		//HelpRequest.addActionListener(this::helpAction);
		//cancelHelpRequest.addActionListener(this::cancelAction);
		// set border for the panel
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Admin Application"));

		// add the panel to this frame
		add(newPanel);

		pack();
		setLocationRelativeTo(null);
		/*Boolean connect = client.connect();
		this.setVisible(true);
		if (connect) {
			textField.setText("Client Connected Successfully to Server");

		} else {
			textField.setText("Unsuccessful Connection to Server");
			disconnect();
		}*/
	}

	public void disconnect() {
		
		Timer timer = new Timer(6000, (ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		timer.start();
	}

	public void helpAction(ActionEvent e) {
	}

	public void cancelAction(ActionEvent e) {
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		AdminPanel app = new AdminPanel();
		app.setVisible(true);
	}
}
