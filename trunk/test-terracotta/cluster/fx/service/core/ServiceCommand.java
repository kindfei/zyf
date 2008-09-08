package fx.service.core;

public class ServiceCommand {
	private String command;
	private CommandType type;
	private Commandable commandable;
	private String description;
	
	public ServiceCommand(String command, CommandType type, Commandable commandable) {
		this(command, type, commandable, null);
	}
	
	public ServiceCommand(String command, CommandType type, Commandable commandable, String description) {
		this.command = command;
		this.type = type;
		this.commandable = commandable;
		this.description = description;
	}
	
	public String getCommand() {
		return command;
	}
	
	public CommandType getType() {
		return type;
	}
	
	public Commandable getCommandable() {
		return commandable;
	}
	
	public String getDescription() {
		return description;
	}
}
