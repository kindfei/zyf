package test.ejb.hello.spring.ejb2;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import test.ejb.hello.HelloSLSBLocal;
import test.ejb.hello.HelloSLSBRemote;
import test.service.HelloService;

@Stateless(name="spring.ejb2.HelloSLSB", mappedName="s2hl")
public class HelloSLSB extends AbstractStatelessSessionBean implements HelloSLSBRemote, HelloSLSBLocal {

	private static final long serialVersionUID = 1L;
	
	private HelloService helloService;

	@Override
	public String sayHello(String name) {
		return helloService.sayHello(this.toString(), name);
	}

	@Override
	protected void onEjbCreate() throws CreateException {
		helloService = (HelloService) getBeanFactory().getBean("helloService");
	}

	@Override
	public void setSessionContext(SessionContext sessionContext) {
		super.setSessionContext(sessionContext);
		setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
		setBeanFactoryLocatorKey("businessBeanFactory");
	}
}
