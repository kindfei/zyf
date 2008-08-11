package test.cluster.core.tc;

public class Task {
	
	private int mode;
	private Object content;
	
	public Task(int mode, Object content) {
		this.mode = mode;
		this.content = content;
	}
	
	public int getExecuteMode() {
		return mode;
	}
	
	public Object getContent() {
		return content;
	}
}
