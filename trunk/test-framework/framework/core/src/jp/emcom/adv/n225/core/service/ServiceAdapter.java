package jp.emcom.adv.n225.core.service;

import org.aspectj.lang.ProceedingJoinPoint;

public interface ServiceAdapter {
	Object runSync(ProceedingJoinPoint pjp) throws Throwable;
	Object runAsync(ProceedingJoinPoint pjp) throws Throwable;
}
