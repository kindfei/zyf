package test.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitByComma {
	public static void main(String[] args) {
		String str = "'a,aaa,',\",bb,b,\",c,d,'e,e,e',f,g";

		split2(str);
		System.out.println("===========================================================");
		split(str);
		System.out.println("===========================================================");
		parse(str);
	}
	
	static String[] split2(String str) {
		String[] r = str.split("^[\"']|[\"']$|[\"']?,[\"'](?=[^\"']*[\"']([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)|[\"']?,(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
		for (String s : r) {
			System.out.println(s);
		}
		return r;
	}

	static String[] split(String subjectString) {
		String[] r = subjectString.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
		for (String s : r) {
			System.out.println(s);
		}
		return r;
	}

	static List<String> parse(String subjectString) {
		List<String> matchList = new ArrayList<String>();
		Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
		Matcher regexMatcher = regex.matcher(subjectString);
		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				// Add double-quoted string without the quotes
				matchList.add(regexMatcher.group(1));
			} else if (regexMatcher.group(2) != null) {
				// Add single-quoted string without the quotes
				matchList.add(regexMatcher.group(2));
			} else {
				// Add unquoted word
				matchList.add(regexMatcher.group());
			}
		}
		for (String s : matchList) {
			System.out.println(s);
		}
		return matchList;
	}
	
}
