package test.basic;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSocket {
	public static void main(String[] args) {
		try {
			createServerSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createServerSocket() throws IOException {
		ServerSocket server = new ServerSocket(2900);
		
		while (true) {
			final Socket socket = server.accept();
			
			new Thread(new Runnable() {

				public void run() {
					try {
						process(socket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}).start();
		}
	}
	
	private static void process(Socket socket) throws IOException {
		OutputStream out = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		for (int i = 0; i < 2000; i++) {
			writer.write(sdf.format(new Date()) + ",");
			writer.flush();
			
			System.out.println(Thread.currentThread().getName() + " - " + i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
