package test.ejb.hello;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Stateless;


@Stateless(name="HelloSLSBLocalImpl", mappedName="hlli")
@Local(Hello.class)
public class HelloSLSBLocalImpl extends HelloImpl {
	
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
