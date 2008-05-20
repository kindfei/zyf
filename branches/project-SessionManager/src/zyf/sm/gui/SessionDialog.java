package zyf.sm.gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import zyf.sm.bean.ConnectionInfo;

public class SessionDialog extends Dialog {
	public final static int add = 0;
	public final static int edit = 1;
	
	public SessionDialog(Shell parent) {
		super(parent);
	}

	public SessionDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	private ConnectionInfo info;
	private SessionComposite composite;
	
	public ConnectionInfo open(TreeItem item, int operate) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL);
		shell.setText("Session Dialog");

		shell.setLayout(new GridLayout(1, true));
		composite = new SessionComposite(shell, SWT.NULL, item, operate);

		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				info = (ConnectionInfo) composite.getData();
			}
		});
		
		shell.setBounds(getParent().getBounds());
		shell.pack();
		shell.open();

		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return info;
	}
}
