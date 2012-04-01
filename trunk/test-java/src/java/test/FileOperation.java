package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileOperation {
	
	public static void main(String[] args) throws Exception {
		test1();
	}
	
	
	static void test1() throws IOException {
		String fileName = "C:/Documents and Settings/yz69579/Desktop/test.txt";

		StringBuilder content = new StringBuilder();
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		for (String line; (line = reader.readLine()) != null;) {
			content.append(line).append("\r\n");
		}
		reader.close();

		FileWriter writer = new FileWriter(fileName);
		writer.write(content.toString());
		writer.close();
	}
	
	static void test2() throws IOException {
		RandomAccessFile raf = new RandomAccessFile(new File("C:/Documents and Settings/yz69579/Desktop/test.txt"), "rw");
		
		for (int i; (i = raf.read()) != -1;) {
			if (i == '\n') {
				boolean convert = false;
				long l = raf.getFilePointer() - 2;
				if (l < 0) {
					raf.seek(0);
					convert = true;
				} else {
					raf.seek(l);
					if (raf.read() != '\r') {
						convert = true;
					} else {
						raf.skipBytes(1);
					}
				}
				if (convert) {
					raf.write('\r');
					int prev = '\n';
					for (int next; (next = raf.read()) != -1; prev = next) {
						raf.seek(raf.getFilePointer() - 1);
						raf.write(prev);
					}
					raf.write(prev);
					raf.seek(0);
				}
			}
		}
	}
	
}
