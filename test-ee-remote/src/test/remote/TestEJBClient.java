package test.remote;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import test.ejb.hello.Hello;

public class TestEJBClient {
	Context context;
	
	TestEJBClient(String protocol) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, protocol + "://127.0.0.1:7001");
		context = new InitialContext(env);
		System.out.println("================" + protocol + "================");
	}
	
	void invoke(String jndiName, int times) throws NamingException {
		Hello bean = (Hello) context.lookup(jndiName);
		
		for (int i = 0; i < times; i++) {
			System.out.println(bean.sayHello(bean.getClass().getName()));
		}
	}
	
	
	public static void main(String[] args) throws NamingException {
		
		TestEJBClient inst = null;
		
		inst = new TestEJBClient("t3");
		inst.invoke("hl#test.ejb.HelloSLSBRemote", 2);
		inst.invoke("hf#test.ejb.HelloSFSBRemote", 2);
		inst.invoke("hlri#test.ejb.Hello", 2);
		inst.invoke("hfri#test.ejb.Hello", 2);
		inst.invoke("shl#test.ejb.spring.HelloSLSBRemote", 2);
		inst.invoke("shf#test.ejb.spring.HelloSFSBRemote", 2);
		
		inst = new TestEJBClient("iiop");
		inst.invoke("hl#test.ejb.HelloSLSBRemote", 2);
		inst.invoke("hf#test.ejb.HelloSFSBRemote", 2);
		inst.invoke("hlri#test.ejb.Hello", 2);
		inst.invoke("hfri#test.ejb.Hello", 2);
		inst.invoke("shl#test.ejb.spring.HelloSLSBRemote", 2);
		inst.invoke("shf#test.ejb.spring.HelloSFSBRemote", 2);
	}
}
