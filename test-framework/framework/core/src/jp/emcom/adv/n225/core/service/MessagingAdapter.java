package jp.emcom.adv.n225.core.service;

import java.io.Serializable;
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
import org.springframework.aop.MethodMatcher;
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
public class MessagingAdapter extends ProxyFactoryBean implements ServiceAdapter, InitializingBean,
		DisposableBean, MethodInterceptor, MessageHandler<Serializable> {
	
	private final static Logger log = LoggerFactory.getLogger(MessagingAdapter.class);

	private String expression;
	private Destination destination;
	private boolean isSyncRun;
	private boolean isHost;
	
	private MethodMatcher matcher;

	private JmsPublisherTransport<Serializable> publisher;
	private JmsSubscriberTransport<Serializable> subscriber;

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
		publisher = new JmsPublisherTransport<Serializable>(dest);
		if (isHost) {
			subscriber = new JmsSubscriberTransport<Serializable>(dest);
			subscriber.addMessageHandler(this);
		}
	}

	private void initProxyFactoryBean() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(expression);
		matcher = pointcut.getMethodMatcher();

		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, this);
		this.addAdvisor(advisor);
		
		this.setProxyTargetClass(true);
	}

	public void destroy() throws Exception {
		if (publisher != null) publisher.disconnect();
		if (subscriber != null) subscriber.disconnect();
	}

	public Object invoke(MethodInvocation mi) throws Throwable {
		Object[] args = mi.getArguments();

		if (isSyncRun) {
			return runSync(args);
		} else {
			return runAsync(args);
		}
	}

	public Object runSync(Object[] args) throws Throwable {

		publisher.send(args);

		// TODO wait for reply result

		return null; // TODO return result
	}

	public Object runAsync(Object[] args) throws Throwable {

		publisher.send(args);
		
		return null;
	}

	public void onMessage(Serializable msg) {
		
		Class<?> cls = this.getTargetClass();
		
		Method[] methods = cls.getDeclaredMethods();
		
		Method targetMethod = null;
		
		for (Method method : methods) {
			if (matcher.matches(method, cls)) {
				targetMethod = method;
				break;
			}
		}
		
		try {
			Object result = targetMethod.invoke(this.getTargetSource().getTarget(), (Object[]) msg);
			
			//TODO send back the result.
			
		} catch (Exception e) {
			log.error("Service invoke error. expression=" + expression, e);
		}
	}

	public void onException(MessagingException e) {
		log.error("Service host listen message error. expression=" + expression, e);
	}

}
