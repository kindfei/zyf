package test.core.jms;

public enum MessageDestination {
	testQueue("queue/testQueue"),
	testTopic("topic/testTopic");
	
	String strDest;
	private MessageDestination(String strDest) {
		this.strDest = strDest;
	}
	
	public String getStrDest() {
		return strDest;
	}
}
