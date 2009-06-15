package test.basic.xml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.io.DOMReader;
import org.dom4j.io.SAXReader;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;

public class TestDom4j {
	public static void main(String[] args) {
		try {
			BufferedInputStream in = new BufferedInputStream(ClassLoader.getSystemResourceAsStream("dr.xml"));
			XPathTest(DOMRead(tidyRead(in)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static org.w3c.dom.Document tidyRead(InputStream in) {
		Tidy tidy = new Tidy();
		tidy.setXmlOut(true);
		tidy.setCharEncoding(Configuration.UTF8);
		org.w3c.dom.Document domDocument = tidy.parseDOM(in, null);
		return domDocument;
	}
	
	static org.w3c.dom.Document read(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document domDocument = builder.parse(in);
		return domDocument;
	}
	
	static Document DOMRead(org.w3c.dom.Document domDocument) {
		DOMReader domReader = new DOMReader();
		return domReader.read(domDocument);
	}
	
	static void walkDocument(Document doc) {
		Element root = doc.getRootElement();
		walk(root);
	}
	
	static void walk(Element root) {
		int count = root.nodeCount();
		
		for (int i = 0; i < count; i++) {
			Node node = root.node(i);
			System.out.println("Node Type Name: " + node.getNodeTypeName());
			
			switch (node.getNodeType()) {
			case Node.ELEMENT_NODE:
				Element element = (Element) node;
				System.out.println("Element: " + element.getName());
				
				List list = element.attributes();
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					Attribute a = (Attribute) iter.next();
					System.out.println("Attribute: " + a.getName() + " - " + a.getValue());
				}
				
				walk(element);
				break;

			case Node.TEXT_NODE:
				Text text = (Text) node;
				String str = text.getStringValue().trim();
				if (!str.equals(""))
					System.out.println("Text: " + str);
				break;
				
			default :
				break;
			}
		}
	}
	
	static void XPathTest(Document doc) throws DocumentException {
//		List nodes = doc.selectNodes("/html/body/table/tr/td/table");
//		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
//			Node n = (Node) iter.next();
//			if (n.getNodeType() == Node.ELEMENT_NODE) walk((Element)n);
//		}
		
		List nodes = doc.selectNodes("/html/body/table/tr/td/table/tr[position()>3]");
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			Node n = (Node) iter.next();
			if (n.getNodeType() == Node.ELEMENT_NODE) walk((Element)n);
		}
	}
	
	static Document SAXRead(InputStream in) throws DocumentException {
		SAXReader reader = new SAXReader();
		return reader.read(in);
	}
}
