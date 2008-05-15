package test.basic.rmi.hello;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

public class HelloImpl extends UnicastRemoteObject implements Hello {

	private static final long serialVersionUID = 8193635804361276764L;

	public HelloImpl() throws RemoteException {
		super();
	}

	public String sayHello() {
		return "Hello World!";
	}
	
	private Set set = new HashSet();
	
	public Set getSet() {
		return set;
	}
}
