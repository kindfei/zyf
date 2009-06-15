package org.zyf.jms;

public enum MessageDestination {
	testQueue("queue/testQueue"),
	testTopic("topic/testTopic");
	
	private String destName;
	private Provider provider;
	
	private MessageDestination(String destName) {
		this.destName = destName;
		provider = Provider.getProvider(destName);
	}
	
	public String getDestName() {
		return destName;
	}
	
	public Provider getProvider() {
		return provider;
	}
}