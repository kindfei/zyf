package test.util;

import java.io.File;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Utils {
	public static ClassPathXmlApplicationContext loadContext(Class<?> clazz, String fileName) {
		String path = clazz.getPackage().getName().replaceAll("\\.", "/");
		ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext(path + File.separator + fileName);
		return c;
	}
}
