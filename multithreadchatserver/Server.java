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
import java.net.*; 

public class Server  
{ 
	// Vector to store active clients 
	static Vector<ClientHandler> ar = new Vector<>(); 
   
	// counter for clients 
	static int i = 0; 

	public static void main(String[] args) throws IOException  
	{ 
		// server is listening on port 1234 
		@SuppressWarnings("resource")
		ServerSocket ss = new ServerSocket(1234); 	// socket for server side
       
		Socket s; 									// socket for client side
       
		// run infinite loop for getting client requests 
		while (true)  
		{  
			// NOTE: an accept call will wait (block) indefinitely waiting for a connection; if you want the enclosing loop to run regularly, 
			//		you need to put a timeout on your serversocket and use exception handling to determine if the accept call was successful
			//		(any code following the accept call will execute after the accept() succeeds) or failed due to the timeout (throws a 
			//		SocketTimeoutException).  See the Java docs for the ServerSocket class or use online search on this topioc for details.
			// Wait for and accept an incoming request
			s = ss.accept(); 
			// System.out.println("New client request received : " + s); 
         
			// create input and output streams for this socket 
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

			// Create a new handler object for handling this request.
			String clientName = "client " + i;
			ClientHandler mtch = new ClientHandler(s, clientName, dis, dos); 

			// Create a new Thread with this client handler object. 
			Thread t = new Thread(mtch); 
           
			System.out.println("Adding client " + clientName  + " to active client list"); 

			// add this client to active clients list 
			ar.add(mtch); 
			
			// display client list
			System.out.println("Current Clients:");
			for (ClientHandler mc : Server.ar) { 
				if (mc.isloggedin == true) {
					System.out.println(mc.name);
				}
			} 
			System.out.println();
			
			// start the thread. 
			t.start(); 

			// increment i for new client name
			i++; 

		}	// end - while true loop 
	}	// end - method main
	
}	// end - class Server
