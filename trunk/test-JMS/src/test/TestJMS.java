package test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import test.jms.jboss.MessageAdapter;
import test.jms.jboss.P2PMessageAdapter;
import test.jms.jboss.PubMessageAdapter;
import test.jms.jboss.RecvMessenger;
import test.jms.jboss.SendMessenger;
import test.jms.jboss.DurableTopicRecvMessenger;

public class TestJMS {
	
	public static void main(String[] args) {
		
		try {
//			new TestReceive("topic/testTopic", "L1").start();
//			new TestReceive("topic/testDurableTopic", "L1").start();
//			new TestReceive("queue/testQueue", "L1").start();
//			new TestReceive("queue/testQueue", "L2").start();
//			new TestReceive("queue/testQueue", "L3").start();
			
			new TestAdapterReceive("topic/testTopic").start();
//			new TestAdapterReceive("topic/testDurableTopic").start();
//			new TestAdapterReceive("queue/testQueue").start();
			
			new TestSend("topic/testTopic", 10000, false).start();
//			new TestSend("topic/testDurableTopic", 100, true).start();
//			new TestSend("queue/testQueue", 1000, false).start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class TestSend extends Thread {
	private String destName;
	private int msgCount;
	private boolean sendShutdown;
	
	TestSend(String destName, int msgCount, boolean sendShutdown) {
		this.destName = destName;
		this.msgCount = msgCount;
		this.sendShutdown = sendShutdown;
	}

	public void run() {
		try {
			SendMessenger s = new SendMessenger(destName);
			String tn = Thread.currentThread().getName();
			for (int i = 0; i < msgCount; i++) {
//				if (i % 3 == 0) s.setProperty("type", "feeder");
//				if (i % 5 == 0) s.setProperty("type", "all");
				s.send(i + "");
				System.out.println(tn + " send: " + i);
				Thread.sleep(800);
			}
			if (sendShutdown) s.send("shutdown");
//			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class TestReceive extends Thread {
	private String destName;
	private String name;
	private RecvMessenger r;
	
	TestReceive(String destName, String name) {
		this.destName = destName;
		this.name = name;
	}

	public void run() {
		try {
			r = new RecvMessenger(destName, new Listener(name), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Listener implements MessageListener {
	String name;
	
	Listener(String name) {
		this.name = name;
	}
	
	public void onMessage(Message msg) {
		TextMessage tMsg = (TextMessage) msg;
		try {
			String str = tMsg.getText();
			System.out.println(name + ":" + str);
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class TestAdapterReceive extends Thread {
	private String destName;
	private MessageAdapter adp;
	private RecvMessenger r;
	
	TestAdapterReceive(String destName) {
		this.destName = destName;
	}

	public void run() {
		try {
			adp = new P2PMessageAdapter(0, 5);
			Listener l1 = new Listener("L1");
			Listener l2 = new Listener("L2");
			Listener l3 = new Listener("L3");
			adp.addListener(l1);
			adp.addListener(l1);
//			adp.addListener(l2);
//			adp.addListener(l3);
			r = new RecvMessenger(destName, adp);

//			Thread.sleep(2000);
//			adp.removeListener(l1);
//			Thread.sleep(2000);
//			adp.removeListener(l2);
//			Thread.sleep(5000);
//			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}