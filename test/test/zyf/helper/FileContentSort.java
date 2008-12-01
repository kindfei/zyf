package zyf.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileContentSort {
	public static void main(String[] args) {
		String from = "E:/PuttyDefaultSetting.properties";
		String to = "E:/PuttyDefaultSetting.properties";
		String charsetName = "GB2312";
		try {
			List<String> list = readFile(from, charsetName);
			writeFile(to, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static List<String> readFile(String path, String charsetName) throws IOException {
		FileInputStream in = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(in, charsetName);
		BufferedReader reader = new BufferedReader(isr);
		
		List<String> list = new ArrayList<String>();
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			if (!line.equals("")) {
				list.add(line);
			}
		}

		reader.close();
		isr.close();
		in.close();
		
		Comparator<String> c = new Comparator<String>() {
			public int compare(String o1, String o2) {
				String str1 = o1.toLowerCase();
				String str2 = o2.toLowerCase();
				
				char[] chars1 = str1.toCharArray();
				char[] chars2 = str2.toCharArray();
				
				int len1 = chars1.length;
				int len2 = chars2.length;
				
				int ret = 0;
				int len = 0;
				if (len1 > len2) {
					len = len2;
					ret = 1;
				} else if (len1 < len2) {
					len = len1;
					ret = -1;
				} else {
					len = len1;
					ret = 0;
				}
				
				for (int i = 0; i < len; i++) {
					if (chars1[i] > chars2[i]) {
						return 1;
					} else if (chars1[i] < chars2[i]) {
						return -1;
					}
				}
				
				return ret;
			}
		};
		
		Collections.sort(list, c);
		
		return list;
	}
	
	private static void writeFile(String path, List<String> list) throws IOException {
		FileOutputStream fos = new FileOutputStream(path);
		PrintWriter writer = new PrintWriter(fos);
		
		for (String line : list) {
			writer.println(line);
		}
		
		writer.close();
		fos.close();
	}
}
