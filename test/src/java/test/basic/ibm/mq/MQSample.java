package test.basic.ibm.mq;

import java.io.IOException;
import java.util.Hashtable;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class MQSample {
	private static String qManager = "QM_sheng";
	private static MQQueueManager qMgr;
	
	public static void main(String agrs[]) {
		Hashtable properties;
		
		try {
			// Create a connection to the queue manager
			qMgr = new MQQueueManager(qManager);
			
			// Set up the options on the queue we wish to open...
			// Note. All MQSeries Options are prefixed with MQC in Java.
			int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT ;
			
			// Now specify the queue that we wish to open,
			// and the open options...
			MQQueue system_default_local_queue = 
				qMgr.accessQueue("SYSTEM.DEFAULT.LOCAL.QUEUE",
									openOptions,
									null,           // default q manager
									null,           // no dynamic q name
									null);          // no alternate user id
			
			// Define a simple MQSeries message, and write some text in UTF format..
			MQMessage hello_world = new MQMessage();
			hello_world.writeUTF("hello world");
			
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			
			system_default_local_queue.put(hello_world, pmo);
			
			MQMessage receiveMessage = new MQMessage();
			receiveMessage.messageId = hello_world.messageId;
			
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			
			system_default_local_queue.get(receiveMessage, gmo);
			System.out.println(receiveMessage.readUTF());
			
			system_default_local_queue.close();
			qMgr.disconnect();

		} catch (MQException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
	}

}
