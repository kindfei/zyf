package jp.emcom.adv.n225.core.base.service;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jp.emcom.adv.common.messaging.MessageHandler;
import jp.emcom.adv.common.messaging.MessagingException;
import jp.emcom.adv.common.messaging.Messenger;
import jp.emcom.adv.common.messaging.Publisher;
import jp.emcom.adv.common.messaging.RepliableMessage;
import jp.emcom.adv.common.messaging.Subscriber;
import jp.emcom.adv.n225.core.utils.messaging.Destinations;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author zhangyf
 *
 */
public class MessagingAdapter extends ProxyFactoryBean implements InitializingBean,
		DisposableBean, MethodInterceptor, MessageHandler<InvocationWrapper> {
	
	private final static Logger log = LoggerFactory.getLogger(MessagingAdapter.class);
	
	/* common config */
	private Messenger.Factory factory; // required. inject for one connection per-app
	private Destinations destination;
	private boolean isHost; // default false

	/* client config */
	private String expression; // optional
	private boolean isSyncRun; // default false. depend on service method return type and destination definition
	
	/* host config */
	private int corePoolSize; // optional
	private int maximumPoolSize; // optional
	private ThreadPoolExecutor executor; // optional

	/* created by init */
	private Publisher<InvocationWrapper> publisher;
	private Subscriber<InvocationWrapper> subscriber;
	
	/*
	 * inject properties
	 */

	public void setFactory(Messenger.Factory factory) {
		this.factory = factory;
	}

	public void setDestination(Destinations destination) {
		this.destination = destination;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setSyncRun(boolean isSyncRun) {
		this.isSyncRun = isSyncRun;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public void setExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
	}

	/**
	 * InitializingBean implementation
	 */
	public void afterPropertiesSet() throws Exception {
		initProxyFactoryBean();
		initMessaging();
	}

	/**
	 * Initialize ProxyFactoryBean
	 */
	private void initProxyFactoryBean() {
		AspectJExpressionPointcut pointcut = null;
		if (expression != null) {
			pointcut = new AspectJExpressionPointcut();
			pointcut.setExpression(expression);
		}

		DefaultPointcutAdvisor advisor = null;
		if (pointcut != null) {
			advisor = new DefaultPointcutAdvisor(pointcut, this);
		} else {
			advisor = new DefaultPointcutAdvisor(this);
		}
		
		this.addAdvisor(advisor);
	}

	/**
	 * Initialize messaging
	 */
	private void initMessaging() {
		// init executor
		if (isHost) {
			if (executor == null && corePoolSize != 0 && maximumPoolSize != 0) {
				executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
		                60L, TimeUnit.SECONDS,
		                new SynchronousQueue<Runnable>());
			}
		}
		
		// init publisher
		publisher = factory.getPublisher(destination.getDestination());
		
		// init subscriber
		if (isHost) {
			Subscriber<InvocationWrapper> subscriber = factory.getSubscriber(destination.getDestination());
			
			subscriber.addMessageHandler(this);
		}
	}

	/**
	 * DisposableBean implementation
	 */
	public void destroy() throws Exception {
		if (publisher != null) publisher.stop(0);
		if (subscriber != null) subscriber.stop(0);
		if (executor != null) executor.shutdown();
	}

	/**
	 * MethodInterceptor implementation
	 */
	public Object invoke(MethodInvocation mi) throws Throwable {
		
		InvocationWrapper msg = new InvocationWrapper(mi);
		
		publisher.send(msg);
		
		if (isSyncRun) {
			// TODO wait for reply result
			
			// TODO return result
			return null;
		} else {
			
			// Should be void method proxy.
			return null;
		}
	}

	/**
	 * MessageHandler implementation
	 */
	public void onMessage(final InvocationWrapper invoker) {
		if (executor != null) {
			executor.submit(new Runnable() {
				public void run() {
					invoke(invoker);
				}
			});
		} else {
			invoke(invoker);
		}
	}

	@SuppressWarnings("unchecked")
	private void invoke(InvocationWrapper invoker) {
		try {
			Object result = invoker.invoke(this.getTargetSource().getTarget());
			
			if (result != null && invoker instanceof RepliableMessage) {
				// send back the result
				RepliableMessage<Object> replier = (RepliableMessage<Object>) invoker;
				replier.reply(result);
			}
			
		} catch (Throwable e) {
			log.error("Service invoke error. targetClass=" + this.getTargetClass() + ", methodName" + invoker.getMethodName(), e);
		}
	}

	/**
	 * MessageHandler implementation
	 */
	public void onException(MessagingException e) {
		log.error("Service host listen message error. targetClass=" + this.getTargetClass(), e);
	}
}

/**
 * Invocation Wrapper
 * @author zhangyf
 *
 */
class InvocationWrapper implements Externalizable {
	private String methodName;
	private Class<?>[] parameterTypes;
	private Object[] arguments;

	public InvocationWrapper(MethodInvocation mi) {
		Method method = mi.getMethod();
		
		this.methodName = method.getName();
		this.parameterTypes = method.getParameterTypes();
		this.arguments = mi.getArguments();
	}

	public Object invoke(Object target) throws Exception {
		Method method = target.getClass().getMethod(methodName, parameterTypes);
		return method.invoke(target, arguments);
	}
	
	public String getMethodName() {
		return methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public Object[] getArguments() {
		return arguments;
	}
	
	/*
	 * Externalizable
	 */

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(methodName);
		out.writeObject(parameterTypes);
		out.writeObject(arguments);
	}
	
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		methodName = (String) in.readObject();
		parameterTypes = (Class<?>[]) in.readObject();
		arguments = (Object[]) in.readObject();
	}
}
