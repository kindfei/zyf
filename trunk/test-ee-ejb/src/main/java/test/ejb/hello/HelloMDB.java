package test.ejb.hello;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(
	activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	},
	mappedName = "TestQueue"
)
public class HelloMDB implements MessageListener {

    public HelloMDB() {
    	
    }

    @Override
    public void onMessage(Message message) {
		try {
			String str = ((TextMessage) message).getText();
	        System.out.println(Thread.currentThread().getName() + "-" + this.toString() + ": " + str);
	        Thread.sleep(1 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
