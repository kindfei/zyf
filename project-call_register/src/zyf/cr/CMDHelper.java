package zyf.cr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CMDHelper {
	private final static String YES = "Y";
	private final static String NO = "N";
	private static BufferedReader reader;
	private static PrintWriter writer;
	
	static {
		reader = new BufferedReader(new InputStreamReader(System.in));
		writer = new PrintWriter(System.out);
	}
	
	public static void pause() {
		writer.print("Press <enter> to continue.");
		writer.flush();
		try {
			reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String input(String msg) {
		String str = null;
		writer.print(msg + ">");
		writer.flush();
		try {
			str = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static boolean confirm(String msg) {
		boolean result = false;
		msg +="(" + YES + " or " + NO + ")>";
		try {
			while (true) {
				writer.print(msg);
				writer.flush();
				String cmd = reader.readLine();
				if (cmd.equalsIgnoreCase(YES)) {
					result = true;
					break;
				} else if (cmd.equalsIgnoreCase(NO)) {
					result = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int option(String[] opts) {
		int opt = 0;
		StringBuffer msg = new StringBuffer();
		for (int i = 0; i < opts.length; i++) {
			msg.append(i).append(". ").append(opts[i]).append("\n");
		}
		try {
			writer.print(msg.toString());
			while(true) {
				writer.print("Select your operation>");
				writer.flush();
				String cmd = reader.readLine();
				try {
					opt = Integer.parseInt(cmd);
				} catch (NumberFormatException e) {
				}
				if (opt >= 0 && opt < opts.length) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return opt;
	}
	
	public static void main(String[] args) {
		pause();
	}
}
