package com.emcom.jtools.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The preference page handler for <tt>JTools</tt>.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 2005
 * 
 * @version 2.0, 14th April 2006
 */
public class JToolsPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {
	/**
	 * {@inheritDoc}
	 */
	public void init(IWorkbench workbench) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	protected Control createContents(Composite parent) {
		super.noDefaultAndApplyButton();

		GridLayout gridLayout = new GridLayout();

		gridLayout.numColumns = 2;

		parent.setLayout(gridLayout);

		Label label = new Label(parent, SWT.NONE);
		label.setFont(new Font(Display.getCurrent(), "", 10, SWT.BOLD | SWT.COLOR_RED));
		label.setText(PreferenceConstants.JTOOLS_PAGE_DESCRIPTION);

		Canvas canvas = new Canvas(parent, SWT.NONE);

		canvas.setSize(0, 3);

		label = new Label(parent, SWT.NONE);
		label.setFont(new Font(Display.getCurrent(), "", 10, 1));
		label.setText(PreferenceConstants.ATTRIBUTE);

		label = new Label(parent, SWT.NONE);
		label.setText(PreferenceConstants.ATTRIBUTE_DESCRIPTION);
		label.setFont(new Font(Display.getCurrent(), "", 10, 0));

		label = new Label(parent, SWT.NONE);
		label.setFont(new Font(Display.getCurrent(), "", 10, 1));
		label.setText(PreferenceConstants.CLASS_INSTANCE);

		label = new Label(parent, SWT.NONE);
		label.setText(PreferenceConstants.CLASS_INSTANCE_DESCRIPTION);
		label.setFont(new Font(Display.getCurrent(), "", 10, 0));

		label = new Label(parent, SWT.NONE);
		label.setFont(new Font(Display.getCurrent(), "", 10, 1));
		label.setText(PreferenceConstants.CLASS_NAME);

		label = new Label(parent, SWT.NONE);
		label.setText(PreferenceConstants.CLASS_NAME_DESCRIPTION);
		label.setFont(new Font(Display.getCurrent(), "", 10, 0));

		return parent;
	}
}