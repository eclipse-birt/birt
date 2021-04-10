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

package org.eclipse.birt.report.designer.ui.ide.navigator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.actions.IPreviewAction;
import org.eclipse.birt.report.designer.ui.preview.IPreviewConstants;
import org.eclipse.birt.report.designer.ui.preview.PreviewUtil;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;

/**
 * The action to generate report document in navigator view
 */
public class GenerateDocumentAction extends AbstractViewAction {

	protected boolean preGenerate() {
		PreviewUtil.clearSystemProperties();
		return true;
	}

	public void run(IAction action) {
		if (!preGenerate()) {
			return;
		}

		IFile file = getSelectedFile();
		if (file != null) {
			String url = file.getLocation().toOSString();

			Map options = new HashMap();
			options.put(WebViewer.RESOURCE_FOLDER_KEY, ReportPlugin.getDefault().getResourceFolder(file.getProject()));
			options.put(WebViewer.SERVLET_NAME_KEY, WebViewer.VIEWER_DOCUMENT);

			Object adapter = ElementAdapterManager.getAdapter(action, IPreviewAction.class);

			if (adapter instanceof IPreviewAction) {
				IPreviewAction delegate = (IPreviewAction) adapter;

				delegate.setProperty(IPreviewConstants.REPORT_PREVIEW_OPTIONS, options);
				delegate.setProperty(IPreviewConstants.REPORT_FILE_PATH, url);

				delegate.run();

				return;
			}

			try {
				WebViewer.display(url, options);
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				return;
			}
		} else {
			action.setEnabled(false);
		}
	}

}
