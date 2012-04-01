package test.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import test.po.Test;

public class TestDaoImpl extends HibernateDaoSupport implements TestDao {

	/*
	 * 
	 */
	protected Session session() {
		return getSessionFactory().getCurrentSession();
	}
	
	public void insertInTx(Test test) {
		session().save(test);
	}
	
	public void insertOutTx(Test test) {
		session().save(test);
	}
	
	public void insertInJtaTx(Test test) {
		session().save(test);
	}
	
	/*
	 * 
	 */
	protected Session openSession() {
		return getSessionFactory().openSession();
	}
	
	public void insertWithNewSessionInTx(final Test test) {
		getHibernateTemplate().executeWithNewSession(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.save(test);
			}
		});
	}
	
	public void insertWithoutSessionClose(Test test) {
		openSession().save(test);
	}
	
	/*
	 * 
	 */
	private HibernateTemplate hibernateTemplate1;

	public void setHibernateTemplate1(HibernateTemplate hibernateTemplate1) {
		this.hibernateTemplate1 = hibernateTemplate1;
	}
	
	public void insertInTx1(Test test) {
		session().save(test);
	}
	
	public void insertOutTx1(Test test) {
		hibernateTemplate1.save(test);
	}
	
	public void insertInJtaTx1(Test test) {
		session().save(test);
	}

	/*
	 * 
	 */
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void insertWithJdbc(Test test) {
		jdbcTemplate.update("insert into test values (" + test.getId() + "," + test.getVersion() + 1 + "," + test.getContent() + ")");
	}
}
