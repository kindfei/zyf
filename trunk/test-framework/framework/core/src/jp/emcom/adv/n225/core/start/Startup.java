package jp.emcom.adv.n225.core.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author zhangyf
 * 
 */
public class Startup implements Runnable {
	private static final String propsFileName = "jp/emcom/adv/n225/core/start/startup.properties";

	private static final String SHUTDOWN_COMMAND = "SHUTDOWN";

	private Classpath classPath = new Classpath(System.getProperty("java.class.path"));

	private ClassLoader classloader;

	private Config config;

	private ServerSocket serverSocket;

	private Thread serverThread;

	private AtomicBoolean serverStopping = new AtomicBoolean(false);

	private AtomicBoolean serverRunning = new AtomicBoolean(true);

	private List<StartupLoader> loaders;

	/**
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		init(true);
		startServer();
	}

	/**
	 * 
	 */
	public void stop() {
		shutdownServer();
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String firstArg = args.length > 0 ? args[0] : "";

		Startup inst = new Startup();

		if (firstArg.equals("-shutdown")) {
			inst.init(false);
			System.out.println("Shutting down server : " + inst.sendShutdownCommand());
		} else {
			inst.init(true);
			inst.startServer();
		}
	}

	private void startServer() {
		// start the loaders
		for (StartupLoader loader : loaders) {
			try {
				loader.start();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(99);
			}
		}
	}

	private void shutdownServer() {
		if (!serverStopping.compareAndSet(false, true)) {
			return;
		}
		if (loaders != null && loaders.size() > 0) {
			for (StartupLoader loader : loaders) {
				try {
					loader.unload();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		serverRunning.set(false);
	}

	private void init(boolean fullInit) throws IOException {
		config = new Config();

		config.readConfig(propsFileName);

		if (fullInit) {

			initClasspath();

			initListenerThread();

			initStartLoaders();

			if (config.useShutdownHook) {
				setShutdownHook();
			} else {
				System.out.println("Shutdown hook disabled");
			}
		}

	}

	private void initClasspath() throws IOException {
		if (config.coreLib != null) {
			loadLibs(config.coreLib, true);
		}

		if (config.coreJar != null) {
			loadLibs(config.coreJar, true);
		}

		if (config.coreConfig != null) {
			classPath.addComponent(config.coreConfig);
		}

		System.setProperty("java.class.path", classPath.toString());
		this.classloader = classPath.getClassLoader();
		Thread.currentThread().setContextClassLoader(classloader);
	}

	private void loadLibs(String path, boolean recurse) throws IOException {
		File libDir = new File(path);
		if (libDir.exists()) {
			File files[] = libDir.listFiles();
			for (File file : files) {
				String fileName = file.getName();
				if (file.isDirectory() && recurse) {
					loadLibs(file.getCanonicalPath(), recurse);
				} else if (fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
					classPath.addComponent(file);
				}
			}
		}
	}

	private void initListenerThread() throws IOException {
		if (config.adminPort > 0) {
			this.serverSocket = new ServerSocket(config.adminPort, 1, config.adminAddress);
			this.serverThread = new Thread(this, this.toString());
			this.serverThread.setDaemon(false);
			System.out.println("Admin socket configured on - " + config.adminAddress + ":" + config.adminPort);
			this.serverThread.start();
		} else {
			System.out.println("Admin socket not configured; set to port 0");
		}
	}

	public void run() {
		while (serverRunning.get()) {
			try {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Received connection from - " + clientSocket.getInetAddress() + " : " + clientSocket.getPort());
				processClientRequest(clientSocket);
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		shutdownServer();
		System.exit(0);
	}

	private void processClientRequest(Socket client) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		String request = reader.readLine();

		PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
		writer.println(processRequest(request));
		writer.flush();

		writer.close();
		reader.close();
	}

	private String processRequest(String request) {
		if (request != null) {
			String key = request.substring(0, request.indexOf(':'));
			String command = request.substring(request.indexOf(':') + 1);
			if (!key.equals(config.adminKey)) {
				return "FAIL";
			} else {
				if (command.equals(Startup.SHUTDOWN_COMMAND)) {
					if (serverStopping.get()) {
						return "IN-PROGRESS";
					}
					new Thread() {
						public void run() {
							shutdownServer();
						}
					}.start();
					return "OK";
				}
				return "FAIL";
			}
		} else {
			return "FAIL";
		}
	}

	private void initStartLoaders() {
		// initialize the loaders
		for (String loaderClassName : config.loaders) {
			try {
				Class<?> loaderClass = classloader.loadClass(loaderClassName);
				StartupLoader loader = (StartupLoader) loaderClass.newInstance();
				loader.load(config);
				loaders.add(loader);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(99);
			}
		}
	}

	private void setShutdownHook() {
		try {
			Method shutdownHook = java.lang.Runtime.class.getMethod("addShutdownHook", new Class[] { java.lang.Thread.class });
			Thread hook = new Thread() {
				@Override
				public void run() {
					setName("ShutdownHook");
					shutdownServer();
					// Try to avoid JVM crash
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};

			shutdownHook.invoke(Runtime.getRuntime(), new Object[] { hook });
		} catch (Exception e) {
			// VM Does not support shutdown hook
			e.printStackTrace();
		}
	}

	private String sendShutdownCommand() throws IOException, ConnectException {
		Socket socket = new Socket(config.adminAddress, config.adminPort);

		// send the command
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
		writer.println(config.adminKey + ":" + Startup.SHUTDOWN_COMMAND);
		writer.flush();

		// read the reply
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String response = reader.readLine();

		reader.close();

		// close the socket
		writer.close();
		socket.close();

		return response;
	}

	/**
	 * 
	 * @author zhangyf
	 * 
	 */
	public static class Config {
		public String appHome;
		public String coreConfig;
		public String coreLib;
		public String coreJar;
		public String containerConfig;
		public InetAddress adminAddress;
		public int adminPort;
		public String adminKey;
		public boolean useShutdownHook;
		public List<String> loaders;

		public void readConfig(String propsFileName) throws IOException {
			Properties props = this.getPropertiesFile(propsFileName);

			if (appHome == null) {
				appHome = getProp(props, "application.home", ".");
				if (appHome.equals(".")) {
					appHome = System.getProperty("user.dir");
					appHome = appHome.replace('\\', '/');
				}
			}

			String serverHost = getProp(props, "admin.host", "127.0.0.1");
			adminAddress = InetAddress.getByName(serverHost);

			String adminPortStr = getProp(props, "admin.port", "0");
			try {
				adminPort = Integer.parseInt(adminPortStr);
			} catch (Exception e) {
				adminPort = 0;
			}

			adminKey = getProp(props, "admin.key", "");

			useShutdownHook = "true".equalsIgnoreCase(getProp(props, "enable.hook", "true"));

			coreConfig = getHomeProp(props, "core.config", "framework/core/config");

			coreLib = getHomeProp(props, "core.lib", "framework/core/lib");

			coreJar = getHomeProp(props, "core.jar", "framework/core/build/lib");

			containerConfig = getHomeProp(props, "container.config", "framework/core/config/containers.xml");

			loaders = new ArrayList<String>();
			int currentPosition = 1;
			while (true) {
				String loaderClass = props.getProperty("start.loader" + currentPosition);
				if (loaderClass == null || loaderClass.length() == 0) {
					break;
				} else {
					loaders.add(loaderClass);
					currentPosition++;
				}
			}
		}

		private String getProp(Properties props, String key, String def) {
			String value = System.getProperty(key);
			if (value != null && value.length() > 0)
				return value;
			return props.getProperty(key, def);
		}

		private String getHomeProp(Properties props, String key, String def) {
			String value = System.getProperty(key);
			if (value != null && value.length() > 0)
				return value;
			return appHome + "/" + props.getProperty(key, def);
		}

		private Properties getPropertiesFile(String propsFileName) throws IOException {
			InputStream propsStream = null;
			Properties props = new Properties();
			try {
				// first try classpath
				propsStream = getClass().getClassLoader().getResourceAsStream(propsFileName);
				if (propsStream != null) {
					props.load(propsStream);
				} else {
					throw new IOException();
				}
			} catch (IOException e) {
				// next try file location
				File propsFile = new File(propsFileName);
				if (propsFile != null) {
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(propsFile);
						if (fis != null) {
							props.load(fis);
						} else {
							throw new FileNotFoundException();
						}
					} catch (FileNotFoundException e2) {
						// do nothing; we will see empty props below
					} finally {
						if (fis != null) {
							fis.close();
						}
					}
				}
			} finally {
				if (propsStream != null) {
					propsStream.close();
				}
			}

			// check for empty properties
			if (props.isEmpty()) {
				throw new IOException("Cannot load configuration properties : " + propsFileName);
			}
			return props;
		}
	}
}
