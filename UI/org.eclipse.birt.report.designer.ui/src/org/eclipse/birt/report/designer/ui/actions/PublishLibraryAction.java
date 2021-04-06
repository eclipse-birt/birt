/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.actions;

import java.io.File;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishLibraryWizard;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * 
 */

public class PublishLibraryAction implements IWorkbenchWindowActionDelegate {

	private IFile libFile = null;
	private boolean selectLibrary = false;

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		String fileName = null;
		LibraryHandle libHandle = null;
		if (editLibrary() == false && selectLibrary == false) {
			return;
		}

		if (editLibrary()) {
			ModuleHandle module = SessionHandleAdapter.getInstance().getReportDesignHandle();

			String filePath = module.getFileName();
			fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
			libHandle = (LibraryHandle) module;
		} else if (libFile != null && libFile.getFileExtension().equals("rptlibrary")) //$NON-NLS-1$
		{
			String url = libFile.getLocation().toOSString();
			ModuleHandle handle = null;
			try {
				handle = SessionHandleAdapter.getInstance().getSessionHandle().openLibrary(url);

//				if ( !( handle instanceof LibraryHandle ) )
//				{
//					action.setEnabled( false );
//					return;
//				}

				String filePath = handle.getFileName();
				if (filePath != null && filePath.length() != 0) {
					fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
				}
				libHandle = (LibraryHandle) handle;
			} catch (Exception e) {
				ExceptionHandler.handle(e);
				return;
			} finally {
				if (handle != null) {
					handle.close();
				}
			}
		}

		if (fileName != null && libHandle != null) {
			PublishLibraryWizard publishLibrary = new PublishLibraryWizard(libHandle, fileName,
					ReportPlugin.getDefault().getResourceFolder());

			WizardDialog dialog = new BaseWizardDialog(UIUtil.getDefaultShell(), publishLibrary);

			dialog.setPageSize(500, 250);
			dialog.open();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof TreeSelection) {
			IFile file = null;
			if (((TreeSelection) selection).getFirstElement() instanceof IFile) {
				file = (IFile) ((TreeSelection) selection).getFirstElement();
			}
			if (file != null) {
				if (file.getFileExtension() != null && file.getFileExtension().equals("rptlibrary")) //$NON-NLS-1$
				{
					libFile = file;
					selectLibrary = true;
					action.setEnabled(true);
				} else {
					libFile = null;
					selectLibrary = false;
					action.setEnabled(false);
				}

				return;
			}
		}

		libFile = null;
		selectLibrary = false;
		action.setEnabled(isEnable()); // $NON-NLS-1$

	}

	private boolean isEnable() {
		return editLibrary();
	}

	private boolean editLibrary() {
		IEditorPart editor = UIUtil.getActiveEditor(true);
		if (editor != null) {
			return (editor.getEditorInput().getName().endsWith(".rptlibrary")); //$NON-NLS-1$
		}
		return false;
	}
}
