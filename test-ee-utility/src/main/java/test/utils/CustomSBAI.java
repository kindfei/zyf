package test.utils;

import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

public class CustomSBAI extends SpringBeanAutowiringInterceptor {
	@Override
	protected BeanFactoryLocator getBeanFactoryLocator(Object target) {
		return ContextSingletonBeanFactoryLocator.getInstance();
	}
	@Override
	protected String getBeanFactoryLocatorKey(Object target) {
		return "businessBeanFactory";
	}
}
