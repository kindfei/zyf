package jp.emcom.adv.n225.core.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import jp.emcom.adv.n225.test.messaging.Destination;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class MessagingAdapter extends ProxyFactoryBean implements ServiceAdapter, InitializingBean, DisposableBean, Pointcut, MethodInterceptor {
	
	private Destination destination;
	
	private boolean isSyncRun;

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public void afterPropertiesSet() throws Exception {
		initProxyFactoryBean();
		
		// TODO start message receiver
	}

	public void destroy() throws Exception {
		// TODO stop message receiver
	}
	
	public void onMessage(Serializable msg) {
		// TODO
	}
	
	private void initProxyFactoryBean() {
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(this, this);
		this.addAdvisor(advisor);
	}

	public Object runSync(Object[] args) throws Throwable {
		
		// TODO send message in order
		
		// TODO wait for reply result
		
		return null; // TODO return result
	}

	public Object runAsync(Object[] args) throws Throwable {
		
		// TODO send message in order
		
		return null;
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object[] args = mi.getArguments();
		
		System.out.println("succeed!!!!!!!!!!!!!!!!!");
		
		if (isSyncRun) {
			return runSync(args);
		} else {
			return runAsync(args);
		}
	}

	@Override
	public ClassFilter getClassFilter() {
		return new ClassFilter() {

			@Override
			public boolean matches(Class arg0) {
				System.out.println("matches(Class arg0): Class=" + arg0.getName());
				return true;
			}
			
		};
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return new MethodMatcher() {

			@Override
			public boolean isRuntime() {
				System.out.println("isRuntime()");
				return true;
			}

			@Override
			public boolean matches(Method arg0, Class arg1) {
				System.out.println("matches(Method arg0, Class arg1): Method=" + arg0.getName() + ", Class=" + arg1.getName());
				return true;
			}

			@Override
			public boolean matches(Method arg0, Class arg1, Object[] arg2) {
				System.out.println("matches(Method arg0, Class arg1, Object[] arg2): Method=" + arg0.getName() + ", Class=" + arg1.getName() + ", Object[]" + Arrays.toString(arg2));
				return false;
			}
			
		};
	}
}
