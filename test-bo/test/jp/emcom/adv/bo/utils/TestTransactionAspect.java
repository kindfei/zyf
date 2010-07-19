package jp.emcom.adv.bo.utils;

import java.util.Set;

import jp.emcom.adv.bo.core.dao.datasource.DataSourceType;
import jp.emcom.adv.bo.core.utils.spring.tx.TransactionInfo;
import jp.emcom.adv.bo.core.utils.spring.tx.Tx;
import jp.emcom.adv.bo.test.ContextLoader;
import jp.emcom.adv.common.spring.aop.caching.CachingOperation;
import jp.emcom.adv.common.spring.aop.caching.FlushListener;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;

public class TestTransactionAspect {
	public static void main(String[] args) {
		ContextLoader loader = new ContextLoader(TestTransactionAspect.class);
		
		Service1 test = (Service1) loader.getBean("service1");
		System.out.println("#################### read1 ####################");
		test.read1();
		test.printStatus();
		System.out.println("#################### read2 ####################");
		test.read2();
		test.printStatus();
		System.out.println("#################### read3 ####################");
		test.read3();
		test.printStatus();
		System.out.println("#################### write1 ####################");
		test.write1();
		test.printStatus();
		System.out.println("#################### write2 ####################");
		test.write2();
		test.printStatus();
		System.out.println("#################### write3 ####################");
		test.write3();
		test.printStatus();
		
		loader.close();
	}
}

interface Service {
	String read1();
	String read2();
	String read3();
	void write1();
	void write2();
	void write3();
}

class Service1 implements Service {
	Service service;

	@Tx(datasource=DataSourceType.main, propagation=Propagation.SUPPORTS, cachingOperation=CachingOperation.CACHEABLE)
	public String read1() {
		if (service != null) {
			service.read1();
		}
		printStatus();
		return "read1";
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED, cachingOperation=CachingOperation.CACHEABLE)
	public String read2() {
		if (service != null) {
			service.read2();
		}
		printStatus();
		return "read2";
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED, cachingOperation=CachingOperation.CACHEABLE)
	public String read3() {
		if (service != null) {
			service.read3();
		}
		printStatus();
		return "read3";
	}
	
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED)
	public void write1() {
		if (service != null) {
			service.write1();
		}
		printStatus();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.SUPPORTS)
	public void write2() {
		if (service != null) {
			service.write2();
		}
		printStatus();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.SUPPORTS)
	public void write3() {
		if (service != null) {
			service.write3();
		}
		printStatus();
	}
	
	void printStatus() {
		TransactionInfo info = TransactionInfo.currentTransactionInfo(DataSourceType.main);
		System.out.println("====================================================");
		if (info == null) {
			System.out.println("TransactionInfo is null");
		} else {
			System.out.println("isNewTransaction=" + info.getTransactionStatus().isNewTransaction());
			System.out.println("isCompleted=" + info.getTransactionStatus().isCompleted());
			System.out.println("isRollbackOnly=" + info.getTransactionStatus().isRollbackOnly());
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
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED, readonly=true, cachingOperation=CachingOperation.CACHEABLE)
	public String read1() {
		return super.read1();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.SUPPORTS, readonly=true, cachingOperation=CachingOperation.CACHEABLE)
	public String read2() {
		return super.read2();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.SUPPORTS, isolationLevel=TransactionDefinition.ISOLATION_REPEATABLE_READ, cachingOperation=CachingOperation.CACHEABLE)
	public String read3() {
		return super.read3();
	}
	
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED, cachingOperation=CachingOperation.CACHE_FLUSH)
	public void write1() {
		super.write1();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.SUPPORTS, cachingOperation=CachingOperation.CACHE_FLUSH)
	public void write2() {
		super.write2();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED, cachingOperation=CachingOperation.CACHE_FLUSH)
	public void write3() {
		super.write3();
	}
}

class Service3 extends Service1 {
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRES_NEW, cachingOperation=CachingOperation.CACHEABLE)
	public String read1() {
		return super.read1();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED, readonly=true, cachingOperation=CachingOperation.CACHEABLE)
	public String read2() {
		return super.read2();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED, isolationLevel=TransactionDefinition.ISOLATION_REPEATABLE_READ, cachingOperation=CachingOperation.CACHEABLE)
	public String read3() {
		return super.read3();
	}
	
	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED, cachingOperation=CachingOperation.CACHE_FLUSH)
	public void write1() {
		super.write1();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.REQUIRED, cachingOperation=CachingOperation.CACHE_FLUSH)
	public void write2() {
		super.write2();
	}

	@Tx(datasource=DataSourceType.main, propagation=Propagation.SUPPORTS, cachingOperation=CachingOperation.CACHE_FLUSH)
	public void write3() {
		super.write3();
	}
}

class TestCacheFlushListener implements FlushListener {

	@Override
	public void onFlush(Set<String> groups) {
		System.out.println("********* onFlush: " + groups + " *********");
	}
	
}