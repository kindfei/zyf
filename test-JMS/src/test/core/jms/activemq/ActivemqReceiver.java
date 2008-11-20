package test.core.jms.activemq;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.MessageListener;

import test.core.jms.Receiver;

public class ActivemqReceiver implements Receiver {

	@Override
	public Serializable receive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMessageListener(MessageListener listener) throws JMSException {
		// TODO Auto-generated method stub
		
	}

}
