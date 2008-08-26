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
	 * The <tt>MessageReceiver</tt> will creates new threads as needed to execute 
	 * <tt>masterProcess</tt> when it got a message from JMS receiver.
	 * <p>There is no <tt>TaskTaker</tt> to take task from task queue. So can not
	 * add task to the task queue.
	 * 
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
	 * The fixed number of threads(<tt>MessageReceiver</tt>) was created to receive
	 * message from the JMS receiver and execute <tt>masterProcess</tt> with the message.
	 * <p>There is no <tt>TaskTaker</tt> to take task from task queue. So can not add
	 * task to the task queue.
	 * 
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param processor the business implementation
	 * @param destName the destination name of JMS for receive message
	 * @param receiverSize how many threads to receive and process the message
	 * @return the service
	 */
	public synchronized static Service getMessageDrivenServiceWithoutWorker(ServiceMode serviceMode
			, MainMessageProcessor processor, String destName, int receiverSize) {
		return getMessageDrivenService(serviceMode, 0, true, processor, destName, receiverSize, true);
	}
	
	/**
	 * The <tt>MessageReceiver</tt> will creates new threads as needed to execute 
	 * <tt>masterProcess</tt> when it got a message from JMS receiver.
	 * <p>The <tt>TaskTaker</tt> will creates new threads as needed to execute
	 * <tt>workerProcess</tt> when it got a task from task queue.
	 * 
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
	 * The <tt>MessageReceiver</tt> will creates new threads as needed to execute
	 * <tt>masterProcess</tt> when it got a message from JMS receiver.
	 * <p>The fixed number of threads(<tt>TaskTaker</tt>) was created to take task
	 * from the task queue and execute <tt>workerProcess</tt> with the task.
	 * 
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param takerSize how many threads to take and process the task
	 * @param processor the business implementation
	 * @param destName the destination name of JMS for receive message
	 * @return the service
	 */
	public synchronized static Service getMessageDrivenService(ServiceMode serviceMode
			, int takerSize, MessageProcessor processor, String destName) {
		return getMessageDrivenService(serviceMode, takerSize, true, processor, destName, 1, false);
	}
	
	/**
	 * The fixed number of threads(<tt>MessageReceiver</tt>) was created to receive
	 * message from the JMS receiver and execute <tt>masterProcess</tt> with the message.
	 * <p>The <tt>TaskTaker</tt> will creates new threads as needed to execute
	 * <tt>workerProcess</tt> when it got a task from task queue.
	 * 
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param processor the business implementation
	 * @param destName the destination name of JMS for receive message
	 * @param receiverSize how many threads to receive and process the message
	 * @return the service
	 */
	public synchronized static Service getMessageDrivenService(ServiceMode serviceMode
			, MessageProcessor processor, String destName, int receiverSize) {
		return getMessageDrivenService(serviceMode, 1, false, processor, destName, receiverSize, true);
	}
	
	/**
	 * The fixed number of threads(<tt>MessageReceiver</tt>) was created to receive
	 * message from the JMS receiver and execute <tt>masterProcess</tt> with the message.
	 * <p>The fixed number of threads(<tt>TaskTaker</tt>) was created to take task
	 * from the task queue and execute <tt>workerProcess</tt> with the task.
	 * 
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param takerSize how many threads to take and process the task
	 * @param processor the business implementation
	 * @param destName the destination name of JMS for receive message
	 * @param receiverSize how many threads to receive and process the message
	 * @return the service
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
	 * <p>The <tt>TaskTaker</tt> will creates new threads as needed to execute
	 * <tt>workerProcess</tt> when it got a task from task queue.
	 * 
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param processor the business implementation
	 * @param period time in milliseconds between successive <tt>masterProcess</tt> executions
	 * @return the service
	 */
	public synchronized static Service getTimerDrivenService(ServiceMode serviceMode
			, TimerProcessor processor, int period) {
		return getTimerDrivenService(serviceMode, 1, false, processor, period);
	}
	
	/**
	 * <p>The fixed number of threads(<tt>TaskTaker</tt>) was created to take task
	 * from the task queue and execute <tt>workerProcess</tt> with the task.
	 * 
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param takerSize how many threads to take and process the task
	 * @param processor the business implementation
	 * @param period time in milliseconds between successive <tt>masterProcess</tt> executions
	 * @return the service
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
	 * <p>The <tt>TaskTaker</tt> will creates new threads as needed to execute
	 * <tt>workerProcess</tt> when it got a task from task queue.
	 * 
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param processor the business implementation
	 * @param propName quartz properties file name
	 * @return the service
	 */
	public synchronized static Service getQuartzDrivenService(ServiceMode serviceMode
			, QuartzProcessor processor, String propName) {
		return getQuartzDrivenService(serviceMode, 1, false, processor, propName);
	}
	
	/**
	 * <p>The fixed number of threads(<tt>TaskTaker</tt>) was created to take task
	 * from the task queue and execute <tt>workerProcess</tt> with the task.
	 * 
	 * @param serviceMode ServiceMode.ACTIVE_STANDBY or ServiceMode.ALL_ACTIVE
	 * @param takerSize how many threads to take and process the task
	 * @param processor the business implementation
	 * @param propName quartz properties file name
	 * @return the service
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
