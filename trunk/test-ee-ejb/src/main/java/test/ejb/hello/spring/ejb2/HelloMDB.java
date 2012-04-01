package test.ejb.hello.spring.ejb2;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractMessageDrivenBean;

import test.service.HelloService;

@MessageDriven(
	name = "spring.ejb2.HelloMDB",
	mappedName = "TestTopic",
	activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
	}
)
public class HelloMDB extends AbstractMessageDrivenBean implements MessageListener {

	private static final long serialVersionUID = 1L;
	
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

    @PostConstruct
    public void ejbCreate() {
    	super.ejbCreate();
    }
    
	@Override
	protected void onEjbCreate() {
		helloService = (HelloService) getBeanFactory().getBean("helloService");
	}

	@Override
	public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) {
		super.setMessageDrivenContext(messageDrivenContext);
		setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
		setBeanFactoryLocatorKey("businessBeanFactory");
	}
}
