package test.cluster.tc.caches;

public class TestCache {
	
	private String str;
	
	public synchronized void setValue(String str) {
		this.str = str;
	}
	
	public String getValue() {
		return str;
	}

}
