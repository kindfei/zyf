package fei.tools.perfanal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogAnalyzer {
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	
	private final static String regex = "^[\"']|[\"']$|[\"']?,[\"'](?=[^\"']*[\"']([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)|[\"']?,(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)";
	
	private static String[] threadNames;
	
	private static String[] keywords;
	
	private static Map<String, Integer[]> resultFormat = new LinkedHashMap<String, Integer[]>();
	
	static {
		threadNames = split(System.getenv("THREAD_NAMES"));
		keywords = split(System.getenv("KEYWORDS"));
		
		String[] strResultFormat = split(System.getenv("RESULT_FORMAT"));
		for (String s : strResultFormat) {
			String[] a1 = s.split("=");
			String[] a2 = a1[1].split("-");
			resultFormat.put(a1[0], new Integer[] { Integer.parseInt(a2[0]), Integer.parseInt(a2[1]) });
		}
	}
	
	private static String[] split(String str) {
		if (str == null) return null;
		
		String[] array = str.split(regex);
		
		List<String> r = new ArrayList<String>();
		for (String s : array) {
			if (!s.isEmpty()) {
				r.add(s);
			}
		}
		return r.toArray(new String[] {});
	}
	
	public static void process(String threadName, String filename) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		List<Date[]> r = new ArrayList<Date[]>();
		Date[] dates = null;
		
		int expectingItem = 0;
		for (String str; (str = br.readLine()) != null;) {
			for (int j = 0; j < keywords.length; j++) {
				if (!str.contains(threadName) || !str.matches(keywords[j])) {
					continue;
				}
				
				if (j != expectingItem) {
					System.out.println("WARN: expecting [" + expectingItem + "], actually [" + j + "]");
				}

				if (j == 0 || j < expectingItem) {
					if (dates != null) {
						r.add(dates);
					}
					dates = new Date[keywords.length];
				}

				if (dates == null) {
					dates = new Date[keywords.length];
				}
				
				dates[j] = sdf.parse(str.substring(0, 23));
				
				expectingItem = (j + 1) % keywords.length;
				break;
			}
		}
		
		for (String key : resultFormat.keySet()) {
			System.out.print(key + "\t");
		}
		
		System.out.println();
		
		a:for (Date[] ds : r) {
			for (Date d : ds) {
				if (d == null) continue a;
			}
			for (Integer[] i : resultFormat.values()) {
				System.out.print(elapsed(ds[i[0]], ds[i[1]]) + "\t");
			}
			System.out.println();
		}
	}
	
	private static String elapsed(Date start, Date end) {
		if (start == null || end == null) {
			return "NA";
		}
		return String.valueOf(end.getTime() - start.getTime());
	}
	
	public static void main(String[] args) throws Exception {
		for (String threadName : threadNames) {
			System.out.println("================================= " + threadName + " =================================");
			process(threadName, args[0]);
		}
	}
	
}
