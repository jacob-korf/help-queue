/*
 * Client - Java implementation for multi-threaded chat client  
 *
 *  from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/
 */

package client;

import java.io.*;
import java.net.*;

public class Client {
	// data
	final static int ServerPort = 3014; // correct port
	final static String Host = "localhost";
	private Socket s;
	private DataInputStream dis;
	private DataOutputStream dos;

	@SuppressWarnings("resource")
	public Client() {

	}

	public boolean connect() {
		// getting host ip
		InetAddress ip;
		try {
			ip = InetAddress.getByName(Host);

			// establish the connection
			try {
				s = new Socket(ip, ServerPort);
				// obtaining input and out streams
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			return false;
		}

		return true;
	}

	public String sendRequest() {
	//Information: Workstation send help request
		// write on the output stream
		try {
			dos.writeUTF("Sent");
			return dis.readUTF();
		} catch (IOException e) {
			return "Failure";
		}
	}

	public String cancelRequest() {
	// Information : Workstation cancel help request
		// write on the output stream
			try {
			dos.writeUTF("Cancel");

				return dis.readUTF();
			} catch (IOException e) {
				return "Failure";
			}
		
	}

	public String update() {
	// Information: Update information displayed within ClientPanel, whether that be a forced cancellation by admin,
	// or updating average wait time and updating queue position
	// write on the output stream
		try {
		dos.writeUTF("Update");
			return dis.readUTF();
		} catch (IOException e) {
			return "Failure";
		}
	}

	public String sendName(String name) {
		// Information: send a new preferred name to Client Handler to be put on all current and future requests
		try {
		dos.writeUTF(name + "#Name");
			return dis.readUTF();
		} catch (IOException e) {
			return "Failure";
		}
	}

} // end - class Client
