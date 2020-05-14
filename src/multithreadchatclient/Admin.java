package multithreadchatclient;

import DAO.DataAccessObject;
import multithreadchatserver.AdminHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Admin {
    static final int ServerPort = 3016;
    static final String Host = "localhost";
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    DataAccessObject dao;
    private boolean flag;

    public Admin() {
    }

    //connect to server; returns false in case of failed connection to send message to shutdown the adminPanel
    public boolean connect() {
        try {
            InetAddress ip = InetAddress.getByName("localhost");

            try {
                this.s = new Socket(ip, 3016);
                this.dis = new DataInputStream(this.s.getInputStream());
                this.dos = new DataOutputStream(this.s.getOutputStream());
            } catch (UnknownHostException var3) {
                var3.printStackTrace();
            }

        } catch (IOException var4) {// Failure to connect to Server
            return false;
        }

        return true;
    }

    //Sends a message to AdminHandler to cancel the client with workstation name "wk" and receives a message whether it was a success
    public String cancelRequest(String wkName) {
        try {
            this.dos.writeUTF("Cancel#"+ wkName);
            return this.dis.readUTF();
        } catch (IOException var2) {// Failure to connect to Server
            return "Failure";
        }
    }

    //Sends a message to AdminHandler to clear the requestList and receives a message whether it was a success
    public String resetList() {
        try {
            this.dos.writeUTF("Reset");
            return this.dis.readUTF();
        } catch (IOException var2) {// Failure to connect to Server
            return "Failure";
        }

    }
    
    //Sends a message to AdminHandler to add a new calendar entry
    public String getQuery(String query) {
        try {
            this.dos.writeUTF("query" + query);
            return this.dis.readUTF();
        } catch (IOException var2) { // Failure to connect to Server
            return "Failure";
        }

    }
    public String login(String username, String  password){
        try {
            this.dos.writeUTF("credentials" + username + "/" + password);
            return this.dis.readUTF();
        } catch (IOException var2) { // Failure to connect to Server
            return "Failure";
        }
    }

    public void islogin(Boolean yesNo){
        flag = yesNo;
    }
    public boolean returnLogin(){
        return flag;
    }


}