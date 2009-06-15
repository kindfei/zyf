package test.basic.swt;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class MainPanel {

	private Shell sShell = null;  
	private Composite cTree = null;
	private Composite cDetail = null;
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		sShell = new Shell(display);
		sShell.setText("Shell");
		sShell.setLayout(gridLayout);
		sShell.setSize(new Point(500, 300));
		createTree();
		createDetail();
	}

	/**
	 * This method initializes cTree	
	 *
	 */
	private void createTree() {
		GridData gridData = new GridData();
		gridData.widthHint = 153;
		gridData.heightHint = 257;
		cTree = new TreePanel(sShell, SWT.NONE);
		cTree.setLayoutData(gridData);
	}

	/**
	 * This method initializes cDetail	
	 *
	 */
	private void createDetail() {
		GridData gridData = new GridData();
		gridData.heightHint = 257;
		gridData.widthHint = 334;
		cDetail = new DetailPanel(sShell, SWT.NONE);
		cDetail.setLayoutData(gridData);
	}
	
//	public void display() {
//        Display display = new Display();
//        createSShell(display);
//        sShell.open();
//        while (!sShell.isDisposed()) {
//            if (!display.readAndDispatch()) {
//               display.sleep();
//            }
//        }
//        display.dispose();
//	}
//	
//	public static void main(String args[]) {
//		MainPanel main = new MainPanel();
//		main.display();
//	}
}
