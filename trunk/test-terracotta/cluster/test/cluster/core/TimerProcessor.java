package test.cluster.core;

import java.util.List;

import test.cluster.core.tc.Task;

/**
 * TimerProcessor
 * @author Zhangyf
 *
 */
public abstract class TimerProcessor extends AbstractProcessor<Object> {
	
	public final List<Task> masterProcess(Object obj) {
		return masterProcess();
	}
	
	public abstract List<Task> masterProcess();
}
