package test.dao;

import test.po.Test;

public interface TestDao {
	void insertInTx(Test test);
	void insertOutTx(Test test);
	void insertInJtaTx(Test test);
	
	void insertWithNewSessionInTx(Test test);
	void insertWithoutSessionClose(Test test);
	
	void insertInTx1(Test test);
	void insertOutTx1(Test test);
	void insertInJtaTx1(Test test);
	
	void insertWithJdbc(Test test);
}
