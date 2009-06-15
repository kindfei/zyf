package test.basic.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TestXML {
	public static void main(String[] args) {
		try {
			SAXRead("test.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void DOMRead(String str) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(ClassLoader.getSystemResourceAsStream(str));
		
		Element root = doc.getDocumentElement();
		walk(root.getChildNodes());
	}
	
	private static void walk(NodeList children) {
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element childElement = (Element) child;
				String tagName = childElement.getTagName();
				System.out.println("Element: " + tagName);
				NamedNodeMap attributes = childElement.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					Attr attribute = (Attr) attributes.item(j);
					String name = attribute.getNodeName();
					String value = attribute.getNodeValue();
					System.out.println("Attribute: " + name + " - " + value);
				}
				walk(childElement.getChildNodes());
			} else if (child instanceof CharacterData) {
				if (child instanceof Text) {
					if (child instanceof CDATASection) {
						System.out.println("CDATASection: " + ((CDATASection) child).getData());
					} else {
						Text childText = (Text) child;
						String data = childText.getData().trim();
						if (!data.equals(""))
							System.out.println("Text: [" + data + "]");
					}
				} else if (child instanceof Comment) {
					System.out.println("Comment: " + ((Comment) child).getData());
				} else {
					System.out.println("Unknown type of CharacterData. Type:" + child.getNodeType());
				}
			} else {
				System.out.println("Unknown type of Node. Type:" + child.getNodeType());
			}
		}
	}
	
	public static void XPathRead(String str) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(ClassLoader.getSystemResourceAsStream(str));
		
		XPathFactory pathFactory = XPathFactory.newInstance();
		XPath path = pathFactory.newXPath();
		
		String sex = path.evaluate("/customer/sex", doc).trim();
		System.out.println("sex: [" + sex + "]");
		
		NodeList children = (NodeList) path.evaluate("/customer/address", doc, XPathConstants.NODESET);
		walk(children);
		
		int count = ((Number) path.evaluate("count(/customer/personal/email)", doc, XPathConstants.NUMBER)).intValue();
		System.out.println("count: " + count);
		
		Node email = (Node) path.evaluate("/customer/personal/email[1]", doc, XPathConstants.NODE);
		walk(email.getChildNodes());
	}
	
	public static void SAXRead(String str) throws ParserConfigurationException, SAXException, IOException {
		DefaultHandler handler = new DefaultHandler() {
			public void startElement(String uri, String localName
					, String qName, Attributes attributes) throws SAXException {
				System.out.println("Element: " + qName);
				
				for (int i = 0; i < attributes.getLength(); i++) {
					String name = attributes.getQName(i);
					String value = attributes.getValue(i);
					System.out.println("Attribute: " + name + " - " + value);
				}
			}
			
			public void characters(char[] ch, int start, int length) throws SAXException {
				String text = new String(ch, start, length).trim();
				if (!text.equals("")) System.out.println("Text: [" + text + "]");
			}
		};
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		System.out.println("SAXParserFactory: " + factory.getClass().getName());
		SAXParser parser = factory.newSAXParser();
		
		parser.parse(ClassLoader.getSystemResourceAsStream(str), handler);
	}
}
