package test.service;

import test.po.Test;

public interface TestService {
	
	void testDeclarativeTx();
	
	void testNewSession();
	void testNotClosedSession();
	
	void testDaoTx();
	void testMixedTx();
	
	Test testInJtaTx();
	
	void testJmsInJtaTx(boolean rollback, boolean errorMsg);
	
}
