package test.basic.swt;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SessionComposite extends Composite {

	private Label lName = null;
	private Text tName = null;
	private Label lType = null;
	private Combo cType = null;
	private Label lHost = null;
	private Text tHost = null;
	private Label lUser = null;
	private Text tUser = null;
	private Label lPassword = null;
	private Text tPassword = null;
	private Label lPath = null;
	private Text tPath = null;
	private Button bCommit = null;
	private Button bCancel = null;
	private Label lMessage = null;

	public SessionComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData61 = new GridData();
		gridData61.horizontalSpan = 3;
		gridData61.widthHint = 280;
		gridData61.heightHint = 20;
		GridData gridData51 = new GridData();
		gridData51.widthHint = 50;
		gridData51.horizontalIndent = 20;
		GridData gridData31 = new GridData();
		gridData31.heightHint = -1;
		gridData31.widthHint = 50;
		GridData gridData21 = new GridData();
		gridData21.heightHint = 15;
		gridData21.widthHint = 50;
		GridData gridData11 = new GridData();
		gridData11.heightHint = 15;
		gridData11.horizontalSpan = 2;
		gridData11.widthHint = 220;
		lPath = new Label(this, SWT.NONE);
		lPath.setText("Path");
		lPath.setLayoutData(gridData21);
		GridData gridData9 = new GridData();
		gridData9.heightHint = 15;
		gridData9.horizontalSpan = 2;
		gridData9.widthHint = 150;
		GridData gridData8 = new GridData();
		gridData8.heightHint = 15;
		gridData8.horizontalSpan = 2;
		gridData8.widthHint = 150;
		GridData gridData7 = new GridData();
		gridData7.heightHint = 15;
		gridData7.horizontalSpan = 2;
		gridData7.widthHint = 150;
		GridData gridData5 = new GridData();
		gridData5.heightHint = 15;
		gridData5.horizontalSpan = 2;
		gridData5.widthHint = 220;
		GridData gridData4 = new GridData();
		gridData4.widthHint = 50;
		gridData4.heightHint = 15;
		GridData gridData3 = new GridData();
		gridData3.widthHint = 50;
		gridData3.heightHint = 15;
		GridData gridData2 = new GridData();
		gridData2.widthHint = 50;
		gridData2.heightHint = 15;
		GridData gridData1 = new GridData();
		gridData1.widthHint = 50;
		gridData1.heightHint = 15;
		GridData gridData = new GridData();
		gridData.widthHint = 50;
		gridData.heightHint = 15;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.verticalSpacing = 8;
		tPath = new Text(this, SWT.BORDER);
		tPath.setLayoutData(gridData11);
		lName = new Label(this, SWT.NONE);
		lName.setText("Name");
		lName.setLayoutData(gridData4);
		tName = new Text(this, SWT.BORDER);
		tName.setLayoutData(gridData5);
		lType = new Label(this, SWT.NONE);
		lType.setText("Type");
		lType.setLayoutData(gridData3);
		this.setLayout(gridLayout);
		createCType();
		this.setSize(new Point(294, 239));
		lHost = new Label(this, SWT.NONE);
		lHost.setText("Host");
		lHost.setLayoutData(gridData2);
		tHost = new Text(this, SWT.BORDER);
		tHost.setLayoutData(gridData7);
		lUser = new Label(this, SWT.NONE);
		lUser.setText("User");
		lUser.setLayoutData(gridData1);
		tUser = new Text(this, SWT.BORDER);
		tUser.setLayoutData(gridData8);
		lPassword = new Label(this, SWT.NONE);
		lPassword.setText("Password");
		lPassword.setLayoutData(gridData);
		tPassword = new Text(this, SWT.BORDER);
		tPassword.setLayoutData(gridData9);
		Label filler7 = new Label(this, SWT.NONE);
		bCommit = new Button(this, SWT.NONE);
		bCommit.setText("Commit");
		bCommit.setLayoutData(gridData31);
		bCommit.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
			}
		});
		bCancel = new Button(this, SWT.NONE);
		bCancel.setText("Cancel");
		bCancel.setLayoutData(gridData51);
		lMessage = new Label(this, SWT.NONE);
		lMessage.setText("");
		lMessage.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		lMessage.setLayoutData(gridData61);
		bCancel.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
			}
		});
	}

	/**
	 * This method initializes cType	
	 *
	 */
	private void createCType() {
		GridData gridData6 = new GridData();
		gridData6.heightHint = 15;
		gridData6.widthHint = 40;
		gridData6.horizontalSpan = 2;
		cType = new Combo(this, SWT.READ_ONLY);
		cType.setLayoutData(gridData6);
		cType.add("SSH1");
		cType.add("SSH2");
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
