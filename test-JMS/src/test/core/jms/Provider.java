package test.core.jms;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Provider {
	private final static Log log = LogFactory.getLog(Provider.class);
	
	private static Map<String, Provider> providers = new HashMap<String, Provider>();
	
	static {
		Provider provider1 = new Provider("provider1", ProviderType.valueOf("JBossMQ"), true, "jnp://localhost:1099", null, null);
		Provider provider2 = new Provider("provider2", ProviderType.valueOf("JBossMQ"), true, "jnp://localhost:1099,jnp://10.4.6.218:1099", null, null);
		Provider provider3 = new Provider("provider3", ProviderType.valueOf("ActiveMQ"), false, "tcp://localhost:61616", null, null);
		Provider provider4 = new Provider("provider4", ProviderType.valueOf("ActiveMQ"), false, "failover://(tcp://localhost:61616,tcp://10.4.6.218:61616)", null, null);
		Provider provider5 = new Provider("provider5", ProviderType.valueOf("JBossMessaging"), false, "jnp://localhost:1099", null, null);
		Provider provider6 = new Provider("provider6", ProviderType.valueOf("JBossMessaging"), false, "localhost:1100,10.4.6.218:1100", null, null);
		
		providers.put("queue/testQueue", provider2);
		providers.put("topic/testTopic", provider1);
	}
	
	static Provider getProvider(String destName) {
		return providers.get(destName);
	}
	
	private String providerId;
	private ProviderType providerType;
	private boolean manualHa;
	private String url;
	private String user;
	private String password;
	
	private Provider(String providerId, ProviderType providerType,
			boolean manualHa, String url, String user, String password) {
		this.providerId = providerId;
		this.providerType = providerType;
		this.manualHa = manualHa;
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	String getProviderId() {
		return providerId;
	}
	
	ProviderType getProviderType() {
		return providerType;
	}
	
	String getCurrentURL() {
		if (!manualHa) return url;
		
		String[] urls = url.split(",");
		
		return urls[0];
	}
	
	String getNextURL() {
		if (!manualHa) return url;
		
		String[] urls = url.split(",");
		if (urls.length != 1) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < urls.length; i++) {
				sb.append(urls[i]).append(",");
			}
			sb.append(urls[0]);
			String newUrl = sb.toString();
			
			updateProvider(newUrl);
			url = newUrl;
		}
		
		return urls[1];
	}
	
	private void updateProvider(String newUrl) {
		
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
