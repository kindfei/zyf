package test.fx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import test.jms.activemq.Sender;
import fx.cluster.core.Service;
import fx.cluster.core.ServiceFactory;
import fx.cluster.core.ServiceMode;
import fx.service.core.ServiceEntry;

public class TestJMSService extends ServiceEntry {
	private String[] params;
	private Service service;
	private Timer timer;

	public TestJMSService(int listenPort, String[] params) {
		super(listenPort);
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
		String destName = "queue/TestQueue";
		
		if (takerSize > 0) {
			service = ServiceFactory.getMessageDrivenServiceWithoutWorker(ServiceMode.ALL_ACTIVE, new TestJMSProcessor(takerSize, msgCount, msgSize), destName, takerSize);
		} else {
			service = ServiceFactory.getMessageDrivenServiceWithoutWorker(ServiceMode.ALL_ACTIVE, new TestJMSProcessor(takerSize, msgCount, msgSize), destName);
		}
		service.startup();
		
		if (sendMsg) startSender(destName, msgCount, msgSize);
		
		return "Startup OK!";
	}
	
	private void startSender(String destName, int msgCount, int msgSize) {
		timer = new Timer();
		timer.schedule(new SendTask(destName, msgCount, msgSize), 0, 10 * 1000);
	}
	
	public static void main(String[] args) {
		TestJMSService inst = new TestJMSService(Integer.parseInt(args[1]), args);
		inst.process(args[0]);
	}
	
	private static class SendTask extends TimerTask {
		private String destName;
		private int msgCount;
		
		private int[] content;
		
		public SendTask(String destName, int msgCount, int msgSize) {
			this.destName = destName;
			this.msgCount = msgCount;
			
			int size = 1024 * msgSize;
			content = new int[size];
			for (int i = 0; i < size; i++) {
				content[i] = i;
			}
		}

		@Override
		public void run() {
			try {
				Sender sender = new Sender(destName);
				
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
					sender.send(b);
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
