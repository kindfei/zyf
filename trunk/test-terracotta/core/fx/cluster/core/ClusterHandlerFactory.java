package fx.cluster.core;

import java.util.HashMap;

public class ClusterHandlerFactory {
	public static final ClusterHandlerFactory instance = new ClusterHandlerFactory();
	
	private HashMap<String, ClusterHandler> map = new HashMap<String, ClusterHandler>();
	
	ClusterHandler getClusterHandler(String procName) {
		ClusterHandler handler = map.get(procName);
		if (handler == null) {
			synchronized (map) {
				handler = map.get(procName);
				if (handler == null) {
					handler = new ClusterHandler(procName);
					map.put(procName, handler);
				}
			}
		}
		return handler;
	}
}
