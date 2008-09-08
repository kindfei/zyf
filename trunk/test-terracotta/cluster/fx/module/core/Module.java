package fx.module.core;

import java.util.Map;

public interface Module {
	public String startup(Map<String, String> props) throws Exception;
	public String shutdown() throws Exception;
}
