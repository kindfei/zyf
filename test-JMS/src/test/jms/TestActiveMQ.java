package test.jms;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import test.jms.activemq.core.Receiver;
import test.jms.activemq.core.Sender;

public class TestActiveMQ {
	
	public static void main(String[] args) {
//		testSend();
//		testRecv();
		
		testBoth();
	}
	
	public static void testBoth() {
		try {
			Producer prod = new Producer("queue/TEST.FOO");
			Consumer cons1 = new Consumer("queue/TEST.FOO");
			Consumer cons2 = new Consumer("queue/TEST.FOO");
			
			prod.start();
			cons1.start();
			cons2.start();
			
			Thread.sleep(1 * 60 * 1000);
			
			prod.close();
			cons1.close();
			cons2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testSend() {
		try {
			Producer prod = new Producer("queue/TEST.FOO");
			
			prod.start();
			
			Thread.sleep(1 * 60 * 1000);
			
			prod.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testRecv() {
		try {
			Consumer cons = new Consumer("queue/TEST.FOO");
			
			cons.start();
			
			Thread.sleep(1 * 60 * 1000);
			
			cons.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static class Producer extends Thread {
		private Sender sender;
		private boolean isActive = true;
		
		Producer(String destName) throws JMSException {
			sender = new Sender(destName);
		}
		
		public void run() {
			try {
				for (int i = 0; isActive; i++) {
					sender.send(i + "");
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				sender.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
		public void close() {
			isActive = false;
		}
	}
	
	static class Consumer extends Thread {
		private Receiver receiver;
		private boolean isActive = true;
		
		Consumer(String destName) throws JMSException {
			receiver = new Receiver(destName);
		}
		
		public void run() {
			try {
				while(isActive) {
					ObjectMessage msg = (ObjectMessage) receiver.receive();
					if (msg != null)
						System.out.println(getName() + " - " + msg.getObject());
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
		public void close() {
			isActive = false;
			try {
				receiver.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
