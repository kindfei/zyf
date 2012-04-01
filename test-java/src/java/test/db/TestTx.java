package test.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import test.db.tx.Tx;
import test.util.Utils;


public class TestTx {
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	private void execute(Session session, Integer transactional, Integer spRollback) {
		SQLQuery query = session.createSQLQuery("{? = call testInsTest(?, ?, ?, ?, ?, ?)}");

		query.setParameter(0, null);
		query.setParameter(1, null);
		query.setParameter(2, "TestTx");
		query.setParameter(3, null);
		query.setParameter(4, transactional);
		query.setParameter(5, spRollback);
		
		List<?> r = query.list();
		for (Object o : r) {
			if (o.getClass().isArray()) {
				System.out.println(Arrays.toString((Object[]) o));
			} else {
				System.out.println(o.toString());
			}
		}
	}
	
	private void printTransactionMode(Session session) throws HibernateException, SQLException {
		ResultSet rs = session.connection().createStatement().executeQuery("select @@tranchained");
		if (rs.next()) {
			System.out.println("@@tranchained = " + rs.getObject(1));
		}
	}
	
	/**
	 * 
	 * @param rollback
	 * @param spTx
	 * @param spRollback
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void testDeclarativeTx(final boolean rollback, final boolean spTx, final boolean spRollback) {
		hibernateTemplate.execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				execute(session, spTx ? 1 : 0, spRollback ? 1 : 0);
				
				if (rollback) throw new SQLException();
				
				return null;
			}
		});
	}
	
	/**
	 * 
	 * @param rollback
	 * @param spTx
	 * @param spRollback
	 */
	public void testHibernateTx(final boolean rollback, final boolean spTx, final boolean spRollback) {
		hibernateTemplate.execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Transaction tx = session.beginTransaction();
				
				execute(session, spTx ? 1 : 0, spRollback ? 1 : 0);
				
				if (rollback) tx.rollback();
				else tx.commit();
				
				return null;
			}
		});
	}
	
	/**
	 * 
	 * @param rollback
	 * @param spTx
	 * @param spRollback
	 */
	@Tx(propagation = Propagation.REQUIRED, unchained = true)
	public void testCustomTx(final boolean rollback, final boolean spTx, final boolean spRollback) {
		hibernateTemplate.execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				printTransactionMode(session);
				execute(session, spTx ? 1 : 0, spRollback ? 1 : 0);
				printTransactionMode(session);
				
				if (rollback) {
					throw new RuntimeException("Just for rollback");
				}
				
				return null;
			}
		});
	}
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext c = Utils.loadContext(TestProcedure.class, "context.xml");
		TestTx inst = (TestTx) c.getBean("testTx");
		
		inst.testHibernateTx(false, false, false);
	}
}
