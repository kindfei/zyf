package jp.emcom.adv.n225.core.container;

public interface Container {
	public void init(String configFile) throws ContainerException;

	public boolean start() throws ContainerException;

	public void stop() throws ContainerException;
}
