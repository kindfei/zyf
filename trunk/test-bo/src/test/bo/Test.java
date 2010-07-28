package test.bo;

import java.util.NavigableMap;
import java.util.TreeMap;

import jp.emcom.adv.bo.test.ContextLoader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Test {
	static final Logger log = LogManager.getLogger(Test.class);
	
	private Object obj;
	
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	public void start() {
		System.out.println("start...");
	}
	
	public void stop() {
		System.out.println("stop...");
	}

	public static void main(String[] args) throws Exception {
		ContextLoader loader = new ContextLoader(Test.class);
		Test test = (Test) loader.getBean("test");
		
	}
	
	static void testTreeMap() {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();

		int size = 100;
		for (int i = 0; i < size; i++) {
			map.put(i, i + "");
		}
		System.out.println(map.size());
		
		NavigableMap<Integer, String> headMap = map.headMap(size/4, true);
		System.out.println(headMap.size());
		
		headMap.clear();
		System.out.println(map.size());
	}
}
