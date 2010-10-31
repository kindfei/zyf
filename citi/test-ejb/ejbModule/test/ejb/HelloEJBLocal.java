package test.ejb;
import javax.ejb.Local;

@Local
public interface HelloEJBLocal {
	String sayHello(String name);
}
