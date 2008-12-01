package test.ibm.mq;

//------------------------------------------------------------------------
//MQExit.java - Source file for Java language MQ Exit functions
//------------------------------------------------------------------------
import com.ibm.mq.MQChannelDefinition;
import com.ibm.mq.MQChannelExit;
import com.ibm.mq.MQReceiveExit;
import com.ibm.mq.MQSendExit;

public class MQExit implements MQSendExit, MQReceiveExit {
	static long MIN_MASK_SIZE = 300;

	static int TSHOFFSET = 10;

	public byte[] sendExit(MQChannelExit channelExitParms,
			MQChannelDefinition channelDefinition, byte[] agentBuffer) {
		byte[] ret = agentBuffer, tmp;

		if (channelExitParms.exitID != MQChannelExit.MQXT_CHANNEL_SEND_EXIT) {
			return ret;
		}

		if (channelExitParms.exitReason == MQChannelExit.MQXR_XMIT) {
			channelExitParms.exitResponse = MQChannelExit.MQXCC_OK;

			if (agentBuffer.length < MIN_MASK_SIZE)
				return ret;
			tmp = new byte[agentBuffer.length - TSHOFFSET];
			System.arraycopy(agentBuffer, TSHOFFSET, tmp, 0, tmp.length);
			tmp = xor(tmp);
			System.arraycopy(tmp, 0, ret, TSHOFFSET, tmp.length);
		}
		return ret;
	}

	public byte[] receiveExit(MQChannelExit channelExitParms,
			MQChannelDefinition channelDefinition, byte[] agentBuffer) {
		byte[] ret = agentBuffer, tmp;
		if (channelExitParms.exitID != MQChannelExit.MQXT_CHANNEL_RCV_EXIT) {
			return ret;
		}

		if (channelExitParms.exitReason == MQChannelExit.MQXR_XMIT) {
			channelExitParms.exitResponse = MQChannelExit.MQXCC_OK;

			if (agentBuffer.length < MIN_MASK_SIZE)
				return ret;
			tmp = new byte[agentBuffer.length - TSHOFFSET];
			System.arraycopy(agentBuffer, TSHOFFSET, tmp, 0, tmp.length);
			tmp = xor(tmp);
			System.arraycopy(tmp, 0, ret, TSHOFFSET, tmp.length);
		}
		return ret;
	}

	public static byte[] xor(byte[] b1) {
		int len = b1.length;
		byte[] b = new byte[b1.length];

		for (int i = 0; i < len; i++) {
			b[i] = (byte) (b1[i] ^ (byte) 255);
		}
		return b;
	}
}
