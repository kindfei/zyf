package incubation.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author zhangyf
 *
 */
public class CommandUtils {
	private final static String YES = "Y";
	private final static String NO = "N";

	private final static String PROMPT = ">";
	private final static String PAUSE_PROMPT = "\nPress <enter> key to continue";
	private final static String CONFIRM_PROMPT = "(" + YES + " or " + NO + ")>";
	private final static String CHOOSE_PROMPT = "Choose the number of option>";
	
	private static IOHanlder io =  new IOHanlder();

	public static void pause(String msg) {
		try {
			io.print(msg + PAUSE_PROMPT);
			io.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean confirm(String msg) {
		boolean r = false;
		try {
			for (;;) {
				io.print(msg + CONFIRM_PROMPT);
				String cmd = io.readLine();
				if (cmd.equalsIgnoreCase(YES)) {
					r = true;
					break;
				} else if (cmd.equalsIgnoreCase(NO)) {
					r = false;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	public static int choose(String[] opts) {
		StringBuffer msg = new StringBuffer("\n");
		for (int i = 0; i < opts.length; i++) {
			msg.append(i).append(" ").append(opts[i]).append("\n");
		}

		int r = -1;
		try {
			io.print(msg.toString());
			for (;;) {
				io.print(CHOOSE_PROMPT);
				try {
					r = Integer.parseInt(io.readLine());
				} catch (NumberFormatException e) {
				}
				if (r >= 0 && r < opts.length) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return r;
	}

	public static String input(String msg) {
		String r = null;
		try {
			io.print(msg + PROMPT);
			r = io.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public static void close() {
		io.close();
	}
	
	static class IOHanlder {
		private BufferedReader reader;
		
		IOHanlder() {
			reader = new BufferedReader(new InputStreamReader(System.in));
		}
		
		public String readLine() throws IOException {
			return reader.readLine();
		}
		
		public void print(String msg) {
			System.out.print(msg);
		}
		
		public void println(String msg) {
			System.out.println(msg);
		}
		
		public void close() {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
