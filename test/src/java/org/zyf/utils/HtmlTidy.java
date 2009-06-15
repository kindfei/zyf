package org.zyf.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

public class HtmlTidy {
	private static final Logger log = LogManager.getLogger(HtmlTidy.class);
	
	private String charset;
	
	public HtmlTidy(String charset) {
		this.charset = charset;
	}
	
	public void execute(File root) throws IOException {
		File[] files = root.listFiles();
		for (int i=0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				execute(file);
			} else {
				parse(file);
			}
		}
	}

	private void parse(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
		StringBuffer sb = new StringBuffer();
		for(String str = reader.readLine(); str != null; str = reader.readLine()) {
			sb.append(str).append("\n");
		}
		reader.close();
		
		byte[] bytes = sb.toString().getBytes("UTF-8");
		InputStream in = new ByteArrayInputStream(bytes);
		
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()));
		
        Tidy tidy = new Tidy();
        tidy.setXmlOut(true);
        tidy.setCharEncoding(Configuration.UTF8);
        
        tidy.parse(in, out);
        
        out.close();
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			log.error("Missing required arguments.");
			return;
		}
		
		String dir = args[0];
		String charset = args[1];
		
		HtmlTidy inst = new HtmlTidy(charset);
		
		log.info("Html tidy begin. dir=" + dir + ", charset" + charset);
		
		try {
			inst.execute(new File(dir));
		} catch (IOException e) {
			log.error("Html tidy execute error.", e);
		}
		
		log.info("Html tidy end. dir=" + dir + ", charset" + charset);
	}
}
