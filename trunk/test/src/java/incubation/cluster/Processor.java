package incubation.cluster;

import java.util.List;

/**
 * Processor
 * @author zhangyf
 *
 * @param <T>
 */
public interface Processor<T> {

	public abstract List<ClusterTask> masterProcess(T t);

	public abstract void workerProcess(ClusterTask task);

}