package zyf.sm.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

public class Helper {
	private static Log log = Helper.getLog(Helper.class);
	
	static {
		try {
			PropertyConfigurator.configure(getProperties("./conf/log4j.properties"));
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
			BasicConfigurator.configure();
		}
	}
	
	public static Properties getProperties(String propertyFileName)
			throws java.io.FileNotFoundException {
		try {
			String configPath = System.getProperty("configPath");
			if (configPath != null) propertyFileName = configPath + propertyFileName;
			
			Properties props = new Properties();
			
			File file = new File(propertyFileName);
			if (file.exists()) {
				props.load(new BufferedInputStream(new FileInputStream(file)));
				return props;
			}
			
			InputStream in = getResource(propertyFileName);
			if (in != null) {
				props.load(in);
				return props;
			}
			
			throw new Exception();
	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new java.io.FileNotFoundException(propertyFileName + " not found");
		} finally {}
	}
	
	public static InputStream getResource(String fileName) {
		InputStream in = ClassLoader.getSystemResourceAsStream(fileName);
		if(in == null) return null;
		return new BufferedInputStream(in);
	}
	
	public static Log getLog(Class cls) {
		return LogFactory.getLog(cls);
	}
	
	public static String docPath = "./conf/connection.xml";
	
	public static Document readDocument() {
		Document document = null;
		try {
			File file = new File(docPath);
			if (!file.exists()) {
				Document doc = DocumentHelper.createDocument();
				doc.addElement("connections");
				writeDocument(doc);
				document = doc;
			} else {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				org.w3c.dom.Document domDocument = builder.parse(docPath);
				DOMReader reader = new DOMReader();
				document = reader.read(domDocument);
			}
		} catch (ParserConfigurationException e) {
			log.error(e.getMessage(), e);
		} catch (SAXException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
        return document;
	}
	
	public static void writeDocument(Document document) {
		try {
			OutputFormat format = new OutputFormat("\t", true);
			FileWriter out = new FileWriter(docPath);
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			out.close();
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	

	public static Object copy(Object obj) {
		if (obj == null)
			return null;
		Object clone = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.close();
			ObjectInputStream ois = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			clone = ois.readObject();
			ois.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return clone;
	}
}
