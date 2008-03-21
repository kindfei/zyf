package test.jms.jboss.fx;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.MessageListener;

import test.jms.jboss.BuildException;
import test.jms.jboss.ConnectException;
import test.jms.jboss.FailoverException;
import test.jms.jboss.RecvMessenger;
import test.jms.jboss.SendMessenger;

public class TopicMessage {
	private String destName;
	private RecvMessenger receiver;
	private SendMessenger sender;

	public TopicMessage(String destName) {
		this.destName = destName;
	}
	
	public void setListener(MessageListener listener) throws BuildException, FailoverException, ConnectException {
		if (receiver == null) {
			receiver = new RecvMessenger(destName, listener);
		}
	}
	
	public void sendMessage(String text) throws BuildException, FailoverException, ConnectException, JMSException {
		if (sender == null) {
			sender = new SendMessenger(destName);
		}
		sender.send(text);
	}
	
	public void sendMessage(Serializable obj) throws BuildException, FailoverException, ConnectException, JMSException {
		if (sender == null) {
			sender = new SendMessenger(destName);
		}
		sender.send(obj);
	}
	
	public void close() {
		if (receiver != null) receiver.close();
		if (sender != null) sender.close();
	}
}
