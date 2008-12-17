package zyf.cr;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.DOMReader;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

public class HttpHandler {
	public final static String MODE_CALL = "0";
	public final static String MODE_MESSAGE = "13";
	
	private static Log log = LogFactory.getLog(HttpHandler.class);
	private HttpClient client;
	private String phoneNumber;
	private String password;
	private String beginDate;
	private String endDate;
	
	public HttpHandler () {
		client = new HttpClient();
		client.getHostConfiguration().setHost("khfw.ln.chinamobile.com", 80, "http");
	}
	
	public HttpHandler (String num, String pwd, String bDate, String eDate) {
		this();
		phoneNumber = num;
		password = pwd;
		beginDate = bDate;
		endDate = eDate;
	}
	
	public ResultInfo process() throws HttpException, IOException {
		visit();
		login();
		
		Document docCall = search(MODE_CALL);
		List calls = parseCall(docCall);
		log.info("CallInfo count:" + calls.size());
		
		Document docMessage = search(MODE_MESSAGE);
		List messages = parseMessage(docMessage);
		log.info("MessageInfo count:" + messages.size());
		
		ResultInfo result = new ResultInfo();
		result.setCalls((CallInfo[])calls.toArray(new CallInfo[0]));
		result.setMessages((MessageInfo[])messages.toArray(new MessageInfo[0]));
		
		return result;
	}
	
	public InputStream getImage() throws HttpException, IOException {
		GetMethod get = new GetMethod("/aincs/createVerifyImageServlet");
		client.executeMethod(get);
		BufferedInputStream bis = new BufferedInputStream(get.getResponseBodyAsStream(), 1024);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buff = new byte[1024];
		for (int len = bis.read(buff); len != -1; len = bis.read(buff)) {
			out.write(buff, 0, len);
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		bis.close();
		get.releaseConnection();
		return in;
	}
	
	private void visit() throws HttpException, IOException {
		GetMethod get = new GetMethod("/aincs/webpage/index.jsp");
		client.executeMethod(get);
		get.releaseConnection();
	}
	
	private void login() throws HttpException, IOException {
		PostMethod post = new PostMethod("/aincs/webpage/selfserver/login");
		post.setParameter("method", "loginFromIndex");
		post.setParameter("loginType", "1");
		post.setParameter("phone", phoneNumber);
		post.setParameter("password", password);
		post.setParameter("operVerifyCode", getCode());
		post.setParameter("smsType", "0");
		client.executeMethod(post);
		post.releaseConnection();
	}
	
	private String getCode() throws HttpException, IOException {
		GetMethod get = new GetMethod("/aincs/createVerifyImageServlet");
		client.executeMethod(get);
		VerifyImageHandler imgHandler = new VerifyImageHandler();
		String imgStr = imgHandler.parse(get.getResponseBodyAsStream());
		get.releaseConnection();
		return imgStr;
	}
	
	private Document search(String mode) throws HttpException, IOException {
		PostMethod search = new PostMethod("/aincs/selfserv/query.detail.action.DetailQueryAction?action=showDetailInfoUI");
		search.setParameter("QueryMode", mode);
		search.setParameter("StartDate", beginDate);
		search.setParameter("EndDate", endDate);
		client.executeMethod(search);
		Document doc = readDom(search.getResponseBodyAsStream(), "GB2312");
		search.releaseConnection();
		return doc;
	}
	
	private Document readDom(InputStream in, String charset) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
		StringBuffer sb = new StringBuffer();
		for(String str = reader.readLine(); str != null; str = reader.readLine()) {
			sb.append(str).append("\n");
		}
		reader.close();
		byte[] bytes = sb.toString().getBytes("UTF-8");
		InputStream bin = new ByteArrayInputStream(bytes);
		
        Tidy tidy = new Tidy();
        tidy.setXmlOut(true);
        tidy.setCharEncoding(Configuration.UTF8);
        org.w3c.dom.Document domDoc = tidy.parseDOM(bin, null);
        DOMReader domReader = new DOMReader();
        Document doc = domReader.read(domDoc);
		return doc;
	}
	
	private List parseCall(Document doc) {
		check(doc);
		
        List list = new ArrayList();
        Node[] nodes = (Node[]) doc.selectNodes("/html/body/table/tr[2]/td/table/tr[2]/td/table/tr").toArray(new Node[0]);
        for (int i = 1; i < nodes.length - 3; i++) {
			Node node = nodes[i];
			if (node.getNodeType() != Node.ELEMENT_NODE) continue;
			Element tr = (Element) node;
			Element[] tds = (Element[]) tr.elements().toArray(new Element[0]);
			CallInfo bean = new CallInfo();
			bean.setType(tds[1].getTextTrim());
			bean.setNumber(tds[3].getTextTrim());
			bean.setTime(tds[4].getTextTrim());
			bean.setSeconds(tds[5].getTextTrim());
			bean.setLocation(tds[6].getTextTrim());
			bean.setCost(tds[9].getTextTrim());
			list.add(bean);
		}
        return list;
	}
	
	private List parseMessage(Document doc) {
		check(doc);
		
        List list = new ArrayList();
        Node[] nodes = (Node[]) doc.selectNodes("/html/body/table/tr[2]/td/table/tr[2]/td/table/tr").toArray(new Node[0]);
        for (int i = 1; i < nodes.length - 2; i++) {
			Node node = nodes[i];
			if (node.getNodeType() != Node.ELEMENT_NODE) continue;
			Element tr = (Element) node;
			Element[] tds = (Element[]) tr.elements().toArray(new Element[0]);
			MessageInfo bean = new MessageInfo();
			bean.setType(tds[1].getTextTrim());
			bean.setNumber(tds[2].getTextTrim());
			bean.setMessageType(tds[3].getTextTrim());
			bean.setBeginTime(tds[4].getTextTrim());
			bean.setEndTime(tds[5].getTextTrim());
			bean.setCast(tds[6].getTextTrim());
			list.add(bean);
		}
        return list;
	}
	
	private void check(Document doc) {
        Node titleNode = doc.selectSingleNode("/html/head/title");
        if (titleNode != null) {
        	String title = titleNode.getText().trim();
            if (title != null && title.trim().equals("ÌבÊ¾")) {
            	String msg = "error";
            	Node msgNode = doc.selectSingleNode("/html/body/table/tr[2]/td");
            	if (msgNode != null) {
            		msg = msgNode.getText().trim();
            	}
            	throw new RuntimeException(msg);
            }
        }
	}
}
