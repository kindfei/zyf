package test.module.core;

public enum Option {
	STARTUP  ("-startup",  "-startup           Startup Command. Execute the startup method and listen on the port."),
	SHUTDOWN ("-shutdown", "-shutdown          Shutdown Command. Send the shutdown command to remote service."),
	REMOTE   ("-r",        "-r <methodname>    Remote Command. Send the command to the port which the service listen on."),
	LOCAL    ("-l",        "-l <methodname>    Local Command. Execute the method locally.");

	private String command;
	private String description;
	
	private Option(String command, String description) {
		this.command = command;
		this.description = description;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getDescription() {
		return description;
	}
}
