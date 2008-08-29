package test.cluster.core.tc;

import test.cluster.core.ExecuteMode;

public class GroupTask extends Task {
	private String id;

	public GroupTask(ExecuteMode executeMode, Object content, String id) {
		super(executeMode, content);
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
