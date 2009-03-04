package core.cluster;

/**
 * AbstractProcessor
 * @author zhangyf
 *
 * @param <T>
 */
public abstract class AbstractProcessor<T> implements Processor<T> {
	protected String procName;
	private ClusterCache cache;
	
	protected AbstractProcessor() {
		this.procName = this.getClass().getName();
		cache = ClusterHandlerFactory.getClusterCache(procName);
	}
	
	public final void setCacheValue(String key, Object value) {
		cache.set(key, value);
	}
	
	public final Object getCacheValue(String key) {
		return cache.get(key);
	}
}
