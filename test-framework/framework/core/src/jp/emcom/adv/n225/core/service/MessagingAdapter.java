package jp.emcom.adv.n225.core.service;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;

import jp.emcom.adv.common.messaging.MessageHandler;
import jp.emcom.adv.common.messaging.MessagingException;
import jp.emcom.adv.common.messaging.Publisher;
import jp.emcom.adv.common.messaging.Subscriber;
import jp.emcom.adv.common.messaging.Destination.Domain;
import jp.emcom.adv.common.messaging.impl.DefaultMessengerFactory;
import jp.emcom.adv.common.messaging.impl.jms.JmsDestination;
import jp.emcom.adv.common.messaging.impl.jms.JmsProvider;
import jp.emcom.adv.n225.test.messaging.Destination;

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

	private String expression;
	private DefaultMessengerFactory factory;
	private Destination destination;
	private Domain domain;
	private Domain replyDomain;
	private boolean isSyncRun;
	private boolean isHost;

	private Publisher<InvocationWrapper> publisher;
	private Subscriber<InvocationWrapper> subscriber;

	/*
	 * inject properties
	 */

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public void setFactory(DefaultMessengerFactory factory) {
		this.factory = factory;
	}
	
	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public void setReplyDomain(Domain replyDomain) {
		this.replyDomain = replyDomain;
	}

	public void setSyncRun(boolean isSyncRun) {
		this.isSyncRun = isSyncRun;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
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
		
		this.setProxyTargetClass(true);
	}

	/**
	 * Initialize messaging
	 */
	private void initMessaging() {
		JmsDestination dest = new JmsDestination(destination.toString(), domain, new JmsProvider(), replyDomain);
		
		publisher = factory.getPublisher(dest);
		if (isHost) {
			subscriber = factory.getSubscriber(dest);
			subscriber.addMessageHandler(this);
		}
	}

	/**
	 * DisposableBean implementation
	 */
	public void destroy() throws Exception {
		if (publisher != null) publisher.stop(0);
		if (subscriber != null) subscriber.stop(0);
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
			
			//TODO return send OK
			return null;
		}
	}

	/**
	 * MessageHandler implementation
	 */
	public void onMessage(InvocationWrapper invoker) {
		try {
			Object result = invoker.invoke(this.getTargetSource().getTarget());
			
			//TODO send back the result.
			
		} catch (Exception e) {
			log.error("Service invoke error. expression=" + expression, e);
		}
	}

	public void onException(MessagingException e) {
		log.error("Service host listen message error. expression=" + expression, e);
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
	
	/**
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
