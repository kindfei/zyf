package test.cluster.core;

import test.cluster.core.tc.ClusterShareRoot;


public abstract class AbstractProcessor<T> implements Processor<T> {
	protected String procName;
	private ClusterShareRoot root;
	
	public AbstractProcessor() {
		this.procName = this.getClass().getName();
		root = ClusterShareRoot.instance;
	}
	
	public void setCacheValue(String key, Object value) {
		root.setCache(procName, key, value);
	}
	
	public Object getCacheValue(String key) {
		return root.getCache(procName, key);
	}
}
