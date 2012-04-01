package test.ejb.tx;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

import org.springframework.beans.factory.annotation.Autowired;

import test.dao.TestDao;
import test.po.Test;
import test.service.TestService;
import test.utils.CustomSBAI;


/**
 * Session Bean implementation class TestTransaction
 */
@Stateless(mappedName="TestBMT")
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors(CustomSBAI.class)
public class TestBMT implements TestBMTLocal, TestBMTRemote {
    
    @Resource
    private UserTransaction userTransaction;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    private EntityManager appManagedEntityManger;
    
    @PostConstruct
	public void initialize() {
    	appManagedEntityManger = entityManagerFactory.createEntityManager();
    }

	@PreDestroy
	public void cleanup() {
		if (appManagedEntityManger.isOpen()) {
			appManagedEntityManger.close();
		}
	}
    
    public void test0() {
    	try {
			userTransaction.begin();
			
			Test test = new Test();
			entityManager.persist(test);
			test.setContent("BMT JPA works (container)");
			
			userTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void test1() {
    	try {
			userTransaction.begin();
			
	    	appManagedEntityManger.joinTransaction();
	    	Test test = new Test();
	    	appManagedEntityManger.persist(test);
	    	test.setContent("BMT JPA works (app)");
	    	
			userTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    @Autowired
	private TestService testService;
	
    @Autowired
	private TestDao testDao;
    
    public void test2() {
    	try {
			userTransaction.begin();
			
	    	Test test = new Test();
	    	testDao.insertOutTx1(test);
	    	test.setContent("BMT JTA tx works");
	    	
			userTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void test10() {
    	testService.testDeclarativeTx();
    }
    
    public void test11() {
    	testService.testDaoTx();
    }
    
    public void test12() {
    	testService.testMixedTx();
    }
    
    public void test13() {
    	testService.testInJtaTx();
    }
    
    public void test14() {
    	testService.testJmsInJtaTx(false, false);
    }
    
    public void test15() {
    	testService.testJmsInJtaTx(true, false);
    }
    
    public void test16() {
    	testService.testJmsInJtaTx(false, true);
    }
    
    public void test17() {
    }
    
    public void test100() {
    	testService.testNewSession();
    }
    
    public void test101() {
    	testService.testNotClosedSession();
    }
}
