package fx.service.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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

	private String ipAddress;
	private int listenPort;
	private List<ServiceCommand> commands = new ArrayList<ServiceCommand>();
	
	protected ServiceEntry(int listenPort) {
		this("127.0.0.1", listenPort);
	}
	
	protected ServiceEntry(String ipAddress, int listenPort) {
		this.listenPort = listenPort;
		this.ipAddress = ipAddress;
		initCommand();
	}
	
	private void initCommand() {
		ServiceCommand startup = new ServiceCommand("startup", CommandType.STARTUP, new Commandable() {
			public String operate() throws Exception {
				return startup();
			}
		}, "Startup the service, and listen the command on the specified port.");
		
		ServiceCommand shutdown = new ServiceCommand("shutdown", CommandType.REMOTE, new Commandable() {
			public String operate() throws Exception {
				return shutdown();
			}
		}, "Shutdown the service, send the shutdown command to the port that the service listen on.");
		
		commands.add(startup);
		commands.add(shutdown);
	}
	
	public abstract String startup() throws Exception;
	public abstract String shutdown() throws Exception;
	
	public void addCommand(ServiceCommand command) {
		commands.add(command);
	}
	
	public void executeCommand(String[] args) {
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
			help();
		} catch (Throwable e) {
			log.error("Unknown error occurred when execute command.", e);
			System.err.println("Unknown error occurred when execute command. " + e.getMessage());
		}
	}
	
	ServiceCommand searchCommand(String strCmd) {
		for (ServiceCommand command : commands) {
			if (command.getCommand().equals(strCmd)) {
				return command;
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
	
	private void help() {
		log.info("Supported Commands:");
		System.out.println("Supported Commands:");
		
		for (ServiceCommand command : commands) {
			String strCmd = command.getCommand();
			String desc = command.getDescription();
			log.info(strCmd + " - " + desc);
			System.out.println(strCmd + " - " + desc);
		}
	}}
