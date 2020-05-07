/*
 * ClientHandler - Java implementation of ClientHandler class under Server class for multi-threaded chat server
 *
 * from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
 */
package multithreadchatserver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import DAO.DataAccessObject;
import Gui.Request;

import java.net.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

//ClientHandler class
class ClientHandler implements Runnable {
    String name; // client name
    final DataInputStream dis; // input stream for this client
    final DataOutputStream dos; // output stream for this client
    Socket s; // socket for this client
    boolean isloggedin; // flag, whether client is currently connected
    DataAccessObject dao = new DataAccessObject();
    String endDate = ""; // help request end
    String startDate = ""; // help request start
    String courseName = ""; // course name

    // constructor
    public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos, String courseName) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin = true;
        this.courseName = courseName;
        

    } // end - constructor

    // run method - called when thread starts
    @Override
    public void run() {
        while (this.isloggedin) {
            // try {
            // receive the string
            String hh = "";
            try {
                hh = dis.readUTF();
                LocalDateTime current = LocalDateTime.now();
                int position = 1;
                for (int x = 0; x < Server.requestList.size(); x++) {
                    if (Server.requestList.get(x).getPosition() >= position) {
                        position = Server.requestList.get(x).getPosition() + 1;
                    }
                }
                // if the client help request is sent
                if (hh.equals("Sent")) {
                    int pos = -1;
                    for (int x = 0; x < Server.requestList.size(); ++x) {
                        if (pos == -1) {
                            if (Server.requestList.get(x).getName().equals(this.name)) {
                                pos = x;
                            }
                        }

                    }
                    if (pos == -1) {
                    	System.out.println("check");
                        Server.requestList.add(new Request(this.name, current, position, 0));
                        for (DisplayHandler mc : Server.disAr) {
                            // if the recipient is found, write on its output stream
                            mc.update(courseName);
                        }
                        dos.writeUTF("Help Request is submitted");
                    }
                    else {
                        dos.writeUTF("Help Request has already been submitted. User may only submit One help request.");
                    }
                    dao.connect(); // establish db connection
                    dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false

                    // get the date/time and format it.
                    String dateformat = "YYYY-MM-dd hh:mm:ss";
                    SimpleDateFormat dtf1 = new SimpleDateFormat(dateformat);
                    startDate = dtf1.format(new java.util.Date());

                    if(courseName.length() > 15) {
                    String courseN = courseName.substring(courseName.indexOf("Course Number: ")+ 15, courseName.indexOf("Section Number:")-2); // get correct course number
                    String sectionN = courseName.substring(courseName.indexOf("Section Number:") + 16); // get correct section number
                    // perform and SQL non query adding the help request into the database.
                    dao.executeSQLNonQuery("INSERT INTO helpRequest (unique_id, event, workStation, originator, course_number, section, request_time) VALUES(help_seq.nextval, 'Help', '"+ this.name + "', 'Client', '" + courseN + "', '" + sectionN + "', to_date('" + startDate + "', 'YYYY/MM/DD HH24:MI:SS'))") ;
                    }
                    else {
                        // perform and SQL non query adding the help request into the database.
                    	dao.executeSQLNonQuery("INSERT INTO helpRequest (unique_id, event, workStation, originator, request_time) VALUES(help_seq.nextval, 'Help', '"+ this.name + "', 'Client', to_date('" + startDate + "', 'YYYY/MM/DD HH24:MI:SS'))") ;
                        
                    }
                    dao.commit(); // commit the transaction.
                    dao.disconnect(); // disconnects (have to do this for every method?)

                }
                // if the client cancels the help request
                else if (hh.equals("Cancel")) {
                    int pos = -1;
                    int waitTime = 0;
                    for (int x = 0; x < Server.requestList.size(); ++x) {
                        if (pos == -1) {
                            if (Server.requestList.get(x).getName().equals(this.name)) {
                                pos = x;
                                waitTime = Server.requestList.get(x).getWaitTime(); // getting the wait time

                            }
                        } else {
                            Server.requestList.get(x).lowerQueue(); // adjusting the queue
                        }

                    }
                    if (pos > -1) {
                        Server.requestList.remove(pos); // removing help request
                    }
                    for (DisplayHandler mc : Server.disAr) {
                        // if the recipient is found, write on its output stream
                        mc.update(courseName);
                    }
                    dos.writeUTF("Help Request is cancelled");
                    dao.connect(); // establish db connection
                    dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false

                    // get the date/time and format it.
                    String dateformat = "YYYY-MM-dd hh:mm:ss";
                    SimpleDateFormat dtf1 = new SimpleDateFormat(dateformat);
                    endDate = dtf1.format(new java.util.Date());

                    if(courseName.length() > 15) {
                        String courseN = courseName.substring(courseName.indexOf("Course Number: ")+ 15, courseName.indexOf("Section Number:")); // getting correct course number
                        String sectionN = courseName.substring(courseName.indexOf("Section Number:") + 16); // getting correct section number
                        // perform and SQL non query adding the help request into the database.
                    dao.executeSQLNonQuery("INSERT INTO helpRequest (unique_id, event, workStation, originator, course_number, section, request_time, cancel_time, wait_time) VALUES(help_seq.nextval, 'Cancel', '"+ this.name + "', 'Client', '"+ courseN + "', '" + sectionN + ", to_date('" + startDate + "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate + "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + waitTime + "' SECOND)") ;
                    }
                    else {
                        // perform and SQL non query adding the help request into the database.
                        dao.executeSQLNonQuery("INSERT INTO helpRequest (unique_id, event, workStation, originator, request_time, cancel_time, wait_time) VALUES(help_seq.nextval, 'Cancel', '"+ this.name + "', 'student', to_date('" + startDate + "', 'YYYY/MM/DD HH24:MI:SS'), to_date('" + endDate + "', 'YYYY/MM/DD HH24:MI:SS'), INTERVAL '" + waitTime + "' SECOND)") ;
                    }
                    dao.commit(); // commit the transaction.
                    dao.disconnect(); // disconnects (have to do this for every method?)
                }

                // if the admin cancels the help request
                else if (hh.equals("Update")) {
                    int pos = -1;
                    for (int x = 0; x < Server.requestList.size(); ++x) {
                        if (Server.requestList.get(x).getName().equals(this.name)) {
                            pos = 1;
                        }
                    }
                    if(pos==-1) {
                        dos.writeUTF("Cancel");
                    } else {
                        dos.writeUTF("No changes");
                    }
                } else {
                    dos.writeUTF("Bad Input from Client");
                }



            } catch (IOException e) {
                // TODO Auto-generated catch block
                this.isloggedin = false;
            }
        } // end - while true

        // closing resources
        try {
            int pos = -1;
            for (int x = 0; x < Server.requestList.size(); ++x) {
                if (pos == -1) {
                    if (Server.requestList.get(x).getName().equals(this.name)) {
                        pos = x;
                    }
                } else {
                    Server.requestList.get(x).lowerQueue();
                }

            }
            if (pos > -1) {
                Server.requestList.remove(pos);
            }
            for (DisplayHandler mc : Server.disAr) {
                // if the recipient is found, write on its output stream
                mc.update(courseName);
            }
            this.dis.close();
            this.dos.close();
            ClientHandler rem = null;
            for (ClientHandler mc : Server.ar) {
                // if the recipient is found, write on its output stream
                if(this.name.equals(mc.name)) {
                    rem = mc;
                }
            }
            Server.ar.remove(rem);
        } catch (IOException e) {
            System.out.println("ClientHandler, closing resources section");
            e.printStackTrace();
        }
    } // end - method run

// getting course name
	public void updateCourse(String courseName) {
		this.courseName = courseName;
	}

} // end - class ClientHandler
