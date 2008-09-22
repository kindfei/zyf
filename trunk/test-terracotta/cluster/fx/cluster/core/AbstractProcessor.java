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
		handler = ClusterHandlerFactory.instance.getClusterHandler(procName);
	}
	
	public final void setCacheValue(String key, Object value) {
		handler.setCacheValue(key, value);
	}
	
	public final Object getCacheValue(String key) {
		return handler.getCacheValue(key);
	}
}
