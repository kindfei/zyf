package test.basic.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class TestHttpPushClient {
	public static void main(String[] args) {
		GetMethod[] objs = new GetMethod[205];
		
		for (int i = 0; i < objs.length; i++) {
			HttpClient client = new HttpClient();
			final GetMethod get = new GetMethod("http://10.0.143.38/demo/servlet/PushServlet");
			try {
				client.executeMethod(get);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			objs[i] = get;
			
//			new Thread(new Runnable() {
//				public void run() {
//					read(get);
//				}
//			}).start();
			
			System.out.println("connect: " + i);
		}
		
		for (int i = 0; i < objs.length; i++) {
			objs[i].releaseConnection();
			System.out.println("release: " + i);
		}
	}
	
	static void read(GetMethod get) {
		try {
			InputStream in = get.getResponseBodyAsStream();
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			char[] c = new char[1];
			for (reader.read(c); c[0] != -1; reader.read(c)) {
//				if (c[0] == 0) continue;
//				else if (c[0] == ',') System.out.println();
//				else System.out.print(c[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
