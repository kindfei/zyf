package fx.module.core;

public class CommandInfo {
	private String moduleName;
	private String strCmd;
	
	CommandInfo(String[] args) {
		int len = args.length;
		
		if (len == 2) {
			strCmd = args[0];
			moduleName = args[1];
		} else {
			throw new IllegalArgumentException("Error command format.");
		}
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getStrCmd() {
		return strCmd;
	}
}
