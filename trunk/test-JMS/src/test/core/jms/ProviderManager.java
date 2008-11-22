package test.core.jms;

public class ProviderManager {
	private ProviderType type;
	private String groupId;
	
	private String providerUrl;
	private boolean manualHa;
	private String user;
	private String password;
	
	ProviderManager(ProviderType type, String groupId) {
		this.type = type;
		this.groupId = groupId;
	}
	
	void init() {
		this.providerUrl = "";
		this.manualHa = false;
		this.user = "";
		this.password = "";
	}
	
	String getCurrentURL() {
		return null;
	}
	
	String getNextURL() {
		return null;
	}
	
	String getUser() {
		return user;
	}
	
	String getPassword() {
		return password;
	}
	
	boolean isManualHa() {
		return manualHa;
	}
}
