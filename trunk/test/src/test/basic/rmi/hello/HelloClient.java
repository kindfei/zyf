package test.basic.rmi.hello;

import java.rmi.Naming;

public class HelloClient {
	
	public static void main(String[] args) {
		try {
			Hello remoteObj = (Hello) Naming.lookup("//localhost:1099/HelloServer");
			System.out.println(remoteObj.sayHello());
			
			HelloServer.command(remoteObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
