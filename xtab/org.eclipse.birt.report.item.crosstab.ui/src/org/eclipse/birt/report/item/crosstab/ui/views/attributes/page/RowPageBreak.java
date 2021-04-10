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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.CheckSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.RepeatHeaderProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.RowPageBreakProvider;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class RowPageBreak extends AttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(1, 15));

		RepeatHeaderProvider repeatHeaderProvider = new RepeatHeaderProvider(
				ICrosstabReportItemConstants.REPEAT_COLUMN_HEADER_PROP, ReportDesignConstants.EXTENDED_ITEM);
		CheckSection repeatRowHeaderSection = new CheckSection(container, true);
		repeatRowHeaderSection.setProvider(repeatHeaderProvider);
		repeatRowHeaderSection.setWidth(200);
		addSection(CrosstabSectionPageId.ROWPAGEBREAK_REPEAT_COLUMN_HEADER, repeatRowHeaderSection);

		RowPageBreakProvider pageBreakProvider = new RowPageBreakProvider();
		FormSection pageBreakSection = new FormSection(pageBreakProvider.getDisplayName(), container, true);
		pageBreakSection.setProvider(pageBreakProvider);
		pageBreakSection.setButtonWithDialog(true);
		pageBreakSection.setStyle(FormPropertyDescriptor.NO_UP_DOWN);
		pageBreakSection.setFillForm(true);
		pageBreakSection.setHeight(170);
		addSection(CrosstabPageSectionId.ROW_PAGE_BREAK, pageBreakSection);
		createSections();
		layoutSections();
	}
}
