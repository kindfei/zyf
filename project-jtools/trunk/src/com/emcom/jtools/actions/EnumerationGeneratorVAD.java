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

public class EnumerationGeneratorVAD implements IEditorActionDelegate {

	private static final IPreferenceStore PREF_STORE = JToolsPlugin.getDefault().getPreferenceStore();

	private IEditorPart editorPart;

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
				MessageDialog.openInformation(shell, Messages.getString("enumeration.failure.title"), Messages
						.getString("enumeration.failure.message"));

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
				SourceManipulator.createEnumerationWithJavaDoc(theType);

				if (PREF_STORE.getBoolean(PreferenceConstants.ENUMERATION_AUTOSAVE)) {
					compUnit.commitWorkingCopy(false, new NullProgressMonitor());
				}
			} else {
				MessageDialog.openInformation(shell, Messages.getString("enumeration.failure.title"), Messages
						.getString("enumeration.failure.message"));
			}
		} catch (JavaModelException e) {
			MessageDialog.openError(shell, Messages.getString("exception.title"), Messages
					.getString("exception.message"));

			Logger.error("Error generating enumeration through VAD", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// selection has changed. But, do nothing.
	}
}
