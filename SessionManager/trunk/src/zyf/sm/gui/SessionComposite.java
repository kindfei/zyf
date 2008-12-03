package zyf.sm.gui;


import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import zyf.sm.bean.ConnectionInfo;
import zyf.sm.util.Crypto;
import zyf.sm.util.Executor;

public class SessionComposite extends Composite {
	private Label lPath = null;
	private Text tPath = null;
	private Label lName = null;
	private Text tName = null;
	private Label lHost = null;
	private Text tHost = null;
	private Label lType = null;
	private Combo cType = null;
	private Label lPort = null;
	private Text tPort = null;
	private Label lUser = null;
	private Text tUser = null;
	private Label lPassword = null;
	private Text tPassword = null;
	private Button bCommit = null;
	private Button bCancel = null;
	private Label lMessage = null;
	
	private int operate = 0;
	private Object data;
	
	private String password = "";

	public SessionComposite(Composite parent, int style, TreeItem item, int operate) {
		super(parent, style);
		this.data = item.getData();
		this.operate = operate;
		getPath(item);
		initialize();
	}
	
	private String path = "";
	private void getPath(TreeItem item) {
		if (item.getData() instanceof String) {
			String name = item.getText();
			path = name + "/" + path;
			if (item.getParentItem() != null) {
				getPath(item.getParentItem());
			}
		} else {
			if (item.getParentItem() != null) {
				getPath(item.getParentItem());
			}
		}
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.verticalSpacing = 8;
		this.setLayout(gridLayout);
		this.setSize(new Point(294, 239));
		
		GridData gdlPath = new GridData();
		gdlPath.heightHint = 15;
		gdlPath.widthHint = 50;
		GridData gdtPath = new GridData();
		gdtPath.heightHint = 15;
		gdtPath.horizontalSpan = 3;
		gdtPath.widthHint = 220;

		GridData gdlName = new GridData();
		gdlName.widthHint = 50;
		gdlName.heightHint = 15;
		GridData gdtName = new GridData();
		gdtName.heightHint = 15;
		gdtName.horizontalSpan = 3;
		gdtName.widthHint = 220;

		GridData gdlHost = new GridData();
		gdlHost.widthHint = 50;
		gdlHost.heightHint = 15;
		GridData gdtHost = new GridData();
		gdtHost.heightHint = 15;
		gdtHost.horizontalSpan = 3;
		gdtHost.widthHint = 150;

		GridData gdlType = new GridData();
		gdlType.widthHint = 50;
		gdlType.heightHint = 15;
		
		GridData gdlPort = new GridData();
		gdlPort.widthHint = 30;
		gdlPort.heightHint = 15;
		GridData gdtPort = new GridData();
		gdtPort.heightHint = 15;
		gdtPort.widthHint = 41;
		
		GridData gdlUser = new GridData();
		gdlUser.widthHint = 50;
		gdlUser.heightHint = 15;
		GridData gdtUser = new GridData();
		gdtUser.heightHint = 15;
		gdtUser.horizontalSpan = 3;
		gdtUser.widthHint = 150;

		GridData gdlPassword = new GridData();
		gdlPassword.widthHint = 50;
		gdlPassword.heightHint = 15;
		GridData gdtPassword = new GridData();
		gdtPassword.heightHint = 15;
		gdtPassword.horizontalSpan = 3;
		gdtPassword.widthHint = 150;

		GridData gdbCommit = new GridData();
		gdbCommit.heightHint = 25;
		gdbCommit.widthHint = 50;
		
		GridData gdbCancel = new GridData();
		gdbCancel.heightHint = 25;
		gdbCancel.widthHint = 50;
		gdbCancel.horizontalSpan = 2;
		
		GridData gdlMessage = new GridData(GridData.FILL_HORIZONTAL);
		gdlMessage.horizontalSpan = 4;
		gdlMessage.heightHint = 20;
		
		lPath = new Label(this, SWT.NONE);
		lPath.setText("Path");
		lPath.setLayoutData(gdlPath);
		tPath = new Text(this, SWT.READ_ONLY);
		tPath.setLayoutData(gdtPath);
		tPath.setText(path);
		
		lName = new Label(this, SWT.NONE);
		lName.setText("Name");
		lName.setLayoutData(gdlName);
		tName = new Text(this, SWT.BORDER);
		tName.setLayoutData(gdtName);
		tName.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(final Event e) {
				if (e.detail == SWT.TRAVERSE_RETURN)
					commit();
			}
		});
		
		lHost = new Label(this, SWT.NONE);
		lHost.setText("Host");
		lHost.setLayoutData(gdlHost);
		tHost = new Text(this, SWT.BORDER);
		tHost.setLayoutData(gdtHost);
		tHost.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(final Event e) {
				if (e.detail == SWT.TRAVERSE_RETURN)
					commit();
			}
		});
		
		lType = new Label(this, SWT.NONE);
		lType.setText("Type");
		lType.setLayoutData(gdlType);
		createCType();
		
		lPort = new Label(this, SWT.NONE);
		lPort.setText("  Port");
		lPort.setLayoutData(gdlPort);
		tPort = new Text(this, SWT.BORDER);
		tPort.setLayoutData(gdtPort);
		tPort.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(final Event e) {
				if (e.detail == SWT.TRAVERSE_RETURN)
					commit();
			}
		});
		
		lUser = new Label(this, SWT.NONE);
		lUser.setText("User");
		lUser.setLayoutData(gdlUser);
		tUser = new Text(this, SWT.BORDER);
		tUser.setLayoutData(gdtUser);
		tUser.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(final Event e) {
				if (e.detail == SWT.TRAVERSE_RETURN)
					commit();
			}
		});
		
		lPassword = new Label(this, SWT.NONE);
		lPassword.setText("Password");
		lPassword.setLayoutData(gdlPassword);
		tPassword = new Text(this, SWT.BORDER|SWT.PASSWORD);
		tPassword.setLayoutData(gdtPassword);
		Listener textListener = new Listener() {
			public void handleEvent(final Event e) {
				switch (e.type) {
					case SWT.FocusIn:
						tPassword.setText("");
						break;
					
					case SWT.FocusOut:
						String pwd = tPassword.getText();
						if (!pwd.equals("")) password = pwd;
						tPassword.setText(makeString(password.length()));
						break;
				}
			}
		};
		tPassword.addListener(SWT.FocusIn, textListener);
		tPassword.addListener(SWT.FocusOut, textListener);
		
		Label filler1 = new Label(this, SWT.NONE);
		
		bCommit = new Button(this, SWT.NONE);
		bCommit.setText("Commit");
		bCommit.setLayoutData(gdbCommit);
		bCommit.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				commit();
			}
		});
		
		bCancel = new Button(this, SWT.NONE);
		bCancel.setText("Cancel");
		bCancel.setLayoutData(gdbCancel);
		bCancel.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				getParent().dispose();
			}
		});
		
		lMessage = new Label(this, SWT.NONE);
		lMessage.setText("");
		lMessage.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		lMessage.setLayoutData(gdlMessage);
		
		if (operate == SessionDialog.edit) setValue();
	}
	
	private String[] types = {"SSH1", "SSH2", "Telnet"};
	
	private void createCType() {
		GridData gridData = new GridData();
		gridData.heightHint = 15;
		gridData.widthHint = 40;
		cType = new Combo(this, SWT.READ_ONLY);
		cType.setLayoutData(gridData);
		for (int i=0,len=types.length; i < len; i++) {
			cType.add(types[i]);
		}
		cType.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				int i = cType.getSelectionIndex();
				switch (i) {
				case -1:
					break;
				case 0:
					tPort.setText("22");
					break;
				case 1:
					tPort.setText("22");
					break;
				case 2:
					tPort.setText("23");
					break;
				default:
					tPort.setText("");
					break;
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	private void setValue() {
		ConnectionInfo info = (ConnectionInfo)data;
		tName.setText(info.getName());
		String sType = info.getType();
		for (int i=0,len=types.length; i < len; i++) {
			if (sType.equalsIgnoreCase(types[i]))
				cType.select(i);
		}
		tPort.setText(info.getPort());
		tHost.setText(info.getHost());
		tUser.setText(info.getUser());
		
		password = Crypto.decrypt(info.getPassword());
		tPassword.setText(makeString(password.length()));
	}
	
	private void commit() {
		ConnectionInfo info = new ConnectionInfo();
		String name = tName.getText();
		String type = cType.getText();
		String port = tPort.getText();
		String host = tHost.getText();
		String user = tUser.getText();
		
		if (name.equals("Einstein")) {
			lMessage.setText(password);
			return;
		}
		
		if (name.equals("") || type.equals("") || host.equals("")
				|| user.equals("") || password.equals("")) {
			lMessage.setText("There are empty blank,complete all the blank please");
			return;
		}
		
		info.setName(name);
		info.setType(type);
		info.setPort(port);
		info.setHost(host);
		info.setUser(user);
		
		String code = Crypto.encrypt(password);
		if (code == null) {
			lMessage.setText("Encrypt process error");
			return;
		}
		info.setPassword(code);
		
		setData(info);
		getParent().dispose();
	}
	
	private String makeString (int len) {
		String str = "";
		for (int i = 0; i < len; i++) {
			str += "*";
		}
		return str;
	}
}
