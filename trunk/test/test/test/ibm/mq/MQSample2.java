package test.ibm.mq;

import java.io.IOException;

import com.ibm.mq.*;

public class MQSample2 {
	
	private String hostname = "localhost";
	private String channel = "testChannel";
	private String 	qManager = "QM_sheng";
	private MQQueueManager qMgr;
	
	public void start() {
		MQEnvironment.hostname = hostname;
		MQEnvironment.channel = channel;
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, // Set TCP/IP or server
									MQC.TRANSPORT_MQSERIES); // Connection
		
		try {
			qMgr = new MQQueueManager(qManager);
			
			int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT ;
			
			MQQueue system_default_local_queue =
				qMgr.accessQueue("SYSTEM.DEFAULT.LOCAL.QUEUE",
									openOptions,
									null,           // default q manager
									null,           // no dynamic q name
									null);          // no alternate user id

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
		}
		
	}
	
	public static void main(String args[]) {
		
	}

}
