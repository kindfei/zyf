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
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import test.ejb.hello.Hello;
import test.ejb.hello.HelloSFSBLocal;
import test.ejb.hello.HelloSFSBRemote;
import test.ejb.hello.HelloSLSBLocal;
import test.ejb.hello.HelloSLSBRemote;

/**
 * Servlet implementation class Test
 */
public class TestHelloServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    @EJB(beanInterface=HelloSLSBLocal.class, beanName="HelloSLSB")
    private Hello hll;
    @EJB(beanInterface=HelloSLSBRemote.class, beanName="HelloSLSB")
    private Hello hlr;
    @EJB(beanInterface=HelloSFSBLocal.class, beanName="HelloSFSB")
    private Hello hfl;
    @EJB(beanInterface=HelloSFSBRemote.class, beanName="HelloSFSB")
    private Hello hfr;
    
    @EJB(beanName="HelloSLSBLocalImpl")
    private Hello hlli;
    @EJB(beanName="HelloSLSBRemoteImpl")
    private Hello hlri;
    @EJB(beanName="HelloSFSBLocalImpl")
    private Hello hfli;
    @EJB(beanName="HelloSFSBRemoteImpl")
    private Hello hfri;

    @EJB(beanInterface=HelloSLSBLocal.class, beanName="spring.ejb2.HelloSLSB")
    private Hello s2hll;
    @EJB(beanInterface=HelloSLSBRemote.class, beanName="spring.ejb2.HelloSLSB")
    private Hello s2hlr;
    @EJB(beanInterface=HelloSFSBLocal.class, beanName="spring.ejb2.HelloSFSB")
    private Hello s2hfl;
    @EJB(beanInterface=HelloSFSBRemote.class, beanName="spring.ejb2.HelloSFSB")
    private Hello s2hfr;
    
    @EJB(name="ejb/s3hll")
    private Hello s3hll;
    @EJB(name="ejb/s3hlr")
    private Hello s3hlr;
    @EJB(name="ejb/s3hfl")
    private Hello s3hfl;
    @EJB(name="ejb/s3hfr")
    private Hello s3hfr;

    @Resource(name="TestCF")
    private ConnectionFactory cf;
    @Resource(name="TestQueue")
    private Queue queue;
    
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	StringBuilder r = new StringBuilder();

    	r.append("========== Servlet ==========").append('\n');

    	r.append("---------- Inheritance ----------").append('\n');

		r.append(hll.sayHello(hll.toString())).append('\n');
		r.append(hlr.sayHello(hlr.toString())).append('\n');
		r.append(hfl.sayHello(hfl.toString())).append('\n');
		r.append(hfr.sayHello(hfr.toString())).append('\n');
		
		r.append(hlli.sayHello(hll.toString())).append('\n');
		r.append(hlri.sayHello(hlr.toString())).append('\n');
		r.append(hfli.sayHello(hfl.toString())).append('\n');
		r.append(hfri.sayHello(hfr.toString())).append('\n');
		
    	r.append("---------- Spring Integration ----------").append('\n');

		r.append(s2hll.sayHello(s2hll.toString())).append('\n');
		r.append(s2hlr.sayHello(s2hlr.toString())).append('\n');
		r.append(s2hfl.sayHello(s2hfl.toString())).append('\n');
		r.append(s2hfr.sayHello(s2hfr.toString())).append('\n');

		r.append(s3hll.sayHello(s3hll.toString())).append('\n');
		r.append(s3hlr.sayHello(s3hlr.toString())).append('\n');
		r.append(s3hfl.sayHello(s3hfl.toString())).append('\n');
		r.append(s3hfr.sayHello(s3hfr.toString())).append('\n');

    	r.append("========== POJO ==========").append('\n');
    	
    	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    	TestHelloPojo testHelloPojo = (TestHelloPojo) ctx.getBean("testHelloPojo");
    	r.append(testHelloPojo.testSessionBean());
    	
    	response.getWriter().println(r.toString());
    	
    	/*
    	 * 
    	 */
    	testMessageDrivenBean();
    	
    	testHelloPojo.sendTestMessage("TestTopic");
    }

    private void testMessageDrivenBean() {
    	try {
			Connection conn = cf.createConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);
			TextMessage msg = session.createTextMessage();
			msg.setText(System.currentTimeMillis() + ": Hello MDB");
			producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
    
}
