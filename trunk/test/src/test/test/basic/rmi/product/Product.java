package test.basic.rmi.product;

import java.rmi.*;

public interface Product extends Remote {
	String getDescription() throws RemoteException;
}
