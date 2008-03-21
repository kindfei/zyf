package test.basic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Ttokenizer {
	
	public static void main(String args[]) {
		BufferedReader br =null;
		try {
			br = new BufferedReader(new FileReader("D:/test.txt"));
			
			String str =null;
			while ((str = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(str, ",", false);
				System.out.println("***" + st.countTokens() + "***");
				while (st.hasMoreElements())
					System.out.println(st.nextToken());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
