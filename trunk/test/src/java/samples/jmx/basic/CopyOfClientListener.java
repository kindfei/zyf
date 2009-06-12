package samples.jmx.basic;

import javax.management.Notification;
import javax.management.NotificationListener;

public class CopyOfClientListener implements NotificationListener {
	public void handleNotification(Notification notification, Object handback) {
		System.out.println("\nReceived notification: " + notification);
	}
}
