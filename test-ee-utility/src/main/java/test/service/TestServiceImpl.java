package test.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import test.dao.TestDao;
import test.po.Test;

public class TestServiceImpl implements TestService {
	private TestDao testDao;

	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void testDeclarativeTx() {
		Test test = new Test();
		test.setContent("Tx doesn't work");
		testDao.insertInTx(test);
		test.setContent("Tx works");
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void testNewSession() {
		Test test = new Test();
		test.setContent("Tx doesn't work with new session");
		testDao.insertWithNewSessionInTx(test);
		test.setContent("Tx works with new session");
	}

	public void testNotClosedSession() {
		Test test = new Test();
		testDao.insertWithoutSessionClose(test);
	}
	
	public void testDaoTx() {
		Test test = new Test();
		test.setContent("DAO tx works");
		testDao.insertInTx(test);
		test.setContent("It should never happen");
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void testMixedTx() {
		Test test = new Test();
		test.setContent("Mixed tx doesn't work");
		testDao.insertInTx(test);
		test.setContent("Mixed tx works");
	}
	
	public Test testInJtaTx() {
		Test test = new Test();
		testDao.insertInJtaTx(test);
		test.setContent("JTA tx works");
		return test;
	}
	
	private JmsTemplate jmsTemplate;
	
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void testJmsInJtaTx(boolean rollbackLocal, final boolean rollbackRemote) {
		final Test test = new Test();
		
		testDao.insertInJtaTx(test);
		
		jmsTemplate.send("TestQueue1", new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage msg = session.createObjectMessage(test);
				if (rollbackRemote) msg.setBooleanProperty("rollbackRemote", true);
				return msg;
			}
		});
		
		test.setContent("sent");
		
		if (rollbackLocal) throw new RuntimeException("For rollback testing");
	}
}
