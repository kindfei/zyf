package test.cluster.core;

public abstract class AbstractService implements Service {

	public AbstractService(int mode, AbstractProcessor processor) {

	}

	@Override
	public String startup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String shutdown() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 */
	public abstract void init();

	/**
	 */
	public void process(Object obj) {
	}

}
