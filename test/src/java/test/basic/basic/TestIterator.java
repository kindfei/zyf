package test.basic.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class TestIterator extends Thread {
	
	static ArrayList arrayList;
	static List list;
	static Iterator iter;
	
	public static void main(String[] args) throws Exception {
		arrayList = new ArrayList();
		arrayList.add("A");
		arrayList.add("B");
		arrayList.add("C");
		arrayList.add("D");
		arrayList.add("E");
		arrayList.add("F");
		arrayList.add("G");
		list = Collections.synchronizedList(arrayList);
		iter = arrayList.iterator();
		
		Thread t = new TestIterator();
		t.start();
		
		testList();
//		testIter();
	}
	
	static void testList() throws Exception {
		Iterator irt = list.iterator();
		synchronized (list) {
			for(int i=0; irt.hasNext(); i++) {
				Thread.sleep(1);
				System.out.println("\t" + irt.next());
				irt.remove();
			}
		}
	}
	
	static void testIter() {
		synchronized (iter) {
			for(int i=0; iter.hasNext(); i++) {
				System.out.println();
				iter.remove();
			}
		}
	}
	
	public void run() {
		try {
			listTest();
//			iterTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void listTest() throws Exception {
		Iterator irt = list.iterator();
		synchronized (list) {
			for(int i=0; irt.hasNext(); i++) {
				Thread.sleep(1);
				System.out.println(irt.next());
				irt.remove();
			}
		}
	}
	
	void iterTest() {
		synchronized (iter) {
			for(int i=0; iter.hasNext(); i++) {
				System.out.println();
				iter.remove();
			}
		}
	}
}
