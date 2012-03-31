package fei.smtpserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.HookReturnCode;
import org.apache.james.protocols.smtp.hook.MessageHook;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 
 * @author yz69579
 *
 */
public class RecipientFilterHook implements MessageHook {
	private final static Log log = LogFactory.getLog(RecipientFilterHook.class);
	
	private MessageDumper messageDumper;
	
	private JavaMailSender mailSender;
	
	private List<String> filterRegexs;
	
	private List<String> additionalRecipients;
	
	private boolean appendOriginalRecipient;
	
	public RecipientFilterHook() {
		
	}

	public MessageDumper getMessageDumper() {
		return messageDumper;
	}

	public void setMessageDumper(MessageDumper messageDumper) {
		this.messageDumper = messageDumper;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public List<String> getFilterRegexs() {
		return filterRegexs;
	}

	public void setFilterRegexs(List<String> filterRegexs) {
		this.filterRegexs = filterRegexs;
	}

	public List<String> getAdditionalRecipients() {
		return additionalRecipients;
	}

	public void setAdditionalRecipients(List<String> additionalRecipients) {
		this.additionalRecipients = additionalRecipients;
	}

	public boolean isAppendOriginalRecipient() {
		return appendOriginalRecipient;
	}

	public void setAppendOriginalRecipient(boolean appendOriginalRecipient) {
		this.appendOriginalRecipient = appendOriginalRecipient;
	}

	@Override
	public HookResult onMessage(SMTPSession session, MailEnvelope mail) {
		log.info("Message hook begin.");
		
		log.info("Sender: " + mail.getSender());
		log.info("Recipients: " + mail.getRecipients());
		
		try {
			// Create message
			MimeMessage message = new MimeMessage(Session.getInstance(System.getProperties()), mail.getMessageInputStream());
			
			// Dump
			if (messageDumper != null) {
				try {
					messageDumper.dump(message);
				} catch (Exception e) {
					log.warn("Message dump failed.", e);
				}
			}
			
			// Append original recipient
			if (appendOriginalRecipient) {
				appendRecipient(message);
			}
			
			// Recipient filter
			List<Address> addresses = new ArrayList<Address>();
			if (filterRegexs != null) {
				log.debug("filterRegexs: " + filterRegexs);
				addresses.addAll(Arrays.asList(message.getAllRecipients()));
				for (Iterator<Address> i = addresses.iterator(); i.hasNext();) {
					Address address = i.next();
					for (String regex : filterRegexs) {
						if (Pattern.matches(regex, address.toString())) {
							i.remove();
							log.info("Remove matched recipient. address=" + address + ", regex=" + regex);
						}
					}
				}
				log.info("Remaining recipients: " + addresses);
			} else {
				addresses.clear();
				log.info("No regex specified, remove all recipients.");
			}
			
			for (String address : additionalRecipients) {
				addresses.add(new InternetAddress(address));
				log.info("Add additional recipient. address=" + address);
			}
			
			message.setRecipients(Message.RecipientType.TO, addresses.toArray(new Address[] {}));
			message.setRecipients(Message.RecipientType.CC, new Address[] {});
			message.setRecipients(Message.RecipientType.BCC, new Address[] {});
			
			// Send
			mailSender.send(message);
			log.info("Mail sent successfully. addresses=" + addresses);
			
		} catch (Exception e) {
			log.error("Message hook error.", e);
			return new HookResult(HookReturnCode.DENY);
		}

		log.info("Message hook end.");
		return new HookResult(HookReturnCode.OK);
	}
	
	private void appendRecipient(MimeMessage message) throws Exception {
		StringBuilder r = new StringBuilder();
		r.append("\n--------------------------- ORIGINAL RECIPIENT INFOMATION ---------------------------");
		r.append("\nTO: ").append(toString(message.getRecipients(Message.RecipientType.TO)));
		r.append("\nCC: ").append(toString(message.getRecipients(Message.RecipientType.CC)));
		r.append("\nBCC: ").append(toString(message.getRecipients(Message.RecipientType.BCC)));
		
		append(message, r.toString());
	}
	
	private String toString(Address[] addrs) throws Exception {
		if (addrs == null || addrs.length == 0) {
			return "";
		} else {
			return Arrays.asList(addrs).toString();
		}
	}

	private void append(Part p, String t) throws Exception {
		Object o = p.getContent();

		if (o instanceof String) {
			p.setText(o + t);
		} else if (o instanceof Multipart) {
			Multipart mp = (Multipart) o;
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setText(t);
			mp.addBodyPart(mbp);
		} else if (o instanceof Message) {
			append((Part) p.getContent(), t);
		} else {
			log.warn("Append text failed. content=" + o);
		}
	}
}
