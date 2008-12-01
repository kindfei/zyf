package test.basic.rmi.product;

import java.rmi.*;
import java.rmi.server.*;

public class ProductImpl extends UnicastRemoteObject implements Product {
	
	private static final long serialVersionUID = -410449379119388050L;
	
	private String name;

	public ProductImpl(String n) throws RemoteException {
		name = n;
	}

	public String getDescription() throws RemoteException {
		return "I am a " + name + ". Buy me!";
	}
}
