package test.jms.jboss.core;

public interface Connector {
	public void buildMessenger(Messenger target, String destName) throws BuildException, FailoverException, ConnectException;
	public void removeMessenger(Messenger target);
}
