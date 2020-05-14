package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.time.format.DateTimeFormatter;

public class AdminHandler implements Runnable {
	String name;
	final DataInputStream dis;
	final DataOutputStream dos;
	Socket s;
	boolean isloggedin;
	DataAccessObject dao = new DataAccessObject();
	LocalDateTime startTime;
	String courseName = "";

	public AdminHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos, String courseName) {
		this.dis = dis;
		this.dos = dos;
		this.name = name;
		this.s = s;
		this.isloggedin = true;
		this.courseName = courseName;
	}


	public void run() {
		while (this.isloggedin) {
			String input = "";

			try {
				//Read input from the Admin
				input = this.dis.readUTF();
				
				//Case of resetting the request list
				if (input.equals("Reset")) {

					String dateformat = "YYYY-MM-dd HH:mm:ss";
					SimpleDateFormat dtf1 = new SimpleDateFormat(dateformat);
					String endDate = dtf1.format(new java.util.Date());

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
					//Log cancel request for every request in the current list
					for (int x = 0; x < Server.requestList.size(); ++x) {
						dao.connect(); // establish db connection
						dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false

						String startDate = Server.requestList.get(x).getRequestTime().format(formatter);
						if (courseName.length() > 15) {
							String courseN = courseName.substring(courseName.indexOf("Course Number: ") + 15,
									courseName.indexOf("Section Number:"));
							String sectionN = courseName.substring(courseName.indexOf("Section Number:") + 16);
							dao.executeSQLNonQuery(
									"INSERT INTO helpRequest (unique_id, event, workStation, originator, course_number, section, request_time, cancel_time, wait_time) VALUES(help_seq.nextval, 'Cancel', '"
											+ Server.requestList.get(x).getName() + "', 'Admin', '" + courseN + "', '" + sectionN + "', to_date('"
											+ startDate + "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate
											+ "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + Server.requestList.get(x).getWaitTime() + "' SECOND)");

						} else { //In the case of no course or section
							dao.executeSQLNonQuery(
									"INSERT INTO helpRequest (unique_id, event, workStation, originator, request_time, cancel_time, wait_time) VALUES(help_seq.nextval, 'Cancel', '"
											+ Server.requestList.get(x).getName() + "', 'Admin', to_date('" + startDate
											+ "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate
											+ "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + Server.requestList.get(x).getWaitTime()  + "' SECOND)");
						}

						dao.commit();
						dao.disconnect(); // disconnects (have to do this for every method?)
					}
					//Clear the request list
					Server.requestList.clear();
					//Submit success message back to Admin
					this.dos.writeUTF("Help Request is cancelled");
				} else if (input.contains("Cancel#")) { //Case of cancelling specific workstation
					String wk = input.substring(7); //Cut out "Cancel#" from input

					int pos = -1;
					int waitTime = 0;
					for (int x = 0; x < Server.requestList.size(); ++x) { //Find the correct workstation request if it exists
						if (pos == -1) {
							if (Server.requestList.get(x).getName().equals(wk)) {
								pos = x;
								startTime = Server.requestList.get(x).getRequestTime();
								waitTime = Server.requestList.get(x).getWaitTime();

							}
						} else {
							Server.requestList.get(x).lowerQueue();
						}

					}
					if (pos > -1) { //If request exists, remove it from the request List and log it in the database
						Server.requestList.remove(pos);
						dao.connect(); // establish db connection
						dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false

						//Make the data to be correct formatting for the database
						String dateformat = "YYYY-MM-dd input:mm:ss";
						SimpleDateFormat dtf1 = new SimpleDateFormat(dateformat);
						String endDate = dtf1.format(new java.util.Date());

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd input:mm:ss");
						String startDate = startTime.format(formatter);

						//Connect to database and log data
						if (courseName.length() > 15) {
							String courseN = courseName.substring(courseName.indexOf("Course Number: ") + 15,
									courseName.indexOf("Section Number:"));
							String sectionN = courseName.substring(courseName.indexOf("Section Number:") + 16);
							dao.executeSQLNonQuery(
									"INSERT INTO helpRequest (unique_id, event, workStation, originator, course_number, section, request_time, cancel_time, wait_time) VALUES(help_seq.nextval, 'Cancel', '"
											+ wk + "', 'Admin', '" + courseN + "', '" + sectionN + "', to_date('"
											+ startDate + "', 'YYYY/MM/DD input24:MI:SS'), to_date('" + endDate
											+ "', 'YYYY/MM/DD input24:MI:SS'), INTERVAL '" + waitTime + "' SECOND)");

						} else {//In the case there is no course currently running
							dao.executeSQLNonQuery(
									"INSERT INTO helpRequest (unique_id, event, workStation, originator, request_time, cancel_time, wait_time) VALUES(help_seq.nextval, 'Cancel', '"
											+ wk + "', 'Admin', to_date('" + startDate
											+ "', 'YYYY/MM/DD input24:MI:SS'), to_date('" + endDate
											+ "', 'YYYY/MM/DD input24:MI:SS'), INTERVAL '" + waitTime + "' SECOND)");
						}

						dao.commit();
						dao.disconnect(); // disconnects (have to do this for every method?)

					}
					for (DisplayHandler mc : Server.disAr)   {//Update all display applications to show the correct list of requests
						mc.update(courseName);
					}
					//Send success message back to Admin
					dos.writeUTF(wk + " Help Request is cancelled");
				} else if (input.contains("query")) { //In the case of a calendar query
					String getQueue = input.substring(5); //Cut out "query" from the message written over to keep just the actual query
					DataAccessObject dao = new DataAccessObject();
					//Add new Calendar event
					dao.connect(); // establish db connection
					dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false
					dao.executeSQLNonQuery(getQueue);
					dao.commit(); // commit the transaction.
					dao.disconnect(); // disconnects (have to do this for every method?)
					//Send message back to admin of success
					this.dos.writeUTF("Calendar successfully committed");
				} else if (input.contains("credentials")){
					String userPass = input.substring(11) ;
					String[] arrOfStr = userPass.split("/");
					String username = arrOfStr[0];
					String password = arrOfStr[1];

					dao.connect(); // establish db connection
					dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false
					String cradentials = "SELECT username, password1 FROM login where username = '"+ username +"' AND password1 = '" + password +"'";
					dao.executeSQLQuery(cradentials); // finds something in the db that we want
					ArrayList<String> result = dao.processResultSetArray(); // gets the result of the search
					System.out.println(result);
					dao.commit(); // commit the transaction.
					dao.disconnect(); // disconnects (have to do this for every method?)

					if (result.size() > 0) {
						this.dos.writeUTF("Success");
						//String username =  result.get(0);
						//String password =  result.get(1);
						//System.out.println(username);
						//System.out.println(password);
					}else {
						this.dos.writeUTF("WrongPass");
						//System.out.println("wrong username and password");
					}
				}


				else { //In the case it does fit in any category, send back error message
					this.dos.writeUTF("Bad Input from Admin");
				}
			} catch (IOException var8) {
				this.isloggedin = false;
			}
		}
		//Shut down AdminHandler and remove it from the server
		try {
			this.dis.close();
			this.dos.close();
			AdminHandler rem = null;
			Iterator var16 = Server.ar.iterator();

			while (var16.hasNext()) {
				AdminHandler mc = (AdminHandler) var16.next();
				if (this.name.equals(mc.name)) {
					rem = mc;
				}
			}

			Server.adAr.remove(rem);
		} catch (IOException var7) {
			System.out.println("AdminHandler, closing resources section");
			var7.printStackTrace();
		}

	}
	//Update current course running for latter submissions to the database
	public void updateCourse(String courseName) {
		// TODO Auto-generated method stub
		this.courseName = courseName;
	}

	public boolean correctUsPass(boolean flag){
		return flag;
	}
}