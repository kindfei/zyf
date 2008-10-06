package test.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import test.jms.activemq.Sender;

public class TestAmqSender {
	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new Task(), 0, 10 * 1000);
	}
	
	private static class Task extends TimerTask {

		@Override
		public void run() {
			try {
				Sender sender = new Sender("queue/TestQueue");
				
				int amount = 1000;
				
				List<JmsPerfBean> beans = new ArrayList<JmsPerfBean>();
				JmsPerfBean bean = null;
				for (int i = 0; i < amount; i++) {
					bean = new JmsPerfBean();
					bean.setContent("123456789012345678901234567890");
					beans.add(bean);
				}
				long st = Calendar.getInstance().getTimeInMillis();
				bean.setTime(st);
				bean.setLast(true);
				bean.setAmount(amount);
				
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
		
		private long time;
		private boolean last;
		private int amount;
		private String Content;
		
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
		public boolean isLast() {
			return last;
		}
		public void setLast(boolean last) {
			this.last = last;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
		public String getContent() {
			return Content;
		}
		public void setContent(String content) {
			Content = content;
		}
		public static long getSerialVersionUID() {
			return serialVersionUID;
		}
	}
}
