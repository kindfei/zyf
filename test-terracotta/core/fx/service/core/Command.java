package fx.service.core;

public interface Command {

	public String getKey();
	
	public CommandType getType();
	
	public String getDescription();
	
	public String execute() throws Exception;
	
}
