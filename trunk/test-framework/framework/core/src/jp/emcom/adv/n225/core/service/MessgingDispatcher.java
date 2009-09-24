package jp.emcom.adv.n225.core.service;

import org.aspectj.lang.ProceedingJoinPoint;

public class MessgingDispatcher {
	public void dispatch(ProceedingJoinPoint pjp, String name) {
		System.out.println("around: name=" + name);
	}
}
