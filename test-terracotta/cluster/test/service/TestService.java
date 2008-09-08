package test.service;

import java.util.Timer;
import java.util.TimerTask;

import fx.service.core.CommandType;
import fx.service.core.Commandable;
import fx.service.core.ServiceCommand;
import fx.service.core.ServiceEntry;

public class TestService extends ServiceEntry {
	private Timer timer;
	private int counter;

	protected TestService(int listenPort) {
		super(listenPort);
	}

	public String shutdown() throws Exception {
		timer.cancel();
		
		return "Shutdown OK!";
	}

	public String startup() throws Exception {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				counter++;
			}
		}, 0, 1000);
		
		return "Startup OK!";
	}
	
	public String status() throws Exception {
		return counter + "";
	}
	
	public static void main(String[] args) {
		final TestService test = new TestService(1234);
		
		test.addCommand(new ServiceCommand("status", CommandType.REMOTE, new Commandable() {
			public String operate() throws Exception {
				return test.status();
			}
		}));
		
		test.executeCommand(args);
	}
}
