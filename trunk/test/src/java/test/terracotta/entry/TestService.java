package test.terracotta.entry;

import java.util.Timer;
import java.util.TimerTask;

import zyf.entry.CMD;
import zyf.entry.CommandType;
import zyf.entry.Commandable;
import zyf.entry.NormalCommand;
import zyf.entry.ServiceEntry;
import zyf.jmx.ATTRIBUTE;
import zyf.jmx.OPERATION;


public class TestService extends ServiceEntry {
	private Timer timer;
	
	@ATTRIBUTE(description = "Times counter.")
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
	
	@OPERATION(description = "Get the value of counter.")
	@CMD(key = "stat", type = CommandType.REMOTE)
	public String status() throws Exception {
		return counter + "";
	}
	
	@OPERATION(description = "Restart the count.")
	@CMD(key = "refresh", type = CommandType.REMOTE)
	public void refresh() {
		counter = 0;
	}
	
	@OPERATION(description = "Assign value to counter.")
	@CMD(key = "set", type = CommandType.REMOTE)
	public void setValue(int i) {
		counter = i;
	}
	
	public static void main(String[] args) {
		final TestService test = new TestService();
		
		test.addCommand(new NormalCommand("status", CommandType.REMOTE, new Commandable() {
			public String execute() throws Exception {
				return test.status();
			}
		}));
		
		test.process(args);
	}
}
