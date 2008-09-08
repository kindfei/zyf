package fx.cluster.core;

import java.util.List;


/**
 * Processor
 * @author zhangyf
 *
 * @param <T>
 */
public interface Processor<T> {

	public abstract List<Task> masterProcess(T t);

	public abstract void workerProcess(Task task);

}