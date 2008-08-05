package test.cluster.core;

import java.util.List;

public interface Master {

	public abstract List<? extends Object> process(Object obj);

}
