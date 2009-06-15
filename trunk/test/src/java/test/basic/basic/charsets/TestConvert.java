package test.basic.basic.charsets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import sun.io.ByteToCharConverter;
import sun.io.ByteToCharJIS0208;
import sun.io.ByteToCharMS932;
import sun.io.ByteToCharUnicode;
import sun.io.CharToByteConverter;
import sun.io.CharToByteGB18030;
import sun.io.CharToByteMS932;
import sun.io.CharToByteSJIS;
import sun.io.MalformedInputException;
import sun.nio.cs.ext.MS932;

public class TestConvert {
	
	public static void main(String[] args) {
		TestConvert test = new TestConvert();
		test.readFromFile();
	}
	
	byte[] charToByte(String str) {
		byte[] retArray = null;
		
		try {
			CharToByteConverter converter= CharToByteConverter.getConverter("JIS");
			
			retArray = converter.convertAll(str.toCharArray());
			
		} catch (MalformedInputException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retArray;
	}
	
	char[] byteToChar(byte[] args) {
		char[] retArray = null;
		
		try {
			ByteToCharConverter converter = ByteToCharConverter.getConverter("JIS");
			retArray = converter.convertAll(args);
		} catch (MalformedInputException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return retArray;
	}
	
	void readFromFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("D:/ForRead.txt"));
			String readStr = null;
			List list = new ArrayList();
			while ((readStr = reader.readLine()) != null) {
				System.out.println(readStr);
				printByteArray(charToByte(readStr));
				printCharArray(byteToChar(charToByte(readStr)));
				list.add(convertArrayToString(byteToChar(charToByte(readStr))));
			}
			writeToFile(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void writeToFile(List list) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("D:/ForWrite.txt"));
			
			for (int i = 0; i < list.size(); i++) {
				String str = (String)list.get(i);
				System.out.println("write <" + str + "> to file");
				writer.write(str);
				writer.newLine();
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printByteArray(byte[] args) {
		for (int i=0; i<args.length; i++) {
			if(i < args.length - 1) {
				System.out.print(args[i] + ", ");
			} else {
				System.out.print(args[i]);
			}
		}
		System.out.print("\n");
	}
	
	void printCharArray(char[] args) {
		System.out.print("char: ");
		for (int i=0; i<args.length; i++) {
			System.out.print(args[i] + ", ");
		}
		System.out.print("\n");
	}
	
	String convertArrayToString(char[] args) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
		}
		return sb.toString();
	}
}
