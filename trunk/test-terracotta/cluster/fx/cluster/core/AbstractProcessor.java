package fx.cluster.core;


/**
 * AbstractProcessor
 * @author zhangyf
 *
 * @param <T>
 */
public abstract class AbstractProcessor<T> implements Processor<T> {
	protected String procName;
	private ClusterShareRoot root;
	
	protected AbstractProcessor() {
		this.procName = this.getClass().getName();
		root = ClusterShareRoot.instance;
	}
	
	public final void setCacheValue(String key, Object value) {
		root.setCacheValue(procName, key, value);
	}
	
	public final Object getCacheValue(String key) {
		return root.getCacheValue(procName, key);
	}
}
