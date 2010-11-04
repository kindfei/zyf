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

import test.ejb.HelloStatefulLocal;
import test.ejb.HelloStatefulRemote;
import test.ejb.HelloStatelessLocal;
import test.ejb.HelloStatelessRemote;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private HelloStatelessLocal helloStatelessLocal1;
	@EJB
	private HelloStatelessRemote helloStatelessRemote1;
	@EJB
	private HelloStatelessLocal helloStatelessLocal2;
	@EJB
	private HelloStatelessRemote helloStatelessRemote2;
	
	@EJB
	private HelloStatefulLocal helloStatefulLocal1;
	@EJB
	private HelloStatefulRemote helloStatefulRemote1;
	@EJB
	private HelloStatefulLocal helloStatefulLocal2;
	@EJB
	private HelloStatefulRemote helloStatefulRemote2;
	
	@Resource(name="TestJMSConnFactory", type=ConnectionFactory.class)
	private ConnectionFactory cf;
	@Resource(name="TestQueue", type=Queue.class)
	private Queue queue;
	
	private int count;

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().println(helloStatelessLocal1.sayHello("helloStatelessLocal1"));
		response.getWriter().println(helloStatelessRemote1.sayHello("helloStatelessRemote1"));
		response.getWriter().println(helloStatelessLocal2.sayHello("helloStatelessLocal2"));
		response.getWriter().println(helloStatelessRemote2.sayHello("helloStatelessRemote2"));
		
		response.getWriter().println(helloStatefulLocal1.sayHello("helloStatefulLocal1"));
		response.getWriter().println(helloStatefulRemote1.sayHello("helloStatefulRemote1"));
		response.getWriter().println(helloStatefulLocal2.sayHello("helloStatefulLocal2"));
		response.getWriter().println(helloStatefulRemote2.sayHello("helloStatefulRemote2"));
		
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
