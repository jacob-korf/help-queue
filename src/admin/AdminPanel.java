package admin;

import admin.Admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.*;

@SuppressWarnings("serial")
public class AdminPanel extends JFrame {

	private JButton initHelp = new JButton("Initialize/Reset help queue");
	private JButton cancelWork = new JButton("Submit");
	private JButton setCalendar = new JButton("Submit course");
	private JLabel cancelLabel = new JLabel("Cancel workstation request: ");
	private JLabel courseNumberLabel = new JLabel("Class course number: ");
	private JLabel sectionLabel = new JLabel("Section Number (Integer): ");
	private JLabel startDateLabel = new JLabel("Start Date (Month Day, Year): ");
	private JLabel endDateLabel = new JLabel("End Date (Month Day, Year): ");
	private JLabel startTimeLabel = new JLabel("Start Time (HH:MM, hours range from 0 to 24): ");
	private JLabel endTimeLabel = new JLabel("End Time (HH:MM, hours range from 0 to 24): ");
	private JFormattedTextField startTime = new JFormattedTextField();
	private JFormattedTextField endTime = new JFormattedTextField();
	private JFormattedTextField startDate = new JFormattedTextField();
	private JFormattedTextField endDate = new JFormattedTextField();
	private JFormattedTextField sectionNumber = new JFormattedTextField();
	private JTextArea courseNumber = new JTextArea("");
	private JCheckBox mon = new JCheckBox("Monday");
	private JCheckBox tues = new JCheckBox("Tuesday");
	private JCheckBox wed = new JCheckBox("Wednesday");
	private JCheckBox thurs = new JCheckBox("Thursday");
	private JCheckBox fri = new JCheckBox("Friday");
	private JCheckBox sat = new JCheckBox("Saturday");
	private JCheckBox sun = new JCheckBox("Sunday");
	private JTextArea cancelText = new JTextArea("");
	private JLabel messageLogger = new JLabel("Admin application opened successfuly");
	private Admin admin;

	public AdminPanel(Admin ad) throws UnknownHostException, IOException {
		super("Admin Help Request");
		admin = ad;
		// create a new panel with GridBagLayout manager
		JPanel newPanel = new JPanel(new GridBagLayout());

		//Make all threads end when the red x is clicked including the Admin back-end
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//Set basic traits of JFormattedTextBoxes
		startDate.setValue(new Date());
		startDate.setText("");
		startDate.setFocusLostBehavior(JFormattedTextField.COMMIT);
		
		endDate.setValue(new Date());
		endDate.setText("");
		endDate.setFocusLostBehavior(JFormattedTextField.COMMIT);


		startTime.setText("");
		startTime.setFocusLostBehavior(JFormattedTextField.COMMIT);

		endTime.setText("");
		endTime.setFocusLostBehavior(JFormattedTextField.COMMIT);

		sectionNumber.setText("");
		sectionNumber.setFocusLostBehavior(JFormattedTextField.COMMIT);



		this.cancelWork.addActionListener(this::cancelAction);
		this.setCalendar.addActionListener(this::setCalendar);
		initHelp.addActionListener(this::resetAction);
		cancelWork.addActionListener(this::cancelAction);
		initHelp.setBackground(Color.GREEN);
		cancelWork.setBackground(Color.GREEN);
		setCalendar.setBackground(Color.GREEN);


		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		// add components to the panel

		constraints.gridx = 0;
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
		newPanel.add(courseNumberLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(sectionLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(startDateLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(endDateLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(startTimeLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
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

		constraints.gridx = 0;
		constraints.gridy = 8;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(mon, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(tues, constraints);

		constraints.gridx = 0;
		constraints.gridy = 10;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(wed, constraints);

		constraints.gridx = 0;
		constraints.gridy = 11;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(thurs, constraints);

		constraints.gridx = 0;
		constraints.gridy = 12;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(fri, constraints);

		constraints.gridx = 0;
		constraints.gridy = 13;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(sat, constraints);

		constraints.gridx = 0;
		constraints.gridy = 14;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(sun, constraints);
		

		constraints.gridx = 2;
		constraints.gridy = 14;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		setCalendar.setBounds(20, 20, 20, 20);
		newPanel.add(setCalendar, constraints);

		constraints.gridx = 0;
		constraints.gridy = 16;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		newPanel.add(messageLogger, constraints);
		
		//Create border
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Admin Application"));

		// add the panel to this frame
		add(newPanel);

		pack();
		setLocationRelativeTo(null);
		
	}

	//Call this sequence to end the program in case of errors
	public void disconnect() {

		Timer timer = new Timer(6000, (ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		timer.start();
	}


	//Listener to call Admin to reset the help list
	public void resetAction(ActionEvent e) {
		admin.resetList();
		messageLogger.setText("Help Queue Initialized/Re-initialized");
	}

	//Listener to call Admin to a cancel a specific workstation
	public void cancelAction(ActionEvent e) {
		String str = admin.cancelRequest(cancelText.getText());
		messageLogger.setText(str);
		cancelText.setText("");
	}

	//Listener to call Admin to add a calendar entry
	public void setCalendar(ActionEvent e) {
		// get all the input information
    	String startTimeStr = startTime.getText();
    	String endTimeStr = endTime.getText();
		int i = 0;
		
		//Error Checking; If a text box has bad input, it will turn red
		if(startDate.isEditValid()) {
			++i;
			startDate.setBackground(Color.WHITE);
		} else {
			startDate.setBackground(Color.RED);
		}
		if(endDate.isEditValid()) {
			++i;
			endDate.setBackground(Color.WHITE);
		} else {
			endDate.setBackground(Color.RED);
		}
		 try {
				if (startTimeStr.length() == 4) {
					int hh = Integer.parseInt(startTimeStr.substring(0, 1));
					int mm = Integer.parseInt(startTimeStr.substring(2));
					if (startTimeStr.charAt(1) != ':' || hh < 0 || hh > 23 || mm < 0 || mm > 59) {
						startTime.setBackground(Color.RED);
					} else {
						++i;
						startTime.setBackground(Color.WHITE);
					}
		    	}
		    	else if(startTimeStr.length() == 5) {
					int hh = Integer.parseInt(startTimeStr.substring(0, 2));
					int mm = Integer.parseInt(startTimeStr.substring(3));
					if (startTimeStr.charAt(2) != ':' || hh < 0 || hh > 23 || mm < 0 || mm > 59) {
						startTime.setBackground(Color.RED);
					} else {
						++i;
						startTime.setBackground(Color.WHITE);
					}
		    		
		    	}
		    	else {
					startTime.setBackground(Color.RED);	
		    	}
		    } catch (NumberFormatException nfe) {
		        startTime.setBackground(Color.RED);
		    } catch(StringIndexOutOfBoundsException sie) {
		        startTime.setBackground(Color.RED);    	
		    }
	    
	    try {
			if (endTimeStr.length() == 4) {
				int hh = Integer.parseInt(endTimeStr.substring(0, 1));
				int mm = Integer.parseInt(endTimeStr.substring(2));
				if (endTimeStr.charAt(1) != ':' || hh < 0 || hh > 23 || mm < 0 || mm > 59) {
					endTime.setBackground(Color.RED);
				} else {
					++i;
					endTime.setBackground(Color.WHITE);
				}
	    	}
	    	else if(endTimeStr.length() == 5) {
				int hh = Integer.parseInt(endTimeStr.substring(0, 2));
				int mm = Integer.parseInt(endTimeStr.substring(3));
				if (endTimeStr.charAt(2) != ':' || hh < 0 || hh > 23 || mm < 0 || mm > 59) {
					endTime.setBackground(Color.RED);
				} else {
					++i;
					endTime.setBackground(Color.WHITE);
				}
	    		
	    	}
	    	else {
				endTime.setBackground(Color.RED);	
	    	}
	    } catch (NumberFormatException nfe) {
	        endTime.setBackground(Color.RED);
	    } catch(StringIndexOutOfBoundsException sie) {
	        endTime.setBackground(Color.RED);    	
	    }
	    
	    try {
			int sectionNumberInput = Integer.parseInt(sectionNumber.getText());
			if(sectionNumberInput <= 0) {
				sectionNumber.setBackground(Color.RED);
			} else {
				++i;
				sectionNumber.setBackground(Color.WHITE);
			}
	    } catch (NumberFormatException nfe) {
	        sectionNumber.setBackground(Color.RED);
	    } 
	    if(courseNumber.getText().length()>0) {
	    	++i;
	    }
	    
	    //Make sure one check box is checked
	    String day = "";
	    Boolean daySelected = false;
	    if(mon.isSelected()) {
	    	day+="Mon";
	    	daySelected = true;
	    }
	    if(tues.isSelected()) {
	    	day+="Tue";
	    	daySelected = true;
	    }
	    if(wed.isSelected()) {
	    	day+="Wed";
	    	daySelected = true;
	    }
	    if(thurs.isSelected()) {
	    	day+="Thu";
	    	daySelected = true;
	    }
	    if(fri.isSelected()) {
	    	day+="Fri";
	    	daySelected = true;
	    }
	    if(sat.isSelected()) {
	    	day+="Sat";
	    	daySelected = true;
	    }
	    if(sun.isSelected()) {
	    	day+="Sun";
	    	daySelected = true;
	    }
	   
	    //If i is 6, all text boxes are entered correctly
		if(i == 6 && daySelected) {
			//Build the query to be sent over to the Server
				String que = "INSERT INTO calendar (unique_id, courseNumber, sectionNumber, startDate, endDate, startTime, endTime, daysOfTheWeek) values (cal_seq.nextval, '" + courseNumber.getText() + "', '"
						+ sectionNumber.getText() +"',   TO_DATE('"+ startDate.getText() + "', 'MM/dd/yyyy'), TO_DATE('"+ endDate.getText() + "', 'MM/dd/yyyy'), TO_DATE('" + startTime.getText()
						 + "', 'HH24:MI'), TO_DATE('" + endTime.getText() + "', 'HH24:MI'), '" + day + "' )";
				String response = admin.getQuery(que);

			       startDate.setText("");
			       endDate.setText("");
			       startTime.setText("");
			       endTime.setText("");
			       courseNumber.setText("");
			       sectionNumber.setText("");
			       mon.setSelected(false);
			       tues.setSelected(false);
			       wed.setSelected(false);
			       thurs.setSelected(false);
			       fri.setSelected(false);
			       sat.setSelected(false);
			       sun.setSelected(false);
				//If failure to connect to server
				if(response.equals("Failure")) {
					messageLogger.setText("Unsuccessful Connection to Server");
					disconnect();
				}
				else {
					messageLogger.setText("Successful Submission to Calendar");
				}
			
		}

		else {
			messageLogger.setText("Bad input in textboxes demarked red and/or no checked days of the week");
		}

	}
}
