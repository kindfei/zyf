package test.cluster.caches;

public class TestCacheBean {
	
	private String str;
	
	public synchronized void setValue(String str) {
		this.str = str;
	}
	
	public String getValue() {
		return str;
	}

}
