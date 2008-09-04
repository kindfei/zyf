package test.module;

import java.util.Map;

public class ServiceCommand {
	private CommandType type;
	private String methodName;
	private String description;
	private Map<String, String> parameters;
	
	public ServiceCommand(CommandType type, String methodName, String description) {
		this.type = type;
		this.methodName = methodName;
		this.description = description;
	}
	
	public CommandType getType() {
		return type;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public Map<String, String> getParameters() {
		return parameters;
	}
}
