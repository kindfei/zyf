package test.bo;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Test {
	static final Logger log = LogManager.getLogger(Test.class);
	
	public static void main(String[] args) throws Exception {
		String s = "-cacheFlush=asd";
		int i = s.indexOf("=");
		System.out.println(s.substring(i + 1));
	}
}
