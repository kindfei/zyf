package test.hibernate;

import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import test.helper.CmdHelper;
import test.hibernate.tables.mysql.Product;

public class TestL2Cache {
	public static void main(String[] args) {
		try {
			String[] opts = {
					"Load Product", 
					"Get Product", 
					"Find Product", 
					"Query Product", 
					"Update Product", 
					"Clean cache of Product", 
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
					findProduct(CmdHelper.input("productId"));
					break;
				case 3:
					queryProduct(CmdHelper.input("productName"));
					break;
				case 4:
					updateProductLeverage(CmdHelper.input("productId"), CmdHelper.input("leverage"));
					break;
				case 5:
					SessionUtils.evictCacheMySQL(Product.class);
					break;
				default:
					break a;
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	
	private static Product loadProduct(String id) throws HibernateException {
		Session session = SessionUtils.createSessionMySQL();
		Product bean = (Product) session.load(Product.class, id);
		System.out.println(bean);
		session.close();
		return bean;
	}
	
	private static Product getProduct(String id) throws HibernateException {
		Session session = SessionUtils.createSessionMySQL();
		Product bean = (Product) session.get(Product.class, id);
		System.out.println(bean);
		session.close();
		return bean;
	}
	
	private static Product[] findProduct(String id) throws HibernateException {
		Session session = SessionUtils.createSessionMySQL();
		List list = session.find("from Product t where t.productId = ?", id, Hibernate.STRING);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Product bean = (Product) iter.next();
			System.out.println(bean);
		}
		session.close();
		Product[] products = new Product[list.size()];
		list.toArray(products);
		return products;
	}
	
	private static Product[] queryProduct(String name) throws HibernateException {
		Session session = SessionUtils.createSessionMySQL();
		
		Query q = session.createQuery("from Product t where t.productName = :name");
		q.setString("name", name);
		q.setCacheable(true);
		q.setCacheRegion("query.Products");
		List list = q.list();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Product bean = (Product) iter.next();
			System.out.println(bean);
		}
		session.close();
		Product[] products = new Product[list.size()];
		list.toArray(products);
		return products;
	}
	
	private static void updateProductLeverage(String id, String leverage) throws HibernateException {
		Session session = SessionUtils.createSessionMySQL();
		Product bean = (Product) session.get(Product.class, id);
		bean.setLeverage(new Integer(leverage));
		session.beginTransaction().commit();
		session.close();
	}
}