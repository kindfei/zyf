package zyf.sm.gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import org.apache.commons.logging.Log;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import zyf.sm.bean.ConnectionInfo;
import zyf.sm.util.Helper;

public class ConnectionInfoTransfer extends ByteArrayTransfer {
	private static final Log log = Helper.getLog(ConnectionInfoTransfer.class);
	
	private static final String NAME = "ConnectionInfo";
	private static final int ID = registerType(NAME);
	private static final ConnectionInfoTransfer instance = new ConnectionInfoTransfer();
	
	public static ConnectionInfoTransfer getInstance() {
		return instance;
	}

	protected int[] getTypeIds() {
		return new int[]{ID};
	}

	protected String[] getTypeNames() {
		return new String[]{NAME};
	}
	
	protected void javaToNative(Object object, TransferData transferData) {
		if (object == null || !(object instanceof ConnectionInfo))
			return;

		if (isSupportedType(transferData)) {
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(stream);
				out.writeObject(object);
				out.close();
				super.javaToNative(stream.toByteArray(), transferData);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	protected Object nativeToJava(TransferData transferData) {
		if (isSupportedType(transferData)) {
			byte[] raw = (byte[])super.nativeToJava(transferData);
			if (raw == null) return null;
			
			ConnectionInfo info = null; 
			try {
				ByteArrayInputStream stream = new ByteArrayInputStream(raw);
				ObjectInputStream in = new ObjectInputStream(stream);
				info = (ConnectionInfo)in.readObject();
				in.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				log.error(e.getMessage(), e);
			}

			return info;
		} else {
			return null;
		}
	}
}
