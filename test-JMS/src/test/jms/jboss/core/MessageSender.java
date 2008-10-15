package test.jms.jboss.core;

import java.io.Serializable;
import java.util.Map;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessageSender {
	private static final Log log = LogFactory.getLog(MessageSender.class);
	
	private SendMessenger sender;
	
	public MessageSender(String destName) 
			throws BuildException, FailoverException, ConnectException {
		this(destName, null);
	}
	
	public MessageSender(String destName, Map props) 
			throws BuildException, FailoverException, ConnectException {
		sender = new SendMessenger(destName, props);
		log.info("Create SendMessenger. destName:" + destName);
	}
	
	public String getDestName() {
		return sender.getDestName();
	}
	
	public void setProperty(String key, String value) {
		sender.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return sender.getProperty(key);
	}
	
	public Object removeProperty(String key) {
		return sender.removeProperty(key);
	}
	
	public void clearProperty() {
		sender.clearProperty();
	}

	public void send(String text) throws JMSException {
		sender.send(text);
	}

	public void send(String text, Map props) throws JMSException {
		sender.send(text, props);
	}
	
	public void send(Serializable obj) throws JMSException {
		sender.send(obj);
	}
	
	public void send(Serializable obj, Map props) throws JMSException {
		sender.send(obj, props);
	}
	
	public void close() {
		sender.close();
	}
}
