package test.basic.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class TestObjectCopy {
	
	private static TestObjectCopy instance = new TestObjectCopy();
	
	public static TestObjectCopy getInstance() {
		return instance;
	}
	
	public static void main(String args[]) {
		TestObjectCopy test = new TestObjectCopy();
		
		ArrayList list = new ArrayList();
		HashMap map = new HashMap();
		
		map.put("1", "A");
		map.put("2", "B");
		list.add(map);
		
		List copy = copy = (List)test.cloneObject(list);
		
		Map copyMap = (Map)copy.get(0);
		copyMap.put("3", "C");
		
		System.out.println(map);
		System.out.println(copyMap);
		
		test.writeObject(copy);
		copy = (List)test.readObject();
		copyMap = (HashMap)copy.get(0);
		System.out.println(copyMap);
	}
	
	Object readObject() {
		Object retObj = null;
		try {
			FileInputStream fis = new FileInputStream("D:/test.txt");
			ObjectInputStream oos = new ObjectInputStream(fis);
			retObj = oos.readObject();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return retObj;
	}
	
	void writeObject(Object obj) {
		try {
			FileOutputStream fos = new FileOutputStream("D:/test.txt");
			ObjectOutputStream oos =new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	Object cloneObject(Object obj) {
		Object retObj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			retObj = ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return retObj;
	}
}
