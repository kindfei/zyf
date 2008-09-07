package test.service.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service entry, help bootstrap and manage service.
 * @author zhangyf
 *
 */
public abstract class ServiceEntry {
	private static Log log = LogFactory.getLog(ServiceEntry.class);
	
	private ServiceCommand[] commands;
	private int listenPort;
	private String ipAddress;
	
	protected ServiceEntry(ServiceCommand[] commands, int listenPort) {
		this(commands, listenPort, "127.0.0.1");
	}
	
	protected ServiceEntry(ServiceCommand commands[], int listenPort, String ipAddress) {
		this.commands = commands;
		this.listenPort = listenPort;
		this.ipAddress = ipAddress;
	}
	
	protected void executeCommand(String args[]) {
		try {
			if (args == null || args.length == 0) {
				throw new IllegalArgumentException("No program arguments was specified as a command to execute.");
			}
			
			String strCmd = args[0];
			
			ServiceCommand command = searchCommand(strCmd);
			
			CommandType type = command.getType();
			
			switch (type) {
			case STARTUP:
				startupInvoke(command.getCommandable());
				break;

			case REMOTE:
				remoteInvoke(strCmd);
				break;

			case LOCAL:
				localInvoke(command.getCommandable());
				break;
			}
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
			System.err.println(e.getMessage());
		} catch (Throwable e) {
			log.error("Unknown error occurred when execute command.", e);
			System.err.println("Unknown error occurred when execute command. " + e.getMessage());
		}
	}
	
	ServiceCommand searchCommand(String strCmd) {
		for (int i = 0; i < commands.length; i++) {
			if (commands[i].getCommand().equals(strCmd)) {
				return commands[i];
			}
		}
		
		throw new IllegalArgumentException("No such command matched. Unsupported Command = " + strCmd);
	}
	
	private void startMonitor() throws IOException {
		Thread thread = new Thread(new CommandListener(listenPort, this), "CommandListener");
		thread.setDaemon(true);
		thread.start();		
	}
	
	private void startupInvoke(Commandable commandable) {
		try {
			startMonitor();
			String result = commandable.operate();
			
			log.info(result);
			System.out.println(result);
		} catch (Throwable e) {
			log.error("Error occurred when startup invoke.", e);
			System.err.println("Error occurred when startup invoke. " + e.getMessage());
		}
	}
	
	private void localInvoke(Commandable commandable) {
		try {
			String result = commandable.operate();
			
			log.info(result);
			System.out.println(result);
		} catch (Throwable e) {
			log.error("Error occurred when local invoke.", e);
			System.err.println("Error occurred when local invoke. " + e.getMessage());
		}
	}
	
	private void remoteInvoke(String strCmd) {
		Socket socket = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			socket = new Socket(ipAddress, listenPort);
			isr = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(isr);
			writer = new PrintWriter(socket.getOutputStream());
			
			writer.println(strCmd);
			writer.flush();
			String result = reader.readLine();
			
			log.info(result);
			System.out.println(result);
		} catch (Throwable e) {
			log.error("Error occurred when remote invoke.", e);
			System.err.println("Error occurred when remote invoke. " + e.getMessage());
		} finally {
			if (writer != null) writer.close();
			if (reader != null) try {reader.close();} catch (IOException e) {};
			if (isr != null) try {isr.close();} catch (IOException e) {};
			if (socket != null) try {socket.close();} catch (IOException e) {};
		}
	}
}
