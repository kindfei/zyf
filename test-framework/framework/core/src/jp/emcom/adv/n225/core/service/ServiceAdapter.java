package jp.emcom.adv.n225.core.service;

public interface ServiceAdapter {
	Object runSync(Object[] args) throws Throwable;
	Object runAsync(Object[] args) throws Throwable;
}
