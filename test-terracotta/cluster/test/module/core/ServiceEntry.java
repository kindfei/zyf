package test.module.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service entry, help bootstrap and manage service.
 * @author zhangyf
 *
 */
public abstract class ServiceEntry {
	private static Log log = LogFactory.getLog(ServiceEntry.class);
	
	private static final String helpCmd = "help";
	
	private List startCmds;
	private List controlCmds;
	private List invokeCmds;
	private int port;
	
	protected ServiceEntry(List remoteMethods, List localMethods, int port) {
		this(localMethods, remoteMethods, null, port);
	}
	
	protected ServiceEntry(List startCmds, List controlCmds, List invokeCmds, int port) {
		this.startCmds = startCmds;
		this.controlCmds = controlCmds;
		if (invokeCmds == null) {
			invokeCmds = new ArrayList(1);
		}
		invokeCmds.add(helpCmd);
		this.invokeCmds = invokeCmds;
		this.port = port;
	}
	
	public String help() {
		StringBuffer sb = new StringBuffer();
		sb.append("Supported Command:\n");
		for (Iterator iter = startCmds.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			sb.append(element).append("\t Start command.\n");
		}
		for (Iterator iter = controlCmds.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			sb.append(element).append("\t Control command.\n");
		}
		for (Iterator iter = invokeCmds.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			sb.append(element).append("\t Invoke command.\n");
		}
		return sb.toString();
	}
	
	protected void executeCommand(String args[]) {
		if (args == null || args.length == 0) {
			String str = "No program arguments was specified, enter [help] for more help.";
			log.error(str);
			System.out.println(str);
			return;
		}
		
		String cmd = args[0];
		String ret = null;
		try {
			
			if (startCmds.contains(cmd)) {
				init();
				startMonitor();
				ret = invoke(cmd);
			} else if (controlCmds.contains(cmd)) {
				ret = sendCommand(cmd);
			} else if (invokeCmds.contains(cmd)) {
				ret = invoke(cmd);
			} else {
				ret = "Unsupported Command [" + cmd + "], enter [help] for more help.";
			}
			log.info(ret);
			System.out.println(ret);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	protected abstract void init() throws Exception;
	
	private void startMonitor() throws IOException {
		Thread thread = new Thread(new ServiceControl(port, this), "Control Listener");
		thread.setDaemon(true);
		thread.start();		
	}
	
	String invoke(String method) throws Exception {
		String ret = "Error occurred when execute method: " + method;
		ret = (String) this.getClass().getMethod(method, null).invoke(this, null);
		return ret;
	}
	
	private String sendCommand(String cmd) throws Exception {
		Socket socket = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		String ret = "Error occurred when send command: " + cmd;
		try {
			socket = new Socket("127.0.0.1", port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			
			writer.println(cmd);
			writer.flush();
			ret = reader.readLine();
		} finally {
			if (writer != null) writer.close();
			if (reader != null) try {reader.close();} catch (IOException e) {};
			try {socket.close();} catch (IOException e) {}
		}
		return ret;
	}
}
