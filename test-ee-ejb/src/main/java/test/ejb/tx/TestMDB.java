package test.ejb.tx;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import test.po.Test;

@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"
		) }, 
		mappedName = "TestQueue1")
public class TestMDB implements MessageListener {
	
	@PersistenceContext
	private EntityManager entityManager;

    public TestMDB() {
    }
	
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void onMessage(Message message) {
    	Test msg = null;
    	boolean redelivered = false;
    	boolean rollbackRemote = false;
        try {
        	msg = (Test) ((ObjectMessage) message).getObject();
        	redelivered = message.getJMSRedelivered();
        	rollbackRemote = message.propertyExists("rollbackRemote");
		} catch (JMSException e) {
			e.printStackTrace();
		}
        
    	System.out.println("MDB received msg: " + msg + ", redelivered=" + redelivered + ", rollback=" + rollbackRemote);
    	
    	if (redelivered) {
    		System.out.println("Ignore redelivered message");
    		return;
    	}
    	
    	Test test = entityManager.find(Test.class, msg.getId());
    	test.setContent("received");
    	
    	if (rollbackRemote && !redelivered) {
    		throw new RuntimeException("For remote rollback testing");
    	}
    }

}
