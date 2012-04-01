package test.ejb.tx;
import javax.ejb.Local;

@Local
public interface TestCMTLocal {
	void test0();
	void test1();
	void test10();
	void test11();
	void test12();
	void test13();
}
