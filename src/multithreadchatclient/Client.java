/*
 * Client - Java implementation for multi-threaded chat client  
 *
 *  from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/
 */

package multithreadchatclient;

import java.io.*;
import java.net.*;

public class Client {
	// data
	final static int ServerPort = 1234;
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

	// sendMessage thread
	/*
	 * Thread sendMessage = new Thread(new Runnable() { private boolean exit =
	 * false; // private String name; could new Thread(___, name) above;
	 * 
	 * @Override public void run() { while (!exit) {
	 * 
	 * // read the message to deliver. String msg = scn.nextLine();
	 * 
	 * // write on the output stream try { dos.writeUTF(msg); } catch (IOException
	 * e) { System.out.println("Client, send message section - caught exception");
	 * //e.printStackTrace(); exit = true; } } // end - while
	 * System.out.println("Client, thread sendMessage - run(), after while loop"); }
	 * // end - method run }); // end - thread sendMessage
	 */

	// readMessage thread
	/*
	 * Thread readMessage = new Thread(new Runnable() { private boolean exit =
	 * false; // private String name; could new Thread(___, name) above;
	 * 
	 * @Override public void run() { while (!exit) { try { // read the message sent
	 * to this client String msg = dis.readUTF(); System.out.println(msg); } catch
	 * (IOException e) { //
	 * System.out.println("Client, read message section - caught exception"); //
	 * e.printStackTrace(); exit = true; } } // end - while true
	 * System.out.println("Client, thread readMessage - run(), after while loop"); }
	 * // end - method run }); // end - thread readMessage
	 * 
	 * // sendMessage.start(); readMessage.start();
	 * 
	 * } // end - method main
	 */

	public String sendRequest() {
//Information: Workstation
		// write on the output stream
		try {
			dos.writeUTF("Sent");
			return dis.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "Failure";
		}
	}

	public String cancelRequest() {
		
			try {
			dos.writeUTF("Cancel");

				return dis.readUTF();
			} catch (IOException e) {
				return "Failure";
			}
		
	}

} // end - class Client
