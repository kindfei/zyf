package test.hibernate;

import incubation.hibernate.SessionUtils;
import incubation.utils.CmdHelper;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.Transaction;



import test.hibernate.tables.mysql.Product;

public class TestL1Cache {
	public static void main(String[] args) {
		try {
			String[] opts = {
					"Load Product", 
					"Get Product", 
					"Query Product", 
					"Find Product", 
					"SQL Query Product", 
					"Update Product", 
					"Clean all cache", 
					"Quit"};
			
			a:while (true) {
			int opt = CmdHelper.options(opts);
			switch (opt) {
			case 0:
				loadProduct(CmdHelper.input("productId"));
				break;
			case 1:
				getProduct(CmdHelper.input("productId"));
				break;
			case 2:
				queryProduct(CmdHelper.input("productName"));
				break;
			case 3:
				findProduct(CmdHelper.input("productId"));
				break;
			case 4:
				sqlQueryProduct(CmdHelper.input("productName"));
				break;
			case 5:
				updateProductLeverage(CmdHelper.input("productId"), CmdHelper.input("leverage"));
				break;
			case 6:
				SessionUtils.MySQL.getSession().clear();
				break;
			default:
				break a;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				SessionUtils.MySQL.closeSession();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Product loadProduct(String id) throws Exception {
		Session session = SessionUtils.MySQL.getSession();
		Product bean = (Product) session.load(Product.class, id);
		System.out.println(bean);
		SessionUtils.MySQL.closeSession();
		return bean;
	}
	
	private static Product getProduct(String id) throws Exception {
		Session session = SessionUtils.MySQL.getSession();
		Product bean = (Product) session.get(Product.class, id);
		System.out.println(bean);
		SessionUtils.MySQL.closeSession();
		return bean;
	}
	
	private static void queryProduct(String name) throws Exception {
		Session session = SessionUtils.MySQL.getSession();
		
		Query q = session.createQuery("from Product t where t.productName = :name order by t.productId desc");
		q.setString("name", name);
		
		List list = q.list();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Product bean = (Product) iter.next();
			System.out.println(bean);
		}

		SessionUtils.MySQL.closeSession();
	}
	
	private static void findProduct(String id) throws Exception {
		Session session = SessionUtils.MySQL.getSession();
		
		List list = session.find("from Product t where t.productId = ?", id, Hibernate.STRING);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Product bean = (Product) iter.next();
			System.out.println(bean);
		}

		SessionUtils.MySQL.closeSession();
	}
	
	private static void sqlQueryProduct(String name) throws Exception {
		Session session = SessionUtils.MySQL.getSession();
		
		Query q = session.createSQLQuery("select {p.*} from PRODUCTS {p} where PRODUCT_NAME = ?", "p", Product.class);
		q.setString(0, name);
		
		List list = q.list();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Product bean = (Product) iter.next();
			System.out.println(bean);
		}

		SessionUtils.MySQL.closeSession();
	}
	
	private static void updateProductLeverage(String id, String leverage) throws Exception {
		Session session = SessionUtils.MySQL.getSession();
		Transaction tx = session.beginTransaction();
		
		Product bean = (Product) session.get(Product.class, id);
		bean.setLeverage(new Integer(leverage));
		
		tx.commit();
		SessionUtils.MySQL.closeSession();
	}
}
