package test.cluster.core.tc;

public class Task {
	
	private int mode;
	private Object content;
	
	public Task(int mode) {
		this.mode = mode;
	}
	
	public int getExecuteMode() {
		return mode;
	}
	
	public void setContent(Object content) {
		this.content = content;
	}
	
	public Object getContent() {
		return content;
	}
}
