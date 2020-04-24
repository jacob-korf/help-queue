/*
 * Server - Java implementation of Server class for multi-threaded chat server 
 *
 *  from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
 *  
 *  Assumes: ports used (here, port 1234) is not restricted or blocked by firewall
 */

package multithreadchatserver;

import java.io.*;
import java.util.*;

import Gui.Request;

import java.net.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Server {
	// Vector to store active clients
	static Vector<ClientHandler> ar = new Vector<>();
	static Vector<DisplayHandler> disAr = new Vector<>();
	protected static ArrayList<Request> requestList = new ArrayList<Request>();

	// counter for clients
	static int i = 0;
	static int j = 0;

	public static void main(String[] args) throws IOException {
		// server is listening on port 1234

		System.out.println("Server started Suceessfully");

		@SuppressWarnings("resource")
		ServerSocket ss = new ServerSocket(1234); // socket for server side
		ss.setSoTimeout(100);
		@SuppressWarnings("resource")
		ServerSocket displaySS = new ServerSocket(1235); // socket for server side
		displaySS.setSoTimeout(100);
		Socket s; // socket for client side
		// run infinite loop for getting client requests
		Thread updateTime = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					LocalDateTime current = LocalDateTime.now();
					for (int x = 0; x < requestList.size(); x++) {
						requestList.get(x).updateTime(current);
					}
					DisplayHandler rem = null;
					for (DisplayHandler mc : Server.disAr) {
						if (!mc.update()) {
							rem = mc;

						}

					}

					disAr.remove(rem);
				}
			} // end - method run
		}); // end - thread readMessage

		updateTime.start();
		while (true)

		{
			// NOTE: an accept call will wait (block) indefinitely waiting for a connection;
			// if you want the enclosing loop to run regularly,
			// you need to put a timeout on your serversocket and use exception handling to
			// determine if the accept call was successful
			// (any code following the accept call will execute after the accept() succeeds)
			// or failed due to the timeout (throws a
			// SocketTimeoutException). See the Java docs for the ServerSocket class or use
			// online search on this topioc for details.
			// Wait for and accept an incoming request
			try {
				s = ss.accept();

				// create input and output streams for this socket
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());

				// Create a new handler object for handling this request.
				String clientName = "Lab-P115-";
				if (i > 9) {
					clientName += "0";
				}
				clientName += (i + 1);

				ClientHandler mtch = new ClientHandler(s, clientName, dis, dos);

				// Create a new Thread with this client handler object.
				Thread t = new Thread(mtch);

				// add this client to active clients list
				ar.add(mtch);

				// start the thread.
				t.start();

				// increment i for new client name
				i++;
			} catch (java.io.InterruptedIOException e) {

				try {
					// System.err.println("Timed Out (60 sec)!");
					s = displaySS.accept();

					// create input and output streams for this socket
					DataInputStream dis = new DataInputStream(s.getInputStream());
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());

					// Create a new handler object for handling this request.
					String displayName = "Display " + j;
					DisplayHandler mtch = new DisplayHandler(s, displayName, dis, dos);

					// Create a new Thread with this client handler object.
					Thread t = new Thread(mtch);

					// add this client to active clients list
					disAr.add(mtch);

					// display client list

					// start the thread.
					t.start();

					// increment i for new client name
					j++;
				} catch (java.io.InterruptedIOException ex) {
				}
			}
		} // end - while true loop
	} // end - method main

} // end - class Server
