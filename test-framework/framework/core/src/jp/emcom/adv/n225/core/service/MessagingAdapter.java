package jp.emcom.adv.n225.core.service;

import java.io.Serializable;

import jp.emcom.adv.n225.test.messaging.Destination;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MessagingAdapter implements InitializingBean, DisposableBean, ApplicationContextAware, ServiceAdapter {
	
	private Destination destination;
	
	private ApplicationContext context;
	
	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO start message receiver
	}

	public void destroy() throws Exception {
		// TODO stop message receiver
	}
	
	public void onMessage(Serializable msg) {
		// TODO 
	}

	public Object runSync(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		
		// TODO send message in order
		
		// TODO wait for reply result
		
		return null; // TODO return result
	}

	public Object runAsync(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		
		// TODO send message in order
		
		return null;
	}
}
