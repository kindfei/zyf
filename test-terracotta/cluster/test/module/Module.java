package test.module;

import java.util.Map;

public interface Module {
	public String startup(Map<String, String> parameters);
	public String shutdown(Map<String, String> parameters);
}
