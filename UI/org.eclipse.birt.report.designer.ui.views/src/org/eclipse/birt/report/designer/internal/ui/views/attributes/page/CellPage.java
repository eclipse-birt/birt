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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ColorPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The general attribute page of Cell element.
 */
public class CellPage extends GeneralFontPage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(6, 15));

		// Defines providers.

		IDescriptorProvider dropProvider = new ComboPropertyDescriptorProvider(CellHandle.DROP_PROP,
				ReportDesignConstants.CELL_ELEMENT);

		ColorPropertyDescriptorProvider backgroundProvider = new ColorPropertyDescriptorProvider(
				StyleHandle.BACKGROUND_COLOR_PROP, ReportDesignConstants.STYLE_ELEMENT);
		backgroundProvider.enableReset(true);

		ComboPropertyDescriptorProvider vAlignProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.VERTICAL_ALIGN_PROP, ReportDesignConstants.STYLE_ELEMENT);
		vAlignProvider.enableReset(true);

		IDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider(ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.CELL_ELEMENT);

		// Defines sections.

		ComboSection dropSection = new ComboSection(dropProvider.getDisplayName(), container, true);

		ColorSection backgroundSection = new ColorSection(backgroundProvider.getDisplayName(), container, true);

		ComboSection vAlignSection = new ComboSection(vAlignProvider.getDisplayName(), container, true);

		Section seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);

		SimpleComboSection styleSection = new SimpleComboSection(styleProvider.getDisplayName(), container, true);

		// Sets providers.

		dropSection.setProvider(dropProvider);
		backgroundSection.setProvider(backgroundProvider);
		vAlignSection.setProvider(vAlignProvider);
		styleSection.setProvider(styleProvider);

		// Sets widths.

		dropSection.setWidth(200);
		backgroundSection.setWidth(200);
		vAlignSection.setWidth(200);
		styleSection.setWidth(200);

		// Sets layout num.

		dropSection.setLayoutNum(2);
		backgroundSection.setLayoutNum(4);
		vAlignSection.setLayoutNum(2);
		styleSection.setLayoutNum(4);

		// Sets fill grid num.

		dropSection.setGridPlaceholder(0, true);
		backgroundSection.setGridPlaceholder(2, true);
		vAlignSection.setGridPlaceholder(0, true);
		styleSection.setGridPlaceholder(2, true);

		// Adds sections into this page.

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(200);
		elementIdSection.setLayoutNum(6);
		elementIdSection.setGridPlaceholder(4, true);
		addSection(PageSectionId.CELL_ELEMENT_ID, elementIdSection);

		addSection(PageSectionId.CELL_DROP, dropSection); // $NON-NLS-1$
		addSection(PageSectionId.CELL_BACKGROUND, backgroundSection); // $NON-NLS-1$
		addSection(PageSectionId.CELL_VERTICAL_ALIGN, vAlignSection); // $NON-NLS-1$
		addSection(PageSectionId.CELL_STYLE, styleSection); // $NON-NLS-1$

		addSection(PageSectionId.CELL_SEPERATOR, seperatorSection); // $NON-NLS-1$

		addFontsSection();

		createSections();
		layoutSections();
	}
}
