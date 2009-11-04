package incubation.cluster;

/**
 * Cluster task
 * @author zhangyf
 *
 */
public class ClusterTask {
	private ExecuteMode executeMode;
	private Object content;
	
	public ClusterTask(ExecuteMode executeMode, Object content) {
		this.executeMode = executeMode;
		this.content = content;
	}
	
	public ExecuteMode getExecuteMode() {
		return executeMode;
	}
	
	public Object getContent() {
		return content;
	}
}
