package jp.emcom.adv.n225.core.start;

public interface StartupLoader {

	public void load(Startup.Config config) throws StartupException;

	public void start() throws StartupException;

	public void unload() throws StartupException;
}
