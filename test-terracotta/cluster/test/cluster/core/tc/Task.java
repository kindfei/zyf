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
	private Object content;
	
	private String groupId;
	private ReentrantLock groupLock;
	private Condition finish;
	
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
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public synchronized ReentrantLock getGroupLock() {
		return groupLock;
	}

	public synchronized void setGroupLock(ReentrantLock groupLock) {
		this.groupLock = groupLock;
	}

	public synchronized Condition getFinish() {
		return finish;
	}

	public synchronized void setFinish(Condition finish) {
		this.finish = finish;
	}
}
