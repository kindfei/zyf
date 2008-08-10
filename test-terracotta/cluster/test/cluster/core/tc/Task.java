package test.cluster.core.tc;

import java.util.HashMap;
import java.util.Map;


public class Task {
	
	private int mode;
	private Map<String, Object> content = new HashMap<String, Object>();
	
	public Task(int mode) {
		this.mode = mode;
	}
	
	public int getExecuteMode() {
		return mode;
	}
	
	public void setProperty(String key, Object value) {
		content.put(key, value);
	}
	
	public Object getProerty(String key) {
		return content.get(key);
	}
}
