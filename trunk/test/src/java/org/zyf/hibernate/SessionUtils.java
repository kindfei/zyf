package org.zyf.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionUtils {
	private final static String MYSQL_CFG_XML = "MySQL.cfg.xml";
	
	public static SessionManager MySQL;
	
	static {
		MySQL = new SessionManager(MYSQL_CFG_XML);
	}
	
	public static class SessionManager {
		private SessionFactory sf;
		private ThreadLocal<Session> tlSession = new ThreadLocal<Session>();
		private ThreadLocal<Integer> tlCounter = new ThreadLocal<Integer>() {
			protected synchronized Integer initialValue() {
				return new Integer(0);
			}
		};
		
		private SessionManager(String cfgFile) throws HibernateException {
			Configuration cfg = new Configuration();
			cfg.configure(ClassLoader.getSystemResource(cfgFile));
			sf = cfg.buildSessionFactory();
		}
		
		public Session getSession() throws HibernateException {
			Session sess = tlSession.get();
			int count = tlCounter.get().intValue();
			
			if (sess == null || !sess.isOpen()) {
				sess = sf.openSession();
				tlSession.set(sess);
				count = 0;
				System.out.println("Session is created.");
			}
			
			tlCounter.set(new Integer(++count));
			
			return sess;
		}
		
		public void closeSession() {
			int count = ((Integer) tlCounter.get()).intValue();
			count -= 1;
			
			if (count <= 0) {
				Session sess = (Session) tlSession.get();
				if (sess != null) {
					try {
						sess.close();
					} catch (HibernateException e) {
					}
					tlSession.set(null);
				}
				count = 0;
				System.out.println("Session is closed.");
			}
			
			tlCounter.set(new Integer(count));
		}
		
		public void evict(Class<?> clz) throws HibernateException {
			sf.evict(clz);
		}
		
		public Session getCurrentSession() {
			return sf.getCurrentSession();
		}
	}
}
