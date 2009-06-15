package test.jms;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import test.jms.jboss.BuildException;
import test.jms.jboss.ConnectException;
import test.jms.jboss.FailoverException;
import test.jms.jboss.MessageAdapter;
import test.jms.jboss.MessageReceiver;
import test.jms.jboss.MessageSender;
import test.jms.jboss.P2PMessageAdapter;
import test.jms.jboss.PubMessageAdapter;
import test.jms.jboss.RecvMessenger;
import test.jms.jboss.SendMessenger;

public class TestJBoss {
	
	public static void main(String[] args) {
		
		try {
			
//			new TestSend("topic/testTopic", 1000, 500);
//			new TestSend("topic/testDurableTopic", 1000, 500);
//			new TestSend("queue/testQueue", 1000, 500);
			
			
//			new TestReceive("topic/testTopic", "L1", 0).start();
//			new TestReceive("topic/testDurableTopic", "L1", 0).start();
//			new TestReceive("queue/testQueue", "L1", 0).start();
//			new TestReceive("queue/testQueue", "L2", 0).start();
//			new TestReceive("queue/testQueue", "L3", 0).start();
			
//			new TestP2PAdapter("topic/testTopic", 3).start();
//			new TestP2PAdapter("topic/testDurableTopic", 3).start();
//			new TestP2PAdapter("queue/testQueue", 3).start();
			
//			new TestPubAdapter("topic/testTopic", 3).start();
//			new TestPubAdapter("topic/testDurableTopic", 3).start();
//			new TestPubAdapter("queue/testQueue", 3).start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		test();
	}
	
	public static void test() {
		try {
			MessageReceiver receiver = new MessageReceiver("topic/testTopic", MessageReceiver.DELIVERY_TYPE_P2P, null, true);
			
			for (int i = 0; i < 3; i++) {
				Listener l = new Listener("Listener-" + i, 1000);
				receiver.setListener(l);
			}
			
			new TestSend("topic/testTopic", 100, 500);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class TestSend extends Thread {
	private String destName;
	private int msgCount;
	private SendMessenger sender;
	private int interval;
	
	public TestSend(String destName, int msgCount, int interval) throws Exception {
		this.destName = destName;
		this.msgCount = msgCount;
		this.interval = interval;

		sender = new SendMessenger(this.destName);
		
		start();
	}
	
	public void setProperty(String key, String value) {
		sender.setProperty(key, value);
	}

	public void run() {
		try {
			String tn = Thread.currentThread().getName();
			
			Map prop = new HashMap();
			prop.put("TEST_TARGET", "ALL");
			
			for (int i = 0; i < msgCount; i++) {
//				sender.send(i + "");
				sender.send(new BigDecimal(i), prop);
				System.out.println(tn + " send: " + i);
				Thread.sleep(interval);
			}
			
			sender.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class TestReceive extends Thread {
	private String destName;
	private String name;
	private RecvMessenger receiver;
	private int interval;
	
	TestReceive(String destName, String name, int interval) {
		this.destName = destName;
		this.name = name;
		this.interval = interval;
	}

	public void run() {
		try {
			receiver = new RecvMessenger(destName, new Listener(name, interval), null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		receiver.close();
	}
}

class Listener implements MessageListener {
	private String name;
	private int interval;
	
	Listener(String name, int interval) {
		this.name = name;
		this.interval = interval;
	}
	
	public void onMessage(Message msg) {
		try {
			Object obj = null;
			if (msg instanceof TextMessage) {
				TextMessage tm = (TextMessage) msg;
				obj = tm.getText();
			} else {
				ObjectMessage om = (ObjectMessage) msg;
				obj = om.getObject();
			}
			System.out.println(name + ":" + obj.getClass().getName() + "=" + obj.toString());
			if (interval > 0) Thread.sleep(interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class TestP2PAdapter extends Thread {
	private String destName;
	private MessageAdapter adapter;
	private RecvMessenger receiver;
	private int count;
	
	TestP2PAdapter(String destName, int count) {
		this.destName = destName;
		this.count = count;
	}

	public void run() {
		try {
			adapter = new P2PMessageAdapter(3, false);
			
			for (int i = 0; i < count; i++) {
				Listener l = new Listener("Listener-" + i, 2000);
				adapter.addListener(l);
			}
			
			receiver = new RecvMessenger(destName, adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		receiver.close();
	}
}

class TestPubAdapter extends Thread {
	private String destName;
	private MessageAdapter adapter;
	private RecvMessenger receiver;
	private int count;
	
	TestPubAdapter(String destName, int count) {
		this.destName = destName;
		this.count = count;
	}

	public void run() {
		try {
			adapter = new PubMessageAdapter(2, false);
			
			for (int i = 0; i < count; i++) {
				Listener l = new Listener("Listener-" + i, 1000);
				adapter.addListener(l);
			}
			
			receiver = new RecvMessenger(destName, adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		receiver.close();
	}
}