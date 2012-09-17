package test.jni;

/**
 * 
 * <ul>
 * <li><p> MinGW, must add option <code>-D_JNI_IMPLEMENTATION_ -Wl,--kill-at</code>
 * in the link command.
 * <li><p>Add <code>-Djava.library.path=lib</code> to VM arguments
 * </ul>
 * 
 * @author yz69579
 *
 */
public class TestJNI {
	
	static {
		System.loadLibrary("libtest-cpp-lib");
	}
	
	public native String sayHello(String name);
	
	public static void main(String[] args) {
		TestJNI inst = new TestJNI();
		System.out.println(inst.sayHello("world"));
	}
}
