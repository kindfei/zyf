package fei.tools.util;

import java.util.ArrayList;
import java.util.List;

public class SystemPropertyOptions {
	private String optionName;
	private List<String> options = new ArrayList<String>();
	
	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}
	
	public void select() {
		select("Setting the value of the system property: " + optionName);
	}
	
	public void select(String promptMsg) {
		int i = InteractionHandler.options(promptMsg, options.toArray());
		System.setProperty(optionName, options.get(i));
	}
}
