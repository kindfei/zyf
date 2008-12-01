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
	
	@CMD(key = "refresh", type = CommandType.REMOTE)
	public void refresh() {
		counter = 0;
	}
	
	@CMD(key = "set", type = CommandType.REMOTE)
	public void setValue(int i) {
		counter = i;
	}
	
	public static void main(String[] args) {
		final TestService test = new TestService(1234);
		
		test.addCommand(new NormalCommand("status", CommandType.REMOTE, new Commandable() {
			public String execute() throws Exception {
				return test.status();
			}
		}));
		
		test.process(args);
	}
}