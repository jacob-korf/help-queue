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
	final static int ServerPort = 1235; 
	final static String Host = "localhost";
	
	@SuppressWarnings("resource")
	public static void main(String args[]) throws UnknownHostException, IOException  
	{ 
		Scanner scn = new Scanner(System.in); 
       
		// getting host ip 
		InetAddress ip = InetAddress.getByName(Host); 
       
		// establish the connection 
		Socket s = new Socket(ip, ServerPort); 
       
		// obtaining input and out streams 
		DataInputStream dis = new DataInputStream(s.getInputStream()); 
		DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

		
		
		// readMessage thread 
		Thread updateDisplay = new Thread(new Runnable()  
		{ 
			private boolean exit = false;
			// private String name;   could new Thread(___, name) above;
			
			@Override
			public void run() { 
				while (!exit) {
					try { 
						// read the message sent to this client 
						String msg = dis.readUTF(); 
						System.out.println(msg);
					} catch (IOException e) { 
						//System.out.println("Client, read message section - caught exception");
						//e.printStackTrace(); 
						exit = true;
					} 
				}	// end - while true 
				System.out.println("Client, thread readMessage - run(), after while loop");
			}	// end - method run
		});	// end - thread readMessage

		updateDisplay.start(); 

	}	// end - method main 

}	// end - class Client
