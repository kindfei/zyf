package test.basic;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class TestIO {
	
	public static void main(String[] args) {
		try {
			testPipe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void testPipe() throws IOException {
		final PipedInputStream in = new PipedInputStream();
		final PipedOutputStream out = new PipedOutputStream();
		
//		in.connect(out);
		out.connect(in);
		
		new Thread(new Runnable() {

			public void run() {
				try {
					for (int i = in.read(); i != -1; i = in.read()) {
						System.out.println("Received: " + i);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
		for (int i = 0; i < 1000; i++) {
			out.write(i);
			out.flush();
			System.out.println("Sended: " + i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
