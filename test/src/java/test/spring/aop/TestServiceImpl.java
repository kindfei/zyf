package test.spring.aop;

public class TestServiceImpl implements TestService {

	@Override
	public String sayHello(String name) {
		if (name == null) {
			throw new IllegalArgumentException("The name can't be null");
		}
		
		return "Hello " + name;
	}
	
}
