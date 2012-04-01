package test.ejb.hello;


public class HelloImpl implements Hello {

	private int count;

	@Override
	public String sayHello(String name) {
		return this.toString() + ": Hello " + name + " " + count++;
	}

}
