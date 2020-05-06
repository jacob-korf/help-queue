package admin;

import multithreadchatclient.Admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import javax.swing.*;

public class AdminPanel extends JFrame {

	// private static String contents = "";
	private JButton connectServer = new JButton("Connect to server");
	private JButton initHelp = new JButton("Initialize/Reset help queue");
	private JButton cancelWork = new JButton("Submit");
	private JButton setCalendar = new JButton("Submit course");
	private JLabel cancelLabel = new JLabel("Cancel workstation request: ");
	private JLabel courseNumberLabel = new JLabel("Class course number: ");
	private JLabel sectionLabel = new JLabel("Section Number: ");
	private JLabel startDateLabel = new JLabel("Start Date: ");
	private JLabel endDateLabel = new JLabel("End Date: ");
	private JLabel startTimeLabel = new JLabel("Start Time: ");
	private JLabel endTimeLabel = new JLabel("End Time: ");
	private JTextArea startTime = new JTextArea("");
	private JTextArea endTime = new JTextArea("");
	private JTextArea startDate = new JTextArea("");
	private JTextArea endDate = new JTextArea("");
	private JTextArea cancelText = new JTextArea("");
	private JTextArea sectionNumber = new JTextArea("");
	private JTextArea courseNumber = new JTextArea("");
	private JLabel messageLogger = new JLabel("Admin application opened successfuly");
	private Admin admin;

	public AdminPanel() throws UnknownHostException, IOException {
		super("Admin Help Request");
		admin = new Admin();
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
		newPanel.add(courseNumberLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		cancelWork.setBounds(20, 20, 20, 20);
		newPanel.add(sectionLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		cancelWork.setBounds(20, 20, 20, 20);
		newPanel.add(startDateLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		cancelWork.setBounds(20, 20, 20, 20);
		newPanel.add(endDateLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		cancelWork.setBounds(20, 20, 20, 20);
		newPanel.add(startTimeLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		cancelWork.setBounds(20, 20, 20, 20);
		newPanel.add(endTimeLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		courseNumber.setColumns(20);
		newPanel.add(courseNumber, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		sectionNumber.setColumns(20);
		newPanel.add(sectionNumber, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		startDate.setColumns(20);
		newPanel.add(startDate, constraints);

		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		endDate.setColumns(20);
		newPanel.add(endDate, constraints);

		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		startTime.setColumns(20);
		newPanel.add(startTime, constraints);

		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		endTime.setColumns(20);
		newPanel.add(endTime, constraints);

		constraints.gridx = 2;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		setCalendar.setBounds(20, 20, 20, 20);
		newPanel.add(setCalendar, constraints);

		constraints.gridx = 0;
		constraints.gridy = 8;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(messageLogger, constraints);
		this.cancelWork.addActionListener(this::cancelAction);
		// set border for the panel
		this.setCalendar.addActionListener(this::setCalendar);
		connectServer.addActionListener(this::connectAction);
		initHelp.addActionListener(this::resetAction);
		cancelWork.addActionListener(this::cancelAction);

		initHelp.setEnabled(false);
		cancelWork.setEnabled(false);
		setCalendar.setEnabled(false);

		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Admin Application"));

		// add the panel to this frame
		add(newPanel);

		pack();
		setLocationRelativeTo(null);
		/*
		 * Boolean connect = client.connect(); this.setVisible(true); if (connect) {
		 * textField.setText("Client Connected Successfully to Server");
		 * 
		 * } else { textField.setText("Unsuccessful Connection to Server");
		 * disconnect(); }
		 */
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

	public void connectAction(ActionEvent e) {
		if (admin.connect()) {
			messageLogger.setText("Unsuccessful Connection to Server");
			disconnect();
		} else {
			messageLogger.setText("Successful Connection to Server");
			initHelp.setEnabled(true);
			initHelp.setBackground(Color.GREEN);
			cancelWork.setEnabled(true);
			cancelWork.setBackground(Color.GREEN);
			setCalendar.setEnabled(true);
			setCalendar.setBackground(Color.GREEN);
			connectServer.setEnabled(false);
			connectServer.setBackground(Color.WHITE);
		}
	}

	public void resetAction(ActionEvent e) {
		admin.resetList();
		messageLogger.setText("Help Queue Initialized/Re-initialized");
	}

	public void cancelAction(ActionEvent e) {
		String str = admin.cancelRequest(cancelText.getText());
		messageLogger.setText(str);
	}

	public void setCalendar(ActionEvent e) {
		// get all the input information
		String courseNumberInput = courseNumber.getText();
		int sectionNumberInput = Integer.parseInt(sectionNumber.getText());
		String startDateInput = startDate.getText();
		String endDateInput = endDate.getText();
		int startTimeInput = Integer.parseInt(startTime.getText());
		int endTimeInput = Integer.parseInt(endTime.getText());
		cancelText.setText(courseNumberInput);
		courseNumber.setText("");
		String query = "INSERT INTO calendar (courseNumber, sectionNumber, startDate, endDate, startTime, endTime) VALUES('"
				+ courseNumberInput + "', '" + sectionNumberInput + "', '" + startDateInput + "', '" + endDateInput
				+ "', '" + startTimeInput + "', '" + endTimeInput + "')";
		System.out.println(query);
		admin.getQuery(query);

	}

	public static void main(String[] args) throws UnknownHostException, IOException {

		AdminPanel app = new AdminPanel();
		app.setVisible(true);
	}
}
