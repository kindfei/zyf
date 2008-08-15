package test.cluster.core.tc;

import test.cluster.core.ExecuteMode;

public class Task {
	
	private ExecuteMode executeMode;
	private Object content;
	
	public Task(ExecuteMode executeMode, Object content) {
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
