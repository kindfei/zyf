package zyf.cr;

public class ResultInfo {
	private CallInfo[] calls;
	private MessageInfo[] messages;
	public ResultInfo() {
	}
	public ResultInfo(CallInfo[] calls, MessageInfo[] messages) {
		this.calls = calls;
		this.messages = messages;
	}
	public CallInfo[] getCalls() {
		return calls;
	}
	public void setCalls(CallInfo[] calls) {
		this.calls = calls;
	}
	public MessageInfo[] getMessages() {
		return messages;
	}
	public void setMessages(MessageInfo[] messages) {
		this.messages = messages;
	}
}
