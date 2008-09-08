package fx.cluster.core;

import java.util.List;


/**
 * TimerProcessor
 * @author zhangyf
 *
 */
public abstract class TimerProcessor extends AbstractProcessor<Object> {
	
	public final List<Task> masterProcess(Object obj) {
		return masterProcess();
	}
	
	public abstract List<Task> masterProcess();
}
