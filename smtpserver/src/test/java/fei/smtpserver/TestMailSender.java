package fei.smtpserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class TestMailSender {
	private JavaMailSenderImpl mailSender;
	
	public TestMailSender() {
		mailSender = new JavaMailSenderImpl();
		mailSender.setHost("localhost");
		mailSender.setPort(6628);
	}

	public void sendMail() throws MessagingException {
		MimeMessage msg = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setFrom("iameinstein@tom.com");
		helper.setTo(new String[] {"iameinstein@163.com", "iameinstein@sina.com"});
		helper.setCc(new String[] {"iameinstein@sohu.com", "iameinstein@qq.com"});
		helper.setSubject("test subject");
		
		helper.setText("test text");

		ByteArrayResource res = new ByteArrayResource("test file content".getBytes());
		helper.addAttachment("testFile.txt", res);
		
		mailSender.send(msg);
		System.out.println("Mail sent successfully");
	}
	
	public static void main(String[] args) throws MessagingException {
		final TestMailSender mailer = new TestMailSender();
		
		Runnable r = new Runnable() {
			public void run() {
				try {
					mailer.sendMail();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		};
		
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < 100; i++) {
			es.execute(r);
		}
		es.shutdown();
	}
}
