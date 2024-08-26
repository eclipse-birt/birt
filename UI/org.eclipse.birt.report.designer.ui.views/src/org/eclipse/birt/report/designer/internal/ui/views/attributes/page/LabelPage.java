/*******************************************************************************
 * Copyright (c) 2004, 2024 Actuate Corporation and others
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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontSizePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontStylePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FontSizeSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FontStyleSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelper;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.RadioGroupSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IInternalReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.swt.SWT;

/**
 * The general attribute page of Label element.
 */
public class LabelPage extends GeneralPage {

	private static final int SECTION_WIDTH_LEFT = 216;
	private static final int SECTION_WIDTH_RIGHT = 200;

	@Override
	protected void buildContent() {
		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(IDesignElementModel.NAME_PROP,
				ReportDesignConstants.LABEL_ITEM);
		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);
		nameSection.setProvider(nameProvider);
		nameSection.setLayoutNum(2);
		nameSection.setWidth(SECTION_WIDTH_LEFT);
		addSection(PageSectionId.LABEL_NAME, nameSection);

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(SECTION_WIDTH_RIGHT);
		elementIdSection.setLayoutNum(4);
		elementIdSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.LABEL_ELEMENT_ID, elementIdSection);

		SeperatorSection seperator = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.LABEL_SEPERATOR, seperator);

		ComboPropertyDescriptorProvider fontFamilyProvider = new ComboPropertyDescriptorProvider(
				IStyleModel.FONT_FAMILY_PROP, ReportDesignConstants.STYLE_ELEMENT);
		fontFamilyProvider.enableReset(true);
		ComboSection fontFamilySection = new ComboSection(fontFamilyProvider.getDisplayName(), container, true);
		fontFamilySection.setProvider(fontFamilyProvider);
		fontFamilySection.setLayoutNum(2);
		fontFamilySection.setWidth(SECTION_WIDTH_LEFT);
		addSection(PageSectionId.LABEL_FONT_FAMILY, fontFamilySection);

		FontSizePropertyDescriptorProvider fontSizeProvider = new FontSizePropertyDescriptorProvider(
				IStyleModel.FONT_SIZE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		fontSizeProvider.enableReset(true);
		FontSizeSection fontSizeSection = new FontSizeSection(fontSizeProvider.getDisplayName(), container, true);
		fontSizeSection.setProvider(fontSizeProvider);
		fontSizeSection.setLayoutNum(4);
		fontSizeSection.setGridPlaceholder(2, true);
		fontSizeSection.setWidth(SECTION_WIDTH_RIGHT);
		addSection(PageSectionId.LABEL_FONT_SIZE, fontSizeSection);

		ColorPropertyDescriptorProvider colorProvider = new ColorPropertyDescriptorProvider(IStyleModel.COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		colorProvider.enableReset(true);
		ColorSection colorSection = new ColorSection(colorProvider.getDisplayName(), container, true);
		colorSection.setProvider(colorProvider);
		colorSection.setLayoutNum(2);
		colorSection.setWidth(SECTION_WIDTH_LEFT);
		addSection(PageSectionId.LABEL_COLOR, colorSection);

		ColorPropertyDescriptorProvider bgColorProvider = new ColorPropertyDescriptorProvider(
				IStyleModel.BACKGROUND_COLOR_PROP, ReportDesignConstants.STYLE_ELEMENT);
		bgColorProvider.enableReset(true);
		ColorSection bgColorSection = new ColorSection(bgColorProvider.getDisplayName(), container, true);
		bgColorSection.setProvider(bgColorProvider);
		bgColorSection.setLayoutNum(4);
		bgColorSection.setGridPlaceholder(2, true);
		bgColorSection.setWidth(SECTION_WIDTH_RIGHT);
		addSection(PageSectionId.LABEL_BGCOLOR, bgColorSection);

		String[] textStyles = { IStyleModel.FONT_WEIGHT_PROP, IStyleModel.FONT_STYLE_PROP,
				IStyleModel.TEXT_UNDERLINE_PROP, IStyleModel.TEXT_LINE_THROUGH_PROP,
				IStyleModel.TEXT_HYPERLINK_STYLE_PROP };

		PropertyDescriptorProvider[] providers = new PropertyDescriptorProvider[textStyles.length + 1];
		for (int i = 0; i < textStyles.length; i++) {
			providers[i] = new FontStylePropertyDescriptorProvider(textStyles[i], ReportDesignConstants.STYLE_ELEMENT);
			providers[i].enableReset(true);
		}
		providers[5] = new PropertyDescriptorProvider(IStyleModel.TEXT_ALIGN_PROP, ReportDesignConstants.STYLE_ELEMENT);
		providers[5].enableReset(true);
		FontStyleSection fontStyleSection = new FontStyleSection(container, true, false);
		fontStyleSection.setProviders(providers);
		fontStyleSection.setLayoutNum(3);
		addSection(PageSectionId.LABEL_FONT_STYLE, fontStyleSection);

		ComboPropertyDescriptorProvider wordwrapProvider = new ComboPropertyDescriptorProvider(
				IStyleModel.WHITE_SPACE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		wordwrapProvider.enableReset(true);
		RadioGroupSection wordwrapSection = new RadioGroupSection(wordwrapProvider.getDisplayName(), container, true);
		wordwrapSection.setProvider(wordwrapProvider);
		wordwrapSection.setLayoutNum(4);
		addSection(PageSectionId.WODR_WRAP, wordwrapSection); // $NON-NLS-1$

		SeperatorSection seperator1 = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.LABEL_SEPERATOR_1, seperator1);

		SimpleComboPropertyDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider(
				IStyledElementModel.STYLE_PROP, ReportDesignConstants.REPORT_ITEM);
		SimpleComboSection styleSection = new SimpleComboSection(styleProvider.getDisplayName(), container, true);
		styleSection.setProvider(styleProvider);
		styleSection.setLayoutNum(2);
		styleSection.setWidth(SECTION_WIDTH_LEFT);
		addSection(PageSectionId.LABEL_STYLE, styleSection);

		ComboPropertyDescriptorProvider displayProvider = new ComboPropertyDescriptorProvider(IStyleModel.DISPLAY_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		displayProvider.enableReset(true);
		ComboSection displaySection = new ComboSection(displayProvider.getDisplayName(), container, true);
		displaySection.setProvider(displayProvider);
		displaySection.setLayoutNum(4);
		displaySection.setGridPlaceholder(2, true);
		displaySection.setWidth(SECTION_WIDTH_RIGHT);
		addSection(PageSectionId.LABEL_DISPLAY, displaySection);

	}

	@Override
	protected void applyCustomSections() {
		Object[] helperProviders = ElementAdapterManager.getAdapters(this, ISectionHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				ISectionHelperProvider helperProvider = (ISectionHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					ISectionHelper helper = helperProvider.createHelper(this, PageConstants.THEME_HELPER_KEY);
					if (helper != null) {
						Section section = helper.createSection(container, IInternalReportItemModel.THEME_PROP,
								ReportDesignConstants.LABEL_ITEM, true);
						if (section instanceof SimpleComboSection) {
							((SimpleComboSection) section).setWidth(200);
						}
						section.setLayoutNum(6);
						section.setGridPlaceholder(4, true);
						addSectionAfter(PageSectionId.LABEL_THEME, section, PageSectionId.LABEL_DISPLAY);

					}
				}
			}
		}
	}
}
