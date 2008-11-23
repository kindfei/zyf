package test.jms.jboss;

public interface Connector {
	public void buildMessenger(Messenger target, String destName) throws BuildException, FailoverException, ConnectException;
	public void removeMessenger(Messenger target);
}
