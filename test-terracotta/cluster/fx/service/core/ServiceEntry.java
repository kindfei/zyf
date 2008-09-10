package fx.service.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
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
	private List<Command> commands = new ArrayList<Command>();
	
	public ServiceEntry(int listenPort) {
		this("127.0.0.1", listenPort);
	}
	
	public ServiceEntry(String ipAddress, int listenPort) {
		this.listenPort = listenPort;
		this.ipAddress = ipAddress;
		addBasicCmd();
		addAnnotCmd();
	}
	
	public void executeCommand(String[] args) {
		try {
			if (args == null || args.length == 0) {
				throw new IllegalArgumentException("No program arguments was specified as a command.");
			}
			
			String key = args[0];
			
			Command command = searchCommand(key);
			
			CommandType type = command.getType();
			
			switch (type) {
			case STARTUP:
				startupInvoke(command);
				break;

			case REMOTE:
				remoteInvoke(key);
				break;

			case LOCAL:
				localInvoke(command);
				break;
			}
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
			System.err.println(e.getMessage());
			printHelp();
		} catch (Throwable e) {
			log.error("Unknown error occurred when execute command.", e);
			System.err.println("Unknown error occurred when execute command. " + e.getMessage());
		}
	}
	
	public void addCommand(NormalCommand command) {
		commands.add(command);
	}
	
	public abstract String startup() throws Exception;
	public abstract String shutdown() throws Exception;
	
	private void addBasicCmd() {
		addCommand(new NormalCommand("startup", CommandType.STARTUP, 
				"Startup the service, and listen the command on the specified port.", 
				new Commandable() {
					public String execute() throws Exception {
						return startup();
					}
				}
		));
		
		addCommand(new NormalCommand("shutdown", CommandType.REMOTE, 
				"Shutdown the service, send the shutdown command to the port that the service listen on.", 
				new Commandable() {
					public String execute() throws Exception {
						return shutdown();
					}
				}
		));
	}
	
	private void addAnnotCmd() {
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			CMD cmd = method.getAnnotation(CMD.class);
			if (cmd != null) {
				commands.add(new RelfectionCommand(cmd.key(), cmd.type(), cmd.description(), this, method));
			}
		}
	}
	
	Command searchCommand(String key) {
		for (Command command : commands) {
			if (command.getKey().equals(key)) {
				return command;
			}
		}
		
		throw new IllegalArgumentException("No such command matched. Unsupported Command = " + key);
	}
	
	private void startMonitor() throws IOException {
		Thread thread = new Thread(new CommandListener(listenPort, this), "CommandListener");
		thread.setDaemon(true);
		thread.start();		
	}
	
	private void startupInvoke(Command command) {
		try {
			startMonitor();
			String result = command.execute();
			
			log.info(result);
			System.out.println(result);
		} catch (Throwable e) {
			log.error("Error occurred when startup invoke.", e);
			System.err.println("Error occurred when startup invoke. " + e.getMessage());
		}
	}
	
	private void localInvoke(Command command) {
		try {
			String result = command.execute();
			
			log.info(result);
			System.out.println(result);
		} catch (Throwable e) {
			log.error("Error occurred when local invoke.", e);
			System.err.println("Error occurred when local invoke. " + e.getMessage());
		}
	}
	
	private void remoteInvoke(String key) {
		Socket socket = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			socket = new Socket(ipAddress, listenPort);
			isr = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(isr);
			writer = new PrintWriter(socket.getOutputStream());
			
			writer.println(key);
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
	
	private void printHelp() {
		log.info("Supported Commands:");
		System.out.println("Supported Commands:");
		
		for (Command command : commands) {
			String key = command.getKey();
			String desc = command.getDescription();
			log.info(key + " - " + desc);
			System.out.println(key + " - " + desc);
		}
	}}
