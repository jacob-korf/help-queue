package server;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class Request {
	private String workstation;
	private LocalDateTime requestTime;
	private int queueNumber;
	private int waitTime;
	private String name;
	
	public Request(String workstation, LocalDateTime requestTime, int queueNumber, int waitTime, String preferredName) {
		this.workstation = workstation;
		this.requestTime = requestTime;
		this.queueNumber = queueNumber;
		this.waitTime = waitTime;
		updateName(preferredName);
	}
	public String toString() {
		        DecimalFormat formatM = new DecimalFormat("000");
		        DecimalFormat formatS = new DecimalFormat("00");
		// returning string for help request
		return queueNumber + "            " + workstation+ "    "+ name + formatS.format(requestTime.getHour()) + ":" + formatS.format(requestTime.getMinute()) + ":" +formatS.format(requestTime.getSecond()) + "      "  + formatM.format(waitTime/60) + ":" + formatS.format(waitTime%60);
	}
	// calculating wait time
	public void updateTime(LocalDateTime updatedTime) {
		  waitTime = 3600*(updatedTime.getHour()-requestTime.getHour()) + 60*(updatedTime.getMinute()-requestTime.getMinute()) + updatedTime.getSecond()-requestTime.getSecond();
	}
	public void lowerQueue() {
		queueNumber--;
	}
	// return workstation name
	public String getName() {
		return workstation;
	}
	// return position number
	public int getPosition() {
		return queueNumber;
	}
	// return  wait time
	public int getWaitTime() {
		return waitTime;
	}
	// return help request time
	public LocalDateTime getRequestTime() {
		return requestTime;
	}
	public void updateName(String n) {
		StringBuffer outputBuffer = new StringBuffer(n);
		for (int i = 0; i < 12-n.length(); i++){
		   outputBuffer.append(" ");
		}
		this.name = outputBuffer.toString();
	}
}
