package test.client;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import test.ejb.HelloEJBRemote;

public class TestEJBClient {
	
	public static void main(String[] args) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, "t3://localhost:7001");
		
		Context c = new InitialContext(env);
		
		HelloEJBRemote helloEJBRemote = (HelloEJBRemote) c.lookup("HelloEJBRemoteImpl#test.ejb.HelloEJBRemote");
		System.out.println(helloEJBRemote.sayHello("HelloEJBRemote"));
	}
}
