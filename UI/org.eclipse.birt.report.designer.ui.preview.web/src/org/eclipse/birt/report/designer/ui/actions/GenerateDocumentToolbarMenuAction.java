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
import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.preview.IPreviewConstants;
import org.eclipse.birt.report.designer.ui.preview.PreviewUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * GenerateDocumentToolbarMenuAction
 */
public class GenerateDocumentToolbarMenuAction implements IWorkbenchWindowActionDelegate {

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}

	@Override
	public void run(IAction action) {
		gendoc(action);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	private void gendoc(IAction action) {
		// cleanup system settings
		PreviewUtil.clearSystemProperties();

		FormEditor editor = UIUtil.getActiveReportEditor(false);
		ModuleHandle model = null;
		if (model == null) {
			if (editor instanceof MultiPageReportEditor) {
				model = ((MultiPageReportEditor) editor).getModel();
			}
		}
		if (model == null) {
			return;
		}
		if (editor != null) {
			if (model.needsSave()) {
				editor.doSave(null);
			}
		}

		Map options = new HashMap();
		options.put(WebViewer.RESOURCE_FOLDER_KEY, ReportPlugin.getDefault().getResourceFolder());
		options.put(WebViewer.SERVLET_NAME_KEY, WebViewer.VIEWER_DOCUMENT);

		Object adapter = ElementAdapterManager.getAdapter(action, IPreviewAction.class);

		if (adapter instanceof IPreviewAction) {
			IPreviewAction delegate = (IPreviewAction) adapter;

			delegate.setProperty(IPreviewConstants.REPORT_PREVIEW_OPTIONS, options);
			delegate.setProperty(IPreviewConstants.REPORT_FILE_PATH, model.getFileName());

			delegate.run();

			return;
		}

		WebViewer.display(model.getFileName(), options);
	}

}
