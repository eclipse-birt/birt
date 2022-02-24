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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontSizePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontStylePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FontSizeSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TogglesSection;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * The Font attribute page of DE element.
 * 
 */
public class FontPage extends ResetAttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(6, 15));

		ComboPropertyDescriptorProvider fontFamilyProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.FONT_FAMILY_PROP, ReportDesignConstants.STYLE_ELEMENT);
		fontFamilyProvider.enableReset(true);
		ComboSection fontFamilySection = new ComboSection(fontFamilyProvider.getDisplayName(), container, true);
		fontFamilySection.setProvider(fontFamilyProvider);
		fontFamilySection.setLayoutNum(2);
		fontFamilySection.setWidth(200);
		addSection(PageSectionId.FONT_FAMILY, fontFamilySection);

		FontSizePropertyDescriptorProvider fontSizeProvider = new FontSizePropertyDescriptorProvider(
				StyleHandle.FONT_SIZE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		fontSizeProvider.enableReset(true);
		FontSizeSection fontSizeSection = new FontSizeSection(fontSizeProvider.getDisplayName(), container, true);
		fontSizeSection.setProvider(fontSizeProvider);
		fontSizeSection.setLayoutNum(4);
		fontSizeSection.setWidth(200);
		fontSizeSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.FONT_SIZE, fontSizeSection);

		ColorPropertyDescriptorProvider colorProvider = new ColorPropertyDescriptorProvider(StyleHandle.COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		colorProvider.enableReset(true);
		ColorSection colorSection = new ColorSection(colorProvider.getDisplayName(), container, true);
		colorSection.setProvider(colorProvider);
		colorSection.setWidth(200);
		colorSection.setLayoutNum(2);
		colorSection.setGridPlaceholder(4, true);
		addSection(PageSectionId.FONT_COLOR, colorSection);

		String[] textStyles = new String[] { StyleHandle.FONT_WEIGHT_PROP, StyleHandle.FONT_STYLE_PROP,
				StyleHandle.TEXT_UNDERLINE_PROP, StyleHandle.TEXT_LINE_THROUGH_PROP, };

		FontStylePropertyDescriptorProvider[] providers = new FontStylePropertyDescriptorProvider[4];
		for (int i = 0; i < textStyles.length; i++) {
			providers[i] = new FontStylePropertyDescriptorProvider(textStyles[i], ReportDesignConstants.STYLE_ELEMENT);
			providers[i].enableReset(true);
		}
		TogglesSection fontStyleSection = new TogglesSection(container);
		fontStyleSection.setProviders(providers);
		fontStyleSection.setGridPlaceholder(4, true);
		addSection(PageSectionId.FONT_STYLE, fontStyleSection);

		createSections();
		layoutSections();
	}

}
