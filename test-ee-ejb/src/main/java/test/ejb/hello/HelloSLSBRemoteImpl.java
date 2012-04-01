package test.ejb.hello;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remote;
import javax.ejb.Stateless;


@Stateless(name="HelloSLSBRemoteImpl", mappedName="hlri")
@Remote(Hello.class)
public class HelloSLSBRemoteImpl extends HelloImpl {

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
