package jp.emcom.adv.n225.core.service;

import java.lang.annotation.Inherited;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class MessagingDispatcher implements InitializingBean, DisposableBean {
	
	public Object dispatch(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("around: name=" + pjp.getArgs()[0]);
		
		return pjp.proceed();
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
