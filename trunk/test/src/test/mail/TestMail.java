package test.mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class TestMail {
	public static void main(String[] args) {
		TestMail.test("localhost", "receiver1@10.4.5.103", "sender1@10.4.5.103");
		TestMail.test("localhost", "zhangyf@livedoor.cn", "sender1@10.4.5.103");
		TestMail.test("localhost", "zhangyf@livedoor.cn", "anonymous@somehost.com");
		TestMail.test("10.4.5.103", "receiver1@10.4.5.103", "sender1@10.4.5.103");
		TestMail.test("mail.livedoor.cn", "receiver1@10.4.5.103", "zhangyf@livedoor.cn");
		TestMail.test("mail.livedoor.cn", "zhangyf@livedoor.cn", "anonymous@somehost.com");
		TestMail.test("mail.livedoor.cn", "receiver1@10.4.5.103", "anonymous@somehost.com");
		TestMail.test("10.4.5.202", "zhangyf@livedoor.cn", "anonymous@somehost.com");
	}
	
	static void test(String host, String to, String from) {
		try {
			SimpleEmail email = new SimpleEmail();
			
			email.setHostName(host);
			email.addTo(to, "you");
			email.setFrom(from, "Me");
			email.setSubject("Test message");
			email.setMsg("This is a simple test of commons-email");
			email.send();
			System.out.println("send OK!! Host: <" + host + ">, To: <" + to + ">, from: <" + from + ">");
		} catch (EmailException e) {
			System.err.println("send failed!! Host: <" + host + ">, To: <" + to + ">, from: <" + from + ">");
			//e.printStackTrace();
		}
	}
}
