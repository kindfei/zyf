package fei.tools.util;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class InteractionHandler {
	private final static String trueFlag = "Y";
	private final static String falseFlag = "N";
	
	private final static String pausePrompt = "\nPress <Enter> key to continue";
	private final static String confirmPrompt = "(" + trueFlag + " or " + falseFlag + ")>";
	private final static String optionPrompt = "Select one option>";

	private static ConsoleWrapper console = new ConsoleWrapper();
	
	public static String input(String msg) {
		return console.readLine(msg + ">");
	}
	
	public static void pause(String msg) {
		console.readLine(msg + pausePrompt);
	}
	
	public static boolean confirm(String msg) {
		while (true) {
			String input = console.readLine(msg + confirmPrompt);
			if (input.equalsIgnoreCase(trueFlag)) {
				return true;
			} else if (input.equalsIgnoreCase(falseFlag)) {
				return false;
			}
		}
	}

	
	public static <T> T selectOption(String msg, T[] opts) {
		return opts[options(msg, opts)];
	}
	
	public static int options(String msg, Object[] opts) {
		if (opts.length == 0) {
			throw new IllegalArgumentException("opts.length cann't be 0");
		}
		
		StringBuilder s = new StringBuilder(msg + "\n");
		for (int i = 0; i < opts.length; i++) {
			s.append(i).append(". ").append(opts[i].toString()).append("\n");
		}
		
		int r = -1;
		while (true) {
			try {
				r = Integer.parseInt(console.readLine(s.append(optionPrompt).toString()));
				if (r >= 0 && r < opts.length) {
					return r;
				}
			} catch (NumberFormatException e) {
			}
		}
	}
	
	static class ConsoleWrapper {
		private static Console console = System.console();
		
		public String readLine(String prompt) {
			if (console != null) {
				return console.readLine(prompt);
			}
			
			System.out.print(prompt);
			System.out.flush();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String r = null;
			try {
				r = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return r;
		}
	}
}
