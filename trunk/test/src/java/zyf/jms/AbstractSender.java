package zyf.jms;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

public abstract class AbstractSender implements MessageSender {

	private MessageDestination dest;
	
	private MessageConnection connection;
	
	private Session session;
	private MessageProducer producer;
	
	protected volatile boolean isClosed;
	
	protected AbstractSender(MessageDestination dest) throws MessageException {
		this.dest = dest;
		
		connection = ConnectionFactory.getConnection(dest.getProvider());
		
		init();
	}
	
	protected abstract void init() throws MessageException;
	
	protected void build() throws MessageException, JMSException {
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = connection.createDestination(dest);
		producer = session.createProducer(destination);
	}
	
	/**
	 * Sends a message to the destination, specifying delivery mode, priority, and time to live. <p>
	 * 
	 * Default values:<br>
	 * <table border=0 cellspacing=3 cellpadding=0>
	 *     <tr bgcolor="#ccccff">
	 *         <th align=left>Provider
	 *         <th align=left>deliveryMode
	 *         <th align=left>priority
	 *         <th align=left>timeToLive
	 *     <tr>
	 *         <td><code>JBossMQ</code>
	 *         <td>2
	 *         <td>4
	 *         <td>0
	 *     <tr bgcolor="#eeeeff">
	 *         <td><code>JBossMessaging</code>
	 *         <td>-1
	 *         <td>-1
	 *         <td>Long.MIN_VALUE
	 *     <tr>
	 *         <td><code>ActiveMQ</code>
	 *         <td>0
	 *         <td>0
	 *         <td>0
	 * </table>
	 * 
	 * @param msg the message to send
	 * @param deliveryMode the delivery mode to use
	 * @param priority the priority for this message
	 * @param timeToLive the message's lifetime (in milliseconds)
	 * @param props this <code>Map</code> value only for the objectified primitive object types (Integer, Double, Long ...) and String objects. 
	 * @throws JMSException if the JMS provider fails to send the message due to some internal error.
	 */
	protected void sendMessage(Serializable msg, int deliveryMode, int priority, long timeToLive, Map<String, Object> props) throws JMSException {
		ObjectMessage message = session.createObjectMessage(msg);
		if (props != null) {
			for (Entry<String, Object> entry : props.entrySet()) {
				message.setObjectProperty(entry.getKey(), entry.getValue());
			}
		}
		
		/*
		 * +----------------+--------------+----------+----------------+
		 * | Provider       | deliveryMode | priority | timeToLive     |
		 * +----------------+--------------+----------+----------------+
		 * | JBossMQ        | 2            | 4        | 0              |
		 * +----------------+--------------+----------+----------------+
		 * | JBossMessaging | -1           | -1       | Long.MIN_VALUE |
		 * |----------------+--------------+----------+----------------+
		 * | ActiveMQ       | 0            | 0        | 0              |
		 * +----------------+--------------+----------+----------------+
		 */
		if (deliveryMode == 0) {
			producer.send(message);
		} else {
			producer.send(message, deliveryMode, priority, timeToLive);
		}
	}

	@Override
	public void send(Serializable msg) throws MessageException {
		send(msg, 0, 0, 0, null);
	}

	@Override
	public void send(Serializable msg, Map<String, Object> props) throws MessageException {
		send(msg, 0, 0, 0, props);
	}

	@Override
	public abstract void send(Serializable msg, int deliveryMode, int priority, long timeToLive, Map<String, Object> props) throws MessageException;

	@Override
	public void close() {
		isClosed = true;
		
		try {
			producer.close();
		} catch (JMSException e) {
		}
		
		try {
			session.close();
		} catch (JMSException e) {
		}
	}
}
