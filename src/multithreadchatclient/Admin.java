package multithreadchatclient;

import DAO.DataAccessObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Admin {
    static final int ServerPort = 1236;
    static final String Host = "localhost";
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    DataAccessObject dao;

    public Admin() {
    }

    public boolean connect() {
        try {
            InetAddress ip = InetAddress.getByName("localhost");

            try {
                this.s = new Socket(ip, 1236);
                this.dis = new DataInputStream(this.s.getInputStream());
                this.dos = new DataOutputStream(this.s.getOutputStream());
            } catch (UnknownHostException var3) {
                var3.printStackTrace();
            }

            return true;
        } catch (IOException var4) {
            return false;
        }
    }

    public String sendRequest() {
        try {
            this.dos.writeUTF("Sent");
            return this.dis.readUTF();
        } catch (IOException var2) {
            return "Failure";
        }
    }

    public String cancelRequest(String wkName) {
        try {
            this.dos.writeUTF("Cancel#"+ wkName);
            return this.dis.readUTF();
        } catch (IOException var2) {
            return "Failure";
        }
    }

	public String resetList() {
        try {
            this.dos.writeUTF("Reset");
            return this.dis.readUTF();
        } catch (IOException var2) {
            return "Failure";
        }
		
	}

}