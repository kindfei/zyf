package test.servlet;

import java.io.IOException;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestMDB
 */
public class TestMDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestMDBServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Resource(name="TestCF")
    private ConnectionFactory cf;
    @Resource(name="TestQueue")
    private Queue queue;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
			Connection conn = cf.createConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);
			
			for (int i = 0; i < 30; i++) {
				TextMessage msg = session.createTextMessage();
				msg.setText(System.currentTimeMillis() + ": Hello MDB");
				producer.send(msg);
			}
			
			producer.close();
			session.close();
			conn.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
