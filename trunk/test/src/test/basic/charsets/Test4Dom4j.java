package test.basic.charsets;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class Test4Dom4j {

	public static void main(String[] args) throws Exception {
		//new Test4Dom4j().testMain();
//		new Test4Dom4j().getWrongCode("8859_1");
		for(double i=709; i<1000; i=i + 0.000001) {
			String str = Math.exp(i) + "";
			System.out.println(str);
			if(str.startsWith("I")){
				System.out.println("###" + i + "###");
				break;
			}
		}
	}
	
	public void testMain() throws Exception {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(getStream("D:/output.xml"));
		
		treeWalk(doc.getRootElement());
	}
	
    public void treeWalk(Element element) {
        for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
            Node node = element.node(i);
            if ( node instanceof Element ) {
                treeWalk( (Element) node );
            } else {
            	System.out.println(node.getText());
            }
        }
    }
    
    private InputSource getSource (String path, String charsetName) throws Exception {
    	InputSource is = new InputSource(getStream(path));
    	is.setEncoding(charsetName);
    	return is;
    }
    
    private Reader getReader(String path, String charsetName) throws Exception {
    	return new InputStreamReader(getStream(path), charsetName);
    }
    
    private InputStream getStream(String path) throws Exception {
    	return new FileInputStream(path);
    }
    
    public void getWrongCode(String charsetName) throws Exception {
    	for(int i=0xbf; i <= 0xff; i++) {
    		byte b[] = {(byte)i};
    		String str = new String(b, charsetName);
    		System.out.print(str);
    		System.out.println("\t" + Integer.toHexString((char)str.getBytes(charsetName)[0]));
    		if(str.equals("?")) System.out.println("\t" + Integer.toHexString(str.getBytes(charsetName)[0]));
    	}
    }
}
