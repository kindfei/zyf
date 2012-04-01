package test;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) throws Exception {
		
		System.out.println(Double.valueOf("NaN").isNaN());
	}
	
	
	public static void testDouble() throws ParseException {
		Double d = 25.03155d;
//		Double d = 25.00125d;
//		Double d = 25.12345d;
		
		BigDecimal d1 = BigDecimal.valueOf(d);
		BigDecimal d2 = new BigDecimal(d.toString());
		BigDecimal d3 = new BigDecimal(d);
		
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d3);
		
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
		nf.setGroupingUsed(true);
		nf.setMaximumFractionDigits(4);
		
		System.out.println(nf.parse(nf.format(d)));
		System.out.println(d1.setScale(4, RoundingMode.HALF_DOWN));
	}
	
	public static void rename(File src, File target) {
		File[] srcFiles = src.listFiles();
		File[] targetFiles = target.listFiles();
		
		for (File targetFile : targetFiles) {
			String targetFileName = targetFile.getName();
			targetFileName = targetFileName.substring(0, targetFileName.lastIndexOf("."));
			
			for (File srcFile : srcFiles) {
				String srcFileName = srcFile.getName();
				srcFileName = srcFileName.substring(0, srcFileName.lastIndexOf("."));
				srcFileName = srcFileName.substring(0, srcFileName.lastIndexOf("."));
				
				if (srcFileName.startsWith(targetFileName)) {
					targetFile.renameTo(new File(targetFile.getPath().substring(0, targetFile.getPath().lastIndexOf(File.separator) + 1) + srcFileName + ".mp3"));
					break;
				}
			}
		}
	}
	
	public static void rename(File root) {
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				rename(file);
			} else {
				String name = file.getName();
				String[] strs = name.split("\\.");
				String r = "";
				for (int i = 0; i < strs.length; i++) {
					if (i == 0) {
						r += strs[i];
					} else if (i == 1) {
						r += "." + strs[i];
					} else if (i == 2) {
						if (Pattern.matches("S\\d{2}E\\d{2}", strs[i])) {
							r += "-" + strs[i].substring(strs[i].indexOf("E") + 1) + " [" + strs[++i];
						} else {
							r += " [" + strs[i];
						}
					} else {
						if (strs[i].equalsIgnoreCase("chs") || strs[i].equalsIgnoreCase("en")) {
							r += "]." + strs[i] + ".lrc";
							break;
						} else if (strs[i].equalsIgnoreCase("cn") || strs[i].equalsIgnoreCase("lrc")) {
							r += "].chs.lrc";
							break;
						} else {
							r += " " + strs[i];
						}
					}
				}
				file.renameTo(new File(file.getPath().substring(0, file.getPath().lastIndexOf(File.separator) + 1) + r));
			}
		}
	}
}
