package test.cluster.core;

import java.util.HashMap;
import java.util.Map;

/**
 * ServiceFactory
 * @author Zhangyf
 *
 */
public class ServiceFactory {
	
	private static Map<String, AbstractService<?>> servMap = new HashMap<String, AbstractService<?>>();
	
	/**
	 * The service is no <code>TaskTaker</code> to take the task, so you
	 * can not add task to the task queue. A new thread will be created
	 * to process when the JMS receiver got a message. And the amount of
	 * the thread is no limit to create. In other words, there is no worker,
	 * just a lot of master in there.
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param processor the business implementation
	 * @param destName the destination name of JMS for receive message
	 * @return the service
	 */
	public synchronized static Service getMessageDrivenServiceWithoutWorker(ServiceMode serviceMode
			, MainMessageProcessor processor, String destName) {
		return getMessageDrivenService(serviceMode, 0, true, processor, destName, 1, false);
	}
	
	/**
	 * The service is no <code>TaskTaker</code> to take the task, so you
	 * can not add task to the task queue. Fixed number of threads will be created
	 * when the service been created for receive the message, and the receive
	 * thread also process the message when it was gotten.
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param processor the business implementation
	 * @param destName the destination name of JMS for receive message
	 * @param receiverSize how many receiver(thread) to receive the message
	 * @return the service
	 */
	public synchronized static Service getMessageDrivenServiceWithoutWorker(ServiceMode serviceMode
			, MainMessageProcessor processor, String destName, int receiverSize) {
		return getMessageDrivenService(serviceMode, 0, true, processor, destName, receiverSize, true);
	}
	
	/**
	 * A new thread will be created to process the task when the <code>TaskTaker</code>
	 * take a task from the task queue. And the number of the thread is no limit. The
	 * worker process is the same as master process. A new thread will be created
	 * to process when the JMS receiver got a message. And the amount of
	 * the thread is no limit to create. In other words, there is lots of worker
	 * and also lots of master.
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param processor the business implementation
	 * @param destName the destination name of JMS for receive message
	 * @return the service
	 */
	public synchronized static Service getMessageDrivenService(ServiceMode serviceMode
			, MessageProcessor processor, String destName) {
		return getMessageDrivenService(serviceMode, 1, false, processor, destName, 1, false);
	}
	
	/**
	 * Fixed number of threads
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param takerSize how many <code>TaskTaker</code> to take the task
	 * @param processor the business implementation
	 * @param destName the destination name of JMS for receive message
	 * @return the service
	 */
	public synchronized static Service getMessageDrivenService(ServiceMode serviceMode
			, int takerSize, MessageProcessor processor, String destName) {
		return getMessageDrivenService(serviceMode, takerSize, true, processor, destName, 1, false);
	}
	
	/**
	 * 
	 * @param serviceMode
	 * @param processor
	 * @param destName
	 * @param receiverSize
	 * @return
	 */
	public synchronized static Service getMessageDrivenService(ServiceMode serviceMode
			, MessageProcessor processor, String destName, int receiverSize) {
		return getMessageDrivenService(serviceMode, 1, false, processor, destName, receiverSize, true);
	}
	
	/**
	 * 
	 * @param serviceMode
	 * @param takerSize
	 * @param processor
	 * @param destName
	 * @param receiverSize
	 * @return
	 */
	public synchronized static Service getMessageDrivenService(ServiceMode serviceMode
			, int takerSize, MessageProcessor processor, String destName, int receiverSize) {
		return getMessageDrivenService(serviceMode, takerSize, true, processor, destName, receiverSize, true);
	}
	
	private synchronized static Service getMessageDrivenService(ServiceMode serviceMode, int takerSize, boolean takerExecute
			, MessageProcessor processor, String destName, int receiverSize, boolean receiverExecute) {
		
		String procName = processor.getClass().getName();
		AbstractService<?> service = servMap.get(procName);
		if (service != null) {
			return service;
		}
		
		service = new MessageDrivenService(serviceMode, takerSize, takerExecute, processor, destName, receiverSize, receiverExecute);
		servMap.put(procName, service);
		return service;
	}
	
	/**
	 * 
	 * @param serviceMode
	 * @param processor
	 * @param period
	 * @return
	 */
	public synchronized static Service getTimerDrivenService(ServiceMode serviceMode
			, TimerProcessor processor, int period) {
		return getTimerDrivenService(serviceMode, 1, false, processor, period);
	}
	
	/**
	 * 
	 * @param serviceMode
	 * @param takerSize
	 * @param processor
	 * @param period
	 * @return
	 */
	public synchronized static Service getTimerDrivenService(ServiceMode serviceMode
			, int takerSize, TimerProcessor processor, int period) {
		return getTimerDrivenService(serviceMode, takerSize, true, processor, period);
	}
	
	private synchronized static Service getTimerDrivenService(ServiceMode serviceMode
			, int takerSize, boolean takerExecute, TimerProcessor processor, int period) {
		
		String procName = processor.getClass().getName();
		AbstractService<?> service = servMap.get(procName);
		if (service != null) {
			return service;
		}
		
		service = new TimerDrivenService(serviceMode, takerSize, takerExecute, processor, period);
		servMap.put(procName, service);
		return service;
	}
	
	/**
	 * 
	 * @param serviceMode
	 * @param processor
	 * @param propName
	 * @return
	 */
	public synchronized static Service getQuartzDrivenService(ServiceMode serviceMode
			, QuartzProcessor processor, String propName) {
		return getQuartzDrivenService(serviceMode, 1, false, processor, propName);
	}
	
	/**
	 * 
	 * @param serviceMode
	 * @param takerSize
	 * @param processor
	 * @param propName
	 * @return
	 */
	public synchronized static Service getQuartzDrivenService(ServiceMode serviceMode
			, int takerSize, QuartzProcessor processor, String propName) {
		return getQuartzDrivenService(serviceMode, takerSize, true, processor, propName);
	}

	private synchronized static Service getQuartzDrivenService(ServiceMode serviceMode
			, int takerSize, boolean takerExecute, QuartzProcessor processor, String propName) {
		
		String procName = processor.getClass().getName();
		AbstractService<?> service = servMap.get(procName);
		if (service != null) {
			return service;
		}
		
		service = new QuartzDrivenService(serviceMode, takerSize, takerExecute, processor, propName);
		servMap.put(procName, service);
		return service;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public synchronized static Processor<?> getProcessor(String name) {
		return servMap.get(name).getProcessor();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public synchronized static AbstractService<?> getService(String name) {
		return servMap.get(name);
	}
}
