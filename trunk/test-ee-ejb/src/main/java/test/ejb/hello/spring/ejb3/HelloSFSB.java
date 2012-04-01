package test.ejb.hello.spring.ejb3;

import javax.ejb.Stateful;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;

import test.ejb.hello.HelloSFSBLocal;
import test.ejb.hello.HelloSFSBRemote;
import test.service.HelloService;
import test.utils.CustomSBAI;

@Stateful(name="spring.ejb3.HelloSFSB", mappedName="s3hf")
@Interceptors(CustomSBAI.class)
public class HelloSFSB implements HelloSFSBRemote, HelloSFSBLocal {

	@Autowired
	private HelloService helloService;

	@Override
	public String sayHello(String name) {
		return helloService.sayHello(this.toString(), name);
	}

}
