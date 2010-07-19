package jp.emcom.adv.bo.utils;

import jp.emcom.adv.bo.core.dao.datasource.DataSource;
import jp.emcom.adv.bo.core.dao.datasource.DataSourceType;
import jp.emcom.adv.bo.core.dao.impl.GenericDao;
import jp.emcom.adv.bo.core.dao.po.main.Test;
import jp.emcom.adv.bo.core.utils.spring.tx.Tx;
import jp.emcom.adv.bo.test.ContextLoader;

import org.springframework.transaction.annotation.Propagation;

@DataSource(type=DataSourceType.main)
public class TestTransaction extends GenericDao<Test> {
	TestTransaction testTransaction;
	
	@Tx(datasource=DataSourceType.main, propagation=Propagation.SUPPORTS)
	public void update() {
		
		String sql = "update Test set content=? where id='001'";
		
		update(sql, new Object[] {"C06"});
		update(sql, new Object[] {"C07"});
		
		throw new RuntimeException();
	}

	public static void main(String[] args) {
		ContextLoader loader = new ContextLoader(new String[] {
				"classpath:jp/emcom/adv/bo/utils/TestTransaction.xml",
		});
		
		TestTransaction inst = (TestTransaction) loader.getBean("testTransaction");
		inst.testTransaction = inst;
		
		try {
			inst.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		loader.close();
	}
	
}

