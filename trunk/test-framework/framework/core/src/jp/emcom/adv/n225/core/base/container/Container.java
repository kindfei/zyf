package jp.emcom.adv.n225.core.base.container;

public interface Container {
	public void init(String configFile) throws Exception;

	public boolean start() throws Exception;

	public void stop() throws Exception;
}
