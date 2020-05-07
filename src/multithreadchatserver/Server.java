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
	private int sectionNumber;
	private static String courseName = "";

	public Server() {
	}

	public static Boolean getCourse() {
		// time of the day
		LocalDateTime startTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh");
		String time = startTime.format(formatter);
		// day of the week
		Date day = new Date();
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week spelled out completely
		String thisDay = simpleDateformat.format(day);
		System.out.println(startTime.getHour());
		String dateformat = "YYYY-MM-dd hh:mm:ss";
		SimpleDateFormat dtf1 = new SimpleDateFormat(dateformat);
		String date = dtf1.format(new java.util.Date());
		int startInt = startTime.getHour() % 12;
		if(startInt ==0) {
			startInt+=12;
		}
		DataAccessObject dao = new DataAccessObject();
		dao.connect(); // establish db connection
		dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false
		String search = ("SELECT courseNumber, sectionNumber FROM calendar WHERE to_char(startTime, 'HH') <= '"
				+ startInt + "' AND to_char(endTime, 'HH') >= '" + startTime.getHour() % 12
				+ "' and daysOfTheWeek LIKE '%" + thisDay + "%'");
		dao.executeSQLQuery(search); // finds something in the db that we want
		ArrayList<String> result = dao.processResultSetArray(); // gets the result of the search
		
		System.out.println(result);
		System.out.println("check");
		dao.commit(); // commit the transaction.
		dao.disconnect(); // disconnects (have to do this for every method?)
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
		ServerSocket ss = new ServerSocket(3014);
		ss.setSoTimeout(100);
		ServerSocket displaySS = new ServerSocket(3015);
		displaySS.setSoTimeout(100);
		ServerSocket Adminss = new ServerSocket(3016);
		Adminss.setSoTimeout(100);
		getCourse();
		Thread updateTime = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(3000L);
					} catch (InterruptedException var5) {
						var5.printStackTrace();
					}

					LocalDateTime current = LocalDateTime.now();

					for (int x = 0; x < Server.requestList.size(); ++x) {
						((Request) Server.requestList.get(x)).updateTime(current);
					}

					DisplayHandler rem = null;
					Iterator var4 = Server.disAr.iterator();

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
		updateTime.start();
		Thread updateCourse = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000L);
					} catch (InterruptedException var5) {
						var5.printStackTrace();
					}

					if (getCourse()) {
						DataAccessObject dao = new DataAccessObject();
						String dateformat = "YYYY-MM-dd hh:mm:ss";
						SimpleDateFormat dtf1 = new SimpleDateFormat(dateformat);
						String endDate = dtf1.format(new java.util.Date());

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss");
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
 
							} else {
								dao.executeSQLNonQuery(
										"INSERT INTO helpRequest (unique_id, event, workStation, originator, request_time, cancel_time, wait_time) VALUES(help_seq.nextval,'Cancel', '"
												+ Server.requestList.get(x).getName() + "', 'System', to_date('" + startDate
												+ "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate
												+ "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + Server.requestList.get(x).getWaitTime()  + "' SECOND)");
							}

							dao.commit();
							dao.disconnect(); // disconnects (have to do this for every method?)
						}
						Server.requestList.clear();
						DisplayHandler rem = null;
						Iterator var4 = Server.disAr.iterator();

						while (var4.hasNext()) {
							DisplayHandler mc = (DisplayHandler) var4.next();
							if (!mc.update(courseName)) {
								rem = mc;
							}
						}
						Server.disAr.remove(rem);

						Iterator varCl = Server.disAr.iterator();
						while (varCl.hasNext()) {
							ClientHandler mc = (ClientHandler) varCl.next();
							mc.updateCourse(courseName);
						}

						Iterator varAd = Server.adAr.iterator();

						while (varAd.hasNext()) {
							AdminHandler mc = (AdminHandler) varAd.next();
							mc.updateCourse(courseName);
						}

					}

				}
			}
		});
		updateCourse.start();

		while (true) {
			Socket s;
			try {
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				String clientName = "Lab-P115-";
				if (i < 10) {
					clientName = clientName + "0";
				}

				clientName = clientName + (i + 1);
				ClientHandler mtch = new ClientHandler(s, clientName, dis, dos, courseName);
				Thread t = new Thread(mtch);
				ar.add(mtch);
				t.start();
				++i;
			} catch (InterruptedIOException var12) {
				try {
					s = displaySS.accept();
					DataInputStream dis = new DataInputStream(s.getInputStream());
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					String displayName = "Display " + j;
					DisplayHandler mtch = new DisplayHandler(s, displayName, dis, dos);
					Thread t = new Thread(mtch);
					disAr.add(mtch);
					t.start();
					++j;
				} catch (InterruptedIOException var11) {
					try {
						s = Adminss.accept();
						DataInputStream dis = new DataInputStream(s.getInputStream());
						DataOutputStream dos = new DataOutputStream(s.getOutputStream());
						String AdminName = "Lab-P115-Admin";
						if (k < 10) {
							AdminName = AdminName + "0";
						}

						AdminName = AdminName + (k + 1);
						AdminHandler mtch = new AdminHandler(s, AdminName, dis, dos, courseName);
						Thread t = new Thread(mtch);
						adAr.add(mtch);
						t.start();
						++k;
					} catch (InterruptedIOException var13) {
					}
				}
			}

		}
	}
}
