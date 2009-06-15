package test.basic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.DOMReader;

public class ParseRss {
	
	public static void main(String[] args) {
		ParseRss test = new ParseRss();
		try {
			ArrayList list = test.readDocument();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				RateBean element = (RateBean) iter.next();
				System.out.println(element.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList readDocument() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document domDocument = builder.parse("http://10.0.143.240/mfx_ratesend/RateSendAction.do?strRateId=RSS&1");
		DOMReader reader = new DOMReader();
		Document document = reader.read(domDocument);
	    
		ArrayList list = new ArrayList(18);
		Element rss = document.getRootElement();
		Element channel = (Element)rss.node(1);
		for (int i = 0, size = channel.nodeCount(); i < size; i++) {
			Node nItem = channel.node(i);
			if (nItem instanceof Element) {
				Element item = (Element)nItem;
				RateBean bean = new RateBean();
				for (int j = 0, s = item.nodeCount(); j < s; j++) {
					Node node = item.node(j);
					if (node instanceof Element) {
						setValue(bean, node.getName(), node.getText());
					}
				}
				list.add(bean);
			}
		}
		
		return list;
	}
	
	private void setValue(RateBean bean, String name, String text) throws Exception {
		if (name.equals("uniqid")) {
			bean.setUniqid(text);
			return;
		} else if (name.equals("Rate_date")) {
			bean.setRate_date(text);
			return;
		} else if (name.equals("Change")) {
			bean.setChange(new BigDecimal(text));
			return;
		} else if (name.equals("open")) {
			bean.setOpen(new BigDecimal(text));
			return;
		} else if (name.equals("high")) {
			bean.setHigh(new BigDecimal(text));
			return;
		} else if (name.equals("low")) {
			bean.setLow(new BigDecimal(text));
			return;
		} else if (name.equals("close")) {
			bean.setClose(new BigDecimal(text));
			return;
		} else if (name.equals("now")) {
			bean.setNow(new BigDecimal(text));
			return;
		} else if (name.equals("Mid")) {
			bean.setMid(new BigDecimal(text));
			return;
		} else if (name.equals("Bid")) {
			bean.setBid(new BigDecimal(text));
			return;
		} else if (name.equals("Ask")) {
			bean.setAsk(new BigDecimal(text));
			return;
		} else if (name.equals("Quote_time")) {
			Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(text);
			bean.setQuote_time(date);
			return;
		}
	}
}

class RateBean {
	private String uniqid;
	private String Rate_date;
	private BigDecimal Change;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private BigDecimal now;
	private BigDecimal Mid;
	private BigDecimal Bid;
	private BigDecimal Ask;
	private Date Quote_time;
	public BigDecimal getAsk() {
		return Ask;
	}
	public void setAsk(BigDecimal ask) {
		Ask = ask;
	}
	public BigDecimal getBid() {
		return Bid;
	}
	public void setBid(BigDecimal bid) {
		Bid = bid;
	}
	public BigDecimal getChange() {
		return Change;
	}
	public void setChange(BigDecimal change) {
		Change = change;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public BigDecimal getMid() {
		return Mid;
	}
	public void setMid(BigDecimal mid) {
		Mid = mid;
	}
	public BigDecimal getNow() {
		return now;
	}
	public void setNow(BigDecimal now) {
		this.now = now;
	}
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public Date getQuote_time() {
		return Quote_time;
	}
	public void setQuote_time(Date quote_time) {
		Quote_time = quote_time;
	}
	public String getRate_date() {
		return Rate_date;
	}
	public void setRate_date(String rate_date) {
		Rate_date = rate_date;
	}
	public String getUniqid() {
		return uniqid;
	}
	public void setUniqid(String uniqid) {
		this.uniqid = uniqid;
	}
	
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("uniqid=").append(uniqid).append("\n");
		buff.append("Rate_date=").append(Rate_date).append("\n");
		buff.append("Change=").append(Change).append("\n");
		buff.append("open=").append(open).append("\n");
		buff.append("high=").append(high).append("\n");
		buff.append("low=").append(low).append("\n");
		buff.append("close=").append(close).append("\n");
		buff.append("now=").append(now).append("\n");
		buff.append("Mid=").append(Mid).append("\n");
		buff.append("Bid=").append(Bid).append("\n");
		buff.append("Ask=").append(Ask).append("\n");
		buff.append("Quote_time=").append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(Quote_time)).append("\n");
		
		return buff.toString();
	}
}
