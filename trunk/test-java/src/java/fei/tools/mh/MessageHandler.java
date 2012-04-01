package fei.tools.mh;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import fei.tools.util.InteractionHandler;




public class MessageHandler {
	private static final Log log = LogFactory.getLog(MessageHandler.class);
	
	private JmsTemplate jmsTemplate;

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public void sendTextMsg(final String msg) {
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
	}
	
	public String receiveTextMsg() {
		TextMessage msg = (TextMessage) jmsTemplate.receive();
		String r = null;
		try {
			r = msg.getText();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return r;
	}

	public static void main(String[] args) {
		//
		ClassPathXmlApplicationContext mainContext = new ClassPathXmlApplicationContext("mhMainContext.xml");
		ApplicationContext context = (ApplicationContext) mainContext.getBean("variableContext");
		
		//
		MessageHandler mh = (MessageHandler) context.getBean("messageHandler");
		
		//
		boolean exit = false;
		do {
			int i = InteractionHandler.options("", new String[] {"Send Message", "Receive Message", "Exit"});
			switch (i) {
			case 0:
				try {
					String message;
					if (InteractionHandler.confirm("Input message file name")) {
						message = readFileToString(InteractionHandler.input("Input the name of message file"));
					} else {
						message = InteractionHandler.input("Input the message content");
					}
				
					mh.sendTextMsg(message);
					System.out.println("Sent message:\n" + message);
					log.info("Sent message:\n" + message);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				String message = mh.receiveTextMsg();
				System.out.println("Received message:\n" + message);
				log.info("Received message:\n" + message);
				break;
			default:
				exit = true;
				break;
			}
		} while (!exit);
		
		//
		mainContext.close();
	}
	
	private static String readFileToString(String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName)).useDelimiter("\\z");
		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.next());
		}
		return sb.toString();
	}
}
