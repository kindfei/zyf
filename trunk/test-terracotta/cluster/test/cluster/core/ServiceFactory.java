package test.cluster.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactory {
	private static Map<String, AbstractService<?>> servMap = new ConcurrentHashMap<String, AbstractService<?>>();
	
	public static Service getMessageDrivenService(ServiceMode serviceMode, int executorSize, MessageProcessor processor, String destName) {
		MessageDrivenService service = new MessageDrivenService(serviceMode, executorSize, processor, destName);
		String procName = processor.getClass().getName();
		servMap.put(procName, service);
		return service;
	}

	public static Service getTimerDrivenService(ServiceMode serviceMode, int executorSize, TimerProcessor processor, int period) {
		TimerDrivenService service = new TimerDrivenService(serviceMode, executorSize, processor, period);
		String procName = processor.getClass().getName();
		servMap.put(procName, service);
		return service;
	}

	public static Service getQuartzDrivenService(ServiceMode serviceMode, int excutorSize, QuartzProcessor processor, String propName) {
		QuartzDrivenService service = new QuartzDrivenService(serviceMode, excutorSize, processor, propName);
		String procName = processor.getClass().getName();
		servMap.put(procName, service);
		return service;
	}
	
	public static Processor<?> getProcessor(String name) {
		return servMap.get(name).getProcessor();
	}
	
	public static AbstractService<?> getService(String name) {
		return servMap.get(name);
	}
}
