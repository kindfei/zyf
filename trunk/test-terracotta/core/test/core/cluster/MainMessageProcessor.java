package test.core.cluster;

import java.io.Serializable;
import java.util.List;

/**
 * MainMessageProcessor
 * @author zhangyf
 *
 */
public abstract class MainMessageProcessor extends MessageProcessor {

	public final List<ClusterTask> masterProcess(Serializable msg) {
		mainProcess(msg);
		return null;
	}
	
	public abstract void mainProcess(Serializable msg);

	public final void workerProcess(ClusterTask task) {
	}

}
