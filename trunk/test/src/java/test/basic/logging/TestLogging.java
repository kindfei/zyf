package test.basic.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Category;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

public class TestLogging {

	private Log log;
	private Logger logger;
	private Category cat;
	
	public TestLogging(String name, String prop) {
		if (prop != null) config(prop);
		log = LogFactory.getLog(name);
		logger = LogManager.getLogger(name);
		cat = Category.getInstance(name);
	}
	
	private void config(String fileName) {
		try {
			if (fileName.endsWith(".properties")) {
				PropertyConfigurator.configure(ClassLoader.getSystemResource(fileName));
			} else if (fileName.endsWith(".xml")) {
				DOMConfigurator.configure(ClassLoader.getSystemResource(fileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void log() {
		log.info("org.apache.commons.logging.Log");
		logger.info("org.apache.log4j.Logger");
		cat.info("org.apache.log4j.Category");
	}
	
	private static void test1() {
		TestLogging test1 = new TestLogging("test.1", "log4j_1.xml");
		test1.log();
		
		new Thread() {
			public void run() {
				TestLogging test2 = new TestLogging("test.2", "log4j_2.properties");
				test2.log();
			}
		}.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		test1.log();

	}
	
	private static void test2() {
		TestLogging inst = new TestLogging("test.zyf", "log4j_4.properties");
		
		inst.log();
	}
	
	public static void main(String[] args) {
		test2();
	}
	
}
