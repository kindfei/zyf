package test.basic;


import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {
	
	static Timer timer;
	int cnt = 0;
	
	public static void main(String args[]) {
		timer =new Timer();
		timer.schedule(new TestTimer().new NowTime(), 0, 1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("done!");
	}
	
	void cancel() {
		timer.cancel();
	}
	
	class NowTime extends TimerTask {
		public void run() {
			cnt++;
			Date date = new Date();
			DateFormat df = DateFormat.getTimeInstance();
			String time = df.format(date);
			System.out.println(cnt + ": " + time);
			if (cnt > 10) {
				cancel();
				System.out.println("the task go on running when cancel method had invoked");
			}
		}
	}
}
