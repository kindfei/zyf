package test.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class TestAspect {
	
	public void before(JoinPoint jp, String name) {
		System.out.println("before................" + desc(jp));
		System.out.println("name = " + name);
	}
	
	public Object around(ProceedingJoinPoint pjp, String name) throws Throwable {
		System.out.println("around................" + desc(pjp));
		System.out.println("name = " + name);
		try {
			return pjp.proceed();
		} catch (Throwable e) {
			throw e;
		}
	}
	
	public void after(JoinPoint jp, String name) {
		System.out.println("after................" + desc(jp));
		System.out.println("name = " + name);
	}
	
	public void afterReturning(JoinPoint jp, Object retVal, String name) {
		System.out.println("afterReturning................" + desc(jp));
		System.out.println("retVal = " + retVal + ", name = " + name);
	}
	
	public void afterThrowing(JoinPoint jp, Exception e, String name) {
		System.out.println("afterThrowing................" + desc(jp));
		System.out.println("exception = " + e.getMessage() + ", name = " + name);
	}
	
	private String desc(JoinPoint jp) {
		StringBuilder sb = new StringBuilder();
		sb.append(jp.getSignature().getDeclaringTypeName());
		sb.append(".");
		sb.append(jp.getSignature().getName());
		return sb.toString();
	}
}
