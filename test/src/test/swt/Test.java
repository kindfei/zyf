package test.swt;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class Test {

	private Shell sShell = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setSize(new Point(300, 200));
		sShell.setLayout(new GridLayout());
	}

}
