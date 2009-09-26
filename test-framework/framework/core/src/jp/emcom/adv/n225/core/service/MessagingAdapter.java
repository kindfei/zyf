package jp.emcom.adv.n225.core.service;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;

import jp.emcom.adv.common.messaging.MessageHandler;
import jp.emcom.adv.common.messaging.MessagingException;
import jp.emcom.adv.common.messaging.Destination.Domain;
import jp.emcom.adv.common.messaging.impl.jms.JmsDestination;
import jp.emcom.adv.common.messaging.impl.jms.JmsProvider;
import jp.emcom.adv.common.messaging.impl.jms.transport.JmsPublisherTransport;
import jp.emcom.adv.common.messaging.impl.jms.transport.JmsSubscriberTransport;
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
	private Destination destination;
	private boolean isSyncRun;
	private boolean isHost;

	private JmsPublisherTransport<InvocationWrapper> publisher;
	private JmsSubscriberTransport<InvocationWrapper> subscriber;

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public void setSyncRun(boolean isSyncRun) {
		this.isSyncRun = isSyncRun;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public void afterPropertiesSet() throws Exception {
		initProxyFactoryBean();

		JmsDestination dest = new JmsDestination(destination.toString(), Domain.queue, new JmsProvider());
		publisher = new JmsPublisherTransport<InvocationWrapper>(dest);
		if (isHost) {
			subscriber = new JmsSubscriberTransport<InvocationWrapper>(dest);
			subscriber.addMessageHandler(this);
		}
	}

	private void initProxyFactoryBean() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(expression);

		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, this);
		this.addAdvisor(advisor);
		
		this.setProxyTargetClass(true);
	}

	public void destroy() throws Exception {
		if (publisher != null) publisher.disconnect();
		if (subscriber != null) subscriber.disconnect();
	}

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
