package test.basic.basic.charsets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestEncode {

	public static void main(String[] args) {
		TestEncode tc = new TestEncode();
		tc.testEncode();

	}
	
	private static final int append = 1;
	private static final int overWrite = 2;

	void testEncode() {
		//String str = "?????????";
		String str = "\uc4e3bac3";
		TestConvert tc = new TestConvert();
		try {
			System.out.print("JIS:         ");
			tc.printByteArray(str.getBytes("JIS"));
			System.out.print("Shift_JIS:   ");
			tc.printByteArray(str.getBytes("Shift_JIS"));
			System.out.print("Windows-31J: ");
			tc.printByteArray(str.getBytes("Windows-31J"));
			System.out.print("GB2312:      ");
			tc.printByteArray(str.getBytes("GB2312"));
			System.out.print("GBK:         ");
			tc.printByteArray(str.getBytes("GBK"));
			System.out.print("8859_1:      ");
			tc.printByteArray(str.getBytes("8859_1"));
			
			String temp = null;
			
			String path = "D:/test.txt";
			
			temp = "JIS->8859_1:         " + new String(str.getBytes("JIS"), "8859_1");
			System.out.println(temp);
			writeToFile(temp, TestEncode.append, path);
			
			temp = "Shift_JIS->8859_1:   " + new String(str.getBytes("Shift_JIS"), "8859_1");
			System.out.println(temp);
			writeToFile(temp, TestEncode.append, path);
			
			temp = "Windows-31J->8859_1: " + new String(str.getBytes("Windows-31J"), "8859_1");
			System.out.println(temp);
			writeToFile(temp, TestEncode.append, path);
			
			temp = "GB2312->8859_1:      " + new String(str.getBytes("GB2312"), "8859_1");
			System.out.println(temp);
			writeToFile(temp, TestEncode.append, path);
			
			temp = "GBK->8859_1:         " + new String(str.getBytes("GBK"), "8859_1");
			System.out.println(temp);
			writeToFile(temp, TestEncode.append, path);
			
			temp = "JIS->GB2312:         " + new String(str.getBytes("JIS"), "GB2312");
			System.out.println(temp);
			writeToFile(temp, TestEncode.append, path);
			
			temp = "JIS->GBK:            " + new String(str.getBytes("JIS"), "GBK");
			System.out.println(temp);
			writeToFile(temp, TestEncode.append, path);
			
			temp = "8859_1->Windows-31J: " + new String(str.getBytes("8859_1"), "Windows-31J");
			System.out.println(temp);
			writeToFile(temp, TestEncode.append, path);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * writeToFile
	 * @param str
	 * @param i 1 apeend, 2 over write
	 * @param path
	 */
	public static void writeToFile(String str, int i, String path) {
		BufferedWriter bw = null;
		try {
			if (i == 1) {
				List list = readFromFile(path);
				bw = new BufferedWriter(new FileWriter(path));
				
				Iterator irt = list.iterator();
				while(irt.hasNext()) {
					bw.write((String)irt.next());
					bw.newLine();
				}
				bw.write(str);
				bw.newLine();
				bw.flush();
			} else if (i == 2) {
				bw = new BufferedWriter(new FileWriter(path));
				bw.write(str);
				bw.flush();
			} else {
				System.err.println("parameter wrong!");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List readFromFile(String path) {
		ArrayList list = new ArrayList();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
			String temp = null;
			while((temp = br.readLine()) != null) {
				list.add(temp);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return list;
	}
}
