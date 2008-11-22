package test.core.jms;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.InitialContext;

public class JBossMessagingConnection extends AbstractConnection {
	private InitialContext context;
	
	JBossMessagingConnection(Provider provider, String clientID) throws MessageException {
		super(provider, clientID);
	}

	@Override
	protected Connection createConnection(String url, String user, String password) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		env.put("java.naming.provider.url", url);
		env.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		InitialContext ctx = new InitialContext(env);
		
		ConnectionFactory cf = (ConnectionFactory) ctx.lookup("/ConnectionFactory");
		Connection conn = cf.createConnection(user, password);
		
		context = ctx;
		
		return conn;
	}

	@Override
	protected Destination createDestination(String destName) throws Exception {
		return (Destination) context.lookup(destName);
	}
}
