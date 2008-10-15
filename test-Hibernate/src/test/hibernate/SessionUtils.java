package test.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionUtils {
	private final static String DB2_CFG_XML = "DB2.cfg.xml";
	private final static String MYSQL_CFG_XML = "MySQL.cfg.xml";
	private final static String MYSQL_REPL_CFG_XML = "MySQL.repl.cfg.xml";
	
	private static SessionManager SMDB2;
	private static SessionManager SMMySQL;
	private static SessionManager SMMySQLRepl;
	
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

	public synchronized static Session getSessionMySQLRepl() throws HibernateException {
		if (SMMySQLRepl == null)
			SMMySQLRepl = new SessionManager(MYSQL_REPL_CFG_XML);
		return SMMySQLRepl.getSession();
	}

	public synchronized static void closeSessionDB2() throws HibernateException {
		if (SMDB2 != null)
			SMDB2.closeSession();
	}

	public synchronized static void closeSessionMySQL() throws HibernateException {
		if (SMMySQL != null)
			SMMySQL.closeSession();
	}
	
	public static void evictCacheDB2(Class<?> clz) throws HibernateException {
		SMDB2.evict(clz);
	}
	
	public static void evictCacheMySQL(Class<?> clz) throws HibernateException {
		SMMySQL.evict(clz);
	}
	
	
	private static class SessionManager {
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
		
		private Session createSession() throws HibernateException {
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
		
		private void evict(Class<?> clz) throws HibernateException {
			sf.evict(clz);
		}
		
		private Session getSession() {
			Session sess = sf.getCurrentSession();
			sess.beginTransaction();
			return sess;
		}
	}
}
