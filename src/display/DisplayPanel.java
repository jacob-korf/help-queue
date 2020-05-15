package display;

import display.Display;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

public class DisplayPanel extends JFrame {

	private static JLabel currentClients = new JLabel("Current Client: ");
	private JLabel workstation = new JLabel("WorkStation             ");
	private JLabel RequestTime = new JLabel("   RequestTime          ");
	private JLabel WaitTime = new JLabel("   WaitTime");
	private JLabel positionNumber = new JLabel("PositionNumber     ");
	private JLabel preferName = new JLabel("Name             ");
	private static JLabel clock = new JLabel();
	private static JTextArea queue = null;
	static Display display;
	private static Boolean connect = false;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				DisplayPanel displaygui;
				try {
					displaygui = new DisplayPanel();
					displaygui.setVisible(true);
					// if connected
					if (connect) {
						Thread readMessage = new Thread(new Runnable() {
							@Override
							public void run() {
								while (true) {
									// getting the help request
									String queueText = display.getRequest();
									if (queueText.equals("Failure")) {
										queue.setText("Unsuccessful Connection to Server");
										disconnect();
									} else if (queueText.substring(queueText.indexOf("###")+3).equals("")) { // check if no current help requests
										queue.setText("No Current Help Requests");
										currentClients.setText("Current Course: " + queueText.substring(0, queueText.indexOf("###"))); // setting the current course on the display
									} else {
										queue.setText(queueText.substring(queueText.indexOf("###")+3)); // help request exists
										currentClients.setText("Current Course: " + queueText.substring(0, queueText.indexOf("###"))); // setting the current course on the display
									}
								}
							} // end - method run
						}); // end - thread readMessage
						Thread updateClock = new Thread(new Runnable() {
							// updates the display clock time every second
							@Override
							public void run() {
								while (true) {
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									// getting the current time and setting it in the clock
									LocalDateTime current = LocalDateTime.now();
									setClock(current);
								}
							} // end - method run
						}); // end - thread readMessage
						updateClock.start();
						readMessage.start();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public static void disconnect() {
		Timer timer = new Timer(6000, (ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		timer.start();
	}

	public DisplayPanel() throws UnknownHostException, IOException {

		super("Help Queue Display");
		display = new Display();
		connect = display.connect();
		//Make all threads end when the red x is clicked including the Display back-end
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// create a new panel with GridBagLayout manager
		JPanel newPanel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(10, 10, 10, 10);

		// Adding all display components to the gui
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(clock, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 4;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(currentClients, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(positionNumber, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(workstation, constraints);

		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(preferName, constraints);
		
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(RequestTime, constraints);

		constraints.gridx = 4;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(WaitTime, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 7;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;

		queue = new JTextArea(10, 70);
		queue.setBounds(320, 75, 260, 260);
		newPanel.add(queue, constraints);
		queue.setEditable(false);

		// set border for the panel
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Philips 115 Lab:"));

		// add the panel to this frame
		add(newPanel);

		pack();
		setLocationRelativeTo(null);
		if (!connect) {
			queue.setText("Unsuccessful Connection to Server");
			disconnect();
		}

	}
	// getting preferred size for the display.
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700, 400);
	}

	public static void setClock(LocalDateTime current) {
		// setting the clock initial time.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDateTime = current.format(formatter);
		clock.setText("Time: " + formatDateTime);
	}
}