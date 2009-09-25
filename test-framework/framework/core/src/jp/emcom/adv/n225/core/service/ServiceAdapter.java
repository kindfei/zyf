package jp.emcom.adv.n225.core.service;

import java.lang.reflect.Method;

public interface ServiceAdapter {
	Object runSync(Method method, Object[] args) throws Throwable;
	Object runAsync(Method method, Object[] args) throws Throwable;
}
