package test.basic.basic.rmi.product;

import incubation.utils.CmdHelper;

import java.rmi.registry.LocateRegistry;

import javax.naming.Context;
import javax.naming.InitialContext;





public class ProductServer {
	public static void main(String args[]) {
		try {
			System.out.println("Constructing server implementations...");

			ProductImpl p1 = new ProductImpl("Blackwell Toaster");
			ProductImpl p2 = new ProductImpl("ZapXpress Microwave Oven");
			
			System.out.println("Binding server implementations to registry...");
			LocateRegistry.createRegistry(1099);
			Context namingContext = new InitialContext();
			namingContext.bind("rmi:toaster", p1);
			namingContext.bind("rmi:microwave", p2);
			System.out.println("Waiting for invocations from clients...");
			
			CmdHelper.pause("Stop Service");
			
			namingContext.unbind("rmi:toaster");
			namingContext.unbind("rmi:microwave");
			namingContext.close();
			System.exit(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
