package test.basic.rmi.product;

import java.rmi.*;
import javax.naming.*;

public class ProductClient {
	public static void main(String[] args) {
		System.setProperty("java.security.policy", "E:/eclipse/workspace-zyf/test/src/test/basic/rmi/product/client.policy");
		System.setSecurityManager(new RMISecurityManager());
		String url = "rmi://localhost/";
		
		try {
			Context namingContext = new InitialContext();
			Product c1 = (Product) namingContext.lookup(url + "toaster");
			Product c2 = (Product) namingContext.lookup(url + "microwave");

			System.out.println(c1.getDescription());
			System.out.println(c2.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
