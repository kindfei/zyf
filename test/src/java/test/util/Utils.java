package test.util;

import java.io.File;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Utils {
	public static ClassPathXmlApplicationContext loadContext(Class<?> clazz, String fileName) {
		String path = clazz.getPackage().getName().replaceAll("\\.", "/");
		ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext(path + File.separator + fileName);
		return c;
	}
	
	@SuppressWarnings("unchecked")
	public static Object trimString(Object obj) {
		if (obj instanceof List<?>) {
			List<Object> list = (List<Object>) obj;
			for (int i = 0; i < list.size(); i++) {
				list.set(i, trimString(list.get(i)));
			}
		} else if (obj.getClass().isArray()) {
			Object[] array = (Object[]) obj;
			for (int i = 0; i < array.length; i++) {
				array[i] = trimString(array[i]);
			}
		} else if (obj instanceof String) {
			return ((String) obj).trim();
		}
		return obj;
	}
}
