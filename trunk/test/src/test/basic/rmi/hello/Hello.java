package test.basic.rmi.hello;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Hello extends Remote {
	String sayHello() throws RemoteException;
	Set getSet() throws RemoteException;
}
