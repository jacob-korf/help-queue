package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import javax.swing.*;

import client.Client;

public class ClientPanel extends JFrame {

	private JButton cancelHelpRequest = new JButton("Cancel Help Request");
	private JButton HelpRequest = new JButton("Submit Help Request");
	private JLabel textField = new JLabel();
	private JLabel averageWait = new JLabel("Average Wait Time:");
	private JLabel queuePos = new JLabel("Current Queue Position: N/A");
	private JButton nameButton = new JButton("Submit new preferred name");
	private JTextArea nameText = new JTextArea("");
	private JLabel preferName = new JLabel("Preferred Name: N/A");
	private Client client;
	private Boolean submitted = false;

	public ClientPanel() throws UnknownHostException, IOException {
		super("Client Help Request");
		client = new Client();
		Thread updateClock = new Thread(new Runnable() {
			// updates every second
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					update();
				}
			} // end - method run
		}); // end - thread readMessage

		updateClock.start();
		// create a new panel with GridBagLayout manager
		JPanel newPanel = new JPanel(new GridBagLayout());
		// Make all threads end when the red x is clicked including the Client back-end
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		cancelHelpRequest.setBackground(Color.WHITE);
		HelpRequest.setBackground(Color.GREEN);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 50, 10, 50);

		// add components to the panel

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(preferName, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(HelpRequest, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(cancelHelpRequest, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		averageWait.setBounds(20, 20, 20, 20);
		newPanel.add(averageWait, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.WEST;
		queuePos.setBounds(20, 20, 20, 20);
		newPanel.add(queuePos, constraints);


		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		nameText.setColumns(20);
		nameText.setMinimumSize(new Dimension(224, 20));
		newPanel.add(nameText, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(nameButton, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		textField.setBounds(20, 20, 20, 20);
		newPanel.add(textField, constraints);

		HelpRequest.addActionListener(this::helpAction);
		cancelHelpRequest.addActionListener(this::cancelAction);
		nameButton.addActionListener(this::nameAction);
		// set border for the panel
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Help Request"));

		// add the panel to this frame
		add(newPanel);

		pack();
		setLocationRelativeTo(null);
		// check if client successfully connected to server
		Boolean connect = client.connect();
		this.setVisible(true);
		if (connect) {
			textField.setText("Client Connected Successfully to Server");

		} else {
			textField.setText("Unsuccessful Connection to Server");
			disconnect();
		}
	}

	public void disconnect() {
		// disconnect
		HelpRequest.setEnabled(false);
		cancelHelpRequest.setEnabled(false);
		nameButton.setEnabled(false);

		Timer timer = new Timer(6000, (ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		timer.start();
	}

	public void helpAction(ActionEvent e) {
		// submission of the client help request
		String completed = client.sendRequest();
		if (completed.equals("Failure")) {
			textField.setText("Unsuccessful Connection to Server");
			disconnect();
		} else {
			submitted = true;
			textField.setText("Help Request successfully sent to queue");
			cancelHelpRequest.setBackground(Color.RED); // cancel button set to red showing it can be canceled
			HelpRequest.setBackground(Color.WHITE); // help request set to white showing request was made
			HelpRequest.setEnabled(false); // disable help request button
			cancelHelpRequest.setEnabled(true); // enable cancel button
		}
	}

	public void nameAction(ActionEvent e) {
		// submission of the client help request
		if (nameText.getText().length() > 0) {
			if (nameText.getText().length() < 11) {
				String completed = client.sendName(nameText.getText());
				if (completed.equals("Failure")) {
					textField.setText("Unsuccessful Connection to Server");
					disconnect();
				} else {
					textField.setText("Name updated to " + nameText.getText());
					preferName.setText("Preferred Name: " + nameText.getText());
				}
			} else {
				textField.setText("Preferred Name must be 12 characters or less");
			}
		} else {
			textField.setText("Preferred Name cannot be blank");
		}
	}

	public void cancelAction(ActionEvent e) {
		String cancelled = client.cancelRequest();
		if (cancelled.equals("Failure")) {
			// failed cancel
			textField.setText("Unsuccessful Connection to Server");
			disconnect();
		} else {
			// successful cancel
			textField.setText("Help request cancelled from queue");
			cancelRequest();
		}
	}

	public void cancelRequest() {
		// cancel of the help request
		submitted = false;
		queuePos.setText("Current Queue Position: N/A");
		cancelHelpRequest.setBackground(Color.WHITE); // set cancel button to white showing request was canceled
		HelpRequest.setBackground(Color.GREEN); // set help request button to green so they can make a new request
		HelpRequest.setEnabled(true); // enable help request button
		cancelHelpRequest.setEnabled(false); // disable cancel help request button
	}

	public void update() {
		// update of client if its help request is cancelled by the admin
		if (submitted) {
			String update = client.update();
			if (update.equals("Failure")) {
				textField.setText("Unsuccessful Connection to Server");
				disconnect();
			} else {
				String[] arrOfStr = update.split("#");
				try {
					int average = Integer.parseInt(arrOfStr[0]);
					String typeUpdate = arrOfStr[1];
					averageWait.setText("Average Wait Time: " + average / 60 + "m, " + average % 60 + "s");
					if (typeUpdate.equals("Cancel")) {// admin removed help request
						textField.setText("Help Request Forcefully Removed by Administrator");
						cancelRequest();
					} else {
						queuePos.setText("Current Queue Position: " + arrOfStr[2]);
					}
				} catch (NumberFormatException ne) {
					textField.setText("Unsuccessful Connection to Server");
					disconnect();
				}
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		ClientPanel app = new ClientPanel();
		app.setVisible(true);
	}
}
