/*******************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.internal.rcp.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportEditorInput;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.rcp.nls.DesignerWorkbenchMessages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

/**
 * Action to open designer files.
 */

public class OpenFileAction extends Action implements IWorkbenchWindowActionDelegate, IWorkbenchAction {

	private IWorkbenchWindow fWindow;

	private static String[] filterExtensions;
	private static final int SUPPORT_COUNT;
	static {
		List list = ReportPlugin.getDefault().getReportExtensionNameList();
		filterExtensions = new String[list.size() + 3];
		for (int i = 0; i < list.size(); i++) {
			filterExtensions[i] = "*." + list.get(i); //$NON-NLS-1$
		}
		filterExtensions[filterExtensions.length - 3] = "*.rptlibrary"; //$NON-NLS-1$
		filterExtensions[filterExtensions.length - 2] = "*.rpttemplate"; //$NON-NLS-1$
		filterExtensions[filterExtensions.length - 1] = "*.rptdocument"; //$NON-NLS-1$

		SUPPORT_COUNT = filterExtensions.length;
	}

	public OpenFileAction(IWorkbenchWindow window) {
		init(window);
		setEnabled(true);
		setText(DesignerWorkbenchMessages.Workbench_openFile);
		setToolTipText(DesignerWorkbenchMessages.Action_openReport);
		setId("org.eclipse.birt.report.designer.rcp.internal.ui.actions.OpenFileAction"); //$NON-NLS-1$

		if (filterExtensions.length == SUPPORT_COUNT) {
			Object[] adapters = ElementAdapterManager.getAdapters(this, IExtensionFile.class);
			List<String> tempList = new ArrayList<String>();
			for (int i = 0; i < filterExtensions.length; i++) {
				tempList.add(filterExtensions[i]);
			}
			if (adapters != null) {
				for (int i = 0; i < adapters.length; i++) {
					IExtensionFile newFile = (IExtensionFile) adapters[i];
					tempList.add(newFile.getFileExtension());
				}
			}

			filterExtensions = tempList.toArray(new String[tempList.size()]);
		}
	}

	/*
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		fWindow = null;
	}

	/*
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		if (window == null) {
			throw new IllegalArgumentException();
		}
		fWindow = window;
	}

	/*
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		run();
	}

	/*
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.
	 * IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/*
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		FileDialog dialog = new FileDialog(fWindow.getShell(), SWT.OPEN | SWT.MULTI);
		dialog.setText(DesignerWorkbenchMessages.Dialog_openFile);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getProjectRelativePath().toOSString());
		dialog.open();
		String[] names = dialog.getFileNames();

		if (names != null) {
			String fFilterPath = dialog.getFilterPath();

			int numberOfFilesNotFound = 0;
			StringBuffer notFound = new StringBuffer();
			for (int i = 0; i < names.length; i++) {
				File file = new File(fFilterPath + File.separator + names[i]);
				if (file.exists()) {
					IWorkbenchPage page = fWindow.getActivePage();
					IEditorInput input = new ReportEditorInput(file);
					IEditorDescriptor editorDesc = getEditorDescriptor(input, OpenStrategy.activateOnOpen());
					try {
						page.openEditor(input, editorDesc.getId());
					} catch (Exception e) {
						ExceptionUtil.handle(e);
					}
				} else {
					if (++numberOfFilesNotFound > 1)
						notFound.append('\n');
					notFound.append(file.getName());
				}
			}
			if (numberOfFilesNotFound > 0) {
				// String msgFmt= numberOfFilesNotFound == 1 ?
				// TextEditorMessages.OpenExternalFileAction_message_fileNotFound
				// :
				// TextEditorMessages.OpenExternalFileAction_message_filesNotFound;
				// String msg= MessageFormat.format(msgFmt, new Object[] {
				// notFound.toString() });
				// MessageDialog.openError(fWindow.getShell(),
				// TextEditorMessages.OpenExternalFileAction_title, msg);
			}
		}
	}

	private IEditorDescriptor getEditorDescriptor(IEditorInput input, boolean determineContentType) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		IContentType contentType = Platform.getContentTypeManager().findContentTypeFor(input.getName());
		IEditorRegistry editorReg = PlatformUI.getWorkbench().getEditorRegistry();
		return editorReg.getDefaultEditor(input.getName(), contentType);
	}

}
