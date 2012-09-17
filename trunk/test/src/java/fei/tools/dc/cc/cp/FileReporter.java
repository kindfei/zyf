package fei.tools.dc.cc.cp;

public abstract class FileReporter implements Reporter {

	private String targetDir = ".";

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}
}
