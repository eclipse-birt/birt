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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontSizePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontStylePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FontSizeSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FontStyleSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.RadioGroupSection;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

public abstract class GeneralFontPage extends ResetAttributePage {

	protected void addFontsSection() {
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
		colorSection.setLayoutNum(2);
		colorSection.setWidth(200);
		addSection(PageSectionId.FONT_COLOR, colorSection);

		IDescriptorProvider[] fontStyleProviders = createFontStyleProviders();

		FontStyleSection fontStyleSection = new FontStyleSection(container, true, false);
		fontStyleSection.setProviders(fontStyleProviders);
		fontStyleSection.setLayoutNum(5);
		fontStyleSection.setGridPlaceholder(1, true);
		addSection(PageSectionId.FONT_STYLE, fontStyleSection);

		ComboPropertyDescriptorProvider wordwrapProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.WHITE_SPACE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		wordwrapProvider.enableReset(true);
		RadioGroupSection wordwrapSection = new RadioGroupSection(wordwrapProvider.getDisplayName(), container, true);
		wordwrapSection.setProvider(wordwrapProvider);
		wordwrapSection.setLayoutNum(6);
		addSection(PageSectionId.WODR_WRAP, wordwrapSection);

	}

	private IDescriptorProvider[] createFontStyleProviders() {
		IDescriptorProvider[] providers = new IDescriptorProvider[] {

				new FontStylePropertyDescriptorProvider(StyleHandle.FONT_WEIGHT_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new FontStylePropertyDescriptorProvider(StyleHandle.FONT_STYLE_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new FontStylePropertyDescriptorProvider(StyleHandle.TEXT_UNDERLINE_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new FontStylePropertyDescriptorProvider(StyleHandle.TEXT_LINE_THROUGH_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new PropertyDescriptorProvider(StyleHandle.TEXT_ALIGN_PROP, ReportDesignConstants.STYLE_ELEMENT) };

		for (int i = 0; i < providers.length; i++) {
			if (providers[i] instanceof PropertyDescriptorProvider)
				((PropertyDescriptorProvider) providers[i]).enableReset(true);
		}

		return providers;
	}
}
