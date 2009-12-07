package test.spring;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProxyTest {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext("test/spring/ProxyTest.xml");
		
		testWaiter(c, "waiter");
		testWaiter(c, "waiterHello");
		testWaiter(c, "abstractWaiter");
		testWaiter(c, "abstractWaiterHi");
		testWaiter(c, "waiterHelloImpl");
		testWaiter(c, "waiterImpl");
		testWaiter(c, "waiterImplHi");
		
		testWaiter(c, "waiterOfPeople");
		testCustomer(c, "waiterOfPeople");
		
		testWaiter(c, "unknownOfPeople");
		testCustomer(c, "unknownOfPeople");
		
		testWaiter(c, "customerOfPeople");
		testCustomer(c, "customerOfPeople");
		
		testWaiter(c, "variousPeople");
		testCustomer(c, "variousPeople");
		
		testWaiter(c, "nullWaiter");
		
		testWaiter(c, "variousPeople2");
		testCustomer(c, "variousPeople2");
	}
	
	static void testWaiter(ClassPathXmlApplicationContext c, String name) {
		System.out.println("********* testWaiter-" + name + " *********");
		
		try {
			Waiter w = (Waiter) c.getBean(name);
			try {
				w.sayHello();
			} catch (Throwable e) {
//				e.printStackTrace();
			}
			try {
				w.sayHi();
			} catch (Throwable e) {
//				e.printStackTrace();
			}
		} catch (Throwable e) {
//			e.printStackTrace();
		}
	}
	
	static void testCustomer(ClassPathXmlApplicationContext c, String name) {
		System.out.println("********* testCustomer-" + name + " *********");
		
		try {
			Customer cu = (Customer) c.getBean(name);
			
			try {
				cu.sayHelloToo();
			} catch (Throwable e) {
//				e.printStackTrace();
			}
			
			try {
				cu.sayHiToo();
			} catch (Throwable e) {
//				e.printStackTrace();
			}
			
		} catch (Throwable e) {
//			e.printStackTrace();
		}
	}
}

interface Waiter {
	void sayHello();
	void sayHi();
}

interface Customer {
	void sayHelloToo();
	void sayHiToo();
}

abstract class AbstractWaiter implements Waiter {
	
	protected String name;
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void sayHello() {
		System.out.println(name + " hello");
	}
}

class WaiterHelloImpl implements Waiter {
	
	protected String name;
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void sayHello() {
		System.out.println(name + " hello");
	}

	@Override
	public void sayHi() {
		throw new NotImplementedException();
	}
}

class WaiterImpl extends AbstractWaiter {

	@Override
	public void sayHi() {
		System.out.println(name + " hi");
	}
	
}

class People implements Waiter, Customer {
	
	protected String name;
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void sayHello() {
		System.out.println(name + " hello");
	}

	@Override
	public void sayHi() {
		System.out.println(name + " hi");
	}

	@Override
	public void sayHelloToo() {
		System.out.println(name + " hello too");
	}

	@Override
	public void sayHiToo() {
		System.out.println(name + " hi too");
	}
	
}

class TestProxyFactoryBean extends ProxyFactoryBean implements InitializingBean, MethodInterceptor {
	private static final long serialVersionUID = 3671346627324332268L;
	
	private String expression; // optional

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		AspectJExpressionPointcut pointcut = null;
		if (expression != null) {
			pointcut = new AspectJExpressionPointcut();
			pointcut.setExpression(expression);
		}

		DefaultPointcutAdvisor advisor = null;
		if (pointcut != null) {
			advisor = new DefaultPointcutAdvisor(pointcut, this);
		} else {
			advisor = new DefaultPointcutAdvisor(this);
		}

		this.addAdvisor(advisor);
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Method method = mi.getMethod();
		String methodName = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();
		Object[] arguments = mi.getArguments();
		
		ToStringBuilder s = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		s.append(getProxiedInterfaces());
		s.append(getTargetClass());
		s.append(methodName);
		s.append(parameterTypes);
		s.append(arguments);
		
		System.out.println(s.toString());
		
		return null;
	}
	
}

class FuckingProxyFactoryBean extends ProxyFactoryBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1222247018006242261L;

	public void setAdvisors(Advisor[] advisors) {
		super.addAllAdvisors(advisors);
	}
}

class TestPointcutAdvisor extends DefaultPointcutAdvisor implements InitializingBean, MethodInterceptor {
	private static final long serialVersionUID = 3671346627324332268L;
	
	private String expression;
	private boolean invokeTargetMethod;
	
	private String name;

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setInvokeTargetMethod(boolean invokeTargetMethod) {
		this.invokeTargetMethod = invokeTargetMethod;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (expression != null) {
			AspectJExpressionPointcut p = new AspectJExpressionPointcut();
			p.setExpression(expression);
			this.setPointcut(p);
		}
		this.setAdvice(this);
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Method method = mi.getMethod();
		String methodName = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();
		Object[] arguments = mi.getArguments();
		
		ToStringBuilder s = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		s.append(name);
		s.append(methodName);
		s.append(parameterTypes);
		s.append(arguments);
		
		System.out.println(s.toString());
		
		return invokeTargetMethod ? mi.proceed() : null;
	}
	
}