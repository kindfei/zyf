package test.cluster.core;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


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

	ReentrantLock getGroupLock() {
		return groupLock;
	}

	void setGroupLock(ReentrantLock groupLock) {
		this.groupLock = groupLock;
	}

	Condition getFinish() {
		return finish;
	}

	void setFinish(Condition finish) {
		this.finish = finish;
	}
}
