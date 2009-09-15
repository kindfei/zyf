package com.emcom.jtools.actions;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.emcom.jtools.JToolsException;
import com.emcom.jtools.Messages;
import com.emcom.jtools.internal.Logger;
import com.emcom.jtools.internal.SourceManipulator;

/**
 * The Object Action Delegate for toString.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 2005
 * 
 * @version 2.0, 14th April 2006
 * 
 * @version 3.0, 10th December 2006
 */
public class ToStringGeneratorOAD implements IObjectActionDelegate {
	/**
	 * The associated <tt>PackageExplorerPart</tt>.
	 */
	private PackageExplorerPart packageExplorerPart;

	/**
	 * {@inheritDoc}
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.packageExplorerPart = (PackageExplorerPart) targetPart;
	}

	/**
	 * {@inheritDoc}
	 */
	public void run(IAction action) {
		IWorkbenchPage page = this.packageExplorerPart.getSite().getPage();

		StructuredSelection selection = (StructuredSelection) page.getSelection();

		Object[] compUnits = selection.toArray();

		Shell shell = this.packageExplorerPart.getSite().getShell();

		int failed = 0;

		for (int i = 0; i < compUnits.length; i++) {
			ICompilationUnit compUnit = (ICompilationUnit) compUnits[i];

			IType type = compUnit.findPrimaryType();

			try {
				if (type != null && type.isClass()) {
					SourceManipulator.createToStringWithJavaDoc(type);
				} else {
					failed++;
				}
			} catch (JavaModelException e) {
				MessageDialog.openError(shell, Messages.getString("exception.title"), Messages
						.getString("exception.message"));

				Logger.error("Error generating toString through OAD", e);

				break;
			} catch (JToolsException e) {
				MessageDialog.openError(shell, Messages.getString("exception.title"), Messages
						.getString("exception.message"));

				Logger.error("Fatal error while generating toString", e);

				break;
			}
		}

		if (failed == compUnits.length) {
			MessageDialog.openInformation(shell, Messages.getString("tostring.failure.title"), Messages
					.getString("tostring.failure.message"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// selection has changed. But, do nothing.
	}
}
