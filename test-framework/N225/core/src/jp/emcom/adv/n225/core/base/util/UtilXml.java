package jp.emcom.adv.n225.core.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 
 * @author zhangyf
 * 
 */
public class UtilXml {

	public static Document readXmlDocument(URL url) throws SAXException, ParserConfigurationException, IOException {
		if (url == null) {
			return null;
		}
		InputStream is = url.openStream();
		Document document = readXmlDocument(is);
		is.close();
		return document;
	}

	public static Document readXmlDocument(InputStream is) throws SAXException, ParserConfigurationException,
			IOException {
		if (is == null) {
			return null;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(is);

		return document;
	}

	/**
	 * Return a List of Element objects that are children of the given element
	 * 
	 * @param element
	 * @return
	 */
	public static List<? extends Element> childElementList(Element element) {
		if (element == null)
			return null;

		List<Element> elements = new ArrayList<Element>();
		Node node = element.getFirstChild();

		if (node != null) {
			do {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element childElement = (Element) node;
					elements.add(childElement);
				}
			} while ((node = node.getNextSibling()) != null);
		}
		return elements;
	}

	/**
	 * Return a List of Element objects that have the given name and are
	 * immediate children of the given element; if name is null, all child
	 * elements will be included.
	 * 
	 * @param element
	 * @param childElementName
	 * @return
	 */
	public static List<? extends Element> childElementList(Element element, String childElementName) {
		if (element == null)
			return null;

		List<Element> elements = new ArrayList<Element>();
		Node node = element.getFirstChild();

		if (node != null) {
			do {
				if (node.getNodeType() == Node.ELEMENT_NODE
						&& (childElementName == null || childElementName.equals(node.getNodeName()))) {
					Element childElement = (Element) node;

					elements.add(childElement);
				}
			} while ((node = node.getNextSibling()) != null);
		}
		return elements;
	}

	/**
	 * Return the text (node value) contained by the named child node.
	 * 
	 * @param element
	 * @param childElementName
	 * @return
	 */
	public static String childElementValue(Element element, String childElementName) {
		if (element == null)
			return null;
		// get the value of the first element with the given name
		Element childElement = firstChildElement(element, childElementName);

		return elementValue(childElement);
	}

	/**
	 * Return the text (node value) contained by the named child node or a
	 * default value if null.
	 * 
	 * @param element
	 * @param childElementName
	 * @param defaultValue
	 * @return
	 */
	public static String childElementValue(Element element, String childElementName, String defaultValue) {
		if (element == null)
			return defaultValue;
		// get the value of the first element with the given name
		Element childElement = firstChildElement(element, childElementName);
		String elementValue = elementValue(childElement);

		if (elementValue == null || elementValue.length() == 0)
			return defaultValue;
		else
			return elementValue;
	}

	/**
	 * Return the text (node value) of the first node under this, works best if
	 * normalized.
	 * 
	 * @param element
	 * @return
	 */
	public static String elementValue(Element element) {
		if (element == null)
			return null;
		// make sure we get all the text there...
		element.normalize();
		Node textNode = element.getFirstChild();

		if (textNode == null)
			return null;

		StringBuilder valueBuffer = new StringBuilder();
		do {
			if (textNode.getNodeType() == Node.CDATA_SECTION_NODE || textNode.getNodeType() == Node.TEXT_NODE) {
				valueBuffer.append(textNode.getNodeValue());
			}
		} while ((textNode = textNode.getNextSibling()) != null);
		return valueBuffer.toString();
	}

	/**
	 * Return the first child Element with the given name; if name is null
	 * returns the first element.
	 * 
	 * @param element
	 * @param childElementName
	 * @return
	 */
	public static Element firstChildElement(Element element, String childElementName) {
		if (element == null)
			return null;
		if (childElementName == null || childElementName.length() == 0)
			return null;
		// get the first element with the given name
		Node node = element.getFirstChild();

		if (node != null) {
			do {
				if (node.getNodeType() == Node.ELEMENT_NODE
						&& (childElementName == null || childElementName.equals(node.getNodeName()))) {
					Element childElement = (Element) node;
					return childElement;
				}
			} while ((node = node.getNextSibling()) != null);
		}
		return null;
	}
}
