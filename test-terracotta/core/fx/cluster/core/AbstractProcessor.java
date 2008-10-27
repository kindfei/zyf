package fx.cluster.core;

/**
 * AbstractProcessor
 * @author zhangyf
 *
 * @param <T>
 */
public abstract class AbstractProcessor<T> implements Processor<T> {
	protected String procName;
	private ClusterHandler handler;
	
	protected AbstractProcessor() {
		this.procName = this.getClass().getName();
	}
	
	void setHandler(ClusterHandler handler) {
		this.handler = handler;
	}
	
	public final void setCacheValue(String key, Object value) {
		if (handler != null) handler.setCacheValue(key, value);
	}
	
	public final Object getCacheValue(String key) {
		if (handler != null) return handler.getCacheValue(key);
		return null;
	}
}
