package test.jms.jboss;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HostManager {
	private static final Log log = LogFactory.getLog(HostManager.class);

	private String groupName;
	private LinkedList hosts = new LinkedList();
	
	public HostManager(String groupName) {
		this.groupName = groupName;
		init();
	}
	
	private void init() {
		hosts.add("jnp://10.4.5.60:1099");
	}
	
	public String getCurrentHost() {
		return (String) hosts.getFirst();
	}
	
	public String getNextHost() {
		String curr = fromat();
		String currHost = getCurrentHost();
		hosts.add(hosts.removeFirst());
		String next = fromat();
		String nextHost = getCurrentHost();
		
		boolean isSuccess = false;
		
		log.info("JMS provider change. groupName:" + groupName + " curr:[" + curr + "] ---> next:[" + next + "]");
		if (!curr.equals(next)) {
			log.info("JMS provider change. Synchronize to APP_PROPERTY.");
			isSuccess = synchronize(curr, next);
		}
		
		sendMail(curr, next, currHost, nextHost, isSuccess);
		
		shutdown(currHost);
		
		return getCurrentHost();
	}
	
	private String fromat() {
		StringBuffer result = new StringBuffer();
		for (Iterator iter = hosts.iterator(); iter.hasNext();) {
			result.append(iter.next());
			if (iter.hasNext()) result.append(",");
		}
		return result.toString();
	}
	
	private boolean synchronize(String curr, String next) {
		boolean isSuccess = false;
		
		return isSuccess;
	}
	
	private void sendMail(String prev, String curr, String prevHost, String currHost, boolean isSuccess) {
	}
	
	private void shutdown(String prevHost) {
	}
}
