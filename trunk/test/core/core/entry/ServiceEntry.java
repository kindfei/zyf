package core.entry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service entry, help bootstrap and manage service.
 * @author zhangyf
 *
 */
public abstract class ServiceEntry extends EntryMBean {
	private static Log log = LogFactory.getLog(ServiceEntry.class);

	private ServiceDefinition definition;
	
	private Map<String, Command> commands = new HashMap<String, Command>();
	
	public ServiceEntry() {
		String serviceName = System.getProperty("SERVICE_NAME");
		definition = ServiceDefinition.getServiceDefinition(serviceName);
		if (definition == null) {
			definition = ServiceDefinition.getDefWithClassName(this.getClass().getName());
		}
		if (definition == null) {
			throw new RuntimeException("No service definition for serviceName=" + serviceName + ", className=" + this.getClass().getName());
		}
		addBasicCmd();
		addAnnotCmd();
	}
	
	public void addCommand(NormalCommand command) {
		commands.put(command.getKey(), command);
	}
	
	public abstract String startup() throws Exception;
	public abstract String shutdown() throws Exception;
	
	private void addBasicCmd() {
		addCommand(new NormalCommand("startup", CommandType.STARTUP, 
				"Startup the service, and listen command on the specified port.", 
				new Commandable() {
					public Object execute(Object[] params) throws Exception {
						return startup();
					}
				}
		));
		
		addCommand(new NormalCommand("shutdown", CommandType.REMOTE, 
				"Shutdown the service, send the shutdown command to the port that the service listen on.", 
				new Commandable() {
					public Object execute(Object[] params) throws Exception {
						try {
							if (definition.isStartJMX()) stopServer();
							return shutdown();
						} finally {
							new Timer(true).schedule(new TimerTask() {
								public void run() {
									System.exit(0);
								};
							}, 5000);
						}
					}
				}
		));
	}
	
	private void addAnnotCmd() {
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			CMD cmd = method.getAnnotation(CMD.class);
			if (cmd != null) {
				commands.put(cmd.key(), new RelfectionCommand(cmd.key(), cmd.type(), cmd.description(), this, method));
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
				if (definition.isStartJMX()) startServer();
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
			log.error(e.getMessage(), e);
			printHelp();
		} catch (IOException e) {
			log.error("Start command listener error.", e);
		} catch (Throwable e) {
			log.error("Unknown error occurred when execute command.", e);
		}
	}
	
	private Command search(String key) {
		Command command = commands.get(key);
		
		if (command == null)
			throw new IllegalArgumentException("No such command matched. Unsupported Command = " + key);
		
		return command;
	}
	
	private String execute(Command command) {
		String result = null;
		String key = command.getKey();
		try {
			Object obj = command.execute(null);
			result = "Execute OK. command=" + key + ", result=" + obj;
			log.info(result);
		} catch (Throwable e) {
			result = "Execute error. command=" + key + ", error=" + e.getMessage();
			log.error(result, e);
		}
		return result;
	}
	
	private void startListener() throws IOException {
		Thread thread = new Thread("CommandListener") {
			private long count;
			
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(definition.getListenPort());
					
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
			socket = new Socket(definition.getIpAddress(), definition.getListenPort());
			isr = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(isr);
			writer = new PrintWriter(socket.getOutputStream());
			
			writer.println(key);
			writer.flush();
			String result = reader.readLine();
			
			log.info(result);
		} catch (Throwable e) {
			log.error("Send command to remote service error.", e);
		} finally {
			if (writer != null) writer.close();
			if (reader != null) try {reader.close();} catch (IOException e) {};
			if (isr != null) try {isr.close();} catch (IOException e) {};
			if (socket != null) try {socket.close();} catch (IOException e) {};
		}
	}
	
	private void onCommand(Socket socket) {
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
				log.error(e.getMessage(), e);
				result = e.getMessage();
			}
			
			writer.println(result);
			writer.flush();
		} catch (Throwable e) {
			log.error("Receive remote command error.", e);
		} finally {
			if (writer != null) writer.close();
			if (reader != null) try {reader.close();} catch (IOException e) {};
			if (isr != null) try {isr.close();} catch (IOException e) {};
			if (socket != null) try {socket.close();} catch (IOException e) {};
		}
	}
	
	private void printHelp() {
		log.info("Supported Commands:");
		
		for (Command command : commands.values()) {
			String key = command.getKey();
			String desc = command.getDescription();
			log.info(key + " - " + desc);
		}
	}
}
