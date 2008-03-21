package test.basic.charsets;

public class SysEncoding {
	public static void main(String[] args) {
		System.out.println(System.getProperty("file.encoding"));
		System.out.println(System.getProperty("JVM.encoding"));
	}
}
