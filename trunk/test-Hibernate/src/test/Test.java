package test;

public class Test {
	public static void main(String[] args) {
		String drivers = (String) java.security.AccessController.doPrivileged(
				new sun.security.action.GetPropertyAction("jdbc.drivers"));
		System.out.println(drivers);
	}
}
