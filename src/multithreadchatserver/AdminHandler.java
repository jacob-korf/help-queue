package multithreadchatserver;

import Gui.Request;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Iterator;

public class AdminHandler implements Runnable{
        String name;
        final DataInputStream dis;
        final DataOutputStream dos;
        Socket s;
        boolean isloggedin;

        public AdminHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
            this.dis = dis;
            this.dos = dos;
            this.name = name;
            this.s = s;
            this.isloggedin = true;
        }

        public void run() {
            while(this.isloggedin) {
                String hh = "";

                try {
                    hh = this.dis.readUTF();
                   
                    if (hh.equals("Reset")) {
                       Server.requestList.clear();
                        this.dos.writeUTF("Help Request is cancelled");
                    }  
                    else if (hh.contains("Cancel#")) {
                    	String wk = hh.substring(7);

    					int pos = -1;
    					for (int x = 0; x < Server.requestList.size(); ++x) {
    						if (pos == -1) {
    							if (Server.requestList.get(x).getName().equals(wk)) {
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
    					dos.writeUTF(wk + " Help Request is cancelled");
                    }
                    else {
                        this.dos.writeUTF("Bad Input from Admin");
                    }
                } catch (IOException var8) {
                    this.isloggedin = false;
                }
            }

            try {
                int pos = -1;

                for(int x = 0; x < Server.requestList.size(); ++x) {
                    if (pos == -1) {
                        if (((Request)Server.requestList.get(x)).getName().equals(this.name)) {
                            pos = x;
                        }
                    } else {
                        ((Request)Server.requestList.get(x)).lowerQueue();
                    }
                }

                if (pos > -1) {
                    Server.requestList.remove(pos);
                }

                Iterator var13 = Server.disAr.iterator();

                while(var13.hasNext()) {
                    DisplayHandler mc = (DisplayHandler)var13.next();
                    mc.update();
                }

                this.dis.close();
                this.dos.close();
                AdminHandler rem = null;
                Iterator var16 = Server.ar.iterator();

                while(var16.hasNext()) {
                    AdminHandler mc = (AdminHandler)var16.next();
                    if (this.name.equals(mc.name)) {
                        rem = mc;
                    }
                }

                Server.adAr.remove(rem);
            } catch (IOException var7) {
                System.out.println("AdminHandler, closing resources section");
                var7.printStackTrace();
            }

        }
    }

