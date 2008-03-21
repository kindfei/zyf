package test.basic.charsets;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ReadHtml {
	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream("E:/aaa.html");
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"ISO8859-1"));
			
			String s = "";
			for (String str = reader.readLine(); str != null; str = reader.readLine()) {
				s += str;
			}
			System.out.println("\u9ad8\u91d1\u5229\ufffdB\u901a\u8ca8");
			System.out.println("¢Û");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
