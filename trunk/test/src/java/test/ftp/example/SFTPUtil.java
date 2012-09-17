package test.ftp.example;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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
import com.sshtools.j2ssh.authentication.SshAuthenticationClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;

/**
 * SFTPUtil to put trade file to server upload directory
 * 
 * @author yx47879
 * 
 */
public class SFTPUtil{
	private static Log log = LogFactory.getLog(SFTPUtil.class);
	
	private SftpClient sftpclt = null;

	private SshClient sshclt = null;

	private SshAuthenticationClient sshAuthClt = null;
	
	private String PASSWORD_AUTH_METHOD = "password";

    private String KBI_AUTH_METHOD = "keyboard-interactive";

	private String server = "";
	
	private String user = "";
	
	private String password = "";
	
	private String localDir = "";
	
	private String remoteDir = "";
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocalDir() {
		return localDir;
	}

	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}

	public String getRemoteDir() {
		return remoteDir;
	}

	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}

	public SFTPUtil(String server, String user, String password) {
		this.server = server;
		this.user= user;
		this.password= password;
	}

	/**
	 * create sftp connection
	 */
	private void getSftpConnection() throws Exception {
		String server = this.getServer();
		String user = this.getUser();
		final String pwd = this.getPassword();
		if (sshclt == null) {
			sshclt = new SshClient();
			sshclt.connect(server, new IgnoreHostKeyVerification());
		}
		//support multiple authentication (KBI & Password)
		List authMethods = sshclt.getAvailableAuthMethods(user);
        if(authMethods==null)
        	throw new Exception("No authencation information in server " + server);
        
        //if password authentication method is not available, will use KBI authentication
		if (!authMethods.contains(this.PASSWORD_AUTH_METHOD)
				&& authMethods.contains(this.KBI_AUTH_METHOD)) {
            KBIAuthenticationClient kbiclt = new KBIAuthenticationClient();
            kbiclt.setUsername(user);
            kbiclt.setKBIRequestHandler(new KBIRequestHandler() {
                public void showPrompts(String name, String instruction, KBIPrompt[] prompts) {
                    if(prompts==null) return;
					for (int i = 0; i < prompts.length; i++) {
                        prompts[i].setResponse(pwd);
                    }
                }
            });
            sshAuthClt = kbiclt;
        }
        // Default use Password authentication
        else{
			PasswordAuthenticationClient pwdclt = new PasswordAuthenticationClient();
			pwdclt.setUsername(user);
			pwdclt.setPassword(pwd);
			sshAuthClt = pwdclt;
        }
		if (sftpclt == null) {
			int result = sshclt.authenticate(sshAuthClt);
			if (result == AuthenticationProtocolState.COMPLETE) {
				sftpclt = sshclt.openSftpClient();
				log.info("SFTP connect successful");
				log.info("Remote: present working remote directory "
						+ sftpclt.pwd());
				log.info("Local: present working local directory "
						+ sftpclt.lpwd());
			} else {
				throw new Exception("Authenticating failed " + server
						+ ", " + user + ", " + pwd);
			}
		}
	}

	/**
	 * release sftp connection
	 */
	private void releaseSftpConnection() throws Exception {
		if (sftpclt != null) {
			try {
				sftpclt.quit();
				sftpclt = null;
				log.info("SftpClient closed");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (sshclt != null) {
			try {
				sshclt.disconnect();
				sshclt = null;
				log.info("SshClient closed");
			} catch (Exception es) {
				es.printStackTrace();
			}
		}
	}

	/**
	 * Put trade file to server file upload directory
	 * 
	 * @throws Exception
	 */
	public void putFileToServer(String localDir, String remoteDir) throws Exception {
		this.localDir = localDir;
		this.remoteDir = remoteDir;
		StringBuffer sb = new StringBuffer();
		try{
			// create sftp connection
			this.getSftpConnection();
			// set up local & remote dir
			sftpclt.lcd(this.localDir);
			log.info("local dir is :" + sftpclt.lpwd());
			sftpclt.cd(this.remoteDir);
			log.info("remote dir is :" + sftpclt.pwd());

			// prepare local files need to put to remote server
			File dir = new File(this.localDir);
			File[] files = dir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
			});
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				sftpclt.put(file.getAbsolutePath());
				//Special logic: have no idea why can't put .LOCK file to server. So by this way.
				if(file.getName().indexOf("LOCK999")>=0){
					sftpclt.rename(file.getName(), file.getName().replace("LOCK999", "LOCK"));
				}
				sb.append(file.getName() + "\n");
			}
			log.info("put following files sucessfully");
			log.info(sb.toString());
		}catch(Exception e){
			log.info("Failed when putting file to remote server: [Server:" + this.server + "]\nError Message:\n" + e.getMessage());
			throw new Exception(e);
		}finally{
			// release sftp connection
			this.releaseSftpConnection();
		}
	}
	
}


