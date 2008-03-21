package test.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import test.helper.CmdHelper;
import test.hibernate.tables.db2.Product;

public class TestLock {
	public static void main(String[] args) {
		try {
			SessionUtils.createSessionDB2();
			
			String[] opts = {
					"Load Product RS", 
					"Get Product RS", 
					"Query Product RR", 
					"Query Product RS", 
					"Query Product CS", 
					"Query Product UR", 
					"Find Product", 
					"SQL Query Product RR", 
					"SQL Query Product RS", 
					"SQL Query Product CS", 
					"SQL Query Product US", 
					"Update Product", 
					"select Product RR", 
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
				queryProductRR(CmdHelper.input("productName"));
				break;
			case 3:
				queryProductRS(CmdHelper.input("productName"));
				break;
			case 4:
				queryProductCS(CmdHelper.input("productName"));
				break;
			case 5:
				queryProductUR(CmdHelper.input("productName"));
				break;
			case 6:
				findProduct(CmdHelper.input("productId"));
				break;
			case 7:
				sqlQueryProductRR(CmdHelper.input("productName"));
				break;
			case 8:
				sqlQueryProductRS(CmdHelper.input("productName"));
				break;
			case 9:
				sqlQueryProductCS(CmdHelper.input("productName"));
				break;
			case 10:
				sqlQueryProductUR(CmdHelper.input("productName"));
				break;
			case 11:
				updateProductLeverage(CmdHelper.input("productId"), CmdHelper.input("leverage"));
				break;
			case 12:
				selectProduct(CmdHelper.input("productName"));
				break;
			case 13:
				SessionUtils.createSessionDB2().clear();
				break;
			default:
				break a;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				SessionUtils.closeSessionDB2();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Product loadProduct(String id) throws Exception {
		Session session = SessionUtils.createSessionDB2();
		Transaction tx = session.beginTransaction();
		session.connection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		Product bean = (Product) session.load(Product.class, id, LockMode.UPGRADE);
		System.out.println(bean);
		CmdHelper.pause("commit");
		tx.commit();
		SessionUtils.closeSessionDB2();
		return bean;
	}
	
	private static Product getProduct(String id) throws Exception {
		Session session = SessionUtils.createSessionDB2();
		Transaction tx = session.beginTransaction();
		session.connection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		Product bean = (Product) session.get(Product.class, id, LockMode.UPGRADE);
		System.out.println(bean);
		CmdHelper.pause("commit");
		tx.commit();
		SessionUtils.closeSessionDB2();
		return bean;
	}
	
	private static void queryProductRR(String name) throws Exception {
		queryProduct(name, Connection.TRANSACTION_SERIALIZABLE);
	}
	
	private static void queryProductRS(String name) throws Exception {
		queryProduct(name, Connection.TRANSACTION_REPEATABLE_READ);
	}
	
	private static void queryProductCS(String name) throws Exception {
		queryProduct(name, Connection.TRANSACTION_READ_COMMITTED);
	}
	
	private static void queryProductUR(String name) throws Exception {
		queryProduct(name, Connection.TRANSACTION_READ_UNCOMMITTED);
	}
	
	private static void queryProduct(String name, int il) throws Exception {
		Session session = SessionUtils.createSessionDB2();
		session.connection().setTransactionIsolation(il);
		Transaction tx = session.beginTransaction();
		
		Query q = session.createQuery("from Product t where t.productName = :name order by t.productId desc");
		q.setLockMode("t", LockMode.UPGRADE);
		q.setString("name", name);
		
		List list = q.list();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Product bean = (Product) iter.next();
			System.out.println(bean);
		}
		
		CmdHelper.pause("commit");
		tx.commit();
		SessionUtils.closeSessionDB2();
	}
	
	private static void findProduct(String id) throws Exception {
		Session session = SessionUtils.createSessionDB2();
		Transaction tx = session.beginTransaction();
		
		List list = session.find("from Product t where t.productId = ?", id, Hibernate.STRING);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Product bean = (Product) iter.next();
			System.out.println(bean);
		}
		
		CmdHelper.pause("commit");
		tx.commit();
		SessionUtils.closeSessionDB2();
	}
	
	private static void sqlQueryProductRR(String name) throws Exception {
		sqlQueryProduct(name, "rr");
	}
	
	private static void sqlQueryProductRS(String name) throws Exception {
		sqlQueryProduct(name, "rs");
	}
	
	private static void sqlQueryProductCS(String name) throws Exception {
		sqlQueryProduct(name, "cs");
	}
	
	private static void sqlQueryProductUR(String name) throws Exception {
		sqlQueryProduct(name, "ur");
	}
	
	private static void sqlQueryProduct(String name, String il) throws Exception {
		Session session = SessionUtils.createSessionDB2();
		Transaction tx = session.beginTransaction();
		Query q = session.createSQLQuery("select {p.*} from PRODUCTS {p} where PRODUCT_NAME = ? for update with " + il, "p", Product.class);
		q.setString(0, name);
		
		List list = q.list();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Product bean = (Product) iter.next();
			System.out.println(bean);
		}
		
		CmdHelper.pause("commit");
		tx.commit();
		SessionUtils.closeSessionDB2();
	}
	
	private static void updateProductLeverage(String id, String leverage) throws Exception {
		Session session = SessionUtils.createSessionDB2();
		Transaction tx = session.beginTransaction();
		
		Product bean = (Product) session.get(Product.class, id);
		bean.setLeverage(new Integer(leverage));
		
		CmdHelper.pause("commit");
		tx.commit();
		SessionUtils.closeSessionDB2();
	}
	
	private static void selectProduct(String name) throws Exception {
		Session session = SessionUtils.createSessionDB2();
		Connection conn = session.connection();
		conn.setAutoCommit(false);
		
		PreparedStatement pstmt = conn.prepareStatement("select * from PRODUCTS where PRODUCT_NAME=? for update with rr");
		pstmt.setString(1, name);
		
		ResultSet rs = pstmt.executeQuery();
		
		// You have to move the cursor here for that effect the "with rr" statement.
		while (rs.next()) {
			System.out.println(rs.getString(1));
		}

		CmdHelper.pause("commit");
		conn.commit();
		SessionUtils.closeSessionDB2();
	}
}
