package com.emcom.jtools.actions;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import com.emcom.jtools.JToolsPlugin;
import com.emcom.jtools.Messages;
import com.emcom.jtools.internal.Logger;
import com.emcom.jtools.internal.SourceManipulator;
import com.emcom.jtools.preferences.PreferenceConstants;

/**
 * The Veiwer Action Delegate for externalizable.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 2005
 * 
 * @version 2.0, 14th April 2006
 * 
 * @version 3.0, 10th December 2006
 */
public class ExternalizableGeneratorVAD implements IEditorActionDelegate {
	/**
	 * The preference store associated with the plugin.
	 */
	private static final IPreferenceStore PREF_STORE = JToolsPlugin.getDefault().getPreferenceStore();

	/**
	 * The associated <tt>IEditorPart</tt>.
	 */
	private IEditorPart editorPart;

	/**
	 * {@inheritDoc}
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editorPart = targetEditor;
	}

	/**
	 * {@inheritDoc}
	 */
	public void run(IAction action) {
		IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();

		IEditorInput editorInput = this.editorPart.getEditorInput();

		ITextEditor editor = (ITextEditor) this.editorPart;

		ITextSelection selection = (ITextSelection) editor.getSelectionProvider().getSelection();

		ICompilationUnit compUnit = manager.getWorkingCopy(editorInput);

		Shell shell = this.editorPart.getSite().getShell();

		try {
			IJavaElement suspect = compUnit.getElementAt(selection.getOffset());

			if (suspect == null) {
				MessageDialog.openInformation(shell, Messages.getString("externalizable.failure.title"), Messages
						.getString("externalizable.failure.message"));

				return;
			}

			IType theType = null;

			if (suspect.getElementType() == IJavaElement.TYPE) {
				theType = (IType) suspect;
			} else {
				IJavaElement ancestor = suspect.getAncestor(IJavaElement.TYPE);

				if (ancestor != null) {
					theType = (IType) ancestor;
				}
			}

			if (theType != null && theType.isClass()) {
				SourceManipulator.createExternalizableWithJavaDoc(theType);

				if (PREF_STORE.getBoolean(PreferenceConstants.EXTERNALIZABLE_AUTOSAVE)) {
					compUnit.commitWorkingCopy(false, new NullProgressMonitor());
				}
			} else {
				MessageDialog.openInformation(shell, Messages.getString("externalizable.failure.title"), Messages
						.getString("externalizable.failure.message"));
			}
		} catch (JavaModelException e) {
			MessageDialog.openError(shell, Messages.getString("exception.title"), Messages
					.getString("exception.message"));

			Logger.error("Error generating externalizable through VAD", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// selection has changed. But, do nothing.
	}
}
