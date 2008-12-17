package zyf.cr;

public class MessageInfo implements RegisterInfo {
	private String type;
	private String number;
	private String messageType;
	private String beginTime;
	private String endTime;
	private String cost;
	public MessageInfo() {
	}
	public MessageInfo(String type, String number, String messageType, String beginTime, String endTime, String cost) {
		this.type = type;
		this.number = number;
		this.messageType = messageType;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.cost = cost;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getCost() {
		return cost;
	}
	public void setCast(String cost) {
		this.cost = cost;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("type:").append(type);
		sb.append("\tnumber:").append(number);
		sb.append("\tmessageType:").append(messageType);
		sb.append("\tbeginTime:").append(beginTime);
		sb.append("\tendTime:").append(endTime);
		sb.append("\tcost:").append(cost);
		return sb.toString();
	}
}
