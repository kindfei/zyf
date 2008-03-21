package test.jms.jboss;

import java.util.Hashtable;
import java.util.Map;

public class HostManager {
	private static Map groups = new Hashtable();
	
	static {
		groups.put("G1", new Group("jnp://10.4.5.28:1099", "jnp://10.4.5.31:1099"));
		groups.put("G2", new Group("jnp://10.4.5.31:1099", "jnp://10.4.5.28:1099"));
	}
	
	public static String getFristHost(String groupName) {
		Group g = (Group) groups.get(groupName);
		return g.getFristHost();
	}
	
	public static String getNextHost(String groupName) {
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
