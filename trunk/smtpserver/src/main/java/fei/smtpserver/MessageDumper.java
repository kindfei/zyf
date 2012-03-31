package fei.smtpserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.AddressException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author yz69579
 *
 */
public class MessageDumper {
	private final static Log log = LogFactory.getLog(MessageDumper.class);

	private final static String indentStr = "                                               ";
	
	private boolean showStructure = true;
	
	private boolean saveAttachments = false;
	
	private String savePath = "";
	
	public MessageDumper() {
		
	}

	public boolean isShowStructure() {
		return showStructure;
	}

	public void setShowStructure(boolean showStructure) {
		this.showStructure = showStructure;
	}

	public boolean isSaveAttachments() {
		return saveAttachments;
	}

	public void setSaveAttachments(boolean saveAttachments) {
		this.saveAttachments = saveAttachments;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	public void dump(Message msg) throws Exception {
		MessageDescriber md = new MessageDescriber();
		md.describePart(msg);
		log.info("Mail detail: " + md.result());
	}
	
	/**
	 * 
	 * @author yz69579
	 *
	 */
	class MessageDescriber {
		private int level = 0;
		
		private Appendable r = new StringBuilder();
		
		public String result() {
			return r.toString();
		}

		public void describePart(Part p) throws Exception {
			if (p instanceof Message) {
				describeEnvelope((Message) p);
			}

			String ct = p.getContentType();
			try {
				append("CONTENT-TYPE: " + (new ContentType(ct)).toString());
			} catch (ParseException pex) {
				append("BAD CONTENT-TYPE: " + ct);
			}
			
			String filename = p.getFileName();
			if (filename != null) {
				append("FILENAME: " + filename);
			}

			/*
			 * Using isMimeType to determine the content type avoids fetching the
			 * actual content data until we need it.
			 */
			if (p.isMimeType("text/plain")) {
				append("This is plain text");
				append("---------------------------");
				if (!showStructure && !saveAttachments) {
					append((String) p.getContent());
				}
			} else if (p.isMimeType("multipart/*")) {
				append("This is a Multipart");
				append("---------------------------");
				Multipart mp = (Multipart) p.getContent();
				level++;
				for (int i = 0; i < mp.getCount(); i++) {
					describePart(mp.getBodyPart(i));
				}
				level--;
			} else if (p.isMimeType("message/rfc822")) {
				append("This is a Nested Message");
				append("---------------------------");
				level++;
				describePart((Part) p.getContent());
				level--;
			} else {
				if (!showStructure && !saveAttachments) {
					Object o = p.getContent();
					if (o instanceof String) {
						append("This is a string");
						append("---------------------------");
						append((String) o);
					} else if (o instanceof InputStream) {
						append("This is just an input stream");
						append("---------------------------");
						InputStream is = (InputStream) o;
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						int c;
						while ((c = is.read()) != -1) {
							out.write(c);
						}
						append(out.toString());
					} else {
						append("This is an unknown type");
						append("---------------------------");
						append(o.toString());
					}
				} else {
					append("---------------------------");
				}
			}
			
			if (saveAttachments && level != 0 && !p.isMimeType("multipart/*")) {
				String disp = p.getDisposition();
				if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
					if (filename == null) {
						filename = "Attachment" + System.currentTimeMillis();
					}
					append("Saving attachment to file " + filename);
					try {
						new File(savePath).mkdirs();
						File f = new File(savePath + File.separator + filename);
						if (f.exists()) {
							throw new IOException("file exists");
						}
						((MimeBodyPart) p).saveFile(f);
					} catch (IOException ex) {
						append("Failed to save attachment: " + ex);
					}
					append("---------------------------");
				}
			}
		}

		public void describeEnvelope(Message m) throws Exception {
			append("This is the message envelope");
			append("---------------------------");
			
			append("FROM: " + Arrays.asList(m.getFrom()));
			append("REPLY TO: " + Arrays.asList(m.getReplyTo()));
			append("TO: " + toString(m.getRecipients(Message.RecipientType.TO)));
			append("CC: " + toString(m.getRecipients(Message.RecipientType.CC)));
			append("BCC: " + toString(m.getRecipients(Message.RecipientType.BCC)));
			append("SUBJECT: " + m.getSubject());
			append("SendDate: " + m.getSentDate());

			// FLAGS
			Flags flags = m.getFlags();
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			
			Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags
			for (int i = 0; i < sf.length; i++) {
				String s;
				Flags.Flag f = sf[i];
				if (f == Flags.Flag.ANSWERED)
					s = "\\Answered";
				else if (f == Flags.Flag.DELETED)
					s = "\\Deleted";
				else if (f == Flags.Flag.DRAFT)
					s = "\\Draft";
				else if (f == Flags.Flag.FLAGGED)
					s = "\\Flagged";
				else if (f == Flags.Flag.RECENT)
					s = "\\Recent";
				else if (f == Flags.Flag.SEEN)
					s = "\\Seen";
				else
					continue; // skip it
				if (first)
					first = false;
				else
					sb.append(' ');
				sb.append(s);
			}

			String[] uf = flags.getUserFlags(); // get the user flag strings
			for (int i = 0; i < uf.length; i++) {
				if (first) {
					first = false;
				} else {
					sb.append(' ');
				}
				sb.append(uf[i]);
			}
			append("FLAGS: " + sb.toString());

			// X-MAILER
			String[] hdrs = m.getHeader("X-Mailer");
			if (hdrs != null) {
				append("X-Mailer: " + hdrs[0]);
			} else {
				append("X-Mailer NOT available");
			}
		}
		
		private String toString(Address[] addresses) throws AddressException {
			if (addresses == null) {
				return "";
			}
			
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < addresses.length; j++) {
				if (j > 0) sb.append(", ");
				sb.append(addresses[j].toString());
				
				InternetAddress ia = (InternetAddress) addresses[j];
				if (ia.isGroup()) {
					InternetAddress[] ga = ia.getGroup(false);
					sb.append("(");
					for (int k = 0; k < ga.length; k++) {
						if (j > 0) sb.append(", ");
						sb.append(ga[k].toString());
					}
					sb.append(")");
				}
			}
			return sb.toString();
		}

		private void append(String s) throws IOException {
			r.append("\n");
			
			if (showStructure) {
				r.append(indentStr.substring(0, level * 2));
			}
			
			r.append(s);
		}
	}
}
