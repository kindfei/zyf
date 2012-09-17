package test.db.tx;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

/**
 * 
 * @author yz69579
 *
 */
@Aspect
public final class TransactionAspect {
	private static final Log log = LogFactory.getLog(TransactionAspect.class);
	
	private PlatformTransactionManager transactionManager;
	
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 
	 */
	public TransactionAspect() {
	}
	
	/**
	 * Transaction
	 */
	@Around("@annotation(tx)")
	public Object tx(final ProceedingJoinPoint pjp, final Tx tx) throws Throwable {
		//
		PlatformTransactionManager ptm = this.transactionManager;
		if(ptm == null) {
			throw new IllegalArgumentException("Missing required transactionManager.");
		}
		
		//
		DefaultTransactionAttribute dtd = new DefaultTransactionAttribute();
		dtd.setReadOnly(tx.readonly());
		dtd.setTimeout(tx.timeout());
		dtd.setIsolationLevel(tx.isolationLevel());
		dtd.setPropagationBehavior(tx.propagation().value());
		
		// 
		TransactionStatus ts = ptm.getTransaction(dtd);
		if(log.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder("Begin transaction. ");
			sb.append("method: ").append(descMethodSignature(pjp));
			sb.append(", tx: ").append(descTxDefinition(tx));
			sb.append(", status: ").append(descTransactionStatus(ts));
			log.info(sb.toString());
		}
		
		// Just for Sybase
		if (tx.unchained()) {
			if (!ts.isNewTransaction()) {
				throw new IllegalStateException("Sybase transaction mode cannot be changed in an existing " +
						"transaction, or potentially not running in an actual transaction. " +
						"method: " + descMethodSignature(pjp) + ", status: " + descTransactionStatus(ts));
			}
			
			this.executeSql("set chained off");
			this.executeSql("begin tran");
		}
		
		//
		try {
			
			Object r = pjp.proceed();
			
			ptm.commit(ts);
			
			return r;
			
		} catch(Throwable t) {
			try {
				ptm.rollback(ts); // Rollback anyway without checking dtd.rollbackOn(t)
			} catch(Exception e) {
				log.error("Failed to rollback for method: " + descMethodSignature(pjp) + ", transaction: " + descTransactionStatus(ts), e);
			}
			throw t;
		}
	}
	
	private Boolean executeSql(final String sql) {
		return (Boolean) hibernateTemplate.execute(new HibernateCallback() {
			public Boolean doInHibernate(Session session) throws HibernateException, SQLException {
				return session.connection().createStatement().execute(sql);
			}
		});
	}
	
	/**
	 * 
	 */
	private String descMethodSignature(ProceedingJoinPoint pjp) {
		StringBuilder sb = new StringBuilder();
		sb.append(pjp.getSignature().getDeclaringTypeName());
		sb.append(".");
		sb.append(pjp.getSignature().getName());
		return sb.toString();
	}
	
	private String descTxDefinition(Tx tx) {
		StringBuilder sb = new StringBuilder();
		sb.append(tx.propagation());
		sb.append(", ");
		sb.append(tx.readonly());
		sb.append(", ");
		sb.append(tx.timeout());
		return sb.toString();
	}
	
	private Object descTransactionStatus(TransactionStatus ts) {
		StringBuilder sb = new StringBuilder();
		sb.append(ts.isNewTransaction());
		sb.append(", ");
		sb.append(ts.isRollbackOnly());
		return sb.toString();
	}

	/**
	 * 
	 */
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager manager) {
		this.transactionManager = manager;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
