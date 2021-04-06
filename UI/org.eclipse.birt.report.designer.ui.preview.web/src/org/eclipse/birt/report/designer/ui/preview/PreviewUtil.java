/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.utilities.WebViewer;

/**
 * 
 */

public class PreviewUtil {
	public static void clearSystemProperties() {
		System.clearProperty(IPreviewConstants.SID);
		System.clearProperty(IPreviewConstants.DSID);
		System.clearProperty(IPreviewConstants.MAX_DATASET_ROWS);
		System.clearProperty(IPreviewConstants.MAX_DATA_MODEL_MEMORY_SIZE);
	}

	public static void setSystemProperties() {
		System.setProperty(IPreviewConstants.MAX_DATASET_ROWS,
				ViewerPlugin.getDefault().getPluginPreferences().getString(WebViewer.PREVIEW_MAXROW));

		System.setProperty(IPreviewConstants.MAX_DATA_MODEL_MEMORY_SIZE, ReportPlugin.getDefault()
				.getPluginPreferences().getString(ReportPlugin.DATA_MODEL_MEMORY_LIMIT_PREFERENCE));
	}
}
