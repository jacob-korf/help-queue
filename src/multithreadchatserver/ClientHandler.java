/*
 * ClientHandler - Java implementation of ClientHandler class under Server class for multi-threaded chat server 
 *
 * from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
 */
package multithreadchatserver;

import java.io.*;
import java.util.*;

import Gui.Request;

import java.net.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

//ClientHandler class 
class ClientHandler implements Runnable {
	// data
	// Scanner scn = new Scanner(System.in); // ???
	// private String name; // client name
	String name; // client name
	final DataInputStream dis; // input stream for this client
	final DataOutputStream dos; // output stream for this client
	Socket s; // socket for this client
	boolean isloggedin; // flag, whether client is currently connected

	// constructor
	public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.name = name;
		this.s = s;
		this.isloggedin = true;
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
						Server.requestList.add(new Request(this.name, current, position, 0));

						for (DisplayHandler mc : Server.disAr) {
							// if the recipient is found, write on its output stream
							mc.update();
						}

						dos.writeUTF("Help Request is submitted");

					} else {
						dos.writeUTF("Help Request has already been submitted. User may only submit One help request.");

					}

				} else if (hh.equals("Cancel")) {
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
						mc.update();
					}
					dos.writeUTF("Help Request is cancelled");
				} 
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

			/*
			 * } catch (IOException e) { this.isloggedin = false; }
			 */
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
				mc.update();
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

	public void cancel() {

	}

} // end - class ClientHandler
