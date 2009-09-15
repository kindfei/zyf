package com.emcom.jtools.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.emcom.jtools.JToolsPlugin;
import com.emcom.jtools.internal.Util;

/**
 * The preference page handler for <tt>JTools -> externalizable</tt>.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 2005
 * 
 * @version 2.0, 14th April 2006
 * 
 * @version 3.0, 10th December 2006
 * 
 * @version 3.1, 7th July 2007
 */
public class ExternalizablePreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/**
	 * Contains the text for displaying the JavaDoc in a <tt>SourceViewer</tt>.
	 */
	private Document javaDocDocument = new Document();

	/**
	 * Contains the text for displaying the Method(externalizable) Body in a
	 * <tt>SourceViewer</tt>.
	 */
	private Document bodyDocument = new Document();

	/**
	 * The lone instance of <tt>JToolsPlugin</tt>.
	 */
	private final JToolsPlugin jToolsPlugin = JToolsPlugin.getDefault();

	/**
	 * The preference store associated with this plugin.
	 */
	private final IPreferenceStore prefStore = this.jToolsPlugin.getPreferenceStore();

	public ExternalizablePreferencesPage() {
		super(GRID);
		super.setPreferenceStore(this.prefStore);
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(IWorkbench workbench) {
		this.prefStore.setDefault(PreferenceConstants.EXTERNALIZABLE_SORT, false);
		this.prefStore.setDefault(PreferenceConstants.EXTERNALIZABLE_AUTOSAVE, false);
		this.prefStore.setDefault(PreferenceConstants.EXTERNALIZABLE_OVERWRITE, true);

		String javaDoc = this.prefStore.getString(PreferenceConstants.EXTERNALIZABLE_JAVADOC_STORE_KEY);

		String body = this.prefStore.getString(PreferenceConstants.EXTERNALIZABLE_BODY_STORE_KEY);

		// if something is null, fetch the true copy.
		if (Util.isNullString(javaDoc)) {
			javaDoc = Util.getDefaultExternalizableJavaDoc();
		}

		if (Util.isNullString(body)) {
			body = Util.getDefaultExternalizableImplementation();
		}

		this.javaDocDocument = new Document(javaDoc);
		this.bodyDocument = new Document(body);

		super.setPreferenceStore(this.prefStore);

		super.setDescription(PreferenceConstants.EXTERNALIZABLE_PAGE_DESCRIPTION);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean performOk() {
		this.performApply();

		return super.performOk();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void performApply() {
		this.prefStore.setValue(PreferenceConstants.EXTERNALIZABLE_JAVADOC_STORE_KEY, this.javaDocDocument.get());

		this.prefStore.setValue(PreferenceConstants.EXTERNALIZABLE_BODY_STORE_KEY, this.bodyDocument.get());
	}

	/**
	 * {@inheritDoc}
	 */
	protected void performDefaults() {
		String javadoc = Util.getDefaultExternalizableJavaDoc();
		String body = Util.getDefaultExternalizableImplementation();

		this.javaDocDocument.set(javadoc);
		this.bodyDocument.set(body);

		this.prefStore.setValue(PreferenceConstants.EXTERNALIZABLE_JAVADOC_STORE_KEY, javadoc);

		this.prefStore.setValue(PreferenceConstants.EXTERNALIZABLE_BODY_STORE_KEY, body);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFieldEditors() {
		Composite parent = super.getFieldEditorParent();

		GridData data = new GridData(GridData.FILL_BOTH);

		parent.setLayout(new GridLayout(3, true));
		parent.setLayoutData(data);

		BooleanFieldEditor sortCheckbox = new BooleanFieldEditor(PreferenceConstants.EXTERNALIZABLE_SORT,
				PreferenceConstants.EXTERNALIZABLE_SORT_LABEL, parent);

		BooleanFieldEditor autoSaveCheckbox = new BooleanFieldEditor(PreferenceConstants.EXTERNALIZABLE_AUTOSAVE,
				PreferenceConstants.EXTERNALIZABLE_AUTOSAVE_LABEL, parent);

		BooleanFieldEditor overriteCheckbox = new BooleanFieldEditor(PreferenceConstants.EXTERNALIZABLE_OVERWRITE,
				PreferenceConstants.EXTERNALIZABLE_OVERWRITE_LABEL, parent);

		super.addField(sortCheckbox);
		super.addField(autoSaveCheckbox);
		super.addField(overriteCheckbox);

		PreferenceUtils.createViewer(parent, PreferenceConstants.JAVADOC_LABEL, this.javaDocDocument);

		PreferenceUtils.createViewer(parent, PreferenceConstants.EXTERNALIZABLE_BODY_LABEL, this.bodyDocument);
	}
}
