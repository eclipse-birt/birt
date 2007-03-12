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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabSimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.GrandTotalProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.LayoutMeasuresProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.PageLayoutPropertyDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.ContainerSection;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.CrosstabSimpleComboSection;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.InnerCheckSection;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.InnerTextSection;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.PageLayoutComboSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CrosstabGeneralPage extends AttributePage {

	private TextSection librarySection;
	private SeperatorSection seperatorSection;
	private Button chkBrandColumn;
	private Button chkBrandRow;

	IDescriptorProvider grandTotalColumnProvider, grandTotalRowProvider;
	IDescriptorProvider layoutMeasuresProvider;

	public void buildContent() {
		container.setLayout(WidgetUtil.createGridLayout(5, 15));

		// LibraryDescriptorProvider provider = new LibraryDescriptorProvider();
		// librarySection = new TextSection(provider.getDisplayName(),
		// container,
		// true);
		// librarySection.setProvider(provider);
		// librarySection.setGridPlaceholder(1, true);
		// addSection(CrosstabPageSectionId.CROSSTAB_LIBRARY, librarySection);
		//
		// seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);
		// addSection(CrosstabPageSectionId.CROSSTAB_SEPERATOR,
		// seperatorSection);

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(
				ReportItemHandle.NAME_PROP, ReportDesignConstants.EXTENDED_ITEM);
		TextSection nameSection = new TextSection(
				nameProvider.getDisplayName(), container, true);
		nameSection.setProvider(nameProvider);
		nameSection.setGridPlaceholder(3, true);
		nameSection.setWidth(200);
		addSection(CrosstabPageSectionId.CROSSTAB_NAME, nameSection);

		IDescriptorProvider cubeProvider = new CrosstabSimpleComboPropertyDescriptorProvider(
				ICrosstabReportItemConstants.CUBE_PROP,/* ICrosstabReportItemConstants.CUBE_PROP */
				ReportDesignConstants.EXTENDED_ITEM);
		CrosstabSimpleComboSection cubeSection = new CrosstabSimpleComboSection(
				cubeProvider.getDisplayName(), container, true);
		cubeSection.setProvider(cubeProvider);
		cubeSection.setWidth(200);
		cubeSection.setGridPlaceholder(3, true);
		addSection(CrosstabPageSectionId.CUBE, cubeSection);

		ContainerSection formatOptionSection = new ContainerSection("",
				container, true);
		// formatOptionSection.setProvider( formatOptionProvider );
		formatOptionSection.setWidth(600);
		formatOptionSection.setGridPlaceholder(1, true);
		addSection(CrosstabPageSectionId.FORMAT_OPTION, formatOptionSection);

		grandTotalColumnProvider = new GrandTotalProvider(
				ICrosstabConstants.COLUMN_AXIS_TYPE);
		InnerCheckSection grandTotalColumnSection = new InnerCheckSection(
				formatOptionSection, true);
		grandTotalColumnSection.setProvider(grandTotalColumnProvider);
		grandTotalColumnSection.setLayoutNum(3);
		grandTotalColumnSection.setGridPlaceholder(1, false);
		addSection(CrosstabPageSectionId.BRANDTOTALS_COLUMN,
				grandTotalColumnSection);

		layoutMeasuresProvider = new LayoutMeasuresProvider();
		InnerCheckSection layoutMeasuresSection = new InnerCheckSection(
				formatOptionSection, true);
		layoutMeasuresSection.setProvider(layoutMeasuresProvider);
		layoutMeasuresSection.setLayoutNum(3);
		layoutMeasuresSection.setGridPlaceholder(1, false);
		addSection(CrosstabPageSectionId.LAYOUT_MEASURES, layoutMeasuresSection);

		PageLayoutPropertyDescriptorProvider pageLayoutProvider = new PageLayoutPropertyDescriptorProvider(
				ICrosstabReportItemConstants.PAGE_LAYOUT_PROP,
				ReportDesignConstants.EXTENDED_ITEM);
		PageLayoutComboSection pageLayoutComboSection = new PageLayoutComboSection(
				pageLayoutProvider.getDisplayName(), formatOptionSection, true);
		pageLayoutComboSection.setProvider(pageLayoutProvider);
		pageLayoutComboSection.setLayoutNum(2);
		addSection(CrosstabPageSectionId.PAGE_LAYOUT, pageLayoutComboSection);

		grandTotalRowProvider = new GrandTotalProvider(
				ICrosstabConstants.ROW_AXIS_TYPE);
		InnerCheckSection grandTotalRowSection = new InnerCheckSection(
				formatOptionSection, true);
		grandTotalRowSection.setProvider(grandTotalRowProvider);
		grandTotalRowSection.setLayoutNum(3);
		grandTotalRowSection.setGridPlaceholder(1, false);
		addSection(CrosstabPageSectionId.BRANDTOTALS_ROW, grandTotalRowSection);

		TextPropertyDescriptorProvider emptyCellValueProvider = new TextPropertyDescriptorProvider(
				ICrosstabReportItemConstants.EMPTY_CELL_VALUE_PROP,
				ReportDesignConstants.EXTENDED_ITEM);
		InnerTextSection emptyCellValueSection = new InnerTextSection(
				"For empty cells, show:", formatOptionSection, true);
		emptyCellValueSection.setProvider(nameProvider);
		emptyCellValueSection.setLayoutNum(2);
		emptyCellValueSection.setGridPlaceholder(1, false);
		addSection(CrosstabPageSectionId.EMPTY_CELL_VALUE,
				emptyCellValueSection);

		createSections();
		layoutSections();
	}

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		buildContent();
	}

	// temporary refresh, should update later
	public void refresh() {
		// if (input instanceof List
		// && DEUtil.getMultiSelectionHandle((List) input)
		// .isExtendedElements()) {
		// librarySection.setHidden(false);
		// seperatorSection.setHidden(false);
		// librarySection.load();
		// } else {
		// librarySection.setHidden(true);
		// seperatorSection.setHidden(true);
		// }
		if (grandTotalColumnProvider != null) {
			grandTotalColumnProvider.setInput(input);
		}

		if (grandTotalRowProvider != null) {
			grandTotalRowProvider.setInput(input);
		}
		if (layoutMeasuresProvider != null) {
			layoutMeasuresProvider.setInput(input);
		}

		// container.layout(true);
		// container.redraw();

		super.refresh();
	}
}
