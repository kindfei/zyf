package test.ejb;

import javax.ejb.Stateless;

@Stateless(mappedName = "HelloEJBLocalImpl")
public class HelloEJBLocalImpl extends HelloEJBImpl implements HelloEJBLocal {
	@Override
	public String sayHello(String name) {
		return super.sayHello("HelloEJBLocalImpl " + name);
	}
}
