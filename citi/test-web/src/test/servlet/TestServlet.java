package test.servlet;

import java.io.IOException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.ejb.HelloEJBLocal;
import test.ejb.HelloEJBRemote;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name="HelloEJBLocalImpl", beanInterface=HelloEJBLocal.class)
	private HelloEJBLocal helloEJBLocal;
	@EJB(name="HelloEJBRemoteImpl", beanInterface=HelloEJBRemote.class)
	private HelloEJBRemote helloEJBRemote;
	
	@Resource(name="TestJMSConnFactory", type=ConnectionFactory.class)
	private ConnectionFactory cf;
	@Resource(name="TestQueue", type=Queue.class)
	private Queue queue;
	
	private int count;

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().println(helloEJBLocal.sayHello("HelloEJBLocal"));
		response.getWriter().println(helloEJBRemote.sayHello("HelloEJBRemote"));
		
		try {
			Connection conn = cf.createConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);
			
			producer.send(session.createTextMessage("Hello MDB " + count++));
			
			producer.close();
			session.close();
			conn.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
