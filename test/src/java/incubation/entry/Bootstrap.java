package incubation.entry;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Bootstrap {
	private static Log log = LogFactory.getLog(Bootstrap.class);
	
	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				throw new IllegalArgumentException("Illegal Argument Length.");
			}
			
			ServiceDefinition def = ServiceDefinition.getServiceDefinition();
			if (def == null) {
				throw new RuntimeException("No service definition.");
			}
			
			String className = def.getClassName();
			Class<?> clazz = Class.forName(className);
			
			ServiceEntry entry = null;
			
			if (args.length > 1) {
				Constructor<?>[] constructors = clazz.getConstructors();
				for (Constructor<?> constructor : constructors) {
					Class<?>[] types = constructor.getParameterTypes();
					if (types.length != 1) {
						continue;
					}
					if (!types[0].isAssignableFrom(String[].class)) {
						continue;
					}
					String[] initargs = Arrays.copyOfRange(args, 1, args.length);
					entry = (ServiceEntry) constructor.newInstance(new Object[] { initargs });
				}
			}
			
			if (entry == null) {
				entry = (ServiceEntry) clazz.newInstance();
			}
			
			entry.process(args[0]);
		} catch (Throwable e) {
			log.fatal("ServiceEntry error.", e);
		}
	}
}
