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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.CheckSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The general attribute page of Column element.
 */
public class ColumnPage extends GeneralFontPage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(6, 15));

		// Defines providers.

		IDescriptorProvider widthProvider = new UnitPropertyDescriptorProvider(ColumnHandle.WIDTH_PROP,
				ReportDesignConstants.COLUMN_ELEMENT);

		ColorPropertyDescriptorProvider backgroundProvider = new ColorPropertyDescriptorProvider(
				StyleHandle.BACKGROUND_COLOR_PROP, ReportDesignConstants.STYLE_ELEMENT);
		backgroundProvider.enableReset(true);

		ComboPropertyDescriptorProvider vAlignProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.VERTICAL_ALIGN_PROP, ReportDesignConstants.STYLE_ELEMENT);
		vAlignProvider.enableReset(true);

		IDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider(ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.COLUMN_ELEMENT);

		IDescriptorProvider suppressDuplicatesProvider = new PropertyDescriptorProvider(
				ColumnHandle.SUPPRESS_DUPLICATES_PROP, ReportDesignConstants.COLUMN_ELEMENT);

		// Defines sections.

		ComplexUnitSection widthSection = new ComplexUnitSection(widthProvider.getDisplayName(), container, true);

		ColorSection backgroundSection = new ColorSection(backgroundProvider.getDisplayName(), container, true);

		ComboSection vAlignSection = new ComboSection(vAlignProvider.getDisplayName(), container, true);

		Section seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);

		SimpleComboSection styleSection = new SimpleComboSection(styleProvider.getDisplayName(), container, true);

		CheckSection suppressDuplicatesSection = new CheckSection(container, true);

		// Sets providers.

		widthSection.setProvider(widthProvider);
		backgroundSection.setProvider(backgroundProvider);
		vAlignSection.setProvider(vAlignProvider);
		styleSection.setProvider(styleProvider);
		suppressDuplicatesSection.setProvider(suppressDuplicatesProvider);

		// Sets widths.

		widthSection.setWidth(200);
		backgroundSection.setWidth(200);
		vAlignSection.setWidth(200);
		styleSection.setWidth(200);

		// Sets layout num.

		widthSection.setLayoutNum(2);
		backgroundSection.setLayoutNum(4);
		vAlignSection.setLayoutNum(2);
		styleSection.setLayoutNum(2);
		suppressDuplicatesSection.setLayoutNum(2);

		// Sets fill grid num.

		widthSection.setGridPlaceholder(0, true);
		backgroundSection.setGridPlaceholder(2, true);
		vAlignSection.setGridPlaceholder(0, true);
		styleSection.setGridPlaceholder(0, true);
		suppressDuplicatesSection.setGridPlaceholder(0, true);

		// Adds sections into this page.

		addSection(PageSectionId.COLUMN_WIDTH, widthSection); // $NON-NLS-1$
		addSection(PageSectionId.COLUMN_BACKGROUND_COLOR, backgroundSection); // $NON-NLS-1$
		addSection(PageSectionId.COLUMN_VERTICAL_ALIGN, vAlignSection); // $NON-NLS-1$
		addSection(PageSectionId.COLUMN_STYLE, styleSection); // $NON-NLS-1$
		addSection(PageSectionId.COLUMN_SUPPRESS_DUPLICATES, suppressDuplicatesSection); // $NON-NLS-1$
		addSection(PageSectionId.COLUMN_SEPERATOR, seperatorSection); // $NON-NLS-1$

		addFontsSection();

		createSections();
		layoutSections();
	}
}
