package test.ejb;

import javax.ejb.Stateless;

@Stateless(mappedName = "HelloEJBRemoteImpl")
public class HelloEJBRemoteImpl extends HelloEJBImpl implements HelloEJBRemote {
	@Override
	public String sayHello(String name) {
		return super.sayHello("HelloEJBRemoteImpl " + name);
	}
}
