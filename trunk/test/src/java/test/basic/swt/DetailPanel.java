package test.basic.swt;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

public class DetailPanel extends Composite {

	private Label lPath = null;
	private Label lName = null;
	private Label lType = null;
	private Label lHost = null;
	private Label lUser = null;
	private Label lPassword = null;
	private Text tPath = null;
	private Text tName = null;
	private Combo cType = null;
	private Text tHost = null;
	private Text tUser = null;
	private Text tPassword = null;
	private Button bAdd = null;
	private Button bUpdate = null;
	private Button bSsh = null;
	private Button bScp = null;
	private Button bCrt = null;
	public DetailPanel(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gLPath = new GridData();
		gLPath.widthHint = 50;
		gLPath.heightHint = 15;
		GridData gLName = new GridData();
		gLName.widthHint = 50;
		gLName.heightHint = 15;
		GridData gLType = new GridData();
		gLType.widthHint = 50;
		gLType.heightHint = 15;
		GridData gLHost = new GridData();
		gLHost.widthHint = 50;
		gLHost.heightHint = 15;
		GridData gLUser = new GridData();
		gLUser.widthHint = 50;
		gLUser.heightHint = 15;
		GridData gLPassword = new GridData();
		gLPassword.widthHint = 50;
		gLPassword.heightHint = 15;
		GridData gTPath = new GridData();
		gTPath.widthHint = 200;
		gTPath.heightHint = 15;
		GridData gTName = new GridData();
		gTName.widthHint = 200;
		gTName.heightHint = 15;
		GridData gTHost = new GridData();
		gTHost.widthHint = 100;
		gTHost.heightHint = 15;
		GridData gTUser = new GridData();
		gTUser.widthHint = 100;
		gTUser.heightHint = 15;
		GridData gTPassword = new GridData();
		gTPassword.widthHint = 100;
		gTPassword.heightHint = 15;
		GridData gBAdd = new GridData();
		gBAdd.widthHint = 50;
		gBAdd.heightHint = 20;
		GridData gBUpdate = new GridData();
		gBUpdate.widthHint = 50;
		gBUpdate.heightHint = 20;
		GridData gBSsh = new GridData();
		gBSsh.widthHint = 50;
		gBSsh.heightHint = 20;
		GridData gBScp = new GridData();
		gBScp.widthHint = 50;
		gBScp.heightHint = 20;
		GridData gBCrt = new GridData();
		gBCrt.widthHint = 50;
		gBCrt.heightHint = 20;
		lPath = new Label(this, SWT.NONE);
		lPath.setText("Path");
		lPath.setLayoutData(gLPath);
		tPath = new Text(this, SWT.BORDER);
		tPath.setLayoutData(gTPath);
		bSsh = new Button(this, SWT.NONE);
		bSsh.setText("SSH");
		bSsh.setLayoutData(gBSsh);
		bSsh.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
			}
		});
		lName = new Label(this, SWT.NONE);
		lName.setText("Name");
		lName.setLayoutData(gLName);
		tName = new Text(this, SWT.BORDER);
		tName.setLayoutData(gTName);
		bScp = new Button(this, SWT.NONE);
		bScp.setText("SCP");
		bScp.setLayoutData(gBScp);
		bScp.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
			}
		});
		lType = new Label(this, SWT.NONE);
		lType.setText("Type");
		lType.setLayoutData(gLType);
		createCombo();
		bCrt = new Button(this, SWT.NONE);
		bCrt.setText("CRT");
		bCrt.setLayoutData(gBCrt);
		bCrt.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
			}
		});
		lHost = new Label(this, SWT.NONE);
		lHost.setText("Host");
		lHost.setLayoutData(gLHost);
		tHost = new Text(this, SWT.BORDER);
		tHost.setLayoutData(gTHost);
		Label filler2 = new Label(this, SWT.NONE);
		lUser = new Label(this, SWT.NONE);
		lUser.setText("User");
		lUser.setLayoutData(gLUser);
		tUser = new Text(this, SWT.BORDER);
		tUser.setLayoutData(gTUser);
		Label filler1 = new Label(this, SWT.NONE);
		lPassword = new Label(this, SWT.NONE);
		lPassword.setText("Password");
		lPassword.setLayoutData(gLPassword);
		tPassword = new Text(this, SWT.PASSWORD);
		tPassword.setLayoutData(gTPassword);
		Label filler = new Label(this, SWT.NONE);
		bAdd = new Button(this, SWT.NONE);
		bAdd.setText("ADD");
		bAdd.setLayoutData(gBAdd);
		bAdd.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
			}
		});
		bUpdate = new Button(this, SWT.NONE);
		bUpdate.setText("UPDATE");
		bUpdate.setLayoutData(gBUpdate);
		bUpdate.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
			}
		});
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		this.setLayout(gridLayout);
		setSize(new Point(334, 257));
	}

	/**
	 * This method initializes combo	
	 *
	 */
	private void createCombo() {
		GridData gridData1 = new GridData();
		gridData1.widthHint = 50;
		gridData1.heightHint = 15;
		cType = new Combo(this, SWT.READ_ONLY);
		cType.setText("");
		cType.setLayoutData(gridData1);
		cType.add("SSH1");
		cType.add("SSH2");
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
