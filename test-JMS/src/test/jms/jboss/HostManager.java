package test.jms.jboss;

import java.util.Hashtable;
import java.util.Map;

public class HostManager {
	private Map groups = new Hashtable();
	
	public HostManager() {
		groups.put("G1", new Group("jnp://localhost:1099", "jnp://localhost:1099"));
		groups.put("G2", new Group("jnp://localhost:1099", "jnp://localhost:1099"));
	}
	
	public String getFristHost(String groupName) {
		Group g = (Group) groups.get(groupName);
		return g.getFristHost();
	}
	
	public String getNextHost(String groupName) {
		Group g = (Group) groups.get(groupName);
		return g.getNextHost();
	}

}

class Group {
	private String first;
	private String second;
	
	Group(String first, String second) {
		this.first = first;
		this.second = second;
	}
	
	public String getFristHost() {
		return first;
	}
	
	public String getNextHost() {
		String temp = first;
		first = second;
		second = temp;
		return first;
	}
}
