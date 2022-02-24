/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.ReportDocumentEditor;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * ViewDocumentToolbarMenuAction
 */
public class ViewDocumentToolbarMenuAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}

	public void run(IAction action) {
		gendoc(action);
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	private void gendoc(IAction action) {
		ReportDocumentEditor editor = getActiveReportEditor(false);
		String url = null;
		if (editor != null) {
			url = editor.getFileName();
		}
		if (url == null) {
			return;
		}
		Map options = new HashMap();
		options.put(WebViewer.FORMAT_KEY, WebViewer.HTML);
		options.put(WebViewer.RESOURCE_FOLDER_KEY,
				ReportPlugin.getDefault().getResourceFolder(UIUtil.getCurrentProject()));

		WebViewer.display(url, options);
	}

	public ReportDocumentEditor getActiveReportEditor(boolean activePageOnly) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (window != null) {
			if (activePageOnly) {
				IWorkbenchPage pg = window.getActivePage();

				if (pg != null) {
					IEditorPart editor = pg.getActiveEditor();

					if (editor instanceof ReportDocumentEditor) {
						return (ReportDocumentEditor) editor;
					}
				}
			} else {
				IWorkbenchPage[] pgs = window.getPages();

				for (int i = 0; i < pgs.length; i++) {
					IWorkbenchPage pg = pgs[i];

					if (pg != null) {
						IEditorPart editor = pg.getActiveEditor();

						if (editor instanceof ReportDocumentEditor) {
							return (ReportDocumentEditor) editor;
						}

					}
				}
			}
		}

		return null;

	}

}
