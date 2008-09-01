package test.cluster.core.tc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import test.cluster.core.ExecuteMode;

/**
 * Task
 * @author zhangyf
 *
 */
public class Task {
	
	private ExecuteMode executeMode;
	private String groupId;
	private Object content;
	
	private ReentrantLock groupLock;
	private Condition finish;
	
	public Task(ExecuteMode executeMode, Object content) {
		this.executeMode = executeMode;
		this.content = content;
	}
	
	public Task(String groupId, Object content) {
		this.groupId = groupId;
		this.content = content;
	}
	
	public ExecuteMode getExecuteMode() {
		return executeMode;
	}
	
	public Object getContent() {
		return content;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public ReentrantLock getGroupLock() {
		return groupLock;
	}

	public void setGroupLock(ReentrantLock groupLock) {
		this.groupLock = groupLock;
	}

	public Condition getFinish() {
		return finish;
	}

	public void setFinish(Condition finish) {
		this.finish = finish;
	}
}
