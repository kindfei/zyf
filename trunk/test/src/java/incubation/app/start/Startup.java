package incubation.app.start;

import incubation.app.container.Container;
import incubation.app.container.ContainerConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import static incubation.app.start.ConfigurationConstants.*;

/**
 * 
 * @author zhangyf
 * 
 */
public class Startup implements Runnable {
	private static final String propsFileName = "incubation/app/start/startup.properties";

	private static final String SHUTDOWN_COMMAND = "SHUTDOWN";

	private Classpath classPath = new Classpath(System.getProperty("java.class.path"));

	private ClassLoader classloader;

	private Config config;

	private ServerSocket serverSocket;

	private Thread serverThread;

	private AtomicBoolean serverStopping = new AtomicBoolean(false);

	private AtomicBoolean serverRunning = new AtomicBoolean(true);

	private List<Container> loadedContainers = new LinkedList<Container>();

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
		// start the containers
		for (Container container : loadedContainers) {
			try {
				container.start();
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
		for (int i = loadedContainers.size(); i > 0; i--) {
			Container container = loadedContainers.get(i - 1);
			try {
				container.stop();
			} catch (Exception e) {
				e.printStackTrace();
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

			initContainers();

			if (config.useShutdownHook) {
				setShutdownHook();
			} else {
				System.out.println("Shutdown hook disabled");
			}
		}

	}

	private void initClasspath() throws IOException {
		if (config.baseLib != null) {
			loadLibs(config.baseLib, true);
		}

		if (config.baseJar != null) {
			loadLibs(config.baseJar, true);
		}

		if (config.baseConfig != null) {
			classPath.addComponent(config.baseConfig);
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
				System.out.println("Received connection from - " + clientSocket.getInetAddress() + " : "
						+ clientSocket.getPort());
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

	private void initContainers() {
		// initialize the containers
		try {
			Collection<ContainerConfig.Container> containers = ContainerConfig.getContainers(config.containerConfig);

			if (containers != null) {
				for (ContainerConfig.Container containerCfg : containers) {
					loadedContainers.add(loadContainer(containerCfg));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(99);
		}
	}

	private Container loadContainer(ContainerConfig.Container containerCfg) throws Exception {
		// load the container class
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = ClassLoader.getSystemClassLoader();
		}

		Class<?> containerClass = loader.loadClass(containerCfg.className);

		if (containerClass == null) {
			throw new Exception("Component container class not loaded");
		}

		// create a new instance of the container object
		Container containerObj = (Container) containerClass.newInstance();

		if (containerObj == null) {
			throw new Exception("Unable to create instance of component container");
		}

		// initialize the container object
		containerObj.init(config.containerConfig);

		return containerObj;
	}

	private void setShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
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
		});
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
		public String baseConfig;
		public String baseLib;
		public String baseJar;
		public String containerConfig;
		public InetAddress adminAddress;
		public int adminPort;
		public String adminKey;
		public boolean useShutdownHook;

		public void readConfig(String propsFileName) throws IOException {
			Properties props = this.getPropertiesFile(propsFileName);

            if (appHome == null) {
            	appHome = props.getProperty(APP_HOME, ".");
                if (appHome.equals(".")) {
                	appHome = System.getProperty("user.dir");
                	appHome = appHome.replace('\\', '/');
                }
            }
            System.setProperty(APP_HOME, appHome);
            
			adminAddress = InetAddress.getByName(getProp(props, ADMIN_HOST, "127.0.0.1"));
			adminPort = Integer.parseInt(getProp(props, ADMIN_PORT, "0"));
			adminKey = getProp(props, ADMIN_KEY, "NA");
			useShutdownHook = "true".equalsIgnoreCase(getProp(props, ENABLE_HOOK, "true"));
			baseConfig = getHomeProp(props, BASE_CONFIG);
			baseLib = getHomeProp(props, BASE_LIB);
			baseJar = getHomeProp(props, BASE_JAR);
			containerConfig = getHomeProp(props, CONTAINER_CONFIG);
		}

		private String getProp(Properties props, String key, String defaultValue) {
			String value = System.getProperty(key);
			if (value != null && value.length() > 0)
				return value;
			return props.getProperty(key, defaultValue);
		}

		private String getHomeProp(Properties props, String key) {
			String value = System.getProperty(key);
			if (value != null && value.length() > 0)
				return value;
			return appHome + "/" + props.getProperty(key);
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
