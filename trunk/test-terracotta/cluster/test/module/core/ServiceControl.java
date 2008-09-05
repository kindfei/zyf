package test.module.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service control, listen a command from the specified port.
 * @author zhangyf
 *
 */
public class ServiceControl implements Runnable {
	private static Log log = LogFactory.getLog(ServiceControl.class);
	
	private ServerSocket serverSocket;
	private ServiceEntry instance;
	private boolean isAlive = true;
	
	public ServiceControl(int port, ServiceEntry inst) throws IOException {
		serverSocket = new ServerSocket(port);
		instance = inst;
	}
	
	public void run() {
		while (isAlive) try {
			final Socket socket = serverSocket.accept();
			Thread thread = new Thread() {
						public void run() {
							execute(socket);
						}
					};
			thread.setDaemon(false);
			thread.setName("Control Session");
			thread.start();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	private void execute(Socket socket) {
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			
			String cmd = reader.readLine();
			String ret;
			try {
				ret = instance.invoke(cmd);
			} catch (Exception e) {
				ret = e.getMessage();
			}
			writer.println(ret);
			writer.flush();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (writer != null) writer.close();
			if (reader != null) try {reader.close();} catch (IOException e) {};
			try {socket.close();} catch (IOException e) {}
		}
	}
}
