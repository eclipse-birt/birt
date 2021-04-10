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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.LabelSection;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.EmptyRowColumnProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.EmptyRowColumnSection;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.InnerTextSection;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class EmptyRowColumnPage extends AttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(3, 15));

		LabelSection infoSection = new LabelSection(Messages.getString("EmptyRowColumnPage.Label.Text"), //$NON-NLS-1$
				container, true);
		infoSection.setLayoutNum(3);
		infoSection.setGridPlaceholder(0, true);
		infoSection.setWidth(500);
		addSection(CrosstabPageSectionId.EMPTY_ROWCOLUMN_INFO, infoSection);

		EmptyRowColumnProvider emptyRowProvider = new EmptyRowColumnProvider(ICrosstabConstants.ROW_AXIS_TYPE);
		EmptyRowColumnSection emptyRowSection = new EmptyRowColumnSection(container, true);
		emptyRowSection.setProvider(emptyRowProvider);
		emptyRowSection.setLayoutNum(3);
		emptyRowSection.setGridPlaceholder(0, true);
		emptyRowSection.setWidth(500);
		addSection(CrosstabPageSectionId.EMPTY_ROW, emptyRowSection);

		EmptyRowColumnProvider emptyColumnProvider = new EmptyRowColumnProvider(ICrosstabConstants.COLUMN_AXIS_TYPE);
		EmptyRowColumnSection emptyColumnSection = new EmptyRowColumnSection(container, true);
		emptyColumnSection.setProvider(emptyColumnProvider);
		emptyColumnSection.setLayoutNum(3);
		emptyColumnSection.setGridPlaceholder(0, true);
		emptyColumnSection.setWidth(500);
		addSection(CrosstabPageSectionId.EMPTY_COLUMN, emptyColumnSection);

		TextPropertyDescriptorProvider emptyCellValueProvider = new TextPropertyDescriptorProvider(
				ICrosstabReportItemConstants.EMPTY_CELL_VALUE_PROP, ReportDesignConstants.EXTENDED_ITEM);
		InnerTextSection emptyCellValueSection = new InnerTextSection(
				Messages.getString("CrosstabGeneralPage.ForEmptyCell"), //$NON-NLS-1$
				container, true);
		emptyCellValueSection.setProvider(emptyCellValueProvider);
		emptyCellValueSection.setLayoutNum(3);
		emptyCellValueSection.setWidth(300);
		emptyCellValueSection.setGridPlaceholder(0, true);
		addSection(CrosstabPageSectionId.EMPTY_CELL_VALUE, emptyCellValueSection);

		createSections();
		layoutSections();

	}

}
