package test.bo.aspect;

import jp.emcom.adv.bo.test.ContextLoader;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TestAspect {
	
	@Around("@annotation(test2)")
	public Object test(final ProceedingJoinPoint pjp, final Test2 test2) throws Throwable {
		System.out.println("property21=" + test2.property21() 
				+ ", property22=" + test2.property22()
				+ ", property23=" + test2.property23()
				);
		return pjp.proceed();
	}
	
	@Around("@annotation(test1)")
	public Object test(final ProceedingJoinPoint pjp, final Test1 test1) throws Throwable {
		System.out.println("property11=" + test1.property11() 
				+ ", property12=" + test1.property12()
				+ ", property13=" + test1.property13()
				);
		return pjp.proceed();
	}
	
	public static void main(String[] args) {
		ContextLoader cl = new ContextLoader(TestAspect.class);
		TestBean1 tb1 = (TestBean1) cl.getBean("testBean1");
		TestBean2 tb2 = (TestBean2) cl.getBean("testBean2");
		
		tb1.test1();
		tb1.test2();
		tb1.test3();
		tb2.test1();
		tb2.test2();
		tb2.test3();
	}
}
