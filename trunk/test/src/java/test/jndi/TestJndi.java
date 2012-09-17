package test.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestJndi {
	private Context ctx;
	
	TestJndi(String factory, String url) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
		env.put(Context.PROVIDER_URL, url);

		ctx = new InitialContext(env);
	}
	
	void lookup(String name) throws NamingException {
		Object obj = ctx.lookup(name);
		System.out.println(name + " is bound to: " + obj);
	}
	
	void close() throws NamingException {
		ctx.close();
	}
	
	public static void main(String[] args) throws NamingException {
		TestJndi test = null;
		
		try {
//			test = new Test("weblogic.jndi.WLInitialContextFactory", "t3://epblnx6d.nam.nsroot.net:16584");
//			test.lookup("tradeUploadCF");
//			test.lookup("tradeApproval");
//			test.close();
			
			test = new TestJndi("weblogic.jndi.WLInitialContextFactory", "t3://epblnx6d.nam.nsroot.net:16580");
			test.lookup("tradeUploadCF");
			test.lookup("tradeApproval");
			test.close();
			
		} finally {
			test.close();
		}
	}
}
