package jp.emcom.adv.bo.utils;

import jp.emcom.adv.bo.core.dao.datasource.DataSourceType;
import jp.emcom.adv.bo.core.utils.spring.tx.TransactionAspect;
import jp.emcom.adv.bo.core.utils.spring.tx.Tx;
import jp.emcom.adv.bo.test.ContextLoader;
import jp.emcom.adv.common.spring.aop.caching.CacheFlushListener;
import jp.emcom.adv.common.spring.aop.caching.CachingAspect;
import jp.emcom.adv.common.spring.aop.caching.annotation.CacheFlush;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;

public class TestTransactionAspect {
	public static void main(String[] args) {
		ContextLoader loader = new ContextLoader(TestTransactionAspect.class);
		
		Service1 test = (Service1) loader.getBean("service1");
		System.out.println("#################### operation1 ####################");
		test.operation1();
		test.printStatus();
		System.out.println("#################### operation2 ####################");
		test.operation2();
		test.printStatus();
		System.out.println("#################### operation3 ####################");
		test.operation3();
		test.printStatus();
		
		loader.close();
	}
}

interface Service {
	void operation1();
	void operation2();
	void operation3();
}

class Service1 implements Service {
	Service service;
	
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED)
	public void operation1() {
		if (service != null) {
			service.operation1();
		}
		printStatus();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.SUPPORTS)
	public void operation2() {
		if (service != null) {
			service.operation2();
		}
		printStatus();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED)
	public void operation3() {
		if (service != null) {
			service.operation2();
		}
		printStatus();
	}
	
	void printStatus() {
		TransactionStatus status = TransactionAspect.currentTransactionStatus();
		System.out.println("====================================================");
		if (status == null) {
			System.out.println("TransactionStatus is null");
		} else {
			System.out.println("isNewTransaction=" + status.isNewTransaction());
			System.out.println("isCompleted=" + status.isCompleted());
			System.out.println("isRollbackOnly=" + status.isRollbackOnly());
		}
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}
}

class Service2 extends Service1 {
	@CacheFlush
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED)
	public void operation1() {
		super.operation1();
	}

	@CacheFlush
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED)
	public void operation2() {
		super.operation2();
	}

	@CacheFlush
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED)
	public void operation3() {
		super.operation3();
	}
}

class Service3 extends Service1 {
	@CacheFlush
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED)
	public void operation1() {
		super.operation1();
	}

	@CacheFlush
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED)
	public void operation2() {
		super.operation2();
	}

	@CacheFlush
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED)
	public void operation3() {
		super.operation3();
	}
}

class TestCacheFlushListener implements CacheFlushListener {

	@Override
	public void onFlush(String group) {
		System.out.println("********* onFlush: " + group + " *********");
	}

	@Override
	public void setCachingAspect(CachingAspect aspect) {
		
	}
	
}