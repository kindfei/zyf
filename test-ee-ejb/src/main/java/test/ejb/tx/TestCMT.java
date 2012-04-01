package test.ejb.tx;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Autowired;

import test.dao.TestDao;
import test.po.Test;
import test.utils.CustomSBAI;

@Stateless(mappedName="TestCMT")
@Interceptors(CustomSBAI.class)
public class TestCMT implements TestCMTLocal, TestCMTRemote {
	
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
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void test0() {
    	Test test = new Test();
    	entityManager.persist(test);
    	test.setContent("CMT JPA works (container)");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void test1() {
    	appManagedEntityManger.joinTransaction();
    	Test test = new Test();
    	appManagedEntityManger.persist(test);
    	test.setContent("CMT JPA works (app)");
    }

    @Autowired
	private TestDao testDao;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void test10() {
    	Test test = new Test();
    	testDao.insertOutTx1(test);
    	test.setContent("CMT JTA tx works");
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void test11() {
    	Test test = new Test();
    	testDao.insertInJtaTx1(test);
    	test.setContent("CMT JTA tx works (mixed)");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void test12() {
		
	}

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void test13() {
		
	}
}
