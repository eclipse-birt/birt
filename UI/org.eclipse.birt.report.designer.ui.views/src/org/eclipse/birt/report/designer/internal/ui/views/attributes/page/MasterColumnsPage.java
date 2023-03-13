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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MasterColumnsDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.MasterColumnsSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class MasterColumnsPage extends AttributePage {

	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(8, 15));

		MasterColumnsSection columnsSection = new MasterColumnsSection(container, true);
		columnsSection.setLayoutNum(2);
		MasterColumnsDescriptorProvider provider = new MasterColumnsDescriptorProvider(MasterPageHandle.COLUMNS_PROP,
				ReportDesignConstants.MASTER_PAGE_ELEMENT);
		columnsSection.setProvider(provider);
		addSection(PageSectionId.MASTER_PAGE_COLUMNS, columnsSection);

		Section seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.MASTER_PAGE_COLUMN_SEPERATOR, seperatorSection);

		UnitPropertyDescriptorProvider spaceProvider = new UnitPropertyDescriptorProvider(
				MasterPageHandle.COLUMN_SPACING_PROP, ReportDesignConstants.MASTER_PAGE_ELEMENT);

		ComplexUnitSection spaceSection = new ComplexUnitSection(spaceProvider.getDisplayName(), container, true);

		spaceSection.setProvider(spaceProvider);
		spaceSection.setWidth(200);
		spaceSection.setGridPlaceholder(3, true);

		provider.setColumnSpaceSection(spaceSection);
		addSection(PageSectionId.MASTER_PAGE_COLUMNS_SPACE, spaceSection);

		createSections();
		layoutSections();
	}
}
