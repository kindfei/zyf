package test.core.entry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	
	public void addCommand(NormalCommand command) {
		commands.add(command);
	}
	
	public abstract String startup() throws Exception;
	public abstract String shutdown() throws Exception;
	
	private void addBasicCmd() {
		addCommand(new NormalCommand("startup", CommandType.STARTUP, 
				"Startup the service, and listen command on the specified port.", 
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
						String result = null;
						try {
							result = shutdown();
						} finally {
							new Timer(true).schedule(new TimerTask() {
								public void run() {
									System.exit(0);
								};
							}, 5000);
						}
						return result;
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
	
	public void process(String... args) {
		try {
			if (args == null || args.length == 0) {
				throw new IllegalArgumentException("No program arguments was specified as a command.");
			}
			
			String key = args[0];
			
			Command command = search(key);
			
			CommandType type = command.getType();
			
			switch (type) {
			case STARTUP:
				startListener();
				execute(command);
				break;

			case REMOTE:
				remoteInvoke(key);
				break;

			case LOCAL:
				execute(command);
				break;
			}
		} catch (IllegalArgumentException e) {
			errorLog(e.getMessage(), e);
			printHelp();
		} catch (IOException e) {
			errorLog("Start command listener error.", e);
		} catch (Throwable e) {
			errorLog("Unknown error occurred when execute command.", e);
		}
	}
	
	private Command search(String key) {
		for (Command command : commands) {
			if (command.getKey().equals(key)) {
				return command;
			}
		}
		
		throw new IllegalArgumentException("No such command matched. Unsupported Command = " + key);
	}
	
	private String execute(Command command) {
		String result = null;
		String key = command.getKey();
		try {
			result = command.execute();
			result = "Execute OK. command=" + key + ", result=" + result;
			infoLog(result);
		} catch (Throwable e) {
			result = "Execute error. command=" + key + ", error=" + e.getMessage();
			errorLog(result, e);
		}
		return result;
	}
	
	private void startListener() throws IOException {
		Thread thread = new Thread("CommandListener") {
			private long count;
			
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(listenPort);
					
					while (true) {
						final Socket socket = serverSocket.accept();
						Thread thread = new Thread("CommandExecutor-" + count++) {
							public void run() {
								onCommand(socket);
							}
						};
						thread.setDaemon(false);
						thread.start();
					}
				} catch (Throwable e) {
					log.error("Listen Command error.", e);
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
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
			
			infoLog(result);
		} catch (Throwable e) {
			errorLog("Send command to remote service error.", e);
		} finally {
			if (writer != null) writer.close();
			if (reader != null) try {reader.close();} catch (IOException e) {};
			if (isr != null) try {isr.close();} catch (IOException e) {};
			if (socket != null) try {socket.close();} catch (IOException e) {};
		}
	}
	
	void onCommand(Socket socket) {
		InputStreamReader isr = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			isr = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(isr);
			writer = new PrintWriter(socket.getOutputStream());
			
			String key = reader.readLine();
			String result = null;
			
			try {
				
				Command command = search(key);
				
				result = execute(command);
				
			} catch (IllegalArgumentException e) {
				errorLog(e.getMessage(), e);
				result = e.getMessage();
			}
			
			writer.println(result);
			writer.flush();
		} catch (Throwable e) {
			errorLog("Receive remote command error.", e);
		} finally {
			if (writer != null) writer.close();
			if (reader != null) try {reader.close();} catch (IOException e) {};
			if (isr != null) try {isr.close();} catch (IOException e) {};
			if (socket != null) try {socket.close();} catch (IOException e) {};
		}
	}
	
	private void printHelp() {
		infoLog("Supported Commands:");
		
		for (Command command : commands) {
			String key = command.getKey();
			String desc = command.getDescription();
			infoLog(key + " - " + desc);
		}
	}
	
	private void errorLog(String msg, Throwable e) {
		log.error(msg, e);
//		System.err.println(msg);
	}
	
	private void infoLog(String msg) {
		log.info(msg);
//		System.out.println(msg);
	}
}
