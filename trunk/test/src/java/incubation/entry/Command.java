package incubation.entry;

public interface Command {

	public String getKey();
	
	public CommandType getType();
	
	public String getDescription();
	
	public Object execute() throws Exception;
	
}
