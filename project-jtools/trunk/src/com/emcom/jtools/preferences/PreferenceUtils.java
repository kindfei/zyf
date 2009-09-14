package com.emcom.jtools.preferences;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * A utilities class for all Preference Page related activities.
 * 
 * @author Adarsh
 * 
 * @version 3.1, 7th July 2007
 */
public class PreferenceUtils {
	/**
	 * Creates a new source viewer for each invocation.
	 * 
	 * @param parent
	 *            parent component.
	 * @param labelText
	 *            the text to be used as label.
	 * @param document
	 *            contains the text to be rendered.
	 */
	public static void createViewer(Composite parent, String labelText, IDocument document) {
		/*
		 * Most of this method is a hack. So, I won't explain anything.
		 */
		Label label = new Label(parent, 0);

		label.setFont(new Font(Display.getCurrent(), "", 10, SWT.BOLD | SWT.COLOR_RED));

		label.setText(labelText);

		GridData data = new GridData();

		data.horizontalSpan = PreferenceConstants.WIDTH;

		label.setLayoutData(data);

		JavaTextTools tools = JavaPlugin.getDefault().getJavaTextTools();

		tools.setupJavaDocumentPartitioner(document, PreferenceConstants.PARTITIONING);

		IPreferenceStore store = JavaPlugin.getDefault().getCombinedPreferenceStore();

		SourceViewer viewer = new JavaSourceViewer(parent, null, null, false, PreferenceConstants.SOURCEVIEWER_STYLE,
				store);

		JavaSourceViewerConfiguration configuration = new JavaSourceViewerConfiguration(tools.getColorManager(), store,
				null, PreferenceConstants.PARTITIONING);

		viewer.configure(configuration);

		viewer.setEditable(true);

		viewer.setDocument(document);

		Font font = JFaceResources.getFont(PreferenceConstants.JAVA_FONT);

		viewer.getTextWidget().setFont(font);

		Control control = viewer.getControl();

		data = new GridData(GridData.FILL_BOTH);

		data.widthHint = PreferenceConstants.WIDTH;
		data.heightHint = PreferenceConstants.HEIGHT;

		control.setLayoutData(data);
	}
}
