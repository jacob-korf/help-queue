package Gui;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;

public class Request {
	private String workstation;
	private LocalDateTime requestTime;
	private int queueNumber;
	private int waitTime;
	
	public Request(String workstation, LocalDateTime requestTime, int queueNumber, int waitTime) {
		this.workstation = workstation;
		this.requestTime = requestTime;
		this.queueNumber = queueNumber;
		this.waitTime = waitTime;
	}
	public String toString() {
		 //LocalDateTime d = new LocalDateTime(waitTime.g);
	    
		        DecimalFormat formatM = new DecimalFormat("000");
		        DecimalFormat formatS = new DecimalFormat("00");
		        
		return queueNumber + "            " + workstation+ "     "+formatS.format(requestTime.getHour()) + ":" + formatS.format(requestTime.getMinute()) + ":" +formatS.format(requestTime.getSecond()) + "      "  + formatM.format(waitTime/60) + ":" + formatS.format(waitTime%60);
	}
	public void updateTime(LocalDateTime updatedTime) {
		  waitTime = 3600*(updatedTime.getHour()-requestTime.getHour()) + 60*(updatedTime.getMinute()-requestTime.getMinute()) + updatedTime.getSecond()-requestTime.getSecond();
	}
	public void lowerQueue() {
		queueNumber--;
	}
	public String getName() {
		return workstation;
	}
	public int getPosition() {
		return queueNumber;
	}
}
