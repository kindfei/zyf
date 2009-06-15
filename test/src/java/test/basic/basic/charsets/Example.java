package test.basic.basic.charsets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import sun.io.ByteToCharConverter;
import sun.io.CharToByteConverter;

public class Example {
	
	static String path = "D:/ForRead.txt";
	
	public static void main(String[] args) {
		try {
			Example test = new Example();
			test.main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	String defaultEncode;
	
	/**
	 * main
	 * @throws Exception
	 */
	void main() throws Exception {
		
		defaultEncode = System.getProperty("file.encoding");
		System.out.println("<<< System default encode is " + defaultEncode + " >>>");
		System.out.println(System.getProperty("JVM.encoding"));
		
		//char[] c = readFile("D:/forread.txt", "shift_JIS");
		char[] c = read("D:/forread.txt");
		
		String str = new String(c);
		System.out.println(str);
		
		String pStr = "unicode:    " + str;
		pStr = pStr + "\n";
		pStr = pStr + "\n*GB2312:     " + convert(str, "GB2312", "GB2312");
		pStr = pStr + "\n#GB2312:     " + convert(c, "GB2312", "GB2312");
		pStr = pStr + "\n";
		pStr = pStr + "\n*GBK:        " + convert(str, "GBK", "GBK");
		pStr = pStr + "\n#GBK:        " + convert(c, "GBK", "GBK");
		pStr = pStr + "\n";
		pStr = pStr + "\n*shift_JIS:  " + convert(str, "shift_JIS", "shift_JIS");
		pStr = pStr + "\n#shift_JIS:  " + convert(c, "shift_JIS", "shift_JIS");
		pStr = pStr + "\n";
		pStr = pStr + "\n*JIS:        " + convert(str, "JIS", "JIS");
		pStr = pStr + "\n#JIS:        " + convert(c, "JIS", "JIS");
		pStr = pStr + "\n";
		pStr = pStr + "\n*8859_1:     " + convert(str, "8859_1", "8859_1");
		pStr = pStr + "\n#8859_1:     " + convert(c, "8859_1", "8859_1");
		pStr = pStr + "\n";
		pStr = pStr + "\n*GBK->SJIS:  " + convert(str, "GBK", "SJIS");
		pStr = pStr + "\n#GBK->SJIS:  " + convert(c, "GBK", "SJIS");
		
	    //writeFile(pStr, "D:/forwrite.txt", "shift_JIS");
	    write(pStr, "D:/forwrite.txt");
	}
	
	/**
	 * convert
	 * @param in
	 * @param encode
	 * @param decode
	 * @return
	 * @throws Exception
	 */
	String convert(String in, String encode, String decode) throws Exception {
		System.out.print("\n");
		System.out.println("******** encode: " + encode + " ******** ");
		
		// encode String to byte
		byte[] b = in.getBytes(encode);
		
		for(int i=0; i<b.length; i++) {
			if(i != 0) System.out.print(", ");
			System.out.print(b[i]);
		}
		System.out.print("\n");
		
		for(int i=0; i<b.length; i++) {
			if(i != 0) System.out.print(", ");
			System.out.print(Integer.toHexString(b[i]));
		}
		System.out.print("\n");
		
		// decode byte to String
		String str = new String(b, decode);
		System.out.println(str);
		
		System.out.println("******** decode: " + decode + " **********");
		return str;
	}
	
	/**
	 * convert
	 * @param in
	 * @param encode
	 * @param decode
	 * @return
	 * @throws Exception
	 */
	String convert(char[] in, String encode, String decode) throws Exception {
		System.out.print("\n");
		System.out.println("######## encode: " + encode + " ######## ");
		
		// encode
		CharToByteConverter ctb = CharToByteConverter.getConverter(encode);
	    byte b[] = ctb.convertAll(in);
	    
		for(int i=0; i<b.length; i++) {
			if(i != 0) System.out.print(", ");
			System.out.print(b[i]);
		}
		System.out.print("\n");
	    
		for(int i=0; i<b.length; i++) {
			if(i != 0) System.out.print(", ");
			System.out.print(Integer.toHexString(b[i]));
		}
		System.out.print("\n");
	    
	    // decode
		ByteToCharConverter btc = ByteToCharConverter.getConverter(decode);
		char c[] = btc.convertAll(b);
	    
		String str = new String(c);
		System.out.println(str);
		
		System.out.println("######## decode: " + decode + " ######## ");
		return str;
	}
	
	/**
	 * read
	 * @param path
	 * @return
	 * @throws Exception
	 */
	char[] read(String path) throws Exception {
		char[] temp = null;
		FileReader reader = null;
		try {
			reader = new FileReader(path);
			temp = new char[1000];
			reader.read(temp);
			System.out.println("<<< Read encode is " + reader.getEncoding() + " >>>");
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if(reader != null) reader.close();
			} catch (IOException e) {
			}
		}
		
		int cnt = 0;
		for(int i=0; i<temp.length; i++) {
			if(temp[i] != 0) {
				cnt++;
				if(i != 0) System.out.print(", ");
				System.out.print(temp[i]);
			}
		}
		System.out.print("\n");
		
		char[] c = new char[cnt];
		int index = 0;
		System.out.print("unicode: ");
		for(int i=0; i<temp.length; i++) {
			if (temp[i] != 0) {
				c[index++] = temp[i];
				if(i != 0) System.out.print(", ");
				System.out.print(Integer.toHexString(temp[i]));
			}
		}
		System.out.print("\n");
		return c;
	}
	
	/**
	 * write
	 * @param str
	 * @param path
	 * @throws Exception
	 */
	void write(String str, String path) throws Exception {
		FileWriter writer = null;
		
	    try {
			writer = new FileWriter(path);
			writer.write(str);
			writer.flush();
			System.out.println("<<< Write encode is " + writer.getEncoding() + " >>>");
		} catch (IOException e) {
		    throw e;
		} finally {
			try {
				if(writer != null) writer.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * readFile
	 * @param path
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	char[] readFile(String path, String charsetName) throws Exception {
		char[] temp = null;
		FileInputStream fis = null;
		InputStreamReader reader = null;
		try {
			fis = new FileInputStream(path);
			reader = new InputStreamReader(fis, charsetName);
			temp = new char[1000];
			reader.read(temp);
			System.out.println("<<< Read encode is " + reader.getEncoding() + " >>>");
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if(fis != null) fis.close();
				if(reader != null) reader.close();
			} catch (IOException e) {
			}
		}
		
		int cnt = 0;
		for(int i=0; i<temp.length; i++) {
			if(temp[i] != 0) {
				cnt++;
				if(i != 0) System.out.print(", ");
				System.out.print(temp[i]);
			}
		}
		System.out.print("\n");
		
		char[] c = new char[cnt];
		int index = 0;
		System.out.print("unicode: ");
		for(int i=0; i<temp.length; i++) {
			if (temp[i] != 0) {
				c[index++] = temp[i];
				if(i != 0) System.out.print(", ");
				System.out.print(Integer.toHexString(temp[i]));
			}
		}
		System.out.print("\n");
		return c;
	}
	
	/**
	 * writeFile
	 * @param str
	 * @param path
	 * @param charsetName
	 * @throws Exception
	 */
	void writeFile(String str, String path, String charsetName) throws Exception {
		FileOutputStream fos = null;
		OutputStreamWriter writer = null;
		
		try {
			fos = new FileOutputStream(path);
			writer = new OutputStreamWriter(fos, charsetName);
			writer.write(str.toCharArray());
			writer.flush();
			System.out.println("<<< Write encode is " + writer.getEncoding() + " >>>");
		} catch (FileNotFoundException e) {
			throw e;
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if(fos != null) fos.close();
				if(writer != null) writer.close();
			} catch (IOException e) {
			}
		}
	}
	
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/                     
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/                     
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
	
	/**
	 * string2byte
	 * @throws Exception
	 */
	void string2byte() throws Exception {
		String encoding = "GB2312";
		byte[] c = "\uc4e3".getBytes(encoding);
		for (int i = 0; i < c.length; i++) {
			System.out.println(c[i]);
			System.out.println("HEX: " + Integer.toHexString(c[i]));
		}
	}
	
	/**
	 * byte2string
	 * @throws Exception
	 */
	void byte2string() throws Exception {
		String encoding = "GB2312";
		byte[] b = {(byte)'\u00c4', (byte)'\u00e3'}; 
		String str=new String(b, encoding);
		System.out.println(str);
	}
	
	/**
	 * char2byte
	 * @throws Exception
	 */
	void char2byte() throws Exception {
	    String encoding = "gb2312";
	    char c[] = {'\u4f60'};
	    CharToByteConverter converter = CharToByteConverter.getConverter(encoding);
	    byte b[] = converter.convertAll(c);
	    for (int i = 0; i < b.length; i++) {
	    	System.out.println(b[i]);
	    	System.out.println(Integer.toHexString(b[i]));
	    }
	}
	
	/**
	 * byte2char
	 * @throws Exception
	 */
	void byte2char() throws Exception {
		String charsetGB2312 = "gb2312";
		
		byte b[] = {(byte)'\u00c4',(byte)'\u00e3'};
		
		ByteToCharConverter converter = ByteToCharConverter.getConverter(charsetGB2312);
		char c[] = converter.convertAll(b);
		for (int i = 0; i < c.length; i++) {
			System.out.println(c[i]);
			System.out.println(Integer.toHexString(c[i]));
		}
	}
	
	/**
	 * testFile
	 *
	 */
	void testFile() {
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "GBK");
		    char c[] = new char[100];
		    int length = reader.read(c);
		    for (int i = 0; i < length; i++) {
		       System.out.print(c[i]);
		    }
		     
		    OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path), "shift_jis");
		    writer.write("??????".toCharArray());
		    writer.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
