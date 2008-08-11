package test.cluster.core;

import java.util.List;

import test.cluster.core.tc.Task;

public abstract class TimerProcessor extends AbstractProcessor<Object> {
	
	public List<Task> masterProcess(Object obj) {
		return masterProcess();
	}
	
	public abstract List<Task> masterProcess();
}
