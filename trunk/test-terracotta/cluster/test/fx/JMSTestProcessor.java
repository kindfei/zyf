package test.fx;

import java.util.Calendar;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.jms.TestAmqSender.JmsPerfBean;

import fx.cluster.core.MainMessageProcessor;

public class JMSTestProcessor extends MainMessageProcessor {
	private static final Log log = LogFactory.getLog(JMSTestProcessor.class);
	
	private String nodeId = System.getProperty("nodeId");

	@Override
	public void mainProcess(Message msg) {
		try {
			JmsPerfBean bean = (JmsPerfBean) ((ObjectMessage) msg).getObject();
			if (bean.isLast()) {
				long et = Calendar.getInstance().getTimeInMillis();
				long st = bean.getTime();
				int amount = bean.getAmount();
				log.info("nodeId=" + nodeId + " amount=" + amount + " time cost=" + (et - st) + "ms");
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
