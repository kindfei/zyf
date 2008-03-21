package test.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class TestCollection {

	public static void main(String[] args) {
		TestCollection test = new TestCollection();
		test.testSet();
		
	}
	
	void testSet() {
		Set words = new HashSet(59999);
//		words = new LinkedHashSet();
//		words = new TreeSet();
		
		long totalTime = 0;
		
		try {
			InputStreamReader reader = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(reader);
			String line;
			while(!(line = in.readLine()).equals("")) {
				StringTokenizer token = new StringTokenizer(line, ",");
				while(token.hasMoreTokens()) {
					String word = token.nextToken();
					long callTime = System.currentTimeMillis();
					words.add(word);
					callTime = System.currentTimeMillis() - callTime;
					totalTime += callTime;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Iterator iter = words.iterator();
		while(iter.hasNext()) {
			System.out.print(iter.next());
		}
		System.out.println("\r\n" + words.size() + "\r\n" + totalTime);
	}
	
	void testList () {
		LinkedList a = new LinkedList();
		a.add("A");
		a.add("B");
		a.add("C");
		a.add("D");
		a.add("E");
		a.add("F");
		LinkedList b = new LinkedList();
		b.add("1");
		b.add("2");
		b.add("3");
		b.add("4");
		b.add("5");
		b.add("6");
		
		ListIterator aIter = a.listIterator();
		Iterator bIter = b.iterator();
		
		while (bIter.hasNext()) {
			if(aIter.hasNext()) aIter.next();
			aIter.add(bIter.next());
		}
		
		System.out.println(a);
		
		bIter = b.iterator();
		while (bIter.hasNext()) {
			bIter.next();
			bIter.remove();
		}
		
		System.out.println(b);
		
		a.removeAll(b);
		
		System.out.println(a);
	}
}
