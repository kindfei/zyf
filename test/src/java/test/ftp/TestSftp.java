package test.ftp;

import java.io.File;

import test.ftp.SftpTemplate.SftpCallback;


import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKey;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;

public class TestSftp {
	public static void main(String[] args) throws Exception {
		SftpTemplate inst = new SftpTemplate();
		
		inst.setHostname("securefiletransferuat.citigroup.net");
		inst.setUsername("intpf_zimmer");
		inst.setPrivateKeyFile("C:/yifei/download/id_rsa_2048_a");
		
		SshPrivateKeyFile pkf = SshPrivateKeyFile.parse(new File(""));
		SshPrivateKey spk = pkf.toPrivateKey(null);
		
		inst.execute(new SftpCallback() {
			
			@Override
			public void doInSftp(SftpClient sftp) throws Exception {
				System.out.println(sftp.ls());
			}
		});
	}
}
