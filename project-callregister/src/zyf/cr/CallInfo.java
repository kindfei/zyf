package zyf.cr;

public class CallInfo implements RegisterInfo{

	private String type;
	private String number;
	private String time;
	private String seconds;
	private String location;
	private String cost;
	public CallInfo() {
	}
	public CallInfo(String type, String number, String time, String seconds, String location, String cost) {
		this.type = type;
		this.number = number;
		this.time = time;
		this.seconds = seconds;
		this.location = location;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSeconds() {
		return seconds;
	}
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("type:").append(type);
		sb.append("\tnumber:").append(number);
		sb.append("\ttime:").append(time);
		sb.append("\tseconds:").append(seconds);
		sb.append("\tlocation:").append(location);
		sb.append("\tcost:").append(cost);
		return sb.toString();
	}
}
