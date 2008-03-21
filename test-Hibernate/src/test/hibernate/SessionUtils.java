package test.hibernate;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

public class SessionUtils {
	private final static String DB2_CFG_XML = "DB2.cfg.xml";
	private final static String MYSQL_CFG_XML = "MySQL.cfg.xml";
	
	private static SessionManager SMDB2;
	private static SessionManager SMMySQL;
	
	public static void init() throws HibernateException {
		SMDB2 = new SessionManager(DB2_CFG_XML);
		SMMySQL = new SessionManager(MYSQL_CFG_XML);
	}

	public synchronized static Session createSessionDB2() throws HibernateException {
		if (SMDB2 == null)
			SMDB2 = new SessionManager(DB2_CFG_XML);
		return SMDB2.createSession();
	}

	public synchronized static Session createSessionMySQL() throws HibernateException {
		if (SMMySQL == null)
			SMMySQL = new SessionManager(MYSQL_CFG_XML);
		return SMMySQL.createSession();
	}

	public synchronized static void closeSessionDB2() throws HibernateException {
		if (SMDB2 != null)
			SMDB2.closeSession();
	}

	public synchronized static void closeSessionMySQL() throws HibernateException {
		if (SMMySQL != null)
			SMMySQL.closeSession();
	}
	
	public static void evictCacheDB2(Class clz) throws HibernateException {
		SMDB2.evict(clz);
	}
	
	public static void evictCacheMySQL(Class clz) throws HibernateException {
		SMMySQL.evict(clz);
	}
	
	
	private static class SessionManager {
		private SessionFactory sf;
		private ThreadLocal tlSession = new ThreadLocal();
		private ThreadLocal tlCounter = new ThreadLocal() {
			protected synchronized Object initialValue() {
				return new Integer(0);
			}
		};
		
		private SessionManager(String cfgFile) throws HibernateException {
			Configuration cfg = new Configuration();
			cfg.configure(ClassLoader.getSystemResource(cfgFile));
			sf = cfg.buildSessionFactory();
		}
		
		private Session createSession() throws HibernateException {
			Session sess = (Session) tlSession.get();
			int count = ((Integer) tlCounter.get()).intValue();
			
			if (sess == null || !sess.isOpen()) {
				sess = sf.openSession();
				tlSession.set(sess);
				count = 0;
				System.out.println("Session is created.");
			}
			
			tlCounter.set(new Integer(++count));
			
			return sess;
		}
		
		private void closeSession() {
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
		
		private void evict(Class clz) throws HibernateException {
			sf.evict(clz);
		}
	}
}
