/*
 * Client - Java implementation for multi-threaded chat client  
 *
 *  from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/
 */

package multithreadchatclient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Display {
	// data
	final static int ServerPort = 3015;
	final static String Host = "localhost";
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;

	@SuppressWarnings("resource")
	public Display() throws UnknownHostException, IOException {

	} // end - method main

	public Boolean connect() throws UnknownHostException {

		// getting host ip
		InetAddress ip;
		try {
			ip = InetAddress.getByName(Host);
			// establish the connection
			s = new Socket(ip, ServerPort);

			// obtaining input and out streams
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}

	public String getRequest() {
		// sends request to display
		String msg = "";
		try {
			// read the message sent to this client
			msg = dis.readUTF();

		} catch (IOException e) {
			return "Failure";
		}
		return msg;
	}
	

} // end - class Display
