package test.ejb.hello.spring.ejb2;

import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatefulSessionBean;

import test.ejb.hello.HelloSFSBLocal;
import test.ejb.hello.HelloSFSBRemote;
import test.service.HelloService;

@Stateful(name="spring.ejb2.HelloSFSB", mappedName="s2hf")
public class HelloSFSB extends AbstractStatefulSessionBean implements HelloSFSBRemote, HelloSFSBLocal {
	
	private static final long serialVersionUID = 1L;
	
	private HelloService helloService;

	@Override
	public String sayHello(String name) {
		return helloService.sayHello(this.toString(), name);
	}
	
	@PostConstruct
	public void ejbCreate() {
		loadBeanFactory();
		helloService = (HelloService) getBeanFactory().getBean("helloService");
	}

	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		loadBeanFactory();
		helloService = (HelloService) getBeanFactory().getBean("helloService");
	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		unloadBeanFactory();
		setBeanFactoryLocator(null);
	}

	@Override
	public void setSessionContext(SessionContext sessionContext) {
		super.setSessionContext(sessionContext);
		setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
		setBeanFactoryLocatorKey("businessBeanFactory");
	}

}
