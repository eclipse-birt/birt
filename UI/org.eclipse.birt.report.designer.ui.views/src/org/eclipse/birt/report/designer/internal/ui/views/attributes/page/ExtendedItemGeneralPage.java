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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Page for extended elements
 */
public class ExtendedItemGeneralPage extends AttributePage {
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(5, 15));

		// Defines providers.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ReportItemHandle.NAME_PROP,
				ReportDesignConstants.EXTENDED_ITEM);

		IDescriptorProvider widthProvider = new UnitPropertyDescriptorProvider(ReportItemHandle.WIDTH_PROP,
				ReportDesignConstants.GRID_ITEM);

		IDescriptorProvider heightProvider = new UnitPropertyDescriptorProvider(ReportItemHandle.HEIGHT_PROP,
				ReportDesignConstants.GRID_ITEM);

		IDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider(ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.REPORT_ITEM);

		// Defines sections.

		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);

		Section seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);

		ComplexUnitSection widthSection = new ComplexUnitSection(widthProvider.getDisplayName(), container, true);

		ComplexUnitSection heightSection = new ComplexUnitSection(heightProvider.getDisplayName(), container, true);

		SimpleComboSection styleSection = new SimpleComboSection(styleProvider.getDisplayName(), container, true);

		// Sets providers.

		nameSection.setProvider(nameProvider);
		widthSection.setProvider(widthProvider);
		heightSection.setProvider(heightProvider);
		styleSection.setProvider(styleProvider);

		// Sets widths.

		nameSection.setWidth(180);
		widthSection.setWidth(180);
		heightSection.setWidth(180);
		styleSection.setWidth(180);

		// Sets layout num.

		nameSection.setLayoutNum(5);
		widthSection.setLayoutNum(2);
		heightSection.setLayoutNum(3);
		styleSection.setLayoutNum(5);

		// Sets fill grid num.

		nameSection.setGridPlaceholder(3, true);
		widthSection.setGridPlaceholder(0, true);
		heightSection.setGridPlaceholder(1, true);
		styleSection.setGridPlaceholder(3, true);

		// Adds sections into this page.

		addSection("EXTENDED_NAME_SECTION", nameSection); //$NON-NLS-1$
		addSection("SEPERATOR_SECTION", seperatorSection); //$NON-NLS-1$
		addSection("EXTENDED_WIDTH_SECTION", widthSection); //$NON-NLS-1$
		addSection("EXTENDED_HEIGHT_SECTION", heightSection); //$NON-NLS-1$
		addSection("EXTENDED_STYLE_SECTION", styleSection); //$NON-NLS-1$
	}
}
