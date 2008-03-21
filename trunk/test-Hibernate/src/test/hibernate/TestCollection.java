package test.hibernate;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import test.hibernate.tables.mysql.Customer;
import test.hibernate.tables.mysql.Email;
import test.hibernate.tables.mysql.Product;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

public class TestCollection {
	public static void main(String[] args) {
		try {
			Session session = SessionUtils.createSessionMySQL();
			Customer bean = readCustomer(session);
			update(session, bean);
			session.beginTransaction().commit();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	
	static Customer readCustomer(Session session) throws HibernateException {
		Customer bean = (Customer)session.get(Customer.class, "C01");
		
		Set emailSet = bean.getEmailSet();
		for (Iterator iter = emailSet.iterator(); iter.hasNext();) {
			String element = (String)iter.next();
			System.out.println(element);
		}
		
		Set emailBeanSet = bean.getEmailBeanSet();
		for (Iterator iter = emailBeanSet.iterator(); iter.hasNext();) {
			Email element = (Email)iter.next();
			System.out.println(element);
		}
		
		Map emailMap = bean.getEmailMap();
		for (Iterator iter = emailMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			System.out.println(entry.getKey() + " - " + entry.getValue());
		}
		
		Product[] productBeans = bean.getProductBeans();
		for (int i = 0; i < productBeans.length; i++) {
			System.out.println(i + " - " + productBeans[i]);
		}
		
		Map productBeanMap = bean.getProductBeanMap();
		for (Iterator iter = productBeanMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			System.out.println(entry.getKey() + " - " + entry.getValue());
		}
		
		System.out.println(bean.getGroupBean());
		
		return bean;
	}
	
	static void update(Session session, Customer bean) {
		
//		Set emailBeanSet = bean.getEmailBeanSet();
//		for (Iterator iter = emailBeanSet.iterator(); iter.hasNext();) {
//			Email element = (Email)iter.next();
//			if (element.getEmailId().equals("E01")) 
//				element.setEmail("zhangyf@edgesoft.cn");
//		}
		
//		Map emailMap = bean.getEmailMap();
//		emailMap.put("E01", "iameinstein@tom.com");
//		emailMap.put("E10", "zhangyf@edgesoft.cn");
		
//		Product[] productBeans = bean.getProductBeans();
//		productBeans[1].setLeverage(new Integer(500));
		
		Map productBeanMap = bean.getProductBeanMap();
		((Product)productBeanMap.get("2")).setLeverage(new Integer(600));
		productBeanMap.remove("3");
		productBeanMap.put(null, new Product("P06"));
	}
	
}
