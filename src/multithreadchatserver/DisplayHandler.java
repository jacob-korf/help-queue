/*
 * ClientHandler - Java implementation of ClientHandler class under Server class for multi-threaded chat server 
 *
 * from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
 */
package multithreadchatserver;

import java.io.*;
import java.util.*;
import java.net.*;

//ClientHandler class 
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
		String queueText = "";
		for (int x = 0; x < Server.requestList.size(); ++x) {
			if (x < 10) {
				queueText += Server.requestList.get(x).toString() + "\n";
			}
		}
		System.out.println(queueText);
		try {
			dos.writeUTF(courseName + "###" + queueText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				this.dis.close();
				this.dos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}


} // end - class ClientHandler
