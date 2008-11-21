package test.core.jms;

public class HostManager {
	private ProviderType type;
	private String groupId;
	
	HostManager(ProviderType type, String groupId) {
		this.type = type;
		this.groupId = groupId;
	}
	
	String getURL() {
		return null;
	}
	
	String getCurrentURL() {
		return null;
	}
	
	String getNextURL() {
		return null;
	}
	
	boolean isHA() {
		return false;
	}
}
