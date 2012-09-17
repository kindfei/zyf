package test.ftp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.KBIAuthenticationClient;
import com.sshtools.j2ssh.authentication.KBIPrompt;
import com.sshtools.j2ssh.authentication.KBIRequestHandler;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.authentication.PublicKeyAuthenticationClient;
import com.sshtools.j2ssh.authentication.SshAuthenticationClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKey;

/**
 * 
 * @author yz69579
 *
 */
public class SftpTemplate {
	private final static Log log = LogFactory.getLog(SftpTemplate.class);
	
	private String hostname;
	private String username;
	private String password;
	private String privateKeyFile;
	
	private final static String PASSWORD_AUTH_METHOD = "password";

    private final static String KBI_AUTH_METHOD = "keyboard-interactive";
	
	public void execute(SftpCallback action) {
		this.execute(action, hostname, username, password);
	}
	
	@SuppressWarnings("unchecked")
	public void execute(SftpCallback action, String hostname, String username, final String password) {
		SshClient ssh = new SshClient();
		try {
			ssh.connect(hostname, new IgnoreHostKeyVerification());
			
	        SshAuthenticationClient sshAuthClt = null;

			if (privateKeyFile != null) {
				PublicKeyAuthenticationClient pkclt = new PublicKeyAuthenticationClient();
				pkclt.setUsername(username);
				pkclt.setKeyfile(privateKeyFile);
			} else {
				// Support multiple authentication (KBI & Password)
				List<String> authMethods = ssh.getAvailableAuthMethods(username);
		        if(authMethods == null) {
		        	throw new Exception("No authencation information in server " + hostname);
		        }
		        
		        // If password authentication method is not available, will use KBI authentication
		        // Modified by Terry, add KBI authentication.
				if (!authMethods.contains(PASSWORD_AUTH_METHOD) && authMethods.contains(KBI_AUTH_METHOD)) {
		            KBIAuthenticationClient kbiclt = new KBIAuthenticationClient();
		            kbiclt.setUsername(username);
		            kbiclt.setKBIRequestHandler(new KBIRequestHandler() {
		                public void showPrompts(String name, String instruction, KBIPrompt[] prompts) {
		                    if (prompts == null) {
		                    	return;
		                    }
		                    for (KBIPrompt kbiPrompt : prompts) {
		                    	kbiPrompt.setResponse(password);
		                    }
		                }
		            });
		            sshAuthClt = kbiclt;
		        } else {// Default use Password authentication
					PasswordAuthenticationClient pwdclt = new PasswordAuthenticationClient();
					pwdclt.setUsername(username);
					pwdclt.setPassword(password);
					sshAuthClt = pwdclt;
		        }
			}
			
			int result = ssh.authenticate(sshAuthClt);
			
			if (result == AuthenticationProtocolState.COMPLETE) {
				log.info("Connect SFTP server : " + hostname + " successfully.");
				SftpClient sftp = ssh.openSftpClient();
				// Do action
				action.doInSftp(sftp);
				
			    sftp.quit();
			} else {
				log.error("Sftp connect failed.");
				throw new RuntimeException("Sftp connect failed.");
			}

		} catch (Exception e) {
			log.error("SftpTemplate execute error.", e);
			throw new RuntimeException(e);
		} finally {
			if (ssh.isConnected()) {
				ssh.disconnect();
			}
		}
	}
	
	public interface SftpCallback {
		void doInSftp(SftpClient sftp) throws Exception;
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

	public String getPrivateKeyFile() {
		return privateKeyFile;
	}

	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}
}
