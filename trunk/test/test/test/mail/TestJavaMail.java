package test.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

public class TestJavaMail {

	public static void main(String[] args) {
		TestJavaMail mail = new TestJavaMail();
		try {
			mail.sendMail();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	void sendMail() throws Exception {
		
		String host = "smtp.tom.com";
		String user = "iameinstein";
		String password = "Einstein";
		String from = "iameinstein@tom.com";
		String to = "iameinstein@163.com";
		String cc = "iameinstein@sina.com";
		String bcc = "zhangyf@livedoor.cn";
		String subject = "Test Mail";
		String text = "This is a test mail!";
		boolean auth = true;
		
		Properties props = new Properties();
		props.put("mail.store.protocol", "");
		props.put("mail.transport.protocol", "");
		props.put("mail.host", host);
		props.put("mail.user", user);
		props.put("mail.from", from);
	    if (auth)
			props.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(props);
		session.setDebug(true);
		
		MimeMessage msg = new MimeMessage(session);
		
		//msg.setFrom(new InternetAddress(from));
		
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
		msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
		msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
		msg.setSubject(subject);
		msg.setText(text);
	    msg.setHeader("X-Mailer", "smtpsend");
	    msg.setSentDate(new Date());
	    
	    SMTPTransport t = (SMTPTransport)session.getTransport("smtp");
	    
	    try {
	    	if(auth)
	    		t.connect(host, user, password);
	    	else
			    t.connect();
			t.sendMessage(msg, msg.getAllRecipients());
	    } finally {
	    	System.out.println("Response: " + t.getLastServerResponse());
	    	t.close();
	    }
	    System.out.println("\nMail was sent successfully.");
	}

}
