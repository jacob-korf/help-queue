package multithreadchatserver;

import DAO.DataAccessObject;
import Gui.Request;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
			String hh = "";

			try {
				hh = this.dis.readUTF();

				if (hh.equals("Reset")) {

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
											+ Server.requestList.get(x).getName() + "', 'Admin', '" + courseN + "', '" + sectionN + "', to_date('"
											+ startDate + "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate
											+ "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + Server.requestList.get(x).getWaitTime() + "' SECOND)");

						} else {
							dao.executeSQLNonQuery(
									"INSERT INTO helpRequest (unique_id, event, workStation, originator, request_time, cancel_time, wait_time) VALUES(help_seq.nextval, 'Cancel', '"
											+ Server.requestList.get(x).getName() + "', 'Admin', to_date('" + startDate
											+ "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate
											+ "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + Server.requestList.get(x).getWaitTime()  + "' SECOND)");
						}

						dao.commit();
						dao.disconnect(); // disconnects (have to do this for every method?)
					}
					Server.requestList.clear();
					this.dos.writeUTF("Help Request is cancelled");
				} else if (hh.contains("Cancel#")) {
					String wk = hh.substring(7);

					int pos = -1;
					int waitTime = 0;
					for (int x = 0; x < Server.requestList.size(); ++x) {
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
					if (pos > -1) {
						Server.requestList.remove(pos);
						dao.connect(); // establish db connection
						dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false

						String dateformat = "YYYY-MM-dd hh:mm:ss";
						SimpleDateFormat dtf1 = new SimpleDateFormat(dateformat);
						String endDate = dtf1.format(new java.util.Date());

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss");
						String startDate = startTime.format(formatter);

						if (courseName.length() > 15) {
							String courseN = courseName.substring(courseName.indexOf("Course Number: ") + 15,
									courseName.indexOf("Section Number:"));
							String sectionN = courseName.substring(courseName.indexOf("Section Number:") + 16);
							dao.executeSQLNonQuery(
									"INSERT INTO helpRequest (unique_id, event, workStation, originator, course_number, section, request_time, cancel_time, wait_time) VALUES(help_seq.nextval, 'Cancel', '"
											+ wk + "', 'Admin', '" + courseN + "', '" + sectionN + "', to_date('"
											+ startDate + "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate
											+ "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + waitTime + "' SECOND)");

						} else {
							dao.executeSQLNonQuery(
									"INSERT INTO helpRequest (unique_id, event, workStation, originator, request_time, cancel_time, wait_time) VALUES(help_seq.nextval, 'Cancel', '"
											+ wk + "', 'Admin', to_date('" + startDate
											+ "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate
											+ "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + waitTime + "' SECOND)");
						}

						dao.commit();
						dao.disconnect(); // disconnects (have to do this for every method?)

					}
					for (DisplayHandler mc : Server.disAr) {
						// if the recipient is found, write on its output stream
						mc.update(courseName);
					}
					dos.writeUTF(wk + " Help Request is cancelled");
				} else if (hh.contains("query")) {
					String getQueue = hh.substring(5);
					DataAccessObject dao = new DataAccessObject();
					dao.connect(); // establish db connection
					dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false
					dao.executeSQLNonQuery(getQueue);
					dao.commit(); // commit the transaction.
					dao.disconnect(); // disconnects (have to do this for every method?)
					this.dos.writeUTF("Calendar successfully committed");
				} else {
					this.dos.writeUTF("Bad Input from Admin");
				}
			} catch (IOException var8) {
				this.isloggedin = false;
			}
		}

		try {
			int pos = -1;

			for (int x = 0; x < Server.requestList.size(); ++x) {
				if (pos == -1) {
					if (((Request) Server.requestList.get(x)).getName().equals(this.name)) {
						pos = x;
					}
				} else {
					((Request) Server.requestList.get(x)).lowerQueue();
				}
			}

			if (pos > -1) {
				Server.requestList.remove(pos);
			}

			Iterator var13 = Server.disAr.iterator();

			while (var13.hasNext()) {
				DisplayHandler mc = (DisplayHandler) var13.next();
				mc.update(courseName);
			}

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

	public void updateCourse(String courseName) {
		// TODO Auto-generated method stub
		this.courseName = courseName;
	}
}