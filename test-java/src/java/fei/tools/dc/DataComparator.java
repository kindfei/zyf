package fei.tools.dc;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fei.tools.dc.cc.CompareCase;
import fei.tools.dc.cc.FetchResult;
import fei.tools.dc.da.DataAccessor;




public class DataComparator {
	private static final Log log = LogFactoryImpl.getLog(DataComparator.class);
	
	private ApplicationContext context1;
	private ApplicationContext context2;
	
	private List<CompareCase> compareCases;
	
	public void process() {
		log.info("Begin process. compareCases.size=" + compareCases.size());
		
		for (CompareCase compareCase : compareCases) {
			try {
				DataAccessor da1 = getDataAccessor(context1, compareCase);
				DataAccessor da2 = getDataAccessor(context2, compareCase);
				
				List<Map<String, Object>> r1 = da1.fetch(compareCase.getQueryInfo1());
				List<Map<String, Object>> r2 = da2.fetch(compareCase.getQueryInfo2());
				
				compareCase.compare(new FetchResult(da1.getEnvName(), r1), new FetchResult(da2.getEnvName(), r2));
			} catch (Exception e) {
				log.error("Process error. compareCase=" + compareCase, e);
			}
		}
	}
	
	private DataAccessor getDataAccessor(ApplicationContext context, CompareCase compareCase) {
		return (DataAccessor) context.getBean(compareCase.getDataAccessorName());
	}

	public ApplicationContext getContext1() {
		return context1;
	}

	public void setContext1(ApplicationContext context1) {
		this.context1 = context1;
	}

	public ApplicationContext getContext2() {
		return context2;
	}

	public void setContext2(ApplicationContext context2) {
		this.context2 = context2;
	}

	public List<CompareCase> getCompareCases() {
		return compareCases;
	}

	public void setCompareCases(List<CompareCase> compareCases) {
		this.compareCases = compareCases;
	}
	
	public static void main(String[] args) {
		
		// Create context
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dcMainContext.xml");
		
		// Get dataComparator
		DataComparator dataComparator = (DataComparator) context.getBean("dataComparator");
		
		// Process
		dataComparator.process();
		
		// Close
		context.close();
	}
}
