package test.core.cluster;

/**
 * ClusterExecutor
 * @author zhangyf
 *
 */
public class ClusterExecutor {
	private String procName;
	
	ClusterExecutor(String procName) {
		this.procName = procName;
	}
	
	void execute(ClusterTask task) {
		AbstractService<?> service = ServiceFactory.getService(procName);
		if (service == null) {
			return;
		}
		service.execute(task);
	}
}
