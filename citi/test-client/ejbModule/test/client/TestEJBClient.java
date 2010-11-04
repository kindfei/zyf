package test.client;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import test.ejb.HelloEJB;

public class TestEJBClient {
	
	public static void main(String[] args) throws NamingException {
		InitialContext c = null;
		System.out.println("====================t3====================");
		c = createContext("t3");
		invoke(c, "HelloStateless#test.ejb.HelloStatelessRemote", 2);
		invoke(c, "HelloStateless#test.ejb.HelloStatelessRemote", 2);
		invoke(c, "HelloStateful#test.ejb.HelloStatefulRemote", 2);
		invoke(c, "HelloStateful#test.ejb.HelloStatefulRemote", 2);
		System.out.println("====================iiop====================");
		c = createContext("iiop");
		invoke(c, "HelloStateless#test.ejb.HelloStatelessRemote", 2);
		invoke(c, "HelloStateless#test.ejb.HelloStatelessRemote", 2);
		invoke(c, "HelloStateful#test.ejb.HelloStatefulRemote", 2);
		invoke(c, "HelloStateful#test.ejb.HelloStatefulRemote", 2);
	}
	
	static InitialContext createContext(String protocol) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, protocol + "://localhost:7001");
		
		return new InitialContext(env);
	}
	
	static void invoke(Context c, String jndiName, int times) throws NamingException {
		HelloEJB helloEJB = (HelloEJB) c.lookup(jndiName);
		for (int i = 0; i < times; i++) {
			System.out.println(helloEJB.sayHello(helloEJB.getClass().getName()));
		}
	}
}
