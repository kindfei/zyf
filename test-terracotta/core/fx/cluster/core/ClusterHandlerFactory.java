package fx.cluster.core;

import java.util.HashMap;

/**
 * Cluster handler factory
 * @author zhangyf
 *
 */
public class ClusterHandlerFactory {
	private static final HashMap<String, ClusterHandler> map = new HashMap<String, ClusterHandler>();
	
	static ClusterHandler getClusterHandler(String procName, boolean fairTake) {
		ClusterHandler handler = null;
		synchronized (map) {
			handler = map.get(procName);
			if (handler == null || handler.isFairTake() != fairTake) {
				handler = new ClusterHandler(procName, fairTake);
				map.put(procName, handler);
			}
		}
		return handler;
	}
}
