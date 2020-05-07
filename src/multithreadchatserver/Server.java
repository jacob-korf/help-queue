//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package multithreadchatserver;

import DAO.DataAccessObject;
import Gui.Request;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Server {
	static Vector<ClientHandler> ar = new Vector<ClientHandler>();
	static Vector<DisplayHandler> disAr = new Vector<DisplayHandler>();
	static Vector<AdminHandler> adAr = new Vector<AdminHandler>();
	protected static ArrayList<Request> requestList = new ArrayList<Request>();
	static int i = 0;
	static int j = 0;
	static int k = 0;
	private static String courseName = "";

	public Server() {
	}

	public static Boolean getCourse() {
		// time of the day
		LocalDateTime startTime = LocalDateTime.now();
		// day of the week
		Date day = new Date();
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week spelled out completely
		String thisDay = simpleDateformat.format(day);
		
		//Set up start time for searching the list
		int startInt = startTime.getHour() % 12;
		if(startInt ==0) {
			startInt+=12;
		}
		//Search the database for first calendar event within the time and dates
		DataAccessObject dao = new DataAccessObject();
		dao.connect(); // establish db connection
		dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false
		String search = ("SELECT courseNumber, sectionNumber FROM calendar WHERE to_char(startTime, 'HH') <= '"
				+ startInt + "' AND to_char(endTime, 'HH') >= '" + startTime.getHour() % 12
				+ "' and daysOfTheWeek LIKE '%" + thisDay + "%'");
		dao.executeSQLQuery(search); // finds something in the db that we want
		ArrayList<String> result = dao.processResultSetArray(); // gets the result of the search
		
		//System.out.println(result);
		//System.out.println("check");
		dao.commit(); // commit the transaction.
		dao.disconnect(); // disconnects (have to do this for every method?)
		
		//if it finds a result, change the value of courseName, else return no change
		if (result.size() > 0) {
			String newCourseName = "Course Number: " + result.get(0) + " - Section Number: " + result.get(1);
			if (courseName.equals(newCourseName)) {
				return false;
			} else {
				courseName = newCourseName;
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Server started Suceessfully");
		//Create ServerSockets for Clients, Displays, and Admins
		ServerSocket ss = new ServerSocket(3014);
		ss.setSoTimeout(100);
		ServerSocket displaySS = new ServerSocket(3015);
		displaySS.setSoTimeout(100);
		ServerSocket adminSS = new ServerSocket(3016);
		adminSS.setSoTimeout(100);
		getCourse(); //Get first course on initializiation of server
		Thread updateTime = new Thread(new Runnable() { //Update wait times in all request lists then update displays
			public void run() {
				while (true) {
					try { //Only run this method every 3 seconds
						Thread.sleep(3000L);
					} catch (InterruptedException var5) {
						var5.printStackTrace();
					}

					LocalDateTime current = LocalDateTime.now();//Get current time

					for (int x = 0; x < Server.requestList.size(); ++x) {
						((Request) Server.requestList.get(x)).updateTime(current); //Update each request's time
					}

					DisplayHandler rem = null;
					Iterator var4 = Server.disAr.iterator();
					//Update each display panel; if the display is no longer there, remove the display from the display array
					while (var4.hasNext()) {
						DisplayHandler mc = (DisplayHandler) var4.next();
						if (!mc.update(courseName)) {
							rem = mc;
						}
					}

					Server.disAr.remove(rem);
				}

			}
		});
		updateTime.start(); //Start the thread to update display times
		Thread updateCourse = new Thread(new Runnable() { //Look to see if we changed courses every 5 seconds
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000L); //Only call this method every 5 seconds
					} catch (InterruptedException var5) {
						var5.printStackTrace();
					}

					if (getCourse()) { //If there is a change in course name, reset the request list and log it in the database
						//Formatting data for database submission
						DataAccessObject dao = new DataAccessObject();
						String dateformat = "YYYY-MM-dd hh:mm:ss";
						SimpleDateFormat dtf1 = new SimpleDateFormat(dateformat);
						String endDate = dtf1.format(new java.util.Date());

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss");
						//Log database for each request in requestList
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
												+ Server.requestList.get(x).getName() + "', 'System', '" + courseN + "', '" + sectionN + "', to_date('"
												+ startDate + "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate
												+ "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + Server.requestList.get(x).getWaitTime() + "' SECOND)");
 
							} else { //In the case of no current course
								dao.executeSQLNonQuery(
										"INSERT INTO helpRequest (unique_id, event, workStation, originator, request_time, cancel_time, wait_time) VALUES(help_seq.nextval,'Cancel', '"
												+ Server.requestList.get(x).getName() + "', 'System', to_date('" + startDate
												+ "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate
												+ "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + Server.requestList.get(x).getWaitTime()  + "' SECOND)");
							}

							dao.commit();
							dao.disconnect(); // disconnects (have to do this for every method?)
						}
						Server.requestList.clear(); //clear the request list
						
						//Update all displays with new course name, if any are disconnected remove them from display array
						DisplayHandler rem = null;
						Iterator var4 = Server.disAr.iterator();

						while (var4.hasNext()) {
							DisplayHandler mc = (DisplayHandler) var4.next();
							if (!mc.update(courseName)) {
								rem = mc;
							}
						}
						Server.disAr.remove(rem);
						//Update the client handlers with the new course name
						Iterator varCl = Server.disAr.iterator();
						while (varCl.hasNext()) {
							ClientHandler mc = (ClientHandler) varCl.next();
							mc.updateCourse(courseName);
						}
						//Update the admin handlers with the new course name
						Iterator varAd = Server.adAr.iterator();

						while (varAd.hasNext()) {
							AdminHandler mc = (AdminHandler) varAd.next();
							mc.updateCourse(courseName);
						}

					}

				}
			}
		});
		updateCourse.start(); //Start running the thread for updating the course name

		while (true) {
			Socket s;
			try {
				s = ss.accept(); //Look to accept new client
				//Set up inital information
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				String clientName = "Lab-P115-";
				if (i < 10) {
					clientName = clientName + "0";
				}

				clientName = clientName + (i + 1);
				ClientHandler mtch = new ClientHandler(s, clientName, dis, dos, courseName);
				Thread t = new Thread(mtch); //Create a thread to run the clientHandler in
				ar.add(mtch); //add to the ClientHandler array
				t.start(); //Start the clientHandler
				++i;
			} catch (InterruptedIOException var12) { //When the timeout runs out to check for displays instead of clients
				try {
					s = displaySS.accept(); //Accept any new displays
					//Set up initial information
					DataInputStream dis = new DataInputStream(s.getInputStream());
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					String displayName = "Display " + j;
					DisplayHandler mtch = new DisplayHandler(s, displayName, dis, dos);
					Thread t = new Thread(mtch); //Create a thread to run the display handler in
					disAr.add(mtch);// add to the display handler array
					t.start(); //start the display handler
					++j;
				} catch (InterruptedIOException var11) { //When the timeout runs out to check for admins instead of displays
					try {
						s = adminSS.accept(); //Accept any new admins
						//Set up initial information
						DataInputStream dis = new DataInputStream(s.getInputStream());
						DataOutputStream dos = new DataOutputStream(s.getOutputStream());
						String AdminName = "Lab-P115-Admin";
						if (k < 10) {
							AdminName = AdminName + "0";
						}

						AdminName = AdminName + (k + 1);
						AdminHandler mtch = new AdminHandler(s, AdminName, dis, dos, courseName);
						Thread t = new Thread(mtch);//Create a thread to run the admin handler in
						adAr.add(mtch); // add to the admin handler array
						t.start(); //start the admin handler
						++k;
					} catch (InterruptedIOException var13) { //Return back to looking for clients
					}
				}
			}

		}
	}
}
