package test.all;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.all.TestJMSService.JmsPerfBean;
import test.core.cluster.MainMessageProcessor;


public class TestJMSProcessor extends MainMessageProcessor {
	private static final Log log = LogFactory.getLog(TestJMSProcessor.class);
	
	private int takerSize;
	private int msgCount;
	private int msgSize;
	
	private int[] content;
	private String nodeId = System.getProperty("nodeId");
	
	TestJMSProcessor(int takerSize, int msgCount, int msgSize) {
		this.takerSize = takerSize;
		this.msgCount = msgCount;
		this.msgSize = msgSize;
		
		int size = 1024 * msgSize;
		content = new int[size];
		for (int i = 0; i < size; i++) {
			content[i] = i;
		}
	}

	@Override
	public void mainProcess(Message msg) {
		try {
			JmsPerfBean bean = (JmsPerfBean) ((ObjectMessage) msg).getObject();
			log.info("Got msg " + bean.getSeq());
			long st = bean.getTime();
			if (st != 0) {
				long cost= System.currentTimeMillis() - st;
				log.info("nodeId=" + nodeId + ", takerSize=" + takerSize + ", msgCount=" + msgCount + ", msgSize=" + msgSize + "kb, time cost=" + cost + "ms.");
			}
		} catch (JMSException e) {
			log.error(e.getMessage(), e);
		}
	}

}