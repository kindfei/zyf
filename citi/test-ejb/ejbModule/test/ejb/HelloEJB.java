package test.ejb;

import javax.ejb.Stateless;

/**
 * Session Bean implementation class HelloEJB
 */
@Stateless(mappedName = "b1")
public class HelloEJB implements HelloEJBRemote, HelloEJBLocal {

    /**
     * Default constructor. 
     */
    public HelloEJB() {
        // TODO Auto-generated constructor stub
    }
    
    private int count;

	@Override
	public String sayHello(String name) {
		return "Hello " + name + " " + count++;
	}

}
