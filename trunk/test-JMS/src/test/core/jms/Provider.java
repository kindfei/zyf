package test.core.jms;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Provider {
	private final static Log log = LogFactory.getLog(Provider.class);
	
	private static Map<String, String> destNameMap = new HashMap<String, String>();
	private static Map<String, String> providerMap = new HashMap<String, String>();
	
	static {
		
	}
	
	private String providerId;
	private ProviderType providerType;
	private boolean manualHa;
	private String url;
	private String user;
	private String password;
	
	Provider(String destName) {
	}
	
	String getProviderId() {
		return providerId;
	}
	
	ProviderType getProviderType() {
		return providerType;
	}
	
	String getCurrentURL() {
		if (!manualHa) return url;
		return null;
	}
	
	String getNextURL() {
		if (!manualHa) return url;
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
