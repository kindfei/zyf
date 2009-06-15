package org.zyf.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CmdHelper {
	private final static String trueFlag = "Y";
	private final static String falseFlag = "N";

	private static BufferedReader reader;
	private static PrintWriter writer;
	private static String pauseMsg;
	private static String confirmMsg;
	private static String optionMsg;
	
	static {
		reader = new BufferedReader(new InputStreamReader(System.in));
		writer = new PrintWriter(new OutputStreamWriter(System.out));
		pauseMsg = "\nPress <enter> key to continue";
		confirmMsg = "(" + trueFlag + " or " + falseFlag + ")>";
		optionMsg = "Choose your operation>";
	}
	
	public static void pause(String msg) {
		try {
			writer.print(msg + pauseMsg);
			writer.flush();
			reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean confirm(String msg) {
		boolean r = false;
		try {
			while (true) {
				writer.print(msg + confirmMsg);
				writer.flush();
				String cmd = reader.readLine();
				if (cmd.equalsIgnoreCase(trueFlag)) {
					r = true;
					break;
				} else if (cmd.equalsIgnoreCase(falseFlag)) {
					r = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public static int options(String[] opts) {
		StringBuffer msg = new StringBuffer("\n");
		for (int i = 0; i < opts.length; i++) {
			msg.append(i).append(". ").append(opts[i]).append("\n");
		}
		
		int r = 0;
		try {
			boolean isSended = false;
			int iCmd = 0;
			a:while (true) {
				if (!isSended) {
					writer.print(msg.toString());
					isSended = true;
				}
				writer.print(optionMsg);
				writer.flush();
				String cmd = reader.readLine();
				iCmd = -1;
				try {
					iCmd = Integer.parseInt(cmd);
				} catch (NumberFormatException e) {
				}
				if (iCmd >= 0 && iCmd < opts.length) {
					r = iCmd;
					break a;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public static String input(String msg) {
		String r = null;
		try {
			writer.print(msg + ">");
			writer.flush();
			r = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}
}
