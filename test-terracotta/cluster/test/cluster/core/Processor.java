package test.cluster.core;

import java.util.List;

public interface Processor<T> {

	public abstract List<Task> masterProcess(T t);

	public abstract void workerProcess(Task task);

}