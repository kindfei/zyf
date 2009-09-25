package jp.emcom.adv.n225.core.service;

import java.io.Serializable;
import java.lang.reflect.Method;

import jp.emcom.adv.n225.test.messaging.Destination;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class MessagingAdapter extends ProxyFactoryBean implements ServiceAdapter, InitializingBean,
		DisposableBean, MethodInterceptor {

	private Destination destination;

	private boolean isSyncRun;

	private String expression;

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public void setSyncRun(boolean isSyncRun) {
		this.isSyncRun = isSyncRun;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void afterPropertiesSet() throws Exception {
		initProxyFactoryBean();

		// TODO start message receiver
	}

	public void destroy() throws Exception {
		// TODO stop message receiver
	}

	public void onMessage(Serializable msg) {
		
		this.getObject();
	}

	private void initProxyFactoryBean() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(expression);

		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, this);
		this.addAdvisor(advisor);
		
		this.setProxyTargetClass(true);
	}

	public Object runSync(Method method, Object[] args) throws Throwable {

		// TODO send message in order

		// TODO wait for reply result

		return null; // TODO return result
	}

	public Object runAsync(Method method, Object[] args) throws Throwable {

		// TODO send message in order

		return null;
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object[] args = mi.getArguments();
		
		Method method = mi.getMethod();

		System.out.println("succeed!!!!!!!!!!!!!!!!!");

		if (isSyncRun) {
			return runSync(method, args);
		} else {
			return runAsync(method, args);
		}
	}

}
