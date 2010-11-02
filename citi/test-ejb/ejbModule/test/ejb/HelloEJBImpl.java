package test.ejb;

public class HelloEJBImpl implements HelloEJB {
    
    private int count;

	@Override
	public String sayHello(String name) {
		return "Hello " + name + " " + count++;
	}

}
