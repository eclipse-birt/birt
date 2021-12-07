/*******************************************************************************
 * Copyright (c) 2004, 2021 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.ide.explorer;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preview.PreviewUtil;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * The handler to run a report in navigator view
 */
public class RunReportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PreviewUtil.clearSystemProperties();

		IFile file = null;
		IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);
		if (selection != null && selection.size() == 1) {
			Object singleSelection = selection.getFirstElement();
			if (singleSelection instanceof IFile) {
				file = (IFile) singleSelection;
			}
		}

		if (file != null) {

			String url = file.getLocation().toOSString();
			try {
				ModuleHandle handle = SessionHandleAdapter.getInstance().getSessionHandle().openDesign(url);

				if (!UIUtil.canPreviewWithErrors(handle))
					return null;

				Map<String, Object> options = new HashMap<>();
				options.put(WebViewer.FORMAT_KEY, WebViewer.HTML);
				options.put(WebViewer.RESOURCE_FOLDER_KEY,
						ReportPlugin.getDefault().getResourceFolder(file.getProject()));

				WebViewer.display(url, options);
				handle.close();
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				throw new ExecutionException("Error executing handler", e); //$NON-NLS-1$
			}
		}
		return null;
	}

}
