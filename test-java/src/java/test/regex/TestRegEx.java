package test.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegEx {
	
	public static void testRegEx() {
		
		Matcher m = Pattern.compile("x*?foo").matcher("aaxfooxxxxxxfoo");
		
		if (m.lookingAt()) {
			System.out.println("Starting at " + m.start() + ", Ending at " + m.end() + ", Group is " + m.group());
		} else {
			System.out.println("No match found");
		}
		
		m.reset();
		
		if (m.matches()) {
			System.out.println("Starting at " + m.start() + ", Ending at " + m.end() + ", Group is " + m.group());
		} else {
			System.out.println("No match found");
		}
		
		m.reset();
		
		while (m.find()) {
			System.out.println("Starting at " + m.start() + ", Ending at " + m.end() + ", Group is " + m.group());
		}
		
	}
	
	public static void main(String[] args) {
		testRegEx();
	}
}
