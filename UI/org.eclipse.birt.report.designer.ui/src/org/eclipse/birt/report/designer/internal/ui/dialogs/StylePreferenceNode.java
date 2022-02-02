/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class StylePreferenceNode extends PreferenceNode {

	public StylePreferenceNode(String id, IPreferencePage preferencePage) {
		super(id, preferencePage);
	}

	private Image image;

	public Image getLabelImage() {
		image = null;
		if (getPage() instanceof BaseStylePreferencePage) {
			BaseStylePreferencePage page = (BaseStylePreferencePage) getPage();
			if (page.hasLocaleProperties()) {
				image = ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_STYLE_MODIFIED);
			} else
				image = ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_STYLE_DEFAULT);
		}

		return image;
	}

}
