package Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import javax.swing.*;

import multithreadchatclient.Client;

public class ClientPanel extends JFrame {

	/**
	 * comments max
	 */

	private JButton cancelHelpRequest = new JButton("Cancel Help Request");
	private JButton HelpRequest = new JButton("Submit Help Request");
	private JLabel textField = new JLabel();
	private Client client;
	private Boolean submitted = false;

	public ClientPanel() throws UnknownHostException, IOException {
		super("Client Help Request");
		client = new Client();
		Thread updateClock = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
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
		cancelHelpRequest.setBackground(Color.WHITE);
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
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Help Request"));

		// add the panel to this frame
		add(newPanel);

		pack();
		setLocationRelativeTo(null);
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
		HelpRequest.setEnabled(false);
		cancelHelpRequest.setEnabled(false);

		Timer timer = new Timer(6000, (ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		timer.start();
	}

	public void helpAction(ActionEvent e) {
		String completed = client.sendRequest();
		if (completed.equals("Failure")) {
			textField.setText("Unsuccessful Connection to Server");
			disconnect();
		} else {
			submitted = true;
			textField.setText("Help Request successfully sent to queue");
			cancelHelpRequest.setBackground(Color.RED);
			HelpRequest.setBackground(Color.WHITE);
			HelpRequest.setEnabled(false);
			cancelHelpRequest.setEnabled(true);
		}
	}

	public void cancelAction(ActionEvent e) {
		String cancelled = client.cancelRequest();
		if (cancelled.equals("Failure")) {
			textField.setText("Unsuccessful Connection to Server");
			disconnect();
		} else {
			textField.setText("Help request successfully cancelled from queue");
			cancelRequest();
		}
	}

	public void cancelRequest() {
		submitted = false;
		cancelHelpRequest.setBackground(Color.WHITE);
		HelpRequest.setBackground(Color.GREEN);
		HelpRequest.setEnabled(true);
		cancelHelpRequest.setEnabled(false);
	}

	public void update() {
		if (submitted) {
			String update = client.update();
			if (update.equals("Failure")) {
				textField.setText("Unsuccessful Connection to Server");
				disconnect();
			} else if(update.equals("Cancel")){
				
				textField.setText("Help Request Forcefully Removed by Administrator");
				cancelRequest();
			}
		}
	}

	

	public static void main(String[] args) throws UnknownHostException, IOException {
		ClientPanel app = new ClientPanel();
		app.setVisible(true);
	}
}
