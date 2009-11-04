package test.terracotta.all;

import incubation.cluster.Service;
import incubation.cluster.ServiceFactory;
import incubation.cluster.ServiceMode;
import incubation.entry.ServiceEntry;
import incubation.jms.MessageDestination;
import incubation.jms.MessageException;
import incubation.jms.MessageFactory;
import incubation.jms.MessageSender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;



public class TestJMSService extends ServiceEntry {
	private String[] params;
	private Service service;
	private Timer timer;

	public TestJMSService(String[] params) {
		this.params = params;
	}

	@Override
	public String shutdown() throws Exception {
		if (timer != null) timer.cancel();
		if (service != null) service.shutdown();
		return "Shutdown OK!";
	}

	@Override
	public String startup() throws Exception {
		boolean sendMsg = "true".equalsIgnoreCase(params[2]) ? true : false;
		int takerSize = Integer.parseInt(params[3]);
		int msgCount = Integer.parseInt(params[4]);
		int msgSize = Integer.parseInt(params[5]);
		
		if (takerSize > 0) {
			service = ServiceFactory.getMessageDrivenServiceWithoutWorker(ServiceMode.ALL_ACTIVE,
					new TestJMSProcessor(takerSize, msgCount, msgSize), MessageDestination.testQueue, takerSize);
		} else {
			service = ServiceFactory.getMessageDrivenServiceWithoutWorker(ServiceMode.ALL_ACTIVE,
					new TestJMSProcessor(takerSize, msgCount, msgSize), MessageDestination.testQueue, "name = 'zhangyf'");
		}
		service.startup();
		
		if (sendMsg) startSender(MessageDestination.testQueue, msgCount, msgSize);
		
		return "Startup OK!";
	}
	
	private void startSender(MessageDestination dest, int msgCount, int msgSize) {
		timer = new Timer();
		timer.schedule(new SendTask(dest, msgCount, msgSize), 0, 10 * 1000);
	}
	
	public static void main(String[] args) {
		TestJMSService inst = new TestJMSService(args);
		inst.process(args[0]);
	}
	
	private static class SendTask extends TimerTask {
		private int msgCount;
		
		private byte[] content;
		private MessageSender sender;
		
		public SendTask(MessageDestination dest, int msgCount, int msgSize) {
			this.msgCount = msgCount;
			
			int size = 1024 * msgSize;
			content = new byte[size];
			for (int i = 0; i < size; i++) {
				content[i] = 0;
			}
			
			try {
				sender = MessageFactory.createSender(dest);
			} catch (MessageException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				List<JmsPerfBean> beans = new ArrayList<JmsPerfBean>();
				JmsPerfBean bean = null;
				for (int i = 0; i < msgCount; i++) {
					bean = new JmsPerfBean();
					bean.setSeq(i);
					bean.setContent(content);
					beans.add(bean);
				}
				long st = System.currentTimeMillis();
				bean.setTime(st);
				
				for (JmsPerfBean b : beans) {
					Map<String, Object> props = new HashMap<String, Object>();
					props.put("name", "zhangyf");
					props.put("age", new Integer(29));
					sender.send(b, props);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class JmsPerfBean implements Serializable {
		private static final long serialVersionUID = 2094561965522115466L;
		
		private int seq;
		private Object Content;
		private long time;
		
		public int getSeq() {
			return seq;
		}
		public void setSeq(int seq) {
			this.seq = seq;
		}
		public Object getContent() {
			return Content;
		}
		public void setContent(Object content) {
			Content = content;
		}
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
	}
}
