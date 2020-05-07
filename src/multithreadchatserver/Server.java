//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package multithreadchatserver;

import DAO.DataAccessObject;
import Gui.Request;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class Server {
	static Vector<ClientHandler> ar = new Vector<ClientHandler>();
	static Vector<DisplayHandler> disAr = new Vector<DisplayHandler>();
	static Vector<AdminHandler> adAr = new Vector<AdminHandler>();
	protected static ArrayList<Request> requestList = new ArrayList<Request>();
	static int i = 0;
	static int j = 0;
	static int k = 0;
	DataAccessObject dao;

	public Server() {
	}

	public void insert() { // method for inserting stuff into the database
		dao = new DataAccessObject();
		dao.connect(); // establish db connection
		dao.setAutoCommit(false); // Set AutoCommit to false -- not sure is we want this to be false

		String search = ("SELECT TIME(time)");
		dao.executeSQLQuery(search); // finds something in the db that we want
		String result = dao.processResultSet(); // gets the result of the search

		String stuffToAddTodb = "stuff";
		dao.executeSQLNonQuery("INSERT INTO tableName VALUES(" + stuffToAddTodb + ")");

		dao.commit(); // commit the transaction.

		dao.disconnect(); // disconnects (have to do this for every method?)

	}

	public static void main(String[] args) throws IOException {
		System.out.println("Server started Suceessfully");
		ServerSocket ss = new ServerSocket(3014);
		ss.setSoTimeout(100);
		ServerSocket displaySS = new ServerSocket(3015);
		displaySS.setSoTimeout(100);
		ServerSocket Adminss = new ServerSocket(3016);
		Adminss.setSoTimeout(100);
		Thread updateTime = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(3000L);
					} catch (InterruptedException var5) {
						var5.printStackTrace();
					}

					LocalDateTime current = LocalDateTime.now();

					for (int x = 0; x < Server.requestList.size(); ++x) {
						((Request) Server.requestList.get(x)).updateTime(current);
					}

					DisplayHandler rem = null;
					Iterator var4 = Server.disAr.iterator();

					while (var4.hasNext()) {
						DisplayHandler mc = (DisplayHandler) var4.next();
						if (!mc.update()) {
							rem = mc;
						}
					}

					Server.disAr.remove(rem);
				}
			}
		});
		updateTime.start();

		while (true) {
			Socket s;
			try {
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				String clientName = "Lab-P115-";
				if (i < 10) {
					clientName = clientName + "0";
				}

				clientName = clientName + (i + 1);
				ClientHandler mtch = new ClientHandler(s, clientName, dis, dos);
				Thread t = new Thread(mtch);
				ar.add(mtch);
				t.start();
				++i;
			} catch (InterruptedIOException var12) {
				try {
					s = displaySS.accept();
					DataInputStream dis = new DataInputStream(s.getInputStream());
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					String displayName = "Display " + j;
					DisplayHandler mtch = new DisplayHandler(s, displayName, dis, dos);
					Thread t = new Thread(mtch);
					disAr.add(mtch);
					t.start();
					++j;
				} catch (InterruptedIOException var11) {
					try {
						s = Adminss.accept();
						DataInputStream dis = new DataInputStream(s.getInputStream());
						DataOutputStream dos = new DataOutputStream(s.getOutputStream());
						String AdminName = "Lab-P115-Admin";
						if (k <10) {
							AdminName = AdminName + "0";
						}

						AdminName = AdminName + (k + 1);
						AdminHandler mtch = new AdminHandler(s, AdminName, dis, dos);
						Thread t = new Thread(mtch);
						adAr.add(mtch);
						t.start();
						++k;
					} catch (InterruptedIOException var13) {
					}
				}
			}

		}
	}
}
