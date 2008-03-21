package test.ibm.mq;

//------------------------------------------------------------------------
//MQExitSample.java - Sample source file of MQ Java.
//------------------------------------------------------------------------
import java.io.IOException;
import com.ibm.mq.*;

public class MQExitSample {
	private String qManager = "";

	private MQQueueManager qMgr;

	public static void main(String args[]) throws MQException, IOException {
		MQExitSample test = new MQExitSample();
		test.PutMsg();
	}

	void PutMsg() throws MQException, IOException {
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,
				MQC.TRANSPORT_MQSERIES);
		MQEnvironment.CCSID = 819;
		MQEnvironment.hostname = "127.0.0.1";
		MQEnvironment.port = 1414;
		MQEnvironment.channel = "A.SVRCONN";

		MQEnvironment.sendExit = new MQExit();
		MQEnvironment.receiveExit = new MQExit();

		qMgr = new MQQueueManager(qManager);

		int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT;
		MQQueue lq = qMgr.accessQueue("LQ", openOptions, null, null, null);

		MQMessage msg = new MQMessage();
		msg.writeUTF("Hello,Java MQ Exit!");

		MQPutMessageOptions pmo = new MQPutMessageOptions();
		lq.put(msg, pmo);

		MQMessage retmsg = new MQMessage();
		retmsg.messageId = msg.messageId;
		MQGetMessageOptions gmo = new MQGetMessageOptions();
		lq.get(retmsg, gmo);

		String msgText = retmsg.readUTF();
		System.out.println("The message is: " + msgText);

		lq.close();
		qMgr.disconnect();
	}
}
