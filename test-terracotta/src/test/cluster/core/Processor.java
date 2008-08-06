package test.cluster.core;

import java.util.List;

public interface Processor {

	public abstract List<?> masterProcess(Object obj);

	public abstract void workerProcess(Object task);

}
