package core.entry;

import java.lang.reflect.Method;

public class RelfectionCommand implements Command {
	private String key;
	private CommandType type;
	private String description;
	
	private Object instance;
	private Method method;
	
	public RelfectionCommand(String key, CommandType type, String description
			, Object instance, Method method) {
		super();
		this.key = key;
		this.type = type;
		this.description = description;
		this.instance = instance;
		this.method = method;
	}

	public String getKey() {
		return key;
	}

	public CommandType getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public String execute() throws Exception {
		Object result = method.invoke(instance, new Object[0]);
		if (result != null) {
			return result.toString();
		} else {
			return null;
		}
	}
}