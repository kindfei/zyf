package fx.service.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Command listener, listen the command which send remotely.
 * @author zhangyf
 *
 */
public class CommandListener implements Runnable {
	private static Log log = LogFactory.getLog(CommandListener.class);
	
	private ServerSocket serverSocket;
	private ServiceEntry service;
	private long count;
	
	CommandListener(int listenPort, ServiceEntry service) throws IOException {
		serverSocket = new ServerSocket(listenPort);
		this.service = service;
	}
	
	public void run() {
		try {
			while (true) {
				final Socket socket = serverSocket.accept();
				Thread thread = new Thread() {
							public void run() {
								execute(socket);
							}
						};
				thread.setDaemon(false);
				thread.setName("CommandOperator-" + count++);
				thread.start();
			}
		} catch (Throwable e) {
			System.err.println("Run CommandListener error.");
			log.error("Run CommandListener error.", e);
		}
	}
	
	private void execute(Socket socket) {
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
				Command command = service.searchCommand(key);
				result = command.execute();
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage(), e);
				result = e.getMessage();
			} catch (Throwable e) {
				log.error("Error occurred when execute command remotely.", e);
				result = "Error occurred when execute command remotely. " + e.getMessage();
			}
			writer.println(result);
			writer.flush();
		} catch (Throwable e) {
			log.error("Execute remote operation error.", e);
		} finally {
			if (writer != null) writer.close();
			if (reader != null) try {reader.close();} catch (IOException e) {};
			if (isr != null) try {isr.close();} catch (IOException e) {};
			if (socket != null) try {socket.close();} catch (IOException e) {};
		}
	}
}
