package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SourceConvert {
	
	public static void main(String args[]) {
		SourceConvert inst = new SourceConvert();
		int len = args.length;
		for (int i=1; i<len; i++) {
			inst.process(new File(args[0] + "/" + args[i]));
		}
	}
	
	private void process(File rootFile) {
		File[] files = null;
		if (rootFile.isFile()) {
			files = new File[]{rootFile};
		} else if (rootFile.isDirectory()) {
			files = rootFile.listFiles();
		}
		int len = files.length;
		for (int i= 0; i < len; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				process(file);
			} else if (file.isFile()) {
				encrypt(file);
				String fileName = file.getName();
				String name = fileName.substring(0, fileName.lastIndexOf('.'));
				File parent = new File(file.getParent());
				File[] broFiles = parent.listFiles();
				for (int j=0; j<broFiles.length; j++) {
					File broFile = broFiles[j];
					String broName = broFile.getName();
					if (broName.indexOf(".class") != -1 && broName.indexOf(name + "$") != -1) {
						encrypt(broFile);
					}
				}
			}
		}
	}
	
	private void encrypt(File file) {
		byte key = (byte)0xF1;
		byte[] src = readByteArray(file);
		byte[] code = new byte[src.length];
		for (int i = 0; i < src.length; i++) {
			byte b = src[i];
			code[i] = (byte)(b ^ key);
		}
		writeByteArray(code, toWriteFile(file));
		file.delete();
	}
	
	private File toWriteFile(File file) {
		String path = file.getPath();
		System.out.println("Encrypt Class: " + path);
		String writeFile = path.substring(0, path.lastIndexOf('.')) + ".clz";
		return new File(writeFile);
	}
	
	private void writeByteArray(byte b[], File file) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(b);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private byte[] readByteArray(File file) {
		byte b[] = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			b = new byte[in.available()];
			in.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}
}
