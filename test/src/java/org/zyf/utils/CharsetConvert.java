package org.zyf.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class CharsetConvert {
	
	private String srcCharset;
	private String tarCharset;
	
	public CharsetConvert(String s2, String s3) {
		srcCharset = s2;
		tarCharset = s3;
	}
	
	public void walkFolder(File root) {
		File[] files = root.listFiles();
		for (int i=0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				walkFolder(file);
			} else {
				write(read(file), file);
			}
		}
	}
	
	private String read(File file) {
		String content = "";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, srcCharset);
			reader = new BufferedReader(isr);

			String str = reader.readLine();
			for (; str != null; str = reader.readLine()) {
				content += str + "\n";
			}
			
			System.out.println("<<< Read file " + file.getName() + ", Encoding is " + isr.getEncoding() + " >>>");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
			if(isr != null)
				try {
					isr.close();
				} catch (IOException e) {
				}
			if(fis != null)
				try {
					fis.close();
				} catch (IOException e) {
				}
		}
		return content;
	}
	
	private void write(String content, File file) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, tarCharset);
			
			osw.write(content);
			osw.flush();
			
			System.out.println("<<< Write file " + file.getName() + ", Encoding is " + osw.getEncoding() + " >>>");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(osw != null)
				try {
					osw.close();
				} catch (IOException e) {
				}
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e) {
				}
		}
	}
	
	public static void main(String[] args) {
		CharsetConvert test = new CharsetConvert(args[1], args[2]);
		File root = new File(args[0]);
		test.walkFolder(root);
	}
}
