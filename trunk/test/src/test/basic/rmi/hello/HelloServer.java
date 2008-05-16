package test.basic.rmi.hello;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Set;

import zyf.helper.CmdHelper;

public class HelloServer {
	
	public static void main(String args[]) {
//		System.setProperty("java.rmi.server.codebase", "file:///E:/eclipse/workspace-zyf/test/bin/");
		System.setProperty("java.security.policy", "E:/eclipse/workspace-zyf/test/src/test/basic/rmi/hello/policy");

		// Create and install a security manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			HelloImpl obj = new HelloImpl();
			
			LocateRegistry.createRegistry(1099);
			
			// Bind this object instance to the name "HelloServer"
			Naming.rebind("//localhost:1099/HelloServer", obj);

			System.out.println("HelloServer bound in registry");
			
			command(obj);
			
			try {
				Naming.unbind("//localhost:1099/HelloServer");
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("HelloImpl err: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void command(Hello obj) throws RemoteException {
		a:while (true) {
			int opt = CmdHelper.options(new String[] {"Quit", "Print set", "Get and set value", "Set value"});
			Set set = null;
			
			switch (opt) {	
			case 0:
				break a;

			case 1:
				set = obj.getSet();
				System.out.println(set.hashCode() + " - " +set);
				break;
				
			case 2:
				set = obj.getSet();
				set.add(CmdHelper.input("Input value"));
				break;

			case 3:
				set = obj.getSet();
				obj.setValue(CmdHelper.input("Input value"));
				break;

			default:
				break;
			}
		}
	}
}
