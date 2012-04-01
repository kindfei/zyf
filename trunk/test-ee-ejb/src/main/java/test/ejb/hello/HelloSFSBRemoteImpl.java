package test.ejb.hello;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;


@Stateful(name="HelloSFSBRemoteImpl", mappedName="hfri")
@Remote(Hello.class)
public class HelloSFSBRemoteImpl {
	
	@EJB(beanName="HelloSFSBLocalImpl")
	private Hello local;
	
	@Remove
	public String sayHello(String name) {
		return local.sayHello(name);
	}
	
	@PostConstruct
	public void postConstruct() {
		System.out.println(this.getClass().getName() + " postConstruct.....");
	}
	
	@PreDestroy
	public void preDestroy() {
		System.out.println(this.getClass().getName() + " preDestroy.....");
	}
	
	@PostActivate
	public void postActivate() {
		System.out.println(this.getClass().getName() + " postActivate.....");
	}
	
	@PrePassivate
	public void prePassivate() {
		System.out.println(this.getClass().getName() + " prePassivate.....");
	}
}
