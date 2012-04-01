package test.service;

public class HelloServiceImpl implements HelloService {
    private int count;

	public String sayHello(String str, String name) {
		return str + ": Hello " + name + " " + count++;
	}
}
