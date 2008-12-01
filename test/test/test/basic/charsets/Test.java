package test.basic.charsets;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

public class Test {
	
	public static void main(String[] args) {
		Map map = Charset.availableCharsets();
		Iterator keyItr = map.keySet().iterator();
		while(keyItr.hasNext()) {
			String key = (String)keyItr.next();
			System.out.println(key);
			Iterator aliasesItr = ((Charset)map.get(key)).aliases().iterator();
			while(aliasesItr.hasNext()) {
				System.out.println("	" + aliasesItr.next().toString());
			}
		}
	}

}
