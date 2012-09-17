package test.quartz;

import java.util.Calendar;

public class JobDetailService {

	public void execute() throws InterruptedException {
		Calendar c = Calendar.getInstance();
		int m = c.get(Calendar.SECOND);
		
		System.out.println(Thread.currentThread().getName() + " Triggered at " + m);
		
		if (m % 2 == 0) {
			Thread.sleep(1100);
		}

		if (m % 10 == 0) {
			throw new RuntimeException();
		}
	}
}
