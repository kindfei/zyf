package test.servlet;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import test.ejb.hello.Hello;

public class TestHelloPojo {
	
    private Hello hll;
    private Hello hlr;
    private Hello hfl;
    private Hello hfr;
    
    private Hello hlli;
    private Hello hlri;
    private Hello hfli;
    private Hello hfri;
    
    private Hello s2hll;
    private Hello s2hlr;
    private Hello s2hfl;
    private Hello s2hfr;
    
    private Hello s3hll;
    private Hello s3hlr;
    private Hello s3hfl;
    private Hello s3hfr;

	public String testSessionBean() {
    	StringBuilder r = new StringBuilder();

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
		
		return r.toString();
    }
    
	/**
	 * 
	 */
    private JmsTemplate jmsTemplate;
    
    public void sendTestMessage(final String destination) {
    	jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage msg = session.createTextMessage();
				msg.setText("Test Message");
				return msg;
			}
		});
    }
    
    /**
     * 
     */
	public void setHll(Hello hll) {
		this.hll = hll;
	}

	public void setHlr(Hello hlr) {
		this.hlr = hlr;
	}

	public void setHfl(Hello hfl) {
		this.hfl = hfl;
	}

	public void setHfr(Hello hfr) {
		this.hfr = hfr;
	}

	public void setHlli(Hello hlli) {
		this.hlli = hlli;
	}

	public void setHlri(Hello hlri) {
		this.hlri = hlri;
	}

	public void setHfli(Hello hfli) {
		this.hfli = hfli;
	}

	public void setHfri(Hello hfri) {
		this.hfri = hfri;
	}

	public void setS2hll(Hello s2hll) {
		this.s2hll = s2hll;
	}

	public void setS2hlr(Hello s2hlr) {
		this.s2hlr = s2hlr;
	}

	public void setS2hfl(Hello s2hfl) {
		this.s2hfl = s2hfl;
	}

	public void setS2hfr(Hello s2hfr) {
		this.s2hfr = s2hfr;
	}

	public void setS3hll(Hello s3hll) {
		this.s3hll = s3hll;
	}

	public void setS3hlr(Hello s3hlr) {
		this.s3hlr = s3hlr;
	}

	public void setS3hfl(Hello s3hfl) {
		this.s3hfl = s3hfl;
	}

	public void setS3hfr(Hello s3hfr) {
		this.s3hfr = s3hfr;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

}
