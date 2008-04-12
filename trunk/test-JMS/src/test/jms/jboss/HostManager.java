package test.jms.jboss;

import java.util.HashMap;
import java.util.Map;

public class HostManager {
	private Map groups = new HashMap();
	
	public HostManager() {
		groups.put("GROUP1", new Group("GROUP1", "jnp://10.4.5.28:1099,jnp://localhost:1099"));
		groups.put("GROUP2", new Group("GROUP2", "jnp://10.4.5.28:1099,jnp://localhost:1099"));
	}
	
	public String getHost(String groupName) {
		Group group = (Group) groups.get(groupName);
		return group.getHost();
	}
	
	public String getNextHost(String groupName) {
		Group group = (Group) groups.get(groupName);
		return group.getNextHost();
	}

}

class Group {
	private String groupName;
	private String master;
	private String slaver;
	
	Group(String groupName, String hosts) {
		this.groupName = groupName;
		String[] array = hosts.split(",");
		this.master = array[0];
		this.slaver = array[1];
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public String getHost() {
		return master;
	}
	
	public String getNextHost() {
		String temp = master;
		master = slaver;
		slaver = temp;
		
		String prev = slaver + "," + master;
		String next = master + "," + slaver;
		
		synchronize(prev, next);
		
		return master;
	}
	
	public void synchronize(String prev, String next) {
		
	}
}
