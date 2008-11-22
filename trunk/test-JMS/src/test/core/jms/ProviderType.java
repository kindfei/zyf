package test.core.jms;

public enum ProviderType {
	JBossMQ ("jbossmq"),
	ActiveMQ ("activemq"),
	JBossMessaging ("jbossmessaging");
	
	private String providerName;
	
	private ProviderType(String providerName) {
		this.providerName = providerName;
	}
	
	public String getProviderName() {
		return providerName;
	}
}
