package test.ejb.hello.spring.ejb3;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;

import test.ejb.hello.HelloSLSBLocal;
import test.ejb.hello.HelloSLSBRemote;
import test.service.HelloService;
import test.utils.CustomSBAI;

@Stateless(name="spring.ejb3.HelloSLSB", mappedName="s3hl")
@Interceptors(CustomSBAI.class)
public class HelloSLSB implements HelloSLSBRemote, HelloSLSBLocal {

	@Autowired
	private HelloService helloService;

	@Override
	public String sayHello(String name) {
		return helloService.sayHello(this.toString(), name);
	}
}
