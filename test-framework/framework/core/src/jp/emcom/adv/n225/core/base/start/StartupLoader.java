package jp.emcom.adv.n225.core.base.start;

/**
 * 
 * @author zhangyf
 * 
 */
public interface StartupLoader {

	public void load(Startup.Config config) throws Exception;

	public void start() throws Exception;

	public void unload() throws Exception;
}
