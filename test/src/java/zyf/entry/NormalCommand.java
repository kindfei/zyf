package zyf.entry;

public class NormalCommand implements Command {
	private String key;
	private CommandType type;
	private String description;
	
	private Commandable commandable;
	
	public NormalCommand(String key, CommandType type, Commandable commandable) {
		this(key, type, "No description", commandable);
	}
	
	public NormalCommand(String key, CommandType type, String description, Commandable commandable) {
		this.key = key;
		this.type = type;
		this.description = description;
		this.commandable = commandable;
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

	public Object execute() throws Exception {
		return commandable.execute();
	}
}
