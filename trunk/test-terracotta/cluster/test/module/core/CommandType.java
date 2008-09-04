package test.module.core;

public enum CommandType {
	STARTUP ("Startup Command. Execute the method and listen on the port."),
	REMOTE ("Remote Command. Send the command to the port which the service listen on."),
	LOCAL ("Local Command. Execute the method locally.");
	
	private String description;
	
	private CommandType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
