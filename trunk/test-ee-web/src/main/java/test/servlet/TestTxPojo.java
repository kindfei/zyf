package test.servlet;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import test.ejb.tx.TestBMTRemote;
import test.ejb.tx.TestCMTRemote;

public class TestTxPojo {
	private TestBMTRemote testBMT;
	private TestCMTRemote testCMT;
	
	public TestBMTRemote getTestBMT() {
		return testBMT;
	}

	public void setTestBMT(TestBMTRemote testBMT) {
		this.testBMT = testBMT;
	}

	public TestCMTRemote getTestCMT() {
		return testCMT;
	}

	public void setTestCMT(TestCMTRemote testCMT) {
		this.testCMT = testCMT;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void invokeWithTx(String type, String num) {
		invoke(type, num);
	}

	public void invoke(String type, String num) {
		
		Object instance = null;
		if (type.equalsIgnoreCase("B")) {
			instance = testBMT;
		} else if (type.equalsIgnoreCase("C")) {
			instance = testCMT;
		} else {
			System.out.println("Illegal parameter: type=" + type);
			return;
		}
		
		try {
			instance.getClass().getMethod("test" + num, new Class<?>[] {}).invoke(instance, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
