package test.core.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.MessageListener;

public interface Receiver {
	public Serializable receive() throws JMSException;
	public void setMessageListener(MessageListener listener) throws JMSException;
}
