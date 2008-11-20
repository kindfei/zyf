package test.core.jms.jboss;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import test.core.jms.MessageDestination;
import test.core.jms.Provider;
import test.core.jms.Rebuildable;

public class JbossProvider implements Provider {
	
	private InitialContext context;
	private Connection connection;
	private List<Rebuildable> list = new ArrayList<Rebuildable>();
	
	public JbossProvider() throws NamingException {
	}
	
	public void connect(String url) throws NamingException, JMSException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		env.put("java.naming.provider.url", url);
		env.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		InitialContext ctx = new InitialContext(env);
		
		ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		Connection conn = cf.createConnection();
		conn.setExceptionListener(new ExceptionHandler());
		conn.start();
		
		context = ctx;
		connection = conn;
	}
	
	private class ExceptionHandler implements ExceptionListener {

		@Override
		public void onException(JMSException exception) {
			try {
				connect("");
				for (Rebuildable target : list) {
					target.rebuild();
				}
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void register(Rebuildable target) {
		list.add(target);
	}
	
	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		return connection.createSession(transacted, acknowledgeMode);
	}
	
	@Override
	public Destination createDestination(MessageDestination dest) throws NamingException {
		return (Destination) context.lookup(dest.getStrDest());
	}

}
