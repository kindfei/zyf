package fx.service.core;

import java.io.IOException;
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
								service.remoteExecute(socket);
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
}
