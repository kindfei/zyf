package test.entry;

import java.util.Timer;
import java.util.TimerTask;

import core.entry.CMD;
import core.entry.CommandType;
import core.entry.Commandable;
import core.entry.NormalCommand;
import core.entry.ServiceEntry;

public class TestService extends ServiceEntry {
	private Timer timer;
	private int counter;
	
	public TestService() {
		
	}
	
	public TestService(int i) {
		System.out.println(i);
	}
	
	public TestService(String[] args) {
		for (String str : args) {
			System.out.println(str);
		}
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
	
	@CMD(key = "stat", type = CommandType.REMOTE)
	public String status() throws Exception {
		return counter + "";
	}
	
	@CMD(key = "refresh", type = CommandType.REMOTE)
	public void refresh() {
		counter = 0;
	}
	
	@CMD(key = "set", type = CommandType.REMOTE)
	public void setValue(int i) {
		counter = i;
	}
	
	public static void main(String[] args) {
		final TestService test = new TestService();
		
		test.addCommand(new NormalCommand("status", CommandType.REMOTE, new Commandable() {
			public String execute(Object[] params) throws Exception {
				return test.status();
			}
		}));
		
		test.process(args);
	}
}
