package fei.tools.dc.cc;

public enum ResultEnum {
	ignore("ignore"),
	same("same"),
	different("different")
	;
	
	private String displayName;
	
	ResultEnum(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
