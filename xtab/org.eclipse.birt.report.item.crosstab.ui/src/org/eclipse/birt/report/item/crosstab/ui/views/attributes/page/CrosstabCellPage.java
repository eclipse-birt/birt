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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabCellHeightProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabCellWidthProvider;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * The general attribute page of Cell element.
 */
public class CrosstabCellPage extends CellPage {

	protected void applyCustomSections() {
		IDescriptorProvider widthProvider = new CrosstabCellWidthProvider(ReportItemHandle.WIDTH_PROP,
				ReportDesignConstants.REPORT_ITEM);
		ComplexUnitSection widthSection = new ComplexUnitSection(widthProvider.getDisplayName(), container, true);
		widthSection.setProvider(widthProvider);
		widthSection.setWidth(200);
		widthSection.setLayoutNum(2);
		addSectionAfter(CrosstabPageSectionId.CROSSTAB_CELL_WIDTH, widthSection, PageSectionId.CELL_STYLE);

		IDescriptorProvider heightProvider = new CrosstabCellHeightProvider(ReportItemHandle.HEIGHT_PROP,
				ReportDesignConstants.REPORT_ITEM);
		ComplexUnitSection heightSection = new ComplexUnitSection(heightProvider.getDisplayName(), container, true);
		heightSection.setProvider(heightProvider);
		heightSection.setWidth(200);
		heightSection.setLayoutNum(4);
		heightSection.setGridPlaceholder(2, true);
		addSectionAfter(CrosstabPageSectionId.CROSSTAB_CELL_HEIGHT, heightSection,
				CrosstabPageSectionId.CROSSTAB_CELL_WIDTH);
	}
}
