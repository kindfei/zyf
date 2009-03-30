package test.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class TestEhcache {
	private String cacheName;

	private CacheManager manager;
	
	public TestEhcache(String cacheName, int maxElementsInMemory,
			boolean overflowToDisk, boolean eternal,
			long timeToLiveSeconds, long timeToIdleSeconds) {
		this.cacheName = cacheName;
		
		manager = CacheManager.create(getClass().getResource("/TestEhcache.xml"));
		Cache cache = new Cache(
				"test",
				maxElementsInMemory,
				overflowToDisk,
				eternal,
				timeToLiveSeconds,
				timeToIdleSeconds,
				false,
				0);
		manager.addCache(cache);
	}
	
	public void put(Object key, Object value) {
		Element element = new Element(key, value);
		manager.getCache(cacheName).put(element);
	}
	
	public Object get(Object key) {
		return manager.getCache(cacheName).get(key).getObjectValue();
	}
}
