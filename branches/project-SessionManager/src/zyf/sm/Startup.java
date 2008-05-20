package zyf.sm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;


public class Startup extends ClassLoader {
	public static byte[] rawKey;
	
	public Startup(byte[] rawKey, ClassLoader parent) {
		super(parent);
		Startup.rawKey = rawKey;
	}

	public static void cMain(Object[] objs) {
		try {
			ClassLoader base = ClassLoader.getSystemClassLoader();
			Startup loader = new Startup((byte[])objs[0], base);
			Class clasz = loader.loadClass("zyf.sm.gui.MainShell");
			Method m = clasz.getMethod("display", null);
			m.invoke(clasz.newInstance(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Class findClass(String name) throws ClassNotFoundException {
		String fileName = name.replace('.', '/') + ".clz";
		System.out.println("_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
		System.out.println(fileName);
		System.out.println("_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
		InputStream in = null;
		Class clasz = null;
		try {
			in = getResourceAsStream(fileName);
			int len = in.available();
			byte[] code = new byte[len];
			for (int i = in.read(code); i < len;) {
				i = i + in.read(code, i, len-i);
			}
			if (code != null) {
				byte[] src = decrypt(code);
				clasz = defineClass(name, src, 0, src.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ClassNotFoundException(name);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					System.err.println(e);
				}
		}
		return clasz;
	}
	
	private byte[] decrypt(byte[] code) {
		byte key = (byte)0xF1;
		byte[] src = new byte[code.length];
		for (int i = 0; i < code.length; i++) {
			byte b = code[i];
			src[i] = (byte)(b ^ key);
		}
		return src;
	}
}
