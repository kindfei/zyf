package test.ejb.hello.spring.ejb3;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.interceptor.Interceptors;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import test.service.HelloService;
import test.utils.CustomSBAI;

@MessageDriven(
	name = "spring.ejb3.HelloMDB",
	mappedName = "TestTopic",
	activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
	}
)
@Interceptors(CustomSBAI.class)
public class HelloMDB implements MessageListener {

	@Autowired
	private HelloService helloService;
	
    public HelloMDB() {
    	
    }

	@Override
    public void onMessage(Message message) {
		try {
			String str = ((TextMessage) message).getText();
			String r = helloService.sayHello(this.toString(), str);
			System.out.println(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
