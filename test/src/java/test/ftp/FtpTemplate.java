package test.ftp;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * @author yz69579
 *
 */
public class FtpTemplate {
	private final static Log log = LogFactory.getLog(FtpTemplate.class);
	
	private String hostname;
	private String username;
	private String password;
	
	private boolean binaryTransfer;
	
	public void execute(FtpCallback action) {
		this.execute(action, hostname, username, password, binaryTransfer);
	}
	
	public void execute(FtpCallback action, String hostname, String username, String password, boolean binaryTransfer) {
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(hostname);
			
			if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				if (ftp.login(username, password)) {
					if (binaryTransfer) {
		                ftp.setFileType(FTP.BINARY_FILE_TYPE);
					}
					
					// TODO
					ftp.enterLocalPassiveMode();
					
					// Do action
					action.doInFtp(ftp);
				} else {
					ftp.logout();
				}
			}
		} catch (Exception e) {
			log.error("FtpTemplate execute error.", e);
			throw new RuntimeException(e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
		}
	}
	
	public interface FtpCallback {
		void doInFtp(FTPClient ftp) throws Exception;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isBinaryTransfer() {
		return binaryTransfer;
	}

	public void setBinaryTransfer(boolean binaryTransfer) {
		this.binaryTransfer = binaryTransfer;
	}
	
}
