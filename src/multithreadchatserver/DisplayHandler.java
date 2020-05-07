/*
 * ClientHandler - Java implementation of ClientHandler class under Server class for multi-threaded chat server 
 *
 * from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
 */
package multithreadchatserver;

import java.io.*;
import java.util.*;
import java.net.*;

//DisplayHandler class
class DisplayHandler implements Runnable {
	String name; // client name
	final DataInputStream dis; // input stream for this client
	final DataOutputStream dos; // output stream for this client
	Socket s; // socket for this client

	// constructor
	public DisplayHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.name = name;
		this.s = s;
	} // end - constructor


	public void run() {

	}

	public Boolean update(String courseName) {
		// updating the data in the display
		String queueText = "";
		for (int x = 0; x < Server.requestList.size(); ++x) {
			if (x < 10) {
				queueText += Server.requestList.get(x).toString() + "\n";
			}
		}
		try {
			// writes course name and queue text written out in a string
			dos.writeUTF(courseName + "###" + queueText);
		} catch (IOException e) {
			try {
				this.dis.close();
				this.dos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}


} // end - class DisplayHandler
