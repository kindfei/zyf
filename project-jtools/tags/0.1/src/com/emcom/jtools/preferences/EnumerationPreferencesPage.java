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

public class EnumerationPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private Document javaDocDocument = new Document();

	private Document bodyDocument = new Document();

	private final JToolsPlugin jToolsPlugin = JToolsPlugin.getDefault();

	private final IPreferenceStore prefStore = this.jToolsPlugin.getPreferenceStore();

	public EnumerationPreferencesPage() {
		super(GRID);
		super.setPreferenceStore(this.prefStore);
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(IWorkbench workbench) {
		this.prefStore.setDefault(PreferenceConstants.ENUMERATION_AUTOSAVE, false);

		String javaDoc = this.prefStore.getString(PreferenceConstants.ENUMERATION_JAVADOC_STORE_KEY);

		String body = this.prefStore.getString(PreferenceConstants.ENUMERATION_BODY_STORE_KEY);

		// if something is null, fetch the true copy.
		if (Util.isNullString(javaDoc)) {
			javaDoc = Util.getDefaultEnumerationJavaDoc();
		}

		if (Util.isNullString(body)) {
			body = Util.getDefaultEnumerationImplementation();
		}

		this.javaDocDocument = new Document(javaDoc);
		this.bodyDocument = new Document(body);

		super.setPreferenceStore(this.prefStore);

		super.setDescription(PreferenceConstants.ENUMERATION_PAGE_DESCRIPTION);
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
		this.prefStore.setValue(PreferenceConstants.ENUMERATION_JAVADOC_STORE_KEY, this.javaDocDocument.get());

		this.prefStore.setValue(PreferenceConstants.ENUMERATION_BODY_STORE_KEY, this.bodyDocument.get());
	}

	/**
	 * {@inheritDoc}
	 */
	protected void performDefaults() {
		String javadoc = Util.getDefaultEnumerationJavaDoc();
		String body = Util.getDefaultEnumerationImplementation();

		this.javaDocDocument.set(javadoc);
		this.bodyDocument.set(body);

		this.prefStore.setValue(PreferenceConstants.ENUMERATION_JAVADOC_STORE_KEY, javadoc);

		this.prefStore.setValue(PreferenceConstants.ENUMERATION_BODY_STORE_KEY, body);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFieldEditors() {
		Composite parent = super.getFieldEditorParent();

		GridData data = new GridData(GridData.FILL_BOTH);

		parent.setLayout(new GridLayout(3, true));
		parent.setLayoutData(data);

		BooleanFieldEditor autoSaveCheckbox = new BooleanFieldEditor(PreferenceConstants.ENUMERATION_AUTOSAVE,
				PreferenceConstants.ENUMERATION_AUTOSAVE_LABEL, parent);

		super.addField(autoSaveCheckbox);

		PreferenceUtils.createViewer(parent, PreferenceConstants.JAVADOC_LABEL, this.javaDocDocument);

		PreferenceUtils.createViewer(parent, PreferenceConstants.ENUMERATION_BODY_LABEL, this.bodyDocument);
	}
}
