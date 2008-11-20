package test.core.jms;

import javax.jms.JMSException;
import javax.naming.NamingException;

public interface Rebuildable {
	public void rebuild() throws JMSException, NamingException;
}
