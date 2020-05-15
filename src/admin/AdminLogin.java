package admin;

import admin.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

public class AdminLogin extends JFrame {
	// private boolean flag = false;
	// private static String username = "";
	// private static String password = "";
	private JLabel usernameLabel = new JLabel("Username: ");
	private JLabel passwordLabel = new JLabel("Password: ");
	private JButton loginButton = new JButton("Submit");
	private JLabel error = new JLabel();
	private JTextArea usernameEntry = new JTextArea("");
	private JTextArea passwordEntry = new JTextArea("");
	AdminPanel admin;
	private Admin ad = new Admin();

	public AdminLogin() throws UnknownHostException, IOException {
		super("Admin Login");
		JPanel loginPanel = new JPanel(new GridBagLayout());
		//Make all threads end when the red x is clicked including the Admin back-end
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		loginPanel.add(usernameLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		usernameEntry.setColumns(20);
		loginPanel.add(usernameEntry, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		loginPanel.add(passwordLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		passwordEntry.setColumns(20);
		loginPanel.add(passwordEntry, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		loginPanel.add(loginButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		loginPanel.add(error, constraints);

		this.loginButton.addActionListener(this::password);

		// Create border
		loginPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Admin Login"));
		// add the panel to this frame
		add(loginPanel);

		pack();

		setLocationRelativeTo(null);
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

	public void password(ActionEvent e) {
		// get the username and password
		String username = usernameEntry.getText();
		String password = passwordEntry.getText();
		System.out.println(username + password);
		String response = ad.login(username, password);
		Boolean checkPass = false;
		while (!checkPass) {
			if (response.equals("Failure")) {
				error.setText("Unsuccessful Connection to Server");
				loginButton.setEnabled(false);
				usernameEntry.setEnabled(false);
				usernameEntry.setText("");
				passwordEntry.setEnabled(false);
				passwordEntry.setText("");
				checkPass = true;
				disconnect();
			} else if (response.equals("WrongPass")) {
				error.setText("Incorrect Username or Password");
			} else {
				try {
					checkPass = true;
					admin = new AdminPanel(ad);		
					admin.setVisible(true); // the username and password are correct then show the admin panel
					dispose(); // dispose of the login panel.
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		AdminLogin login = new AdminLogin();
		login.setVisible(true);
	}
}
