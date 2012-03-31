package fei.smtpserver;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author yz69579
 *
 */
public class Bootstrap {
	private final static Log log = LogFactory.getLog(Bootstrap.class);
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("smtp-server-context.xml");
		
		Map<String, SMTPServer> smtpServers = (Map<String, SMTPServer>) context.getBeansOfType(SMTPServer.class);
		
		for (SMTPServer smtpServer : smtpServers.values()) {
			smtpServer.start();
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				context.stop();
			}
		});
		
		log.info("All servers started.");
	}
}
