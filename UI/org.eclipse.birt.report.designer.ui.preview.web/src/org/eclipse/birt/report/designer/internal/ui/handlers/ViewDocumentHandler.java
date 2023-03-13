/*******************************************************************************
 * Copyright (c) 2004, 2022 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  Alexander Fedorov (ArSysOp)  - structural improvements
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.resources.IFile;

/**
 * The handler to view report document in navigator view
 */
public final class ViewDocumentHandler extends AbstractFileHandler {

	@Override
	protected void execute(IFile file) {
		// FIXME: AF: this logic could go to another type to be reusable
		String url = file.getLocation().toString();
		Map<String, Object> options = new HashMap<>();
		options.put(WebViewer.FORMAT_KEY, WebViewer.HTML);
		options.put(WebViewer.RESOURCE_FOLDER_KEY, ReportPlugin.getDefault().getResourceFolder(file.getProject()));
		WebViewer.display(url, options);
	}

}
