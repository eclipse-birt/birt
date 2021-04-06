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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ColorPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The general attribute page of Row element.
 */
public class RowPage extends GeneralFontPage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(6, 15));

		// Defines providers.

		IDescriptorProvider heightProvider = new UnitPropertyDescriptorProvider(RowHandle.HEIGHT_PROP,
				ReportDesignConstants.ROW_ELEMENT);

		ColorPropertyDescriptorProvider backgroundProvider = new ColorPropertyDescriptorProvider(
				StyleHandle.BACKGROUND_COLOR_PROP, ReportDesignConstants.STYLE_ELEMENT);
		backgroundProvider.enableReset(true);

		ComboPropertyDescriptorProvider vAlignProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.VERTICAL_ALIGN_PROP, ReportDesignConstants.STYLE_ELEMENT);
		vAlignProvider.enableReset(true);

		IDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider(ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.ROW_ELEMENT);

		// Defines sections.

		ComplexUnitSection heightSection = new ComplexUnitSection(heightProvider.getDisplayName(), container, true);

		ColorSection backgroundSection = new ColorSection(backgroundProvider.getDisplayName(), container, true);

		ComboSection vAlignSection = new ComboSection(vAlignProvider.getDisplayName(), container, true);

		Section seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);
		SimpleComboSection styleSection = new SimpleComboSection(styleProvider.getDisplayName(), container, true);

		// Sets providers.

		heightSection.setProvider(heightProvider);
		backgroundSection.setProvider(backgroundProvider);
		vAlignSection.setProvider(vAlignProvider);
		styleSection.setProvider(styleProvider);

		// Sets widths.

		heightSection.setWidth(200);
		backgroundSection.setWidth(200);
		vAlignSection.setWidth(200);
		styleSection.setWidth(200);

		// Sets layout num.

		heightSection.setLayoutNum(2);
		backgroundSection.setLayoutNum(4);
		vAlignSection.setLayoutNum(2);
		styleSection.setLayoutNum(4);

		// Sets fill grid num.

		heightSection.setGridPlaceholder(0, true);
		backgroundSection.setGridPlaceholder(2, true);
		vAlignSection.setGridPlaceholder(0, true);
		styleSection.setGridPlaceholder(2, true);

		// Adds sections into container page.

		addSection(PageSectionId.ROW_HEIGHT, heightSection); // $NON-NLS-1$
		addSection(PageSectionId.ROW_BACKGROUND_COLOR, backgroundSection); // $NON-NLS-1$
		addSection(PageSectionId.ROW_VERTICAL_ALIGN, vAlignSection); // $NON-NLS-1$
		addSection(PageSectionId.ROW_STYLE, styleSection); // $NON-NLS-1$
		addSection(PageSectionId.ROW_SEPERATOR, seperatorSection); // $NON-NLS-1$

		addFontsSection();

		createSections();
		layoutSections();
	}
}
