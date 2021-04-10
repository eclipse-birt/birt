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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

public class AutoTextPage extends LabelPage {

	public void applyCustomSections() {

		removeSection(PageSectionId.LABEL_DISPLAY);

		ComboPropertyDescriptorProvider styleProvider = new ComboPropertyDescriptorProvider(
				AutoTextHandle.AUTOTEXT_TYPE_PROP, ReportDesignConstants.AUTOTEXT_ITEM);
		ComboSection styleSection = new ComboSection(styleProvider.getDisplayName(), container, true);
		styleSection.setProvider(styleProvider);
		styleSection.setLayoutNum(4);
		styleSection.setGridPlaceholder(2, true);
		styleSection.setWidth(200);
		addSection(PageSectionId.AUTOTEXT_STYLE, styleSection);
	}
}
